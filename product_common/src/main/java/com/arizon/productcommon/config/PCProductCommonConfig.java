/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author mohan.e
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties("productcommon")
public class PCProductCommonConfig {

    public String baseUrl;
    public String storeHash;
    public String accesstoken;
    public String maxAttempts;
    public String maxPeriod;
    public String period;
    public String prefix;
}
