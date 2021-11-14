package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    @NonNull
    private MemberRepository memberRepository;

    //가입
    @Transactional
    public Long Join(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //하나 조회
    public Member FindById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //전체조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }



    // 중복 회원 검증
    private void validateDuplicateMember(Member member) {
        List<Member> findMemberList = memberRepository.findByName(member.getUsername());
        if(!findMemberList.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
}
