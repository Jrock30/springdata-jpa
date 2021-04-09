package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// @Repository 애노테이션 생략 가능
public interface MemberRepository extends JpaRepository<Member, Long> { // Entity Type, PK Type

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
