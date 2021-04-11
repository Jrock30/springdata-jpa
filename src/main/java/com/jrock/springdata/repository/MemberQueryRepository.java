package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

// 이런식으로 따로 Repository 분리해서 사용해도 된다.
// QueryDSL, JDBC Connection ...
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
