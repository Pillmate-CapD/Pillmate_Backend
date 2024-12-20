package pillmate.backend.common.exception.errorcode;

public enum ErrorCode {
    NOT_EXIST_PAGE("잘못된 페이지입니다."),
    MISMATCH_EMAIL("이메일이 일치하지 않습니다."),
    MISMATCH_PASSWORD("비밀번호가 일치하지 않습니다."),
    NOT_EXIST_PASSWORD("비밀번호가 존재하지 않습니다. 일반회원인 경우 비밀번호는 필수입니다."),
    ALREADY_EXIST_USER("이미 존재하는 회원입니다."),
    ALREADY_LOGOUT_USER("로그아웃된 회원입니다."),
    MISMATCH_USERNAME_TOKEN("이메일과 토큰값이 일치하지 않습니다."),
    NOT_AUTHORIZE_ACCESS("인증되지 않은 접근입니다."),
    CHECK_FAIL_TOKEN("토큰 검증에 실패했습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN_VALID_TIME("토큰의 유효기간이 만료되었습니다."),
    NOT_FOUND_TOKEN("토큰을 찾을 수 없습니다."),
    NOT_HAVE_PERMISSION("권한이 없습니다."),
    NOT_FOUND_USER("일치하는 회원 정보가 없습니다."),
    NOT_FOUND_MEDICINE("일치하는 약이 없습니다."),
    MISMATCH_TOKEN("토큰명이 일치하지 않습니다."),
    NOT_SUPPORTED_JWT("JWT 토큰이 지원하지 않습니다."),
    NOT_EXPIRED_REFRESH_TOKEN("Refresh Token이 만료되지 않았습니다."),
    INVALID_MEMBER_TYPE("회원 타입이 올바르지 않습니다"),
    NOT_FOUND_SOCIAL_INFO("알맞는 소셜 서비스를 찾을 수 없습니다."),
    NOT_FOUND_DIARY("건강 일지를 찾을 수 없습니다."),
    NOT_DEFAULT_TYPE_USER("소셜 연동 계정입니다. 소셜 로그인을 사용하여 로그인해주세요."),
    NOT_EXIST_PROVIDER_ID("소셜 회원가입에서 providerId 값이 존재하지 않습니다."),
    WEB_CLIENT_ERROR("웹 API 호출 예외 발생. 자세한 건 서버 로그를 참고하세요."),
    NOT_FOUND_DATE("날짜 선택을 해주세요."),
    INVALID_REQUEST_BODY_TYPE("유효하지 않은 요청입니다."),
    NOT_FOUND_MEMBER("멤버를 찾을 수 없습니다."),
    INVALID_SORT_TYPE("정렬 기준이 잘못되었습니다."),
    NOT_PARSING_BODY("JSON 형식이 잘못되었습니다."),
    PAYMENT_NOT_CANCELED("결제가 취소되지 않았습니다."),
    CANT_CONVERT_TO_RESERVATION_SETTING_STATUS("해당하는 탭이 없습니다."),
    INVALID_PAGE_NUMBER("잘못된 페이지 번호입니다."),
    INVALID_REQUEST_PARAM("파라미터가 잘못되었습니다."),
    NOT_FOUND_PAGE("페이지가 없습니다."),
    INVALID_PAGE_SIZE("page size는 양수여야 합니다."),
    NOT_FOUND_ALARM("활성화된 알람이 없습니다."),
    INVALID_MEDICINE("해당 약은 수정이 불가능합니다."),
    NOT_FOUND_MEDICINE_MEMBER("사용자의 약을 찾을 수 없습니다."),
    INVALID_SAME_MEDICINE("이미 존재하는 약이 있습니다."),
    INVALID_EMAIL("이미 존재하는 이메일이 있습니다.");

    private final String message;
    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
