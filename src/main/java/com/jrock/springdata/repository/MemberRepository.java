package com.jrock.springdata.repository;

import com.jrock.springdata.dto.MemberDto;
import com.jrock.springdata.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    // 반환타입 컬렉션
    List<Member> findListByUsername(String name);

    // 반환타입 단건
    Member findMemberByUsername(String name);

    // 반환타입 Optional
    Optional<Member> findOptionalByUsername(String name);

    // 카운터 쿼리 분리
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);
//    List<Member> findByAge(int age, Pageable pageable);

    // 벌크성 수정 쿼리

    /**
     * 벌크성 수정, 삭제 쿼리는 @Modifying 어노테이션을 사용 사용하지 않으면 다음 예외 발생
     * - org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations
     * 벌크성 쿼리를 실행하고 나서 영속성 컨텍스트 초기화: @Modifying(clearAutomatically = true) (이 옵션의 기본값은 false )
     * - 이 옵션 없이 회원을 findById로 다시 조회하면 영속성 컨텍스트에 과거 값이 남아서 문제가 될 수 있다.
     * 만약 다시 조회해야 하면 꼭 영속성 컨텍스트를 초기화 하자.
     * <p>
     * 참고: 벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와 DB에 엔티티 상태가 달라질 수 있다.
     * > 권장하는 방안
     * > 1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
     * > 2. 부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화 한다.
     */
    @Modifying(clearAutomatically = true) // jpa .excuteUpdate() 실행, 사용하지 않으면 에러 발생
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    /**
     * EntityGraph 정리
     * 사실상 페치 조인(FETCH JOIN)의 간편 버전
     * LEFT OUTER JOIN 사용
     */
    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    // NamedEntityGraph 사용 방법
    @EntityGraph("Member.all")
    List<Member> findEntityGraphNamedByUsername(String username);
}
