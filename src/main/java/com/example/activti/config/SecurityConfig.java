package com.example.activti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SecurityConfig
 *
 * @author Joyuce
 * @date 2020年07月06日
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore  放行error页面
        web.ignoring().antMatchers("/api/**").antMatchers("/error").antMatchers("/models/**").antMatchers("/service/**")
           .antMatchers("/deployments/**");
        //web.ignoring().anyRequest();
    }

    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    //    //// 关闭csrf保护功能（跨域访问）
    //    //http.csrf().disable();
    //    //http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());
    //}
}