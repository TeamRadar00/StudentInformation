package com.studentinformation.repository;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberName(String memberName);

    Optional<Member> findByStudentNum(String studentNum);

    List<Member> findByState(MemberState state);
}
