package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> { // Entity Type, PK Type

}
