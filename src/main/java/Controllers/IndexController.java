package Controllers;

import config.annotation.WebController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@WebController
public class IndexController {
    @RequestMapping("/")
    public String index(Map<String, Object> model)
    {
        return "/index";
    }
}
