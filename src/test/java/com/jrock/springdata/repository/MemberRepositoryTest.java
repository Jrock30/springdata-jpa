package com.jrock.springdata.repository;

import com.jrock.springdata.dto.MemberDto;
import com.jrock.springdata.entity.Member;
import com.jrock.springdata.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    // 이런식으로 따로 Repository 분리해서 사용해도 된다.
    @Autowired
    MemberQueryRepository memberQueryRepository;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        //when
//        Optional<Member> byId = memberRepository.findById(savedMember.getId()); // java8 optional 제공
        Member findMember = memberRepository.findById(savedMember.getId()).get(); // .get()으로 바로 쓰면 안되고 널체크 체인 같은거 걸어서 사용함.

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        // 영속성 컨텍스트로 인해 isEqualTo 로 비교가 가능, 원래는 == 비교.
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() throws Exception {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        assertThat(usernameList.get(0)).isEqualTo(m1.getUsername());
    }

    @Test
    public void findMemberDto() throws Exception {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = "+ dto);
        }
    }

    @Test
    public void findByNames() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member member : result) {
            System.out.println(member);
        }
    }

    @Test
    public void returnType() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
    }

    @Test
    public void paging() throws Exception {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        // 0페이지 부터, 3페이지 까지, 정렬 방법
        // 주의: Page는 1부터 시작이 아니라 0부터 시작이다.
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        // total count 쿼리까지 같이 날림.(페이징)
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // Entity로 반환하면 안 되기 때문에 .map 메서드를 활용하여 DTO로 변경
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // slice 는 전체 카운트를 가져오지 않는다. limit 가 + 1 되어 쿼리가 날라감. (페이징이 아니고 더보기 형태의)
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        // 바로 List로 받아도 된다.
//        List<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent(); // 조회된 데이터
        // total count
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3); // 조회된 데이터
        assertThat(page.getTotalElements()).isEqualTo(5);   // 전체 데이터 수
        assertThat(page.getTotalPages()).isEqualTo(2);  // 페이지 번호
        assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가?

    }

    /**
     * 벌크성 수정, 삭제 쿼리는 @Modifying 어노테이션을 사용 사용하지 않으면 다음 예외 발생
     *   - org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations
     * 벌크성 쿼리를 실행하고 나서 영속성 컨텍스트 초기화: @Modifying(clearAutomatically = true) (이 옵션의 기본값은 false )
     *   - 이 옵션 없이 회원을 findById로 다시 조회하면 영속성 컨텍스트에 과거 값이 남아서 문제가 될 수 있다.
     *     만약 다시 조회해야 하면 꼭 영속성 컨텍스트를 초기화 하자.
     *
     * 참고: 벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와 DB에 엔티티 상태가 달라질 수 있다.
     * > 권장하는 방안
     * > 1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
     * > 2. 부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화 한다.
     */
    @Test
    public void bulkUpdate() throws Exception {

        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        // @Modifying(clearAutomatically = true) 옵션을 true로 주면 밑에 clear 안해도 된다.
        em.clear(); // 영속성 컨텍스트 clear (clear를 해주어야지 벌크 연산 이후에 DB 재 조회를 통해 영속성 컨텍스트가 맞추어 진다.)

        List<Member> result = memberRepository.findByUsername("member5");
        Member member = result.get(0);
        System.out.println("member5 = "+ member);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        /**
         * member1 -> teamA
         * member2 -> teamB
         */
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when   N + 1
//        List<Member> members = memberRepository.findMemberFetchJoin();
        List<Member> members = memberRepository.findAll();
//        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
//        List<Member> members = memberRepository.findEntityGraphNamedByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }

        //then
    }

    @Test
    public void queryHint() throws Exception {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
        //then
    }

    @Test
    public void lock() throws Exception {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));


        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom() throws Exception {
        List<Member> result = memberRepository.findMemberCustom();
    }

    /**
     * 장점
     * 동적 쿼리를 편리하게 처리
     * 도메인 객체를 그대로 사용
     * 데이터 저장소를 RDB에서 NOSQL로 변경해도 코드 변경이 없게 추상화 되어 있음 스프링 데이터 JPA JpaRepository 인터페이스에 이미 포함
     * 단점
     * 조인은 가능하지만 내부 조인(INNER JOIN)만 가능함 외부 조인(LEFT JOIN) 안됨
     * 다음과 같은 중첩 제약조건 안됨 firstname = ?0 or (firstname = ?1 and lastname = ?2)
     * 매칭 조건이 매우 단순함
     * 문자는 starts/contains/ends/regex 다른 속성은 정확한매칭( = )만지원
     * 정리
     * 실무에서 사용하기에는 매칭 조건이 너무 단순하고, LEFT 조인이 안됨 실무에서는 QueryDSL을 사용하자
     * @throws Exception
     */
    @Test
    public void QueryByExampleTest() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        em.persist(new Member("m1", 0, teamA));
        em.persist(new Member("m2", 0, teamA));
        em.flush();

        //when
        // Probe 생성
        Member member = new Member("m1");
        Team team = new Team("teamA"); //내부조인으로 teamA 가능
        member.setTeam(team);

        //ExampleMatcher 생성, age 프로퍼티는 무시
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    /**
     * 조회할 엔티티의 필드를 getter 형식으로 지정하면 해당 필드만 선택해서 조회(Projection)
     *
     * 주의
     *   - 프로젝션 대상이 root 엔티티면, JPQL SELECT 절 최적화 가능 프로젝션 대상이 ROOT가 아니면
     *   - LEFT OUTER JOIN 처리
     *   - 모든 필드를 SELECT해서 엔티티로 조회한 다음에 계산
     *
     * 정리
     *   - 프로젝션 대상이 root 엔티티면 유용하다.
     *   - 프로젝션 대상이 root 엔티티를 넘어가면 JPQL SELECT 최적화가 안된다! 실무의 복잡한 쿼리를 해결하기에는 한계가 있다.
     *   - 실무에서는 단순할 때만 사용하고, 조금만 복잡해지면 QueryDSL을 사용하자
     */
    @Test
    public void projections() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();

        //when
//        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");
//        List<UserNameOnlyDto> result = memberRepository.findProjectionsByUsername("m1");
//        List<UserNameOnlyDto> result = memberRepository.findProjectionsByUsername("m1", UserNameOnlyDto.class);
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1", NestedClosedProjections.class);

//        for (UsernameOnly usernameOnly : result) {
        for (NestedClosedProjections nestedClosedProjections : result) {
            String username = nestedClosedProjections.getUsername();
            System.out.println("username = " + username);
            String teamName = nestedClosedProjections.getTeam().getName();
            System.out.println("teamName = " + teamName);
        }

        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    /**
     * 네이티브 쿼리
     *   - 가급적 네이티브 쿼리는 사용하지 않는게 좋음, 정말 어쩔 수 없을 때 사용 최근에 나온 궁극의 방법 스프링 데이터 Projections 활용
     * 스프링 데이터 JPA 기반 네이티브 쿼리 페이징 지원
     *
     * 반환 타입
     *   - Object[], Tuple, DTO(스프링 데이터 인터페이스 Projections 지원)
     *
     * 제약
     *   - Sort 파라미터를 통한 정렬이 정상 동작하지 않을 수 있음(믿지 말고 직접 처리)
     *   - JPQL처럼 애플리케이션 로딩 시점에 문법 확인 불가
     *   - 동적 쿼리 불가
     *
     * 네이티브 SQL을 DTO로 조회할 때는 JdbcTemplate or myBatis 권장
     */
    @Test
    public void nativeQuery() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
//        Member result = memberRepository.findByNativeQuery("m1");
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection = " + memberProjection.getUsername());
            System.out.println("memberProjection = " + memberProjection.getTeamName());
        }
    }
}
