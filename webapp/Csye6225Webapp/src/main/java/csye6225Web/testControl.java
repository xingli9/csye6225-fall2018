package csye6225Web;

import csye6225Web.daos.AWSRDSImpl;
import csye6225Web.daos.AWSS3Impl;
import csye6225Web.serviceController.TransactionControllerUseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testControl {

    @Autowired
    private Environment env;

//    private void printkey() {
//
//        // env = new Environment();
//        String ACCESS_KEY=env.getProperty("ACCESS_KEY");
//        String SECRET_KEY = env.getProperty("SECRET_KEY");
//        System.out.println("ACCESS_KEY: " + ACCESS_KEY);
//        System.out.println("SECRET_KEY: " + SECRET_KEY);
//    }
    public static void main(String[] args) {

//        TransactionControllerUseDao transactionControllerUseDao = TransactionControllerUseDao.getInstance();
//        AWSS3Impl awss3 = AWSS3Impl.getInstance();
        //awss3.uploadToS3();
        //awss3.printkey();




//        String url = "http://csye6225-fall2018-lixing1.me.csye6225.com.s3.amazonaws.com/csye6225/husky.jpg";
//        awss3.deleteToS3(url);

//        AWSRDSImpl awsrds = AWSRDSImpl.getInstance();
//        awsrds.setupDatabase();

        String str = "abcgmail.com";
        int n = str.indexOf("@");
        System.out.println(n);



    }

}
