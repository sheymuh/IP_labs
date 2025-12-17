package ru.ulstu.is.server.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SpaController {
    @GetMapping(value = "/{path:^(?!api|assets|images|swagger-ui|.*\\.[a-zA-Z0-9]{2,10}).*}/**")
    public String forwardToIndex(@PathVariable(required = false) String path) {
        return "forward:/index.html";
    }
}
