package com.arizon.Guidant.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arizon.Guidant.util.GuidantUtil;
import com.arizon.customercommon.model.BCCustomerAttributeDTO;
import com.arizon.customercommon.model.BCCustomerDTO;
import com.arizon.customercommon.service.BcCustomerServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderSourceCustomerService {
	
	@Autowired
	BcCustomerServices bcCustomerServices;
	
	public List<BCCustomerAttributeDTO> getBCCustomerAttributeByCutomerId(Integer customerId) {
		try {
			Optional<BCCustomerDTO> customerOpt = bcCustomerServices.getCustomerFromBC(customerId);
			if (customerOpt.isPresent()) {
				BCCustomerDTO customer = customerOpt.get();
				return customer.getAttributes();
			}
		} catch (Exception e) {
			log.info("Exception  occured in integrateOrder: " + GuidantUtil.getStackTrace(e));
		}
		return Collections.emptyList();

	}
	
	public Optional<BCCustomerAttributeDTO> findAttributeById(List<BCCustomerAttributeDTO> attributes, Integer attributeId) {
	
	    if (attributes == null || attributeId == null) {
	        return Optional.empty();
	    }

	    return attributes.stream()
	            .filter(attr -> attributeId.equals(attr.getAttribute_id()))
	            .findFirst();
	}
	
	public String getCustomerAttributeValue(List<BCCustomerAttributeDTO> customerAttributes, Integer attributeId) {
		try {
			Optional<BCCustomerAttributeDTO> customerAttribute = findAttributeById(customerAttributes, attributeId);
			if (customerAttribute.isPresent()) {
				return customerAttribute.get().getAttribute_value();
			} else {
				log.warn("Attribute not found for the customer");

			}
		} catch (Exception e) {
			log.info("Exception  occured in integrateOrder: " + GuidantUtil.getStackTrace(e));
		}
		return "";
	}
}
