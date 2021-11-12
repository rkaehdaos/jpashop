package jpabook.jpashop.hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping(value = "hello", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> helloPost(@RequestBody final String msg) {
        Long returnedId = helloService.setHelloMsg(msg);

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8")));
        headers.setContentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8));

//        return new ResponseEntity<>(String.valueOf(returnedId), headers, HttpStatus.OK);
/*

        return ResponseEntity.ok()
                .headers(headers)
                .body(String.valueOf(returnedId))
                ;
*/

//        URI uri = URI.create("http://localhost/hello/"+returnedId);
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{returnedId}")
                .buildAndExpand(returnedId)
                .toUri();


        log.info("****URI: "+uri);

        return ResponseEntity.created(uri)
                .headers(headers)
                .body(String.valueOf(returnedId))
                ;


    }
}
