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

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jpabook.jpashop.api.MemberApiController.CreateMemberRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    @DisplayName("회원 가입 V1")
    void saveMemberV1Test() throws Exception{

        //given
        Member member = Member.builder().name("memberA").city("Seoul").street("테헤란로").zipcode("123123").build();

        //when
        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").exists())
//                .andExpect(status().isCreated())
                ;

        //then
        List<Member> members = memberService.findMembers();
        assertNotNull(members);
        assertEquals(1, members.size());
        log.info("***debug member: "+members.get(0));
    }

    @Test
    @DisplayName("회원 가입 V2")
    void saveMemberV2Test() throws Exception{
        //given
        CreateMemberRequest request = new CreateMemberRequest();
        request.setName("Member_A");

        //when
        mockMvc.perform(post("/api/v2/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").exists())
//                .andExpect(status().isCreated())
        ;

        //then
        List<Member> members = memberService.findMembers();
        assertNotNull(members);
        assertEquals(1, members.size());
        log.info("***debug member: "+members.get(0));
    }
}