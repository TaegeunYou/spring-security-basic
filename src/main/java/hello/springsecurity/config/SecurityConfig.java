package hello.springsecurity.config;

import hello.springsecurity.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration  //클래스안에 스프링 빈이 등록되도록 함
@EnableWebSecurity  //Spring Security를 설정할 클래스로 정의
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {  //상속받아서 하는 일반적인 방법

    private MemberService memberService;

    @Bean   // Service에서 비밀번호를 암호화할 수 있도록 Bean으로 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //Spring Security에서 제공하는 비밀번호 암호화 객체
    }

    /**
     * configure() 메서드 오버라이딩
     */

    @Override
    public void configure(WebSecurity web) throws Exception {   //FitlerChainProxy를 생성하는 필터
        // static 디렉토리의 하위 파일 목록은 인증 무시
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {  //HTTP 요청에 대한 웹 기반 보안을 구성
        http
                //페이지 권한 설정
                .authorizeRequests()    //HttpServletRequest에 따라 접근을 제한
                .antMatchers("/admin/**").hasRole("ADMIN")  //특정 경로를 지정하여 롤(역할)에 따른 접근 권한 설정
                .antMatchers("/user/myinfo").hasRole("MEMBER")
                .antMatchers("/**").permitAll()
                //.anyRequest().authenticated() //인증된 사용자만
            .and()
                //로그인 설정
                .formLogin()
                .loginPage("/user/login")   //커스텀 로그인 폼 (기본 로그인 폼 : /login) - 컨트롤러에서 매핑 필요
                .defaultSuccessUrl("/user/login/result")    //로그인 성공시 이동되는 페이지 - 컨트롤러에서 매핑 필요
                .permitAll()
            .and()
                //로그아웃 설정
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))    //기본 URL (/logout) 이 아닌 다른 URL로 재정의
                .logoutSuccessUrl("/user/logout/result")
                .invalidateHttpSession(true)    //HTTP 세션을 초기화하는 작업
                //deleteCookies("key명") //로그아웃 시, 특정 쿠기 제거
            .and()
                // 403 예외처리 핸들링 // .exceptionHandling() 메서드로 예외 처리
                .exceptionHandling().accessDeniedPage("/user/denied");  //접근권한이 없을 때 예외 처리
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  //Spring Security의 모든 인증은 AuthenticationManager을 통해 이루어진다.
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
