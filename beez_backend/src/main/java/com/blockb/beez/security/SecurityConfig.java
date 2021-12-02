package com.blockb.beez.security;
import com.blockb.beez.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .httpBasic().disable() // 기본 설정 사용 안함
                .csrf().disable() // csrf 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안함

                .and()
                .authorizeRequests()
                .antMatchers("/*/login", "/*/join", "/*/join/*", "/*/login/*", "/*/StoreList", "/*/find/*").permitAll() //일단 charge는 권한 부여(추후 제거)
                .antMatchers(HttpMethod.POST,"/api/charge/*").hasRole("USER")
                // .antMatchers(HttpMethod.POST,"/api/pass/*").hasRole("USER")
                .antMatchers(HttpMethod.POST,"/api/exchange/*").hasRole("STORE")
                .antMatchers(HttpMethod.POST,"/api/withdrawal/*").hasRole("STORE")
                .anyRequest().authenticated() //authenticated()

                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                // 토큰 인증 필터 추가
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // ID, Password 검사 전에 jwt 필터 먼저 수
                .headers()
                    .httpStrictTransportSecurity()
                    .maxAgeInSeconds(0)
                    .includeSubDomains(true);
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // @Override
    // public void configure(WebSecurity web) throws Exception {
    //     web
    //             .ignoring()
    //             .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**"); // swagger 관련 요청은 허용
    // }
}