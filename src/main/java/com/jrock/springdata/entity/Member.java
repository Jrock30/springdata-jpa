package com.jrock.springdata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
//@Setter // 세터는 안쓰는 것이 좋음
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String username;

    // 기본생성자 필요 JPA, acces level protected 는 외부공개를 안한다고 보자.(프록시 객체를 만들 때 private으로 만들면 접근이 안되므로 protected)
    protected Member() {
    }

    public Member(String username) {
        this.username = username;
    }
}
