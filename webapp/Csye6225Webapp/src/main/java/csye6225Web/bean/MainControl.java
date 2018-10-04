package csye6225Web.bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MainControl {

    public static void main(String[] args)
    {
        SpringApplication.run(MainControl.class,args);
    }

    @RequestMapping(value = "/",produces = "text/plain;charset=UTF-8")
    String index(){
        return "Hello Spring Boot!";
    }
}
