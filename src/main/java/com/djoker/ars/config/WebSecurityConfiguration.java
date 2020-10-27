package com.djoker.ars.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired 
	private UserDetailsService userDetailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		// cung cấp userservice cho spring security và password encoder
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);         
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		String loginPage = "/login";
		String logoutPage = "/logout";
		
		// phân quyền request
		httpSecurity.authorizeRequests()
		   .antMatchers("/").permitAll()                      // permitAll(): cho phép tất cả mọi người truy cập vào địa chỉ này(không cần login)
		   .antMatchers("/**").permitAll()
		   .antMatchers(loginPage).permitAll()                
		   .antMatchers("/login").permitAll()
		   .antMatchers("/register").permitAll()
		   .antMatchers("/admin/**").hasAuthority("ADMIN")    // Để truy cập "/admin/**", bạn cần được xác thực VÀ có quyền ADMIN.
		   .anyRequest().authenticated()                      // tất cả các request khác đều cần phải xác thực mới được truy cập(phải đăng nhập)
		   .and().csrf().disable()
		   .formLogin().loginPage(loginPage).loginPage("/")   // cho phép người dùng xác thực bằng form login(với đường dẫn tới trang login là "/login")
		   .failureUrl("/login?error=true")                   // đường dẫn tới trang đăng nhập thất bại
		   .defaultSuccessUrl("/admin/home")                  // đường dẫn tới trang đăng nhập thành công
		   .usernameParameter("username")                     // gía trị của thuộc tính name của 2 input nhập username và password
		   .passwordParameter("password")
		   .and()
		   .logout().logoutRequestMatcher(new AntPathRequestMatcher(logoutPage)).logoutSuccessUrl(loginPage)
		   .and().exceptionHandling();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// Spring Security nên loại bỏ các URLs có dạng:
		web.ignoring().antMatchers("/webjars/**", "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");		
	}
	
}






