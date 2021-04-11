package com.jrock.springdata.dto;

import com.jrock.springdata.entity.Member;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    // 멤버가 필드로 들어가는 것은 안된다. 아래 처럼은 가능
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
