package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("회원기능 TEST")
@AutoConfigureMockMvc
@Transactional
@Slf4j
class MemberControllerTest {
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired ModelMapper modelMapper;
    @Test void isModelMapper() { assertNotNull(modelMapper); }

    @Test
    @DisplayName("회원가입 폼 화면 정상")
    void createForm_Test() throws Exception {
        mockMvc.perform(get("/members/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("memberForm"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(content().string(containsString("안뇽")))
        ;
    }

    @Test
    @DisplayName("회원 가입 기능")
    void create_Test() throws Exception {

        MemberForm testData = new MemberForm();
        testData.setName("홍길동");
        testData.setCity("서울");
        testData.setStreet("테헤란로");
        testData.setZipcode("12345");

        mockMvc.perform(post("/members/new")
                .contentType(MediaType.TEXT_PLAIN)
                .param("name", testData.getName())
                .param("city", testData.getCity() )
                .param("street", testData.getStreet() )
                .param("zipcode", testData.getZipcode()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("savedMemberId"))
        ;
        List<Member> members = memberService.findMembers();
        assertNotNull(members);
        assertEquals(1, members.size(), "1개만 저장 되어있어야 함");
        Member findMember = members.get(0);
        assertEquals(testData.getName(), findMember.getName());
        assertEquals(testData.getCity(), findMember.getAddress().getCity());
        assertEquals(testData.getStreet(), findMember.getAddress().getStreet());
        assertEquals(testData.getZipcode(), findMember.getAddress().getZipcode());
    }


    @Test
    @DisplayName("회원 목록")
    void listMembers_Test() throws Exception {
        //given
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            member.setName("member_"+i);
            member.setAddress(new Address("city" + i, "street_" + i, "zip" + i));
            memberService.join(member);
        }
        mockMvc.perform(get("/members"))
                .andDo(print())
        ;
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }


}
