package changwonNationalUniv.koko.dto.request;

import changwonNationalUniv.koko.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberRequest {

    @NotEmpty(message = "이름은 필수항목입니다.")
    @Length(min = 0, max = 30, message = "이름은 30글자 이내로 입력해주세요.")
    private String name;

    @Length(min = 5, max = 20, message = "아이디는 5~20글자 이내로 입력해주세요")
    private String userId;

    @Length(min = 5, max = 20, message = "비밀번호는 5~20글자 이내로 입력해주세요")
    private String password;

    private String checkingPassword;

    private String mobile;

    @NotEmpty(message = "이메일은 필수항목입니다")
    @Email(message = "이메일 형식을 정확하게 입력해주세요")
    private String email;


    public Member toEntity(){
        return Member.builder()
                .name(name)
                .userId(userId)
                .password(password)
                .mobile(mobile)
                .email(email)
                .build();
    }
}
