package hello.springsecurity.dto;

import hello.springsecurity.entity.MemberEntity;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private Long id;

    @Size(min = 3, max = 40, message = "최소 3자이상, 40자이하 작성해야 합니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")    //이메일 양식이여야 함.
    private String email;

    @Size(min = 3, max = 20, message = "최소 3자이상, 20자이하 작성해야 합니다.")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{3,20}",
            message = "비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 3~20자의 비밀번호여야 합니다.")
    private String password;

//    private LocalDateTime createdDate;
//    private LocalDateTime modifiedDate;

    @Builder
    public MemberDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .email(email)
                .password(password)
                .build();
    }


}
