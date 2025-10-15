package com.arizon.Guidant.services;

import java.time.OffsetDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.arizon.customercommon.entity.CustomerEntity;
import com.arizon.customercommon.entity.CustomerAttributeMapping;
import com.arizon.Guidant.model.sap.AddressDTO;
import com.arizon.Guidant.model.sap.BusinessPartnerDTO;
import com.arizon.Guidant.model.sap.BusinessPartnerResponse;
import com.arizon.Guidant.model.sap.BusinessPartnerResults;
import com.arizon.Guidant.model.sap.CompanyTextWrapper;
import com.arizon.Guidant.model.sap.PartnerFunctionDTO;
import com.arizon.customercommon.repository.CustomerRespository;
import com.arizon.customercommon.repository.CustomerAttributeMappingRepository;
import com.arizon.Guidant.restclient.SAPAuthClient;
import com.arizon.customercommon.controller.AdisToBcCustomer;
import com.arizon.customercommon.entity.CustomerAddressEntity;
import com.arizon.customercommon.repository.CustomerAddressRepository;
import com.arizon.ordercommons.configs.OCConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.arizon.ordercommons.configs.OCConstants.secrects;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GuidantBusinessPartner {

    @Autowired
    private SAPAuthClient sapAuthClient;

    @Autowired
    private CustomerRespository customerRespository;

    @Autowired
    private CustomerAttributeMappingRepository customerAttributeMappingRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public ResponseEntity<List<BusinessPartnerDTO>> integrateBusinessPartners() {
        try {
            String jsonResponse = fetchBusinessPartners();
            ObjectMapper mapper = new ObjectMapper();
            BusinessPartnerResponse response = mapper.readValue(jsonResponse, BusinessPartnerResponse.class);

            List<BusinessPartnerDTO> businessPartners = Optional.ofNullable(response)
                    .map(BusinessPartnerResponse::getD)
                    .map(BusinessPartnerResults::getResults)
                    .orElse(Collections.emptyList());

            if (businessPartners.isEmpty()) {
                log.warn("No Business Partners found from SAP.");
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<CustomerAttributeMapping> attributeMappings = new ArrayList<>();
            List<CustomerAddressEntity> addressEntities = new ArrayList<>();

            for (BusinessPartnerDTO dto : businessPartners) {
                // Resolve first and last name once per BP (person or org)
                String[] names = resolveFirstLastName(dto);
                String firstName = names[0];
                String lastName = names[1];

                // Map and save CustomerEntity
                CustomerEntity entity = mapToCustomerEntity(dto, firstName, lastName);
                CustomerEntity savedCustomer = customerRespository.save(entity);
                Integer customerId = savedCustomer.getCustomerId();

                // Map and save addresses
                try {
                    if (dto.getToBusinessPartnerAddress() != null &&
                            dto.getToBusinessPartnerAddress().getResults() != null) {

                        String companyName = resolveCompanyName(dto);

                        for (AddressDTO addressDTO : dto.getToBusinessPartnerAddress().getResults()) {
                            CustomerAddressEntity address = mapToCustomerAddressEntity(
                                    customerId,
                                    addressDTO,
                                    firstName,
                                    lastName,
                                    companyName
                            );
                            addressEntities.add(address);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Failed to map/save addresses for BP: {}", dto.getBusinessPartner(), ex);
                }

                // Map and save attributes
                try {
                    if (dto.getToCustomer() != null &&
                            dto.getToCustomer().getToCustomerSalesArea() != null &&
                            dto.getToCustomer().getToCustomerSalesArea().getResults() != null) {

                        dto.getToCustomer().getToCustomerSalesArea().getResults().forEach(salesArea -> {
                            if (salesArea.getToPartnerFunction() != null &&
                                    salesArea.getToPartnerFunction().getResults() != null) {

                                for (PartnerFunctionDTO pf : salesArea.getToPartnerFunction().getResults()) {
                                    CustomerAttributeMapping mapping = new CustomerAttributeMapping();
                                    mapping.setCustomerId(customerId);
                                    mapping.setAttributeId(resolveAttributeId(pf.getPartnerFunction()));
                                    mapping.setDestinationCustomerAttributeId(pf.getPartnerCounter());
                                    mapping.setAttributeValue(pf.getCustomer());
                                    mapping.setIsActive(true);
                                    attributeMappings.add(mapping);
                                }
                            }
                        });
                    }
                } catch (Exception ex) {
                    log.error("Failed to map/save attributes for BP: {}", dto.getBusinessPartner(), ex);
                }
            }

            if (!addressEntities.isEmpty()) {
                customerAddressRepository.saveAll(addressEntities);
                log.info("Saved {} customer addresses to database.", addressEntities.size());
            }

            if (!attributeMappings.isEmpty()) {
                customerAttributeMappingRepository.saveAll(attributeMappings);
                log.info("Saved {} customer attribute mappings to database.", attributeMappings.size());
            }

            return ResponseEntity.ok(businessPartners);

        } catch (Exception ex) {
            log.error("Failed to process SAP Business Partners", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public String fetchBusinessPartners() {
        String basicAuth = "Basic " + Base64.getEncoder()
                .encodeToString((secrects.getSapClientId() + ":" + secrects.getSapClientSecret()).getBytes());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        Map<String, Object> tokenResponse = sapAuthClient.getAccessToken(basicAuth, form);
        String bearerToken = "Bearer " + tokenResponse.get("access_token");

        String expandParams = "to_Customer/to_CustomerSalesArea/to_PartnerFunction," +
                "to_BusinessPartnerAddress/to_EmailAddress," +
                "to_BusinessPartnerAddress/to_PhoneNumber," +
                "to_Customer/to_CustomerCompany/to_CompanyText";

        ResponseEntity<String> response = sapAuthClient.getBusinessPartners(
                bearerToken,
                null,
                expandParams,
                5
        );

        return response.getBody();
    }

    /** Resolves first and last name depending on Person or Organization */
    private String[] resolveFirstLastName(BusinessPartnerDTO dto) {
        String firstName = dto.getFirstName();
        String lastName = dto.getLastName();

        // If person fields missing, fallback to org fields
        if ((firstName == null || firstName.isBlank()) && (lastName == null || lastName.isBlank())) {
            firstName = dto.getOrganizationBPName1();
            lastName = dto.getOrganizationBPName2();
        }

        // Extra fallback: split full name if available
        if ((firstName == null || firstName.isBlank()) && (lastName == null || lastName.isBlank())) {
            String fullName = dto.getBusinessPartnerFullName();
            if (fullName != null && !fullName.isBlank()) {
                String[] parts = fullName.split(" ", 2);
                firstName = parts[0];
                lastName = (parts.length > 1) ? parts[1] : "";
            }
        }

        return new String[]{firstName, lastName};
    }

    private CustomerEntity mapToCustomerEntity(BusinessPartnerDTO dto, String firstName, String lastName) {
        String phone = null;
        String email = null;

        if (dto.getToBusinessPartnerAddress() != null &&
                dto.getToBusinessPartnerAddress().getResults() != null) {
            for (AddressDTO address : dto.getToBusinessPartnerAddress().getResults()) {
                if (phone == null && address.getToPhoneNumber() != null &&
                        address.getToPhoneNumber().getResults() != null &&
                        !address.getToPhoneNumber().getResults().isEmpty()) {
                    phone = address.getToPhoneNumber().getResults().get(0).getPhoneNumber();
                }
                if (email == null && address.getToEmailAddress() != null &&
                        address.getToEmailAddress().getResults() != null &&
                        !address.getToEmailAddress().getResults().isEmpty()) {
                    email = address.getToEmailAddress().getResults().get(0).getEmailAddress();
                }
            }
        }

        if (email == null || email.isBlank()) {
            email = "unknown-" + dto.getBusinessPartner() + "@guidant.com";
        }

        String companyName = resolveCompanyName(dto);

        return CustomerEntity.builder()
                .sourceCustomerId(dto.getBusinessPartner())
                .companyName(companyName)
                .firstName(firstName)
                .lastName(lastName)
                .emailAddress(email)
                .phone(phone)
                .isActive(true)
                .isDeleted(false)
                .status("Pending")
                .createdDate(OffsetDateTime.now())
                .modifiedDate(OffsetDateTime.now())
                .storehash("nhrwflyaph")
                .build();
    }

    private CustomerAddressEntity mapToCustomerAddressEntity(
            Integer customerId,
            AddressDTO dto,
            String firstName,
            String lastName,
            String company
    ) {
        return CustomerAddressEntity.builder()
                .customerId(customerId)
                .firstName(firstName)
                .lastName(lastName)
                .company(company)
                .street1(dto.getStreetName())
                .street2(dto.getStreetSuffixName())
                .city(dto.getCityName())
                .state(dto.getRegion())
                .zip(dto.getPostalCode())
                .country(dto.getCountry())
                .isActive(true)
                .isDeleted(false)
                .status("Pending")
                .createdDate(OffsetDateTime.now())
                .modifiedDate(OffsetDateTime.now())
                .build();
    }

    private String resolveCompanyName(BusinessPartnerDTO dto) {
        String companyName = dto.getOrganizationBPName1();
        if (dto.getToCustomer() != null &&
                dto.getToCustomer().getToCustomerCompany() != null &&
                dto.getToCustomer().getToCustomerCompany().getResults() != null &&
                !dto.getToCustomer().getToCustomerCompany().getResults().isEmpty()) {

            CompanyTextWrapper companyTextWrapper = dto.getToCustomer()
                    .getToCustomerCompany()
                    .getResults()
                    .get(0)
                    .getToCompanyText();

            if (companyTextWrapper != null &&
                    companyTextWrapper.getResults() != null &&
                    !companyTextWrapper.getResults().isEmpty() &&
                    companyTextWrapper.getResults().get(0).getCompanyText() != null) {

                companyName = companyTextWrapper.getResults().get(0).getCompanyText();
            }
        }
        return companyName;
    }

    private Integer resolveAttributeId(String partnerFunction) {
        return switch (partnerFunction) {
            case "SP" -> 1;
            case "SH" -> 2;
            case "BP" -> 3;
            case "PY" -> 4;
            default -> 0;
        };
    }
}
