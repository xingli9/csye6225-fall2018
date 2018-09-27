package csye6225Web;



import java.text.SimpleDateFormat;
import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Null;


@Controller
public class RestController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/currentTime")
    public @ResponseBody String getCurrentTime(@RequestHeader(value="User-Agent",defaultValue ="")String user_header) {


        Date date= new Date();
        SimpleDateFormat datetimeFormat=new SimpleDateFormat();
        String currenttime=datetimeFormat.format(date);
        return "CurrentTime:"+currenttime+" "+"\n";
    }



    @GetMapping(path="/user/register")
    public @ResponseBody String userRegister(@RequestParam String userName, @RequestParam String password)
    {


            String  hashPassword= BCrypt.hashpw(password,BCrypt.gensalt());
            User user = new User();
            user.setUserName(userName);
            user.setPassword(hashPassword);
            userRepository.save(user);
            return "Register success!\n";

    }

    @GetMapping(path = "/user")
    public @ResponseBody String getAllUsers()
    {
        String alluser;
        alluser="";
        for (User a:userRepository.findAll())
        {
            alluser=alluser+"ID:"+a.getId()+" UserName:"+a.getUserName()+"\n";
        }

        return alluser;
    }
}