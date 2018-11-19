package csye6225Web.serviceController;

import csye6225Web.daos.*;
import csye6225Web.models.Receipt;
import csye6225Web.models.Transaction;
import csye6225Web.repositories.TransactionRepository;
import csye6225Web.services.CloudWatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;





@RestController
public class TransactionControllerUseDao {

    private static TransactionControllerUseDao instance = null;

    private TransactionControllerUseDao() {
    }

    public static TransactionControllerUseDao getInstance() {
        if (instance == null)
            instance = new TransactionControllerUseDao();
        return instance;
    }

    private final static Logger logger = LoggerFactory.getLogger(TransactionControllerUseDao.class);


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    CloudWatchService cloudWatchService;

    Double get_transactions=0.0;
    Double get_transaction=0.0;
    Double post_transaction=0.0;
    Double put_transaction=0.0;
    Double delete_transaction=0.0;

    //Get all transactions for the user
    @RequestMapping(value="/transactions", method = RequestMethod.GET,produces ="application/json")
    @ResponseBody
    public ResponseEntity<Object> getAllTransactions(@RequestHeader(value="username", required = true) String username,
                                                      @RequestHeader(value="password", required = true) String password) {

        System.out.println("in get translations");
        cloudWatchService.putMetricData("GetRequest","/transactions",++get_transactions);
        TransactionImpl transactionImpl = TransactionImpl.getInstance();

        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserName does not exist!\n");
        }
        //-----------------

        Collection<Transaction> allTransList = transactionImpl.findAllTransactionsByUserid(user_id);

