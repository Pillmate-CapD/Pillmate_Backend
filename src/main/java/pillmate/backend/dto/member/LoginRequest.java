package pillmate.backend.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
}
