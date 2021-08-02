package jpabook.jpashop.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void memberTest() {
        //given
        Member member = new Member();
        member.setUsername("A");

        //when
        Long savedId = memberRepository.save(member);
        Member findedMember = memberRepository.find(savedId);

        //then - 검증만
        assertThat(findedMember.getId()).isEqualTo(member.getId());
        assertThat(findedMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findedMember).isEqualTo(member);
//        log.info(String.valueOf(findedMember));
//        log.info(String.valueOf(member));

    }

}