package csye6225Web.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import csye6225Web.models.User;
import csye6225Web.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;



    public boolean userNameExist(String username)
    {
        for(User u:userRepository.findAll())
        {
            if (u.getUsername().equals(username))
            {
                return true;
            }
        }
        return false;
    }


    public void saveUser(String username, String password)
    {
        User user= new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
        userRepository.save(user);
    }


    public boolean userIsValid(String username, String password)
    {
        for(User u: userRepository.findAll())
        {
            if(u.getUsername().equals(username)&& BCrypt.checkpw(password,u.getPassword()))
            {
                return true;
            }
        }

        return false;
    }




    public boolean sendResetPassLink(String username)
    {

        ResourcePropertySource propertySource2=null;
        try
        {
            propertySource2 = new ResourcePropertySource("resources", "classpath:application.properties");
        } catch (IOException e) { e.printStackTrace(); }


        String ACCESS_KEY = propertySource2.getProperty("ACCESS_KEY").toString();
        String SECRET_KEY = propertySource2.getProperty("SECRET_KEY").toString();


        AWSCredentials awsCredentials=new BasicAWSCredentials(ACCESS_KEY,SECRET_KEY);
        AmazonSNSClient snsClient= new AmazonSNSClient(awsCredentials);

        String topicARN= "arn:aws:sns:us-east-1:398590284929:Csye6225Topic";
        String msg=username+":"+ UUID.randomUUID().toString();

        PublishRequest publishRequest=new PublishRequest(topicARN,msg);

        try
        {
            PublishResult publishResult=snsClient.publish(publishRequest);
            System.out.println(publishResult.toString());

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Failed to publish userName and token..."+e.toString()+" "+e.getMessage());

            return false;
        }


    }







}
