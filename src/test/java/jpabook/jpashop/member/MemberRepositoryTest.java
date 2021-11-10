package jpabook.jpashop.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest

// 테스트 db 지정
// 프로퍼티로 설정 가능
//  spring.test.database.connection: H2
//@AutoConfigureTestDatabase(connection = H2)

// 기본값 : 기본적으로 내장된 임베디드 db 사용
//@AutoConfigureTestDatabase(replace = ANY)

// @ActiveProfiles에 설정한 프로파일 환경값에
// 따라 데이터 소스가 적용
// yml 파일에서 프로퍼티 설정을
// spring.test.database.replace: NONE
// 으로 변경가능
@AutoConfigureTestDatabase(replace = NONE)
@Slf4j
class MemberRepositoryTest {

    /**
     * @DataJpaTest시 스프링 데이터 JPA아닌 부분은 빈등록 안됨
     * 따라서 테스트 컨피규로 필요한 빈등록
     */
    @TestConfiguration
    static class MemberRepositoryTestContextConfiguration {
        @Bean
        public MemberRepository memberRepository() {
            return new MemberRepository();
        }
    }

    // @DataJpaTest시 자동으로 빈등록되므로 주입받아서 사용
    // EntityManager에 비해 메서드가 적으나 테스트하기엔 충분
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 기본테스트() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member findedMember = memberRepository.findById(savedId);

        log.info(String.valueOf(findedMember));

        //then - 검증만
        assertThat(findedMember.getId()).isEqualTo(member.getId());
        assertThat(findedMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(member).isEqualTo(findedMember);
    }

    @Test
    void 테스트엔티티테스트() throws Exception {
        //given
        Member memberA = new Member();
        memberA.setUsername("memberA");

        //when
        Member returnedMemberA = testEntityManager.persist(memberA);
        Member findedmember = memberRepository.findById(returnedMemberA.getId());

        //then
        assertThat(memberA).isEqualTo(returnedMemberA);
        assertThat(memberA).isEqualTo(findedmember);
    }
}