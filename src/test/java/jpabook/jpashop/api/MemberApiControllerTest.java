package jpabook.jpashop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jpabook.jpashop.api.MemberApiController.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("Member API")
@AutoConfigureMockMvc
@Transactional

class MemberApiControllerTest {
    @Autowired private EntityManager em;
    @Autowired private MockMvc mockMvc;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ObjectMapper objectMapper;


//    @BeforeEach
    void setTestData() {
        //1
        Member userA = Member.builder().name("userA").city("Seoul").street("테헤란로").zipcode("1-A").build();
        Book jpabook1 = Book.builder().name("JPA1").price(10000).stockQuantity(100).build();
        Book jpabook2 = Book.builder().name("JPA2").price(20000).stockQuantity(100).build();
        em.persist(userA);
        em.persist(jpabook1);
        em.persist(jpabook2);

        OrderItem order1Item1 = OrderItem.createOrderItem(jpabook1, 10000, 1);
        OrderItem order1Item2 =OrderItem.createOrderItem(jpabook2,20000,2);
        Order order1 = Order.createOrder(userA, createDelivery(userA), order1Item1, order1Item2);
        em.persist(order1);

        //2
        Member userB = Member.builder().name("userB").city("Asan").street("용연로").zipcode("1-B").build();
        Book springBook1 = Book.builder().name("SPRING1").price(20000).stockQuantity(200).build();
        Book springBook2 = Book.builder().name("SPRING2").price(40000).stockQuantity(300).build();
        em.persist(userB);
        em.persist(springBook1);
        em.persist(springBook2);
        OrderItem order2Item1 = OrderItem.createOrderItem(springBook1, 20000, 3);
        OrderItem order2Item2 =OrderItem.createOrderItem(springBook2,40000,4);
        Order order2 = Order.createOrder(userB, createDelivery(userB), order2Item1, order2Item2);
        em.persist(order2);

    }




    private Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        return delivery;
    }

    @Test
    @DisplayName("회원 가입 V1")
    void saveMemberV1Test() throws Exception {

        //given
        Member member = Member.builder().name("memberA").city("Seoul").street("테헤란로").zipcode("123123").build();

        //when
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(objectMapper.writeValueAsString(member)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").exists())
                .andReturn();
        em.flush();
        em.clear();

        //then
        Long id = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.id", Long.class);
        Member findMember = memberRepository.findById(id);
        assertNotNull(findMember);
    }

    @Test
    @DisplayName("회원 가입 V2")
    void saveMemberV2Test() throws Exception {
        //given
        CreateMemberRequest req = new CreateMemberRequest();
        req.setName("Member_A");

        //when
        MvcResult mvcResult = mockMvc.perform(post("/api/v2/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").exists())
                .andReturn();
        em.flush();
        em.clear();

        //then
        Long id = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.id", Long.class);
        Member findMember = memberRepository.findById(id);
        assertNotNull(findMember);
    }

    @Test
    @DisplayName("회원 수정 V2")
    void updateMemberV2Test() throws Exception {
        //given
        final String CHANGE_NAME = "memberB";
        UpdateMemberRequest req = new UpdateMemberRequest();
        req.setName(CHANGE_NAME);

        Member member = Member.builder().name("memberA").city("Seoul").street("테헤란로").zipcode("123123").build();
        Long memberId = memberRepository.save(member);
        em.flush();
        em.clear();
        //when
        mockMvc.perform(put("/api/v2/members/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("name").value(CHANGE_NAME))
        ;
        em.flush();
        em.clear();

        //then
        Member findMember = memberRepository.findById(memberId);
        assertEquals(CHANGE_NAME, findMember.getName(), "같아야 함");
    }

    @Test
    @DisplayName("회원 조회 v1")
    void listMemberV1Test() throws Exception {
        //given
        final int COUNT = 10;
        IntStream.range(0, COUNT).mapToObj(i -> Member.builder().name("member_" + i).city("Seoul_" + i).street("테헤란로_" + i).zipcode("123123-" + i).build()).forEach(member -> memberRepository.save(member));
        em.flush();
        em.clear();

        //then
        mockMvc.perform(get("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0]").exists())
//                .andExpect(jsonPath("$[9]").exists())
//                .andExpect(jsonPath("$[10]").doesNotExist())
                .andExpect(jsonPath("$.*", hasSize(COUNT))) // 개체 수 판단 : hamcrest Matcher 사용
                .andExpect(jsonPath("$.length()").value(COUNT)) // 길이 판단
                .andExpect(jsonPath("$.size()").value(COUNT)) // 길이 판단
        ;

    }

    @Test
    @DisplayName("회원 조회 v2")
    void listMemberV2Test() throws Exception {
        //given
        final int COUNT = 10;
        IntStream.range(0, COUNT).mapToObj(i -> Member.builder().name("member_" + i).city("Seoul_" + i).street("테헤란로_" + i).zipcode("123123-" + i).build()).forEach(member -> memberRepository.save(member));
        em.flush();
        em.clear();

        //then
        mockMvc.perform(get("/api/v2/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("count").value(COUNT))
                .andExpect(jsonPath("$.data.*", hasSize(10))) // 개체 수
        ;

    }
}