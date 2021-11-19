package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
//순수단위 테스트 지향
@ExtendWith(MockitoExtension.class)
class MemberServiceTest1 {
    public static final Logger log =  LoggerFactory.getLogger(MemberServiceTest1.class);
    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;


    @Test
    void join_and_find() {
        //given
        String username = "testusername";
        Member member = new Member();
        member.setName(username);

        //when
        lenient().when(memberRepository.save(member)).thenReturn(1L);
        lenient().when(memberRepository.findById(1L)).thenReturn(member);

        Long savedId = memberService.join(member);
        String returnedName = memberService.FindById(savedId).getName();
        //then
        assertEquals(returnedName, username);
    }

}