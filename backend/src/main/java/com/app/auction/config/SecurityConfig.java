package com.app.auction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.app.auction.security.CustomUserDetailsService;
import com.app.auction.security.JwtAuthenticationEntryPoint;
import com.app.auction.security.JwtAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private JwtAuthenticationEntryPoint jwtAthenticat;
	@Autowired 
	private JwtAuthenticationFilter jwtFilter;
	@Autowired
	private CustomUserDetailsService customUser;
	@Override  //override config http method
	protected void configure(HttpSecurity http) throws Exception {
		http.
		csrf()
		.disable()
		.authorizeHttpRequests()
		.antMatchers("/admin").hasRole("ADMIN")
		//.antMatchers(HttpMethod.DELETE).hasAuthority("ADMIN")
		.antMatchers("/api/auth/**").permitAll()
		.antMatchers(HttpMethod.GET).permitAll()
		.antMatchers(HttpMethod.OPTIONS).permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(jwtAthenticat)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
		http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class );
		
	}

	@Override  //auth config userdetails with db
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.customUser).passwordEncoder(passwordEncoder());
	
	}
	@Bean //make a bean and return bcrppassword encoder which password stroed in db in bcryptform
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}


}
