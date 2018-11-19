package csye6225Web.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import csye6225Web.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import csye6225Web.repositories.UserRepository;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
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

        String topicARN= "arn:aws:sns:us-east-1:124564112876:Csye6225Topic";
        String msg=username+":"+ UUID.randomUUID().toString();

        PublishRequest publishRequest=new PublishRequest(topicARN,msg);

        try
        {
            PublishResult publishResult=snsClient.publish(publishRequest);
            System.out.println("publish successful");
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
