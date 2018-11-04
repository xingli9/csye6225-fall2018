package csye6225Web.daos;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class AWSS3Impl implements AWSS3 {
    private static String rootPath = "/Users/xingli/Desktop/neu/class/2018fall/CSYE6225cloudcomputing/test";
    private static String BUCKET_NAME   = "csye6225-fall2018-lixing1.me.csye6225.com";
    private static String ACCESS_KEY    ;
    private static String SECRET_KEY    ;

    final String ENDPOINT = "s3.us-east-1.amazonaws.com";

    private static AWSS3Impl instance = null;

    private AWSS3Impl() {
    }

    public static AWSS3Impl getInstance() {
        if (instance == null)
            instance = new AWSS3Impl();
        return instance;
    }
    @Override
    public String uploadToS3(MultipartFile file) {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AmazonS3Client conn = new AmazonS3Client(credentials, clientConfig);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        FileInputStream fi = null;
        try {
            fi = (FileInputStream)file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String key = "csye6225" + "/" + file.getOriginalFilename();

        URL connurl = conn.getUrl(BUCKET_NAME, key);
        System.out.println("URL: " + connurl.toString());

        conn.putObject(BUCKET_NAME , key , fi, metadata);
        System.out.println("putobject:--" + "");
        System.out.println("object:+" + conn.getObject(BUCKET_NAME, key));



        //GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key);
        //URL url = conn.generatePresignedUrl(urlRequest);
        //System.out.println(url);
        return connurl.toString();
    }

    @Override
    public void deleteToS3(String url) {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AmazonS3Client conn = new AmazonS3Client(credentials, clientConfig);

        String[] urls = url.split("/");
        System.out.println("begin");
        for (String s : urls) {
            System.out.println(s);
        }
        System.out.println("end");

        String bucketName = urls[2];
        String key = "";
        for (int i = 3; i < urls.length; i++) {
            if (i == 3) {
                key += urls[i];
                continue;
            }
            key += "/" + urls[i];
        }

        conn.setEndpoint(ENDPOINT);

        URL connurl = conn.getUrl(BUCKET_NAME, key);
        System.out.println("URL: " + connurl.toString());

        System.out.print("BUCKETNAME: " + BUCKET_NAME);
        System.out.println("key: -" + key+"++");




        GetObjectRequest rangeObjectRequest = new GetObjectRequest(BUCKET_NAME, key).withRange(0,9);
        System.out.println("object:+" + conn.getObject(rangeObjectRequest).toString());

        conn.deleteObject(BUCKET_NAME, key);
        System.out.println("DELETE SUCCESS-------------------");


    }

}
