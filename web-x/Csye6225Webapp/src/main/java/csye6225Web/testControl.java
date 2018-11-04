package csye6225Web;

import csye6225Web.daos.AWSRDSImpl;
import csye6225Web.daos.AWSS3Impl;
import csye6225Web.serviceController.TransactionControllerUseDao;

public class testControl {
    public static void main(String[] args) {

//        TransactionControllerUseDao transactionControllerUseDao = TransactionControllerUseDao.getInstance();
//        AWSS3Impl awss3 = AWSS3Impl.getInstance();
//
//        String url = "http://csye6225-fall2018-lixing1.me.csye6225.com.s3.amazonaws.com/csye6225/husky.jpg";
//        awss3.deleteToS3(url);

        AWSRDSImpl awsrds = AWSRDSImpl.getInstance();
        awsrds.setupDatabase();



    }

}
