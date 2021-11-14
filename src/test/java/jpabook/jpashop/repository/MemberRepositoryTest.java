package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
@DisplayName("회원 리포지토리 테스트")
class MemberRepositoryTest {

    /**
     * "DataJpaTest"시 스프링 데이터 JPA아닌 부분은 빈등록 안됨
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
    @DisplayName("기본 저장후 찾기")
    void basic_test() {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        testEntityManager.flush();
        testEntityManager.clear();
        Member findedMember = memberRepository.findById(savedId);

        log.info(String.valueOf(findedMember));

        //then - 검증만
        assertThat(findedMember.getId()).isEqualTo(member.getId());
        assertThat(findedMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(member).isEqualTo(findedMember);
    }

    @Test
    @DisplayName("테스트 엔티티 사용 테스트")
    void entity_test()  {
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

    @Test
    void findByName_test() {
        //given
        String username = "username";
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setUsername(username);
            memberRepository.save(member);
        }

        //when
        testEntityManager.flush();
        testEntityManager.clear();
        List<Member> memberList = memberRepository.findByName(username);



        //then - 검증만
        assertThat(memberList.size()).isEqualTo(10);

    }

    @Test
    void findAll_test()  {
        List<Member> memberList = new ArrayList<>();
        //given
        for (int i = 0; i < 100; i++) {
            Member member = new Member();
            member.setUsername("member_"+i);
            memberRepository.save(member);
        }

        //when
        testEntityManager.flush();
        testEntityManager.clear();
        List<Member> findMembers = memberRepository.findAll();

        //then
        log.debug(findMembers.stream()
                .map(Member::getUsername)
                .collect(Collectors.joining("\n")));
        assertThat(findMembers.size()).isEqualTo(100);
    }
}