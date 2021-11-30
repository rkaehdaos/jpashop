package jpabook.jpashop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("Member API")
@AutoConfigureMockMvc
@Transactional
@Slf4j
class MemberApiControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private MemberService memberService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 가입")
    void saveMemberV1Test() throws Exception{
        Member member = Member.builder().name("memberA").city("Seoul").street("테헤란로").zipcode("123123").build();
        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1))
//                .andExpect(status().isCreated())
                ;
    }

}