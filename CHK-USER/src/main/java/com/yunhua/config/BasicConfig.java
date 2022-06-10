package com.yunhua.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BasicConfig {

    @Value("${chk.merchant.distant}")
    private String chkMerchantDistant;


    @Value("${chk.open.url}")
    private String openUrl;


}
