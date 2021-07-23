package hello.springsecurity.controller;

import hello.springsecurity.dto.MemberDto;
import hello.springsecurity.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MemberController {

    private MemberService memberService;

    //메인 페이지
    @GetMapping("/")
    public String main() {
        return "/index";
    }

    //회원가입 페이지
    @GetMapping("/user/signup")
    public String dispSignup(MemberDto memberDto) {
        return "/signup";
    }

    //회원가입 처리
    @PostMapping("user/signup")
    public String execSingup(@Valid MemberDto memberDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            //회원가입 실패시, 입력 데이터 유지
            model.addAttribute("memberDto", memberDto);

            //유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/signup";
        }
        memberService.joinUser(memberDto);

        return "redirect:/user/login";
    }

    //로그인 페이지
    @GetMapping("/user/login")  //SecurityConfig랑 매핑
    public String dispLoign() {
        return "/login";
    }

    //로그인 결과 페이지
    @GetMapping("user/login/result")    //SecurityConfig랑 매핑
    public String dispLoginResult() {
        return "/loginSuccess";
    }

    //로그아웃 결과 페이지
    @GetMapping("user/logout/result")   //SecurityConfig랑 매핑
    public String dispLogout() {
        return "/logout";
    }

    //접근 거부 페이지
    @GetMapping("/user/denied")     //SecurityConfig랑 매핑
    public String dispDenied() {
        return "/denied";
    }

    //내 정보 페이지
    @GetMapping("user/info")
    public String dispMyInfo() {
        return "/myinfo";
    }

    //어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "/admin";
    }
}

















