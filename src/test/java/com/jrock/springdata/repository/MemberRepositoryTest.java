package com.jrock.springdata.repository;

import com.jrock.springdata.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

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
}
