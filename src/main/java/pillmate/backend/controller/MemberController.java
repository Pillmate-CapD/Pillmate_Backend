package pillmate.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pillmate.backend.common.util.LoggedInMember;
import pillmate.backend.dto.member.FindPasswordRequest;
import pillmate.backend.dto.member.FindPasswordResponse;
import pillmate.backend.dto.member.JwtTokenResponse;
import pillmate.backend.dto.member.LoginRequest;
import pillmate.backend.dto.member.LoginResponse;
import pillmate.backend.dto.member.LogoutResponse;
import pillmate.backend.dto.member.ModifyPasswordRequest;
import pillmate.backend.dto.member.SignUpRequest;
import pillmate.backend.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    private static final String REFRESH_TOKEN = "refresh_token";

    @PostMapping("/signup")
    public JwtTokenResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return memberService.register(signUpRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return memberService.issueToken(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/password")
    public FindPasswordResponse findPassword(@RequestBody @Valid FindPasswordRequest findPasswordRequest) {
        return memberService.issueTemporaryPassword(findPasswordRequest);
    }

    @PostMapping("/logout")
    public LogoutResponse logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        return memberService.expireToken(accessToken);
    }

    @PostMapping("/reissue")
    public LoginResponse reissue(@LoggedInMember Long memberId, @CookieValue(REFRESH_TOKEN) String refreshToken) {
        return memberService.reissueToken(memberId, refreshToken);
    }

    @PatchMapping("/password")
    public void modifyPassword(@LoggedInMember Long memberId, @RequestBody ModifyPasswordRequest modifyPasswordRequest) {
        memberService.modifyPassword(memberId, modifyPasswordRequest);
    }
}
