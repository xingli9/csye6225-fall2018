//package csye6225Web.serviceController;
//import csye6225Web.models.Receipt;
//import csye6225Web.models.Transaction;
//import csye6225Web.repositories.TransactionRepository;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.swing.plaf.synth.Region;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.io.*;
//import java.util.UUID;
//
//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.Protocol;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.ListObjectsV2Request;
//import com.amazonaws.services.s3.model.ListObjectsV2Result;
//import com.amazonaws.services.s3.model.ObjectListing;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectSummary;
//
//
//
//
//@RestController
//public class TransactionController {
//    private static String rootPath = "/Users/xingli/Desktop/neu/class/2018fall/CSYE6225cloudcomputing/test";
//    private static String BUCKET_NAME   = "bucket-name";
//    private static String ACCESS_KEY    = "access_key_id";
//    private static String SECRET_KEY    = "secret_access_key";
//    private static String KEY_NAME      = "/dev/report/余额月报_201705.csv";
//    private static String FILE_NAME     = "/web/files/report/余额月报_201705.csv";
//    final String ENDPOINT = "http://IP:port";
//
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    //Get all transactions for the user
//    @RequestMapping(value="/transactions", method = RequestMethod.GET,produces ="application/json")
//    @ResponseBody
//    public List<Transaction> getAllTransactions(HttpServletRequest request) {
//
//        System.out.println("in get translations");
//
//        List<Transaction> allTransList = new ArrayList<>();
//        transactionRepository.findAll().forEach(allTransList::add);
//        return allTransList;
//
//    }
//
//
//    //
//    @RequestMapping(value="/transactions/{id}", method = RequestMethod.GET,produces ="application/json")
//    @ResponseBody
//    public ResponseEntity<Object> getTransaction(@PathVariable(value="id") String id) {
//        System.out.println("in get translations.id");
//
//        Optional<Transaction> transaction=transactionRepository.findById(id);
//
//        if (!transaction.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id NOT FOUND\n");
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(transaction);
//        }
//
//    }
//
//    //Create a transaction for the user
//    @PostMapping("/transaction")
//    public ResponseEntity<Object> createNewTransaction(@RequestParam(value = "description") String description,
//                                                       @RequestParam(value = "merchant") String merchant,
//                                                       @RequestParam(value = "amount") String amount,
//                                                       @RequestParam(value = "date") String date,
//                                                       @RequestParam(value = "category") String category,
//                                                       @RequestParam(value = "receipt") MultipartFile receipt) {
//        System.out.println("in post translation");
//        Transaction transaction = new Transaction();
//        if (transaction==null) {
//            System.out.println("transaction is null");
//        }
//
//
//
//
//        try {
//
//            if(!receipt.isEmpty()) {
//                //上传文件路径
//                File path = new File(rootPath);
//                //上传文件名
//                String filename = receipt.getOriginalFilename();
//                String url = rootPath + File.separator + filename;
//                File filepath = new File(path,filename);
//                //判断路径是否存在，如果不存在就创建一个
//                if (!filepath.getParentFile().exists()) {
//                    filepath.getParentFile().mkdirs();
//                }
//                //将上传文件保存到一个目标文件当中
//                try {
//                    receipt.transferTo(new File(path + File.separator + filename));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                List<Receipt> attachments = new ArrayList<>();
//                Receipt receipt1 = new Receipt(UUID.randomUUID().toString(), url);
//                attachments.add(receipt1);
//
//                transaction.setId(UUID.randomUUID().toString());
//                transaction.setDescription(description);
//                transaction.setMerchant(merchant);
//                transaction.setAmount(amount);
//                transaction.setDate(date);
//                transaction.setCategory(category);
//                transaction.setAttachments(attachments);
//
//
//
//            } else {
//                return ResponseEntity.status(HttpStatus.CREATED).body("Error!\n");
//            }
//            System.out.println(transaction.toString());
//
//            System.out.println(transactionRepository.save(transaction));
//            System.out.println("saveTransacton done");
//            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//
//    }
//
//    //update a transaction for the user
//    @PutMapping("/transaction/{id}")
//    public ResponseEntity<Object> updateTransaction(@RequestBody Transaction transaction ,
//                                                    @PathVariable String id) {
//        System.out.println("in put translation");
//        Optional<Transaction> old_transaction=transactionRepository.findById(id);
//
//
//        if(!old_transaction.isPresent())
//        {
//            return ResponseEntity.notFound().build();
//        }
//        else
//        {
//            transaction.setId(id);
//            transactionRepository.save(transaction);
//            return ResponseEntity.status(HttpStatus.CREATED).body("Update Success!!\n");
//        }
//
//
//
//
////        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
////
////        ClientConfiguration clientConfig = new ClientConfiguration();
////
////        clientConfig.setProtocol(Protocol.HTTP);
////
////        AmazonS3Client conn = new AmazonS3Client(credentials,clientConfig);
////        conn.setEndpoint(ENDPOINT);
////
////        Region usWest2 = Region.getRegion(Regions.CN_NORTH_1);
////        conn.setRegion(usWest2);
////        PutObjectResult result = null;
////        //上传文件
////        try {
////            File file = new File(FILE_NAME);
////            LOGGER.debug("Uploading a new object:" + file.getName() + " to S3 from a file\n");
////            result = s3client.putObject(new PutObjectRequest(
////                    BUCKET_NAME, KEY_NAME, FILE_NAME));
////        } catch (AmazonServiceException ase) {
////            LOGGER.error(ase);
////        } catch (AmazonClientException ace) {
////            LOGGER.error(ace);
////        }
//
//    }
//
//
//
//
//
//    //Delete a transaction for the user
//    @DeleteMapping("transaction/{id}")
//    public ResponseEntity<Object> deleteTransaction(@PathVariable String id)
//    {
//        System.out.println("in delete translation");
//
//        Optional<Transaction> transaction=transactionRepository.findById(id);
//        if (!transaction.isPresent())
//        {
//            return ResponseEntity.notFound().build();
//        }
//        else
//        {
//            transactionRepository.deleteById(id);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete_Success!!\n");
//        }
//
//    }
//
//
////    private void saveFile(MultipartFile response, File file) {
////        try(InputStream is = new FileInputStream(file);
////            OutputStream os = response.getOutputStream();){
////            byte [] buffer = new byte[1024];
////            while(is.read(buffer) != -1){
////                os.write(buffer);
////            }
////            os.flush();
////        } catch (IOException ioe){
////            ioe.printStackTrace();
////        }
////    }
//
//
//
//
//    //---------------------
///*    //Create a transaction for the user@RequestBody Transaction transaction,
//    @PostMapping("/transaction")
//    public void createNewTransaction(
//            @RequestParam(value = "receipt") MultipartFile receipt) {
//        System.out.println("in post translation");
//
//        try {
////            for(Receipt r:transaction.getAttachments())
////            {
////                r.setTransaction(transaction);
////            }
//            if(!receipt.isEmpty()) {
//                //上传文件路径
//                File path = new File("/Users/xingli/Desktop/neu/class/2018fall/CSYE6225cloudcomputing/test");
//                //上传文件名
//                String filename = receipt.getOriginalFilename();
//                File filepath = new File(path,filename);
//                //判断路径是否存在，如果不存在就创建一个
//                if (!filepath.getParentFile().exists()) {
//                    filepath.getParentFile().mkdirs();
//                }
//                //将上传文件保存到一个目标文件当中
//                try {
//                    receipt.transferTo(new File(path + File.separator + filename));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                //return ResponseEntity.status(HttpStatus.CREATED).body("Error!\n");
//            }
////            transactionRepository.save(transaction);
////            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
//
//
//        } catch (Exception e)
//        {
//
//            //return ResponseEntity.badRequest().build();
//        }
//
//    }*/
//
//
//
//
//}
