package pillmate.backend.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillmate.backend.entity.member.Disease;
import pillmate.backend.entity.member.Member;
import pillmate.backend.entity.member.MemberRole;
import pillmate.backend.entity.member.MemberType;
import pillmate.backend.entity.member.Role;
import pillmate.backend.entity.member.Symptom;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SignUpRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String password;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    @NotNull(message = "질병은 필수입니다.")
    private List<Disease> diseases;

    @NotNull(message = "증상은 필수입니다.")
    private List<Symptom> symptoms;

    @NotNull(message = "권한은 필수입니다.")
    @Size(min = 1, message = "권한은 최소 1개 이상이여야 합니다.")
    private List<String> roles;

    public Member toEntity(String encodedPassword) {
        Member member = Member.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .type(MemberType.DEFAULT)
                .usable(true)
                .build();

        roles.stream().map(
                s -> Role.builder()
                        .value(MemberRole.valueOf(s))
                        .build()
        ).forEach(member::addRole);

        diseases.stream().map(
                d -> Disease.builder()
                        .disease(d.getDisease())
                        .startDate(d.getStartDate())
                        .build()
        ).forEach(member::addDisease);

        symptoms.stream().map(
                s -> Symptom.builder()
                        .name(s.getName())
                        .build()
        ).forEach(member::addSymptom);

        return member;
    }
}
