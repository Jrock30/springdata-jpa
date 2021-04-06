package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberJpaRepository {

    @PersistenceContext // 이 어노테이션을 사용하면 엔티티 매니저를 가져다줌.
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
