package com.footbaltoday.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		//httpSecurity.authorizeRequests().antMatchers("/").permitAll();
		 String[] resources = new String[]{ 
				 "/admin/auth/signup", "/admin/auth/login", "/css/**","/lib/**","/img/**", "/blog/**","/uploads/**", "/" , "/article/*", "/comment/*", "/error"
	        };
		/* httpSecurity.authorizeRequests()
         .antMatchers(resources)
         .permitAll()
         .anyRequest()
         .authenticated()
         .and()
         .formLogin().loginPage("/admin/auth/login").failureUrl("/admin/auth/login?error") .usernameParameter("email").passwordParameter("password");
        
       httpSecurity.csrf().and()
		  .logout()
		  .invalidateHttpSession(true)
       .clearAuthentication(true);
       */
       
       httpSecurity
	        .authorizeRequests()
	        	.antMatchers(resources).permitAll()
	            .antMatchers("/admin/*")
	            .fullyAuthenticated()
	            .and()
	        .formLogin()
	            .loginPage("/admin/auth/login")
	            .defaultSuccessUrl("/admin/dashboard", true)
	            .failureUrl("/admin/auth/login?error")
	            .permitAll()
	            .and()
		        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/admin/auth/logout"))
		        .invalidateHttpSession(true) 
	            .logoutSuccessUrl("/admin/auth/login?logout").deleteCookies("JSESSIONID")
	            .permitAll()
	            .and()
	        .csrf();
        
	}
	

	@Bean
	public SpringSecurityDialect  springSecurityDialect() {
	    return new SpringSecurityDialect();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	 @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/404.html").setViewName("404");
    }
    
    /*
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {

       registry.addResourceHandler("/uploads/**").addResourceLocations("file:///d:/Lab/java/footbaltoday/uploads/"); 
      }*/
}