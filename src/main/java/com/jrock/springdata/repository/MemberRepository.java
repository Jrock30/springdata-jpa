package com.jrock.springdata.repository;

import com.jrock.springdata.dto.MemberDto;
import com.jrock.springdata.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

// @Repository 애노테이션 생략 가능
public interface MemberRepository extends JpaRepository<Member, Long> { // Entity Type, PK Type

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //스프링 데이터 JPA로 NamedQuery 사용
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    //메서드에 JPQL 쿼리 작성
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //단순히 값 하나를 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //@Query, 값, DTO 조회하기
    @Query("select new com.jrock.springdata.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);
}
