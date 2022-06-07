package com.emsthf.webserv.springboot.config.auth;

import com.emsthf.webserv.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // 스프링 시큐리티 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable
                .and()
                    .authorizeRequests() // URL별 관리 설정
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // antMatchers로 관리 대상을 지정. URL, HTTP 메소드별로 관리 가능. 해당 URL은 permitAll로 전체 열람 권한 부여
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // "/api/v1/**" 주소를 가진 api는 USER 권한을 가진 사람만 열람 가능
                    .anyRequest().authenticated() // 위에서 설정한 값 이외의 URL 처리. authenticated로 나머지 주소들을 인증된 사용자만 허용
                .and()
                    .logout().logoutSuccessUrl("/") // 로그아웃 성공 시 홈 주소로 이동
                .and()
                    .oauth2Login() // 로그인 기능 설정
                        .userInfoEndpoint() // 로그인 성공 후 사용자 정보를 가져올 때의 설정
                            .userService(customOAuth2UserService); // 소셜 로그인 성공 시 후속 조치를 진행할 userService 인터페이스 구현체를 등록
    }
}
