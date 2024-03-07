package com.hftamayo.java.todo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Autowired

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistrationBean() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.setOrder(1); // set order here
        return registrationBean;
    }

}
