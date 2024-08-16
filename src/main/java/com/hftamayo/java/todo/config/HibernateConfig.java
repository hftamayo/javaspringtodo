package com.hftamayo.java.todo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
public class HibernateConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.hftamayo.java.todo.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty(
                "spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    public void displayEnvironmentProperties() {
        if (env instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) env;
            logger.info("Displaying environment properties:");
            for (PropertySource<?> propertySource : configurableEnvironment.getPropertySources()) {
                if (propertySource.getSource() instanceof Map) {
                    Map<String, Object> source = (Map<String, Object>) propertySource.getSource();
                    for (String key : source.keySet()) {
                        logger.info("{}: {}", key, env.getProperty(key));
                    }
                }
            }
        } else {
            logger.warn("Environment is not an instance of ConfigurableEnvironment");
        }
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();

        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("spring.jpa.properties.hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("spring.jpa.properties.hibernate.format_sql", env.getProperty("hibernate.format_sql"));

        logger.info("hibernate properties: {}", properties);

        return properties;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> displayEnvironmentProperties();
    }
}