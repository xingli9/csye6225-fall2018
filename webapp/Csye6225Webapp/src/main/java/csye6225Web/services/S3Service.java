package csye6225Web.services;


import java.io.FileInputStream;
import java.io.IOException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

@Service
public class S3Service {



    final static String s3BucketName="csye6225-fall2018-lixing1.me.csye6225.com";



    public String uploadAttachment(String username, MultipartFile receipt)
    {

        ResourcePropertySource propertySource2=null;
        try
        {
            propertySource2 = new ResourcePropertySource("resources", "classpath:application.properties");
        } catch (IOException e){ System.out.println(e.getMessage()); }


        String ACCESS_KEY = propertySource2.getProperty("ACCESS_KEY").toString();
        String SECRET_KEY = propertySource2.getProperty("SECRET_KEY").toString();

        AWSCredentials awsCredentials=new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);


        try
        {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTP))
                    .build();

            ObjectMetadata metadata=new ObjectMetadata();
            metadata.setContentLength(receipt.getSize());
            FileInputStream receiptStream= (FileInputStream)receipt.getInputStream();

            String key=username+"/"+receipt.getOriginalFilename();
            s3Client.putObject(s3BucketName,key,receiptStream,metadata);

            return s3Client.getUrl(s3BucketName,key).toString();

        }
        catch ( Exception E)
        {
            System.out.println("Failed to upload attachment "+receipt.getOriginalFilename()+" "+E.getMessage());
            return null;
        }

    }




    public String updateAttachment(String username, String attachmetURL, MultipartFile receipt)
    {

        ResourcePropertySource propertySource2=null;
        try
        {
            propertySource2 = new ResourcePropertySource("resources", "classpath:application.properties");
        } catch (IOException e){ System.out.println(e.getMessage()); }


        String ACCESS_KEY = propertySource2.getProperty("ACCESS_KEY").toString();
        String SECRET_KEY = propertySource2.getProperty("SECRET_KEY").toString();

        AWSCredentials awsCredentials=new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);


        try
        {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTP))
                    .build();

            ObjectMetadata metadata=new ObjectMetadata();
            metadata.setContentLength(receipt.getSize());
            FileInputStream receiptStream= (FileInputStream)receipt.getInputStream();

            String key=username+"/"+attachmetURL.split("/")[4];
            s3Client.putObject(s3BucketName,key,receiptStream,metadata);
            return s3Client.getUrl(s3BucketName,key).toString();

        }
        catch ( Exception E)
        {
            System.out.println("Failed to upload attachment "+receipt.getOriginalFilename()+" "+E.getMessage());
            return null;
        }

    }




    public void deleteAttachment(String username, String attachmentUrl)
    {

        ResourcePropertySource propertySource2=null;
        try
        {
            propertySource2 = new ResourcePropertySource("resources", "classpath:application.properties");
        } catch (IOException e){ System.out.println(e.getMessage()); }


        String ACCESS_KEY = propertySource2.getProperty("ACCESS_KEY").toString();
        String SECRET_KEY = propertySource2.getProperty("SECRET_KEY").toString();

        AWSCredentials awsCredentials=new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);


        try
        {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTP))
                    .build();

            String key=username+"/"+attachmentUrl.split("/")[4];
            s3Client.deleteObject(s3BucketName,key);

        }
        catch ( Exception E)
        {
            System.out.println("Failed to delete attachment "+attachmentUrl+" "+E.getMessage());

        }



    }





}
