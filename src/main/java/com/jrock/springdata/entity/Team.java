package com.jrock.springdata.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // mappedby 는 foreign key 가 없는 곳에 지정 권장.
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