        return ResponseEntity.status(HttpStatus.OK).body(allTransList);

    }


    //
    @RequestMapping(value="/transactions/{id}", method = RequestMethod.GET,produces ="application/json")
    @ResponseBody
    public ResponseEntity<Object> getTransaction(@RequestHeader(value="username", required = true) String username,
                                                 @RequestHeader(value="password", required = true) String password,
                                                 @PathVariable(value="id") String id) {
        System.out.println("in get translations.id");
        cloudWatchService.putMetricData("GetRequest","/transaction/{id}",++get_transaction);
        TransactionImpl transactionImpl = TransactionImpl.getInstance();

        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User NOT FOUND\n");
        }
        //-----------------

        Transaction transaction = transactionImpl.findAllTransactionById(user_id);
        if (transaction == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id NOT FOUND\n");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }

    }

    //Create a transaction for the user
    @PostMapping("/transaction")
    public ResponseEntity<Object> createNewTransaction(@RequestHeader(value="username", required = true) String username,
                                                       @RequestHeader(value="password", required = true) String password,
                                                       @RequestParam(value = "description") String description,
                                                       @RequestParam(value = "merchant") String merchant,
                                                       @RequestParam(value = "amount") String amount,
                                                       @RequestParam(value = "date") String date,
                                                       @RequestParam(value = "category") String category,
                                                       @RequestParam(value = "receipt") MultipartFile receipt) {
        System.out.println("in post translation");
        cloudWatchService.putMetricData("PostRequest","/transaction",++post_transaction);
        Transaction transaction = new Transaction();
        TransactionImpl transactionImpl = TransactionImpl.getInstance();
        AWSS3Impl awss3 = AWSS3Impl.getInstance();
        LocalImpl local = LocalImpl.getInstance();
        UserImpl userImpl = UserImpl.getInstance();

        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Register failed!\n");
        }
        try {

            if(!receipt.isEmpty()) {
                String url;

                //save to local
                //url = local.saveFile(receipt);

                //save to aws s3
                url = awss3.uploadToS3(receipt);

                List<Receipt> attachments = new ArrayList<>();
                Receipt receipt1 = new Receipt(UUID.randomUUID().toString(), url);
                attachments.add(receipt1);

                transaction.setId(UUID.randomUUID().toString());
                transaction.setDescription(description);
                transaction.setMerchant(merchant);
                transaction.setAmount(amount);
                transaction.setDate(date);
                transaction.setCategory(category);
                transaction.setUser_id(user_id);
                transaction.setAttachments(attachments);

            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body("Error!\n");
            }
            System.out.println(transaction.toString());

            transactionImpl.createTransaction(transaction);
            System.out.println("saveTransacton done");
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } catch (Exception e) {
            System.out.println("badrequest: " + e);
            return ResponseEntity.badRequest().build();
        }

    }

    //update a transaction for the user
    @PutMapping("/transaction/{id}")
    public ResponseEntity<Object> updateTransaction(@RequestHeader(value="username", required = true) String username,
                                                    @RequestHeader(value="password", required = true) String password,
                                                    @RequestParam(value = "description", required = true) String description,
                                                    @RequestParam(value = "merchant", required = false) String merchant,
                                                    @RequestParam(value = "amount", required = false) String amount,
                                                    @RequestParam(value = "date", required = false) String date,
                                                    @RequestParam(value = "category", required = false) String category,
                                                    @RequestParam(value = "receipt", required = false) MultipartFile receipt,
                                                    @PathVariable String id) {
        System.out.println("in put translation");
        cloudWatchService.putMetricData("PutRequest","/transaction/{id}",++put_transaction);
        Optional<Transaction> transaction=transactionRepository.findById(id);
        Transaction transaction1 = transaction.get();
        TransactionImpl transaction2 = TransactionImpl.getInstance();
        LocalImpl local = LocalImpl.getInstance();

        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Register failed!\n");
        }
        //-----------------

        if (!transaction.get().getUser_id().equals(user_id)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("You could only delete your own transaction!\n");
        }

        transaction1.setAttachments(new ArrayList<>());
        if (!transaction1.getAttachments().isEmpty()) {
            System.out.println("attachments is not empty-----");
        }


        if(!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        } else {
            int isChanged = 0;
            if (description != null) {
                System.out.println("description-----");
                transaction1.setDescription(description);
                isChanged = 1;
            }
            if (merchant != null) {
                System.out.println("merchant-----");
                transaction1.setMerchant(merchant);
                isChanged = 1;
            }
            if (amount != null) {
                System.out.println("amount-----");
                transaction1.setAmount(amount);
                isChanged = 1;
            }
            if (date != null) {
                System.out.println("date-----");
                transaction1.setDate(date);
                isChanged = 1;
            }
            if (category != null) {
                System.out.println("category-----");
                transaction1.setCategory(category);
                isChanged = 1;
            }
            if (receipt != null) {
                System.out.println("category-----");
                AWSS3Impl awss3 = AWSS3Impl.getInstance();
                String url = awss3.uploadToS3(receipt);

                List<Receipt> attachments = new ArrayList<>();
                Receipt receipt1 = new Receipt(UUID.randomUUID().toString(), url);
                attachments.add(receipt1);
                transaction1.setAttachments(attachments);
                isChanged = 1;
            }

            if (isChanged == 0) {
                System.out.println("There is no changes!");
                return ResponseEntity.status(HttpStatus.CREATED).body("There is no changes!");
            }



            transaction2.updateTransaction(id, transaction1);
            return ResponseEntity.status(HttpStatus.CREATED).body("Update Success!!\n");
        }
    }


    //Delete a transaction for the user
    @DeleteMapping("transaction/{id}")
    public ResponseEntity<Object> deleteTransaction(@RequestHeader(value="username", required = true) String username,
                                                    @RequestHeader(value="password", required = true) String password,
                                                    @PathVariable String id)
    {
        System.out.println("in delete translation");
        cloudWatchService.putMetricData("DeleteRequest","/transaction/{id}",++delete_transaction);
        AWSS3Impl awss3 = AWSS3Impl.getInstance();
        Optional<Transaction> transaction=transactionRepository.findById(id);

        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Register failed!\n");
        }
        //-----------------

        if (!transaction.get().getUser_id().equals(user_id)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("You could only delete your own transaction!\n");
        }

        Collection<Receipt> receipts = transaction.get().getAttachments();
        if (!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        } else {
            for (Receipt rp : receipts) {
                String url = rp.getUrl();
                awss3.deleteToS3(url);
            }
            transactionRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete_Success!!\n");
        }

    }

}
