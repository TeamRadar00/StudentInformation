package com.studentinformation.security;

import com.studentinformation.domain.Member;
import com.studentinformation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private static final long serialVersionUID = 9031014487545979674L;
    private final MemberRepository memberRepository;

    /**
     * 정보가 들어왔을 때 회원인지 검사하는 로직
     * @param studentNum the username identifying the user whose data is required.
     */
    @Override
    public UserDetails loadUserByUsername(String studentNum) throws UsernameNotFoundException {
        Member member = memberRepository.findByStudentNum(studentNum).orElseThrow(
                () -> new UsernameNotFoundException(studentNum));
        return new PrincipalDetails(member);
    }
}
