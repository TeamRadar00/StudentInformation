package com.studentinformation.service;


import com.studentinformation.domain.Member;
import com.studentinformation.repository.MemberRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



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

    //  개인정보 갱신할때 넘겨주는 newMember 데이터는 controller에서 DTO를 entity로 변환한거임
    @Transactional
    public Member update(Long oldMemberId, Member newMember){
        Member findMember = findById(oldMemberId);
        findMember.update(newMember);
        return findMember;
    }

    //비밀번호 변경
    @Transactional
    public void updatePassword(Long oldMemberId,String newPassword){
        Member findMember = findById(oldMemberId);

        if(findMember.getPassword().equals(newPassword))
            throw new DuplicateRequestException("newPassword is duplicated oldPassword");

        findMember.changePassword(newPassword);
    }

    //아이디 찾을 때 사용
    public String findStudentNum(String membernum){
        Member findMember = memberRepository.findByStudentNum(membernum)
                .orElseThrow(() -> new IllegalArgumentException("not found memberName data"));
        return findMember.getStudentNum();
    }

    /**
     * 비밀번호 찾을 떄 학생이름,학생번호 사용해서 찾음
     * @param memberName
     * @param studentNum
     */
    public String findPassword(String memberName,String studentNum){
        Member findMember = findByMemberNum(studentNum);
        String name = findMember.getMemberName();
        if(name.equals(memberName)){
            return findMember.getPassword();
        }else{
            throw new IllegalArgumentException("not match memberName with studentNum data");
        }
    }


    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    public Member findByMemberNum(String studentNum){
        Member findMember = memberRepository.findByStudentNum(studentNum)
                .orElseThrow(() -> new IllegalArgumentException("not found studentNum data"));
        return findMember;
    }


}
