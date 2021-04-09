package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository 애노테이션 생략 가능
public interface MemberRepository extends JpaRepository<Member, Long> { // Entity Type, PK Type

}
