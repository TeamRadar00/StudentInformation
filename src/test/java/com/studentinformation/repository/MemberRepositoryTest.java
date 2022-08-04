package com.studentinformation.repository;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired EntityManager em;
    @Autowired MemberRepository memberRepository;

    @Test
    void basicTest() {
        Member member = new Member("123", "password", "choi", MemberState.inSchool, "공대");
        memberRepository.save(member);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(member).usingRecursiveComparison().isEqualTo(findMember);
    }
}