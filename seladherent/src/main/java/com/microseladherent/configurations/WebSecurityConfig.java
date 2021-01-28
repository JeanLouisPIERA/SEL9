package com.microseladherent.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
/*import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;*/
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.microseladherent.entities.RoleEnum;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {
/*
extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	
	  @Qualifier("userDetailsServiceImpl")
	  
	  @Autowired private UserDetailsService userDetailsService;
	  
	  @Autowired private AdherentBasicAuthenticationEntryPoint
	  authenticationEntryPoint;
	  
	  @Value("${prop.swagger.enabled}") private boolean enableSwagger;
	  
	 *//**
		 * Création du configurateur Swagger
		 * 
		 * @return
		 *//*
	
	  @Bean public Docket SwaggerConfig() {
	  System.out.println("swagger="+enableSwagger); return new
	  Docket(DocumentationType.SWAGGER_2) .enable(enableSwagger) .select()
	  .apis(RequestHandlerSelectors.basePackage("${prop.swagger.basepackage}"))
	  .paths(PathSelectors.any()) .build(); }
	 *//**
		 * Methode de WebMvcConfigurer qui permet d'accéder à SWAGGER avec SprinSecurity
		 * 
		 * @param web
		 * @throws Exception
		 *//*
	
	  
	  @Override public void configure(WebSecurity web) throws Exception { if
	  (enableSwagger) web.ignoring().antMatchers("/v2/api-docs",
	  "/configuration/ui", "/swagger-resources/**", "/configuration/security",
	  "/swagger-ui.html", "/webjars/**"); }
	  
	  
	 *//**
		 * Methode de WebMvcConfigurer qui permet d'accéder à SWAGGER
		 * 
		 * @param registry
		 *//*
	
	  @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
	  if (enableSwagger) {
	  registry.addResourceHandler("swagger-ui.html").addResourceLocations(
	  "classpath:/META-INF/resources/");
	  registry.addResourceHandler("/webjars/**").addResourceLocations(
	  "classpath:/META-INF/resources/webjars/"); } }
	  
	  
	  
	 *//**
		 * Création d'un mot de passe crypté.
		 *//*
	
	  @Bean public BCryptPasswordEncoder bCryptPasswordEncoder() { return new
	  BCryptPasswordEncoder(); }
	  
	 *//**
		 * Configuration des autorisations par les rôles. Elle définit quels URLS sont
		 * sécurisés ou pas (comme / )
		 *//*
	
	  
	  
	  @Override protected void configure(HttpSecurity http) throws Exception { http
	  .csrf().disable() .authorizeRequests() .antMatchers(HttpMethod.POST,
	  "/sel/users/accounts", "/sel/users/login" ).permitAll()
	  .antMatchers(HttpMethod.POST, "/sel/users/login" ).permitAll()
	  .antMatchers(HttpMethod.GET,
	  "/sel/bureau/accounts").hasAnyAuthority(RoleEnum.ADMIN.toString(),
	  RoleEnum.BUREAU.toString()) .antMatchers(HttpMethod.GET,
	  "/sel/users/accounts").hasAnyAuthority(RoleEnum.ADHERENT.toString())
	  .antMatchers(HttpMethod.POST,
	  "/sel//admin/accounts").hasAnyAuthority(RoleEnum.ADMIN.toString())
	  .antMatchers("/**").authenticated() .anyRequest().authenticated() .and()
	  .httpBasic() .authenticationEntryPoint(authenticationEntryPoint) ;
	  
	  }
	  
	 *//**
		 * Méthode qui permet de vérifier les credentials
		 * 
		 * @return
		 * @throws Exception
		 *//*
	
	  @Bean public AuthenticationManager customAuthenticationManager() throws
	  Exception { return authenticationManager(); }
	  
	  
	 *//**
		 * Création du Manager d'Authentification
		 * 
		 * @param auth
		 * @throws Exception
		 *//*
			  @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
			  throws Exception {
			  auth.userDetailsService(userDetailsService).passwordEncoder(
			  bCryptPasswordEncoder()); }
			  
			 


*/
}