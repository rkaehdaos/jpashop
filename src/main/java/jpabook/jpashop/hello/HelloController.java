package jpabook.jpashop.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final HelloService helloService;

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!!");
        return "hello";
    }

    @GetMapping("hello/{id}")
    @ResponseBody
    public String hello_id(@PathVariable Long id) {
        return helloService.getHelloMsg(id);
    }

    @PostMapping(value = "hello/{msg}")
    @ResponseBody

    public ResponseEntity<String> helloPost(@PathVariable @Validated String msg) {
        Long returnedId = helloService.setHelloMsg(msg);

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8")));
        headers.setContentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8));

        return new ResponseEntity<>(String.valueOf(returnedId), headers, HttpStatus.OK);

    }
}
