package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository 애노테이션 생략 가능
public interface TeamRepository extends JpaRepository<Team, Long> {



}
