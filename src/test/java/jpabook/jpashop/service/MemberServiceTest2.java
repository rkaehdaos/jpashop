package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MemberServiceTest2 {

    @Autowired EntityManager em;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void member_join_test() {
        //given
        Member member = new Member();
        member.setUsername("kim");

        //when
        Long joinedId = memberService.join(member);
        em.flush();
        em.clear();

        //then
        assertEquals(member, memberRepository.findById(joinedId));
    }

    @Test
    void duplicated_exception() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setUsername("kim");
        member2.setUsername("kim");

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2); //예외발생 기대
        });
    }
}
