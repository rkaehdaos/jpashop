package jpabook.jpashop.hello;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = H2)
@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HelloRepositoryTest {

    @TestConfiguration
    static class HelloRepositoryTestContextConfiguration {
        @Bean
        public HelloRepository helloRepository() {
            return new HelloRepository();
        }
    }

    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    private HelloRepository helloRepository;

    @Test
    void basic_test() {
        //given
        Hello helloA = Hello.builder()
                .helloMsg("helloA")
                .helloMsg2(null)
                .build();
        //when
        Long savedId = helloRepository.save(helloA);
        testEntityManager.flush();
        testEntityManager.clear();
        Hello findedHello = helloRepository.findById(savedId);

        //then
        assertEquals(helloA, findedHello);
    }


}