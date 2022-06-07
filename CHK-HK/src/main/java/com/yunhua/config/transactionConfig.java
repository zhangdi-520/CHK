package com.yunhua.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class transactionConfig {

    @Bean
    public JdbcOperations prodJdbcOperations(DataSource dataSourceChk) {
        return new JdbcTemplate(dataSourceChk);
    }

    @Autowired
    private JdbcOperations prodJdbcOperations;

    @Bean
    public PlatformTransactionManager prodTransactionManager(DataSource dataSourceChk) {
        return new DataSourceTransactionManager(dataSourceChk);
    }

}
