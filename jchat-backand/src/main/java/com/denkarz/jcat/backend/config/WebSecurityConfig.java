package com.denkarz.jcat.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    return bCryptPasswordEncoder;
  }
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(this.passwordEncoder())
            .usersByUsernameQuery("select email, password, active from users where nickname=?")
            .authoritiesByUsernameQuery("select u.email, u.roles " +
                    "from users u inner join user_role ur " +
                    "on u.id=ur.user_id where u.email=?");
  }
}
