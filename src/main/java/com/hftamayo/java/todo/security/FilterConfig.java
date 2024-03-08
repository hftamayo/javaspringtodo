package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.security.jwt.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Autowired

    private AuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> jwtFilterRegistrationBean() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.setOrder(1); // set order here
        return registrationBean;
    }

}
