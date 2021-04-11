package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 사용자 정의 구현 클래스
 *   - 규칙: 리포지토리 인터페이스 이름 + Impl
 *   - 스프링 데이터 JPA가 인식해서 스프링 빈으로 등록
 *
 * 스프링 데이터 2.x 부터는 사용자 정의 구현 클래스에 리포지토리 인터페이스 이름 + Impl 을 적용하는 대신에
 * 사용자 정의 인터페이스 명 + Impl 방식도 지원한다.
 * 예를 들어서 위 예제의 MemberRepositoryImpl 대신에 MemberRepositoryCustomImpl 같이 구현해도 된다.
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
