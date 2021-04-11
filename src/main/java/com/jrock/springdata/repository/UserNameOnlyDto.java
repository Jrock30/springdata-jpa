package com.jrock.springdata.repository;

/**
 * 클래스 기반 Projection
 * 다음과 같이 인터페이스가 아닌 구체적인 DTO 형식도 가능 생성자의 파라미터 이름으로 매칭
 */
public class UserNameOnlyDto {

    private final String username;

    // 생성자의 파라메터 명을 분석하니 달라지면 안됨.
    public UserNameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
