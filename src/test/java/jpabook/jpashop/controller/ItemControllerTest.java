package jpabook.jpashop.controller;

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
    @Test void isModelMapper() { assertNotNull(modelMapper); }

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
        BookForm bookForm = new BookForm();
        bookForm.setName("책이름");
        bookForm.setPrice(1000);
        bookForm.setStockQuantity(10);
        bookForm.setAuthor("작가이름");
        bookForm.setIsbn("123123");
        mockMvc.perform(post("/items/new")
                        .contentType(MediaType.TEXT_PLAIN)
                        .param("name", bookForm.getName())
                        .param("price", String.valueOf(bookForm.getPrice()))
                        .param("stockQuantity", String.valueOf(bookForm.getStockQuantity()))
                        .param("author", bookForm.getAuthor()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("savedBookId"))
        ;
        List<Item> items = itemRepository.findAll();
        assertNotNull(items);
        assertEquals(1, items.size(), "1개만 저장");
        Item item = items.get(0);
        assertEquals(bookForm.getName(), item.getName());


    }

}