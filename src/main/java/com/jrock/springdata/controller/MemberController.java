package com.jrock.springdata.controller;

import com.jrock.springdata.dto.MemberDto;
import com.jrock.springdata.entity.Member;
import com.jrock.springdata.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * HTTP 요청은 회원 id를 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환
     * 도메인 클래스 컨버터도 리파지토리를 사용해서 엔티티를 찾음
     * 주의: 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면,
     *      이 엔티티는 단순 조회용으로만 사용해야 한다.
     *      (트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않는다.)
     */
    // 도메인 클래스 컨버터 사용 후
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * 요청 파라미터
     * 예) /members?page=0&size=3&sort=id,desc&sort=username,desc
     * page: 현재 페이지, 0부터 시작한다.
     * size: 한 페이지에 노출할 데이터 건수
     * sort: 정렬 조건을 정의한다. 예) 정렬 속성,정렬 속성...(ASC | DESC), 정렬 방향을 변경하고 싶으면 sort
     * 파라미터 추가 ( asc 생략 가능)
     *
     * 접두사
     *   페이징 정보가 둘 이상이면 접두사로 구분
     *   @Qualifier 에 접두사명 추가 "{접두사명}_xxx”
     *   예제: /members?member_page=0&order_page=1
     *    public String list(
     *       @Qualifier("member") Pageable memberPageable,
     *       @Qualifier("order") Pageable orderPageable, ...
     */
    @GetMapping("/members")
//    public Page<Member> list(Pageable pageable) {
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) { // 페이지 default 설정(글로벌 보다 우선순위)
        Page<Member> page = memberRepository.findAll(pageable);

        // Entity -> DTO
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
//        Page<MemberDto> map = page.map(member -> new MemberDto(member);
        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("userA" + i, i));
        }
    }
}
