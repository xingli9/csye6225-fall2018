package csye6225Web.bean;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class ServicesController {

    @Autowired
    private UserRepository userRepository;


    //@GetMapping("/time")
    @RequestMapping(value = "/time", method = RequestMethod.GET, produces = "application/json")//communication in json
    @ResponseBody
    public String getCurrentTime(@RequestHeader(value="username", required = true) String username,
                               @RequestHeader(value="password", required = true) String password) {

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return "Missing username or password, please verify.";
        }


        JsObject obj = new JsObject();
        System.out.println(username + password);
        Iterable users = userRepository.findAll();
        System.out.checkError();
        for (User a:userRepository.findAll()) {
            if (!a.getUserName().equals(username)) {
                continue;
            } else if (BCrypt.checkpw(password, a.getPassword())) {
                Date date= new Date();
                SimpleDateFormat datetimeFormat=new SimpleDateFormat();
                String currenttime=datetimeFormat.format(date);
                obj.add("Currenttime: ", currenttime);
                obj.add("id", "2");
            } else {
                obj.add("Incorrect password !!\n");
            }
            return obj.toString();
        }
        obj.add("User name does not exist!\n");
        return obj.toString();


    }


    private boolean checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword)) return true;
        else return false;
    }




    @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String userRegister(@RequestHeader(value="username", required = true) String username,
                                             @RequestHeader(value="password", required = true) String password)
    {

        JsObject obj = new JsObject();
        for (User a:userRepository.findAll()) {
            if (a.getUserName().equals(username)) {
                obj.add("Username already exist, please enter another one!!");
                return obj.toString();
            }
        }
        String  hashPassword= BCrypt.hashpw(password,BCrypt.gensalt());
        User user = new User();
        user.setUserName(username);
        user.setPassword(hashPassword);
        userRepository.save(user);
        obj.add("Register success!\n");
        return obj.toString();

    }

    @GetMapping(path = "/user")
    public @ResponseBody String getAllUsers()
    {
        String alluser;
        alluser="";
        for (User a:userRepository.findAll())
        {
            alluser=alluser+"ID:"+a.getUserId()+" UserName:"+a.getUserName()+"\n";
        }

        return alluser;
    }
}