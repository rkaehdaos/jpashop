package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form, RedirectAttributes redirectAttributes) {
        Book book = modelMapper.map(form, Book.class);

        Long savedBookId = itemService.save(book);
        redirectAttributes.addFlashAttribute("savedBookId", savedBookId);
        //아직 안만든 책 화면은 주석처리
/*
        return "redirect:/items";
*/

        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book book =(Book) itemService.findOne(itemId); //예제를 단순화 하기 위해서 Book만
        BookForm bookForm = modelMapper.map(book, BookForm.class);
        model.addAttribute("form", bookForm);
        return "items/updateItemForm";

    }
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm bookForm){
        Book book = modelMapper.map(bookForm, Book.class);
        itemService.save(book);
        return "redirect:/items";
    }
}
