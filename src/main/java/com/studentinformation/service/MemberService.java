package com.studentinformation.service;


import com.studentinformation.domain.Member;
import com.studentinformation.repository.MemberRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;


    // 비밀번호 암호화 해야함
    @Transactional
    public Member addMember(Member member){
        memberRepository.save(member);
        log.info("addMember = {}",member);
        return member;
    }

    /**  개인정보 갱신할때 넘겨주는 newMember 데이터는 controller에서 DTO를 entity로 변환한거임
     * 이거 학생이 바꾸는게 아니라 Admin전용, Admin이 학생 정보 바꿀 떄(휴학, 전과) 사용
     * @param oldMemberId
     * @param newMember
     */
    @Transactional
    public Member update(Long oldMemberId, Member newMember){
        Member findMember = findById(oldMemberId);
        findMember.update(newMember);
        log.info("updateMember = {}",findMember);
        return findMember;
    }

    //비밀번호 변경
    @Transactional
    public Member updatePassword(Long oldMemberId,String newPassword){
        Member findMember = findById(oldMemberId);

        if(findMember.getPassword().equals(newPassword))
            throw new DuplicateRequestException("newPassword is duplicated oldPassword");
        else{
            log.info("changePassword = {}, oldPassword= {}",newPassword,findMember.getPassword());
            findMember.changePassword(newPassword);
            return findMember;
        }
    }

    //아이디 찾을 때 사용
    public String findStudentNum(String memberName){
        Member findMember = memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new IllegalArgumentException("not found memberName data"));
        return findMember.getStudentNum();
    }

    /**
     * 비밀번호 찾을 떄 학생이름,학생번호 사용해서 찾음
     * @param memberName
     * @param memberNum
     */
    public String findPassword(String memberName,String memberNum){
        Member findMember = findByMemberNum(memberNum);
        String name = findMember.getMemberName();
        if(name.equals(memberName)){
            return findMember.getPassword();
        }else{
            throw new IllegalArgumentException("not match memberName with studentNum data");
        }
    }

    public Member login(String studentNum, String password) {
        return memberRepository.findByStudentNum(studentNum)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }


    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    public Member findByMemberNum(String memberNum){
        return memberRepository.findByStudentNum(memberNum)
                .orElseThrow(() -> new IllegalArgumentException("not found studentNum data"));
    }

}
