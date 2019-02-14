/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harun.springjsfwebproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author HARUN
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig() {
        super();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.formLogin()
                .loginPage("/login.xhtml").permitAll()
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .defaultSuccessUrl("/pages/home.xhtml")
                .failureUrl("/login.xhtml?error=true");
        
        http.authorizeRequests()
                .antMatchers("/javax.faces.resource/**").permitAll()
                .antMatchers("/pages/**").access("hasRole('ADMIN')");
        
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login.xhtml");
        
        http.exceptionHandling()
            .accessDeniedPage("/login.xhtml");
        
        http.csrf().disable();

    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build());
        return manager;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }
}
