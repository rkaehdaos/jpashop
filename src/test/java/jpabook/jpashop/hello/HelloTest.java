package jpabook.jpashop.hello;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class HelloTest {

    public static final Logger log = LoggerFactory.getLogger(HelloTest.class);

    @Test
    @DisplayName("기본생성 테스트 \uD83D\uDE31 ff \uD83D\uDC4D \u2614 \u1F90F")
//    @DisplayName("기본생성 테스트 \uD83D\u1F605")
    void basic_constructor_test() {
        Hello hello1 = Hello.builder()
                .helloMsg("hello1")
                .helloMsg2("hello1-2")
                .build();
        log.info(hello1.toString());
    }

    @Test
    void this_is_a_just_test_method_for_diplayname_gernerator(){}

}
