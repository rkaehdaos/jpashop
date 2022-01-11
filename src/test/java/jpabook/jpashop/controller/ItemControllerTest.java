package jpabook.jpashop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DisplayName("상품기능 TEST")
@AutoConfigureMockMvc
@Transactional
@Slf4j
class ItemControllerTest {
    @Autowired private ItemService itemService;
    @Autowired private ItemRepository itemRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private ModelMapper modelMapper;
    @Autowired private ObjectMapper objectMapper;
    @Test void isModelMapper() { assertNotNull(modelMapper); }
    @Test void isObjectMapper() { assertNotNull(objectMapper); }

    @Test
    @DisplayName(" 상품 등록 화면 ")
    void createForm_Test() throws Exception {
        mockMvc.perform(get("/items/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("form"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    @DisplayName("책 등록 기능")
    void create_Test() throws Exception {

        //given
        BookForm bookForm = new BookForm();
        bookForm.setName("책이름");
        bookForm.setPrice(1000);
        bookForm.setStockQuantity(10);
        bookForm.setAuthor("작가이름");
        bookForm.setIsbn("123123");

        //when
        MvcResult mvcResult = mockMvc.perform(post("/items/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", bookForm.getName())
                        .param("price", String.valueOf(bookForm.getPrice()))
                        .param("stockQuantity", String.valueOf(bookForm.getStockQuantity()))
                        .param("author", bookForm.getAuthor()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("savedBookId"))
                .andReturn();
        Long returnedBookId = Long.valueOf(String.valueOf(mvcResult.getFlashMap().get("savedBookId")));

        //then
        Item item = itemRepository.findById(returnedBookId);
        assertEquals(bookForm.getName(), item.getName());
    }

    @Test
    @DisplayName("삼품 목록")
    void listItems_test() throws Exception {

        //given
        for (int i = 0; i < 10; i++) {
            itemService.save(Book.builder().name("book_" + i).price(1000).stockQuantity(10).author("author_" + i).isbn("isbn-" + i).build());
        }
        //then
        mockMvc.perform(get("/items"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("상품 수정")
    void updateItems_test() throws Exception {

        //given
        BookForm bookForm = new BookForm();
        bookForm.setName("bookName");
        bookForm.setPrice(1000);
        bookForm.setStockQuantity(10);
        bookForm.setAuthor("작가이름");
        bookForm.setIsbn("123123");
        Book book = modelMapper.map(bookForm, Book.class);
        Long savedItemId = itemRepository.save(book);
        assertNotNull(savedItemId);


        bookForm.setId(savedItemId);
        bookForm.setName("changedName");
        log.info("************bookForm"+bookForm);
        //when

        mockMvc.perform(post("/items/" + savedItemId + "/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(bookForm.getId()))
                        .param("name", bookForm.getName())
                        .param("price", String.valueOf(bookForm.getPrice()))
                        .param("stockQuantity", String.valueOf(bookForm.getStockQuantity()))
                        .param("author", bookForm.getAuthor())
                        .param("isbn", bookForm.getIsbn())
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("savedBookId"))
        ;


        //then
        Item findItemById = itemRepository.findById(savedItemId);
        assertEquals("changedName", findItemById.getName());
    }



}