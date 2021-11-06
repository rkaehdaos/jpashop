package jpabook.jpashop.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberRepositoryTest {

    @TestConfiguration
    static class MemberRepositoryTestContextConfiguration {
        @Bean
        public MemberRepository memberRepository() {
            return new MemberRepository();
        }
    }

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void test() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("A");

        //when
        Long savedId = memberRepository.save(member);
        Member findedMember = memberRepository.findById(savedId);

        //then - 검증만
        assertThat(findedMember.getId()).isEqualTo(member.getId());
        assertThat(findedMember.getUsername()).isEqualTo(member.getUsername());
    }
}