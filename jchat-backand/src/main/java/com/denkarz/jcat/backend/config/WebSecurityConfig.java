package com.denkarz.jcat.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private DataSource dataSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
            .authorizeRequests()
            .antMatchers("/*").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/get")
            .permitAll()
            .and()
            .logout()
            .permitAll();
    http
            // Todo: Enable csrf
            .cors()
            .and()
            .csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .usersByUsernameQuery("select email, password, active from users where nickname=?")
            .authoritiesByUsernameQuery("select u.email, u.roles " +
                    "from users u inner join user_role ur " +
                    "on u.id=ur.user_id where u.email=?");
  }
}
