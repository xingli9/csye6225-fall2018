package csye6225Web.serviceController;



import csye6225Web.services.CloudWatchService;
import csye6225Web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private CloudWatchService cloudWatchService;

    private Double post_user_register=0.0;
    private Double get_reset=0.0;

    @PostMapping("/user/register")
    public ResponseEntity<Object> userRegister(@RequestHeader(value="username",required = true) String username,
                                               @RequestHeader(value="password",required = true) String password)
    {
        cloudWatchService.putMetricData("PostRequest","/user/register",++post_user_register);

        if(userService.userNameExist(username))
        {
            return ResponseEntity.status(HttpStatus.OK).body("Username exists!\n");
        }

        userService.saveUser(username,password);
        return ResponseEntity.status(HttpStatus.OK).body("Register success!!\n");

    }



    @GetMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestHeader(value="username",required = true) String username,
                                                @RequestHeader(value="password",required = true) String password)

    {

            cloudWatchService.putMetricData("GetRequest","/reset",++get_reset);

            if(!userService.userIsValid(username,password))
            {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid Username and Password\n");
            }


            if (userService.sendResetPassLink(username))
            {
                String returnString="Rest password link sent to "+username+"\n";
                return ResponseEntity.status(HttpStatus.OK).body(returnString);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed\n");
            }



        }



    @GetMapping("/test")
    public ResponseEntity<Object> test()

    {
            return ResponseEntity.status(HttpStatus.OK).body("Hello World\n");

    }




    }





