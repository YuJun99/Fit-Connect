package com.fitconnect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fitconnect.auth.PrincipalDetailsService;
import com.fitconnect.auth.PrincipalOauth2UserService;
import com.fitconnect.filter.JwtFilter;
import com.fitconnect.handler.AuthSuccessHandler;

@Configuration //설정 클래스라고 알려준다
@EnableWebSecurity //Security 를 설정하기 위한 어노테이션
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured,preAuthorize,postAuthorize 어노테이션 활성화 (controller)
public class SecurityConfig {
	
	//jwt 를 쿠키로 저장할때 쿠키의 이름
	@Value("${jwt.name}")
	private String jwtName;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Autowired
	private AuthSuccessHandler authSuccessHandler;
	
	@Bean //메소드에서 리턴되는 SecurityFilterChain 을 bean 으로 만들어준다.
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		//화이트 리스트를 미리 배열에 넣어두기
		String[] whiteList= {"/**", "/auth", "/signup", "/user/**", "/member/**", "/trainer/**"};

	    // 메소드의 매개변수에 HttpSecurity의 참조값이 전달되는데 해당 객체를 이용해서 설정을 한다음
	    httpSecurity
	        .headers(header ->
	            // 동일한 origin에서 iframe을 사용할 수 있도록 설정 (default 값은 사용불가)
	            header.frameOptions(option -> option.sameOrigin()) // SmartEditor에서 필요함
	        )
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(config -> 
	            config
	                .requestMatchers(whiteList).permitAll() // whiteList 요청은 로그인과 상관없이 모두 허용
	                .requestMatchers("/admin/**").hasRole("ADMIN")
	                .requestMatchers("/member/**").hasAnyRole("ADMIN", "MEMBER")
	                .requestMatchers("/trainer/**").hasAnyRole("ADMIN", "TRAINER")
	                .anyRequest().authenticated() // 위에 명시한 이외의 모든 요청은 로그인해야지 요청 가능하게
	        )
	        .oauth2Login(oauth2 -> 
	            oauth2
//	                .loginPage("/auth")
	                .userInfoEndpoint()
	                .userService(principalOauth2UserService)
	                .and()
	                .successHandler(authSuccessHandler)
	        )
	        .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        // 토큰을 검사하는 필터를 security filter가 동작하기 이전에 동작하도록 설정
	        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	    // 설정된 정보대로 SecurityFilterChain 객체를 만들어서 반환한다
	    return httpSecurity.build();
	}
	
	//비밀번호를 암호화 해주는 객체를 bean 으로 만든다.
	@Bean
	PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder();
	}
	//인증 메니저 객체를 bean 으로 만든다. 
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, 
			BCryptPasswordEncoder bCryptPasswordEncoder, PrincipalDetailsService principalDetailsService) throws Exception {
	    
		return http.getSharedObject(AuthenticationManagerBuilder.class) 
	      .userDetailsService(principalDetailsService)
	      .passwordEncoder(bCryptPasswordEncoder)
	      .and()
	      .build();
	}
	
}











