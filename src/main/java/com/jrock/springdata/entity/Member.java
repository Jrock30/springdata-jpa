package com.jrock.springdata.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter // 세터는 안쓰는 것이 좋음
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 protected로 생성
@ToString(of = {"id", "username", "age"}) // @ToString은 가급적 내부 필드만(연관관계 없는 필드만, team 같은), 이렇게 하면 {} 안의 필드로 ToString 생성
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) // 모든 연관관계는 LAZY로 설정 ManyToOne은 기본이 LAZY가 아니니 꼭 바꿀 것
    @JoinColumn(name = "team_id")
    private Team team;

    /**
     * 기본생성자 필요 JPA, acces level protected 는 외부공개를 안한다고 보자.(프록시 객체를 만들 때 private으로 만들면 접근이 안되므로 protected)
     * @NoArgsConstructor(access = AccessLevel.PROTECTED) 로 인해 아래 생략
     */
//    protected Member() {}

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
