package com.denkarz.jcat.backend.security.config;

import com.denkarz.jcat.backend.security.filter.CORSFilter;
import com.denkarz.jcat.backend.security.filter.JwtAuthenticationTokenFilter;
import com.denkarz.jcat.backend.security.jwt.JwtAuthenticationEntryPoint;
import com.denkarz.jcat.backend.security.jwt.JwtAuthenticationProvider;
import com.denkarz.jcat.backend.security.jwt.JwtSuccessHandler;
import com.denkarz.jcat.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Autowired
  private UserService userService;

  @Autowired
  private JwtAuthenticationProvider authenticationProvider;
  @Autowired
  private JwtAuthenticationEntryPoint entryPoint;

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(Collections.singletonList(authenticationProvider));
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationTokenFilter authenticationTokenFilter() {
    JwtAuthenticationTokenFilter filter = new JwtAuthenticationTokenFilter();
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeRequests()
            .antMatchers("api/v1/**").authenticated()
            .antMatchers("api/auth/**").permitAll()
//            .antMatchers("api/test/**").permitAll()
            .and()
            .exceptionHandling().authenticationEntryPoint(entryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
    http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    http.headers().cacheControl();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService)
            .passwordEncoder(this.passwordEncoder());

  }
}
