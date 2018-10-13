package csye6225Web.serviceController;


import java.text.SimpleDateFormat;
import java.util.Date;

//import csye6225Web.models.JsObject;
import csye6225Web.models.User;
import csye6225Web.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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



        JSONObject obj = new JSONObject();

        System.out.println(username + password);
        Iterable users = userRepository.findAll();
        System.out.checkError();
        for (User a:userRepository.findAll()) {
            if (!a.getUsername().equals(username)) {
                continue;
            } else if (BCrypt.checkpw(password, a.getPassword())) {
                Date date= new Date();
                SimpleDateFormat datetimeFormat=new SimpleDateFormat();
                String currenttime=datetimeFormat.format(date);
                try {
                    obj.put("Currenttime: ", currenttime);
                    obj.put("id", "2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                return "Incorrect password !!\n";
            }
            return obj.toString();
        }
        return "User name does not exist!\n";


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

        for (User a:userRepository.findAll()) {
            if (a.getUsername().equals(username)) {
                return "Username already exist, please enter another one!!";
            }
        }
        String  hashPassword= BCrypt.hashpw(password,BCrypt.gensalt());
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPassword);
        userRepository.save(user);
        return "Register success!\n";

    }
    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAllUsers() {
        JSONArray obj = new JSONArray();
        for (User a:userRepository.findAll())
        {
            try {
                obj.put(new JSONObject().put("ID", a.getUserid()).put(" UserName:", a.getUsername()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj.toString();
    }
}