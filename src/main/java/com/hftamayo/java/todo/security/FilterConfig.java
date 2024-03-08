package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.security.jwt.AuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Autowired

    private AuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public FilterRegistrationBean<AuthenticationTokenFilter> jwtFilterRegistrationBean() {
        FilterRegistrationBean<AuthenticationTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationTokenFilter);
        registrationBean.setOrder(1); // set order here
        return registrationBean;
    }

}
