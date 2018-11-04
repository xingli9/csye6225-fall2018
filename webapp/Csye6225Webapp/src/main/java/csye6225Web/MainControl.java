package csye6225Web;

import csye6225Web.daos.AWSRDSImpl;
import csye6225Web.daos.AWSS3;
import csye6225Web.daos.AWSS3Impl;
import csye6225Web.serviceController.TransactionControllerUseDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import csye6225Web.models.User;
import csye6225Web.services.UserService;
import java.util.Arrays;

import java.util.UUID;



@SpringBootApplication
public class MainControl extends SpringBootServletInitializer{


//
//    UUID uuid = UUID.randomUUID();
//    @Bean
//    //@Override
//    public CommandLineRunner setupDefaultUser(UserService service) {
//        return args -> {
//            service.save(new User(
//                    "user", //username
//                    "user", //password
//                    Arrays.asList(new Role("USER"), new Role("ACTUATOR")),//roles
//                    true,//Active
//                    uuid.toString()
//            ));
//        };
//    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(MainControl.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {


        AWSRDSImpl awsrds = AWSRDSImpl.getInstance();
        awsrds.setupDatabase();

        SpringApplication.run(MainControl.class, args);
    }


}
