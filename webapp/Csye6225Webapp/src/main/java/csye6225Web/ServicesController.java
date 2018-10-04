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



@Controller
public class ServicesController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/currentTime")
    public @ResponseBody String getCurrentTime(@RequestHeader(value="User-Agent")String user_header)
    {


        if(user_header.equals("curl/7.59.0"))
        {
            return "Please login!\n";
        }
        String userName=user_header.split(":")[0];
        String password=user_header.split(":")[1];

        for (User a:userRepository.findAll())
        {
            if(a.getUserName().equals(userName) && checkPass(password,a.getPassword()))
            {
                Date date= new Date();
                SimpleDateFormat datetimeFormat=new SimpleDateFormat();
                String currenttime=datetimeFormat.format(date);
                return "CurrentTime:"+currenttime+"\n";
            }

            if(a.getUserName().equals(userName) && !checkPass(password,a.getPassword()))
            {
                return "Incorrect password !!\n";
            }

        }

         return "User name does not exist!\n";


    }


    private boolean checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword)) return true;
        else return false;
    }



    @GetMapping(path="/user/register")
    public @ResponseBody String userRegister(@RequestParam String userName, @RequestParam String password)
    {
        for (User a:userRepository.findAll()) {
            if(a.getUserName().equals(userName)) {
                return "User name has already existed!\n";
            }
        }
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