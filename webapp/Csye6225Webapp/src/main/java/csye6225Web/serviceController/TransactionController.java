package csye6225Web.serviceController;

import csye6225Web.models.Receipt;
import csye6225Web.models.Transaction;
import csye6225Web.repositories.ReceiptRepository;
import csye6225Web.repositories.TransactionRepository;
import csye6225Web.repositories.UserRepository;
import csye6225Web.services.CloudWatchService;
import csye6225Web.services.S3Service;
import csye6225Web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class TransactionController {


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CloudWatchService cloudWatchService;

    @Autowired
    S3Service s3Service;

    @Autowired
    UserService userService;

    Double get_transactions=0.0;
    Double get_transaction=0.0;
    Double post_transaction=0.0;
    Double put_transaction=0.0;
    Double delete_transaction=0.0;

    @GetMapping("/transactions")
    public ResponseEntity<Object> getAllTransactions(@RequestHeader(value="username",required = true) String username,
                                                @RequestHeader(value="password",required = true) String password)
    {
        cloudWatchService.putMetricData("GetRequest","/transactions",++get_transactions);

        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

        return ResponseEntity.status(HttpStatus.OK).body(userService.findUser(username).getTransactions());

    }



    @GetMapping("/transaction/{id}")
    public ResponseEntity<Object> getTransaction(@RequestHeader(value="username",required = true) String username,
                                                 @RequestHeader(value="password",required = true) String password,
                                                 @PathVariable(value="id") long id)
    {

        cloudWatchService.putMetricData("GetRequest","/transaction/{id}",++get_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

        for(Transaction tran: userService.findUser(username).getTransactions())
        {
            if(tran.getId()==id)
            {
                return ResponseEntity.status(HttpStatus.OK).body(tran);
            }
            System.out.println();

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID NOT FOUND");

    }


    @PostMapping("/transaction")
    public ResponseEntity<Object> createNewTransaction(@RequestHeader(value="username",required = true) String username,
                                                       @RequestHeader(value="password",required = true) String password,
                                                       @RequestParam(value = "merchant", required = true) String merchant,
                                                       @RequestParam(value = "amount",required = true) String amount,
                                                       @RequestParam(value = "date", required = true) String date,
                                                       @RequestParam(value = "category", required = true) String category,
                                                       @RequestParam(value = "description", required = true) String description,
                                                       @RequestParam(value = "receipt", required = true) MultipartFile receipt)
    {

        cloudWatchService.putMetricData("PostRequest","/transaction",++post_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

        String receiptURL= s3Service.uploadAttachment(username,receipt);

        if(receiptURL==null){return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("attachment might not valid\n");}

        Receipt _receipt=new Receipt();
        _receipt.setUrl(receiptURL);


        Transaction transaction= new Transaction();
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDate(date);
        transaction.setDescription(description);
        transaction.setMerchant(merchant);
        transaction.getAttachments().add(_receipt);


        try
        {
            _receipt.setTransaction(transaction);
            userService.findUser(username).getTransactions().add(transaction);
            transaction.setUser(userService.findUser(username));
            transactionRepository.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);

        } catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

    }


    @PostMapping("/transaction/{id}")
    public ResponseEntity<Object> updateTransaction(@RequestHeader(value="username",required = true) String username,
                                                    @RequestHeader(value="password",required = true) String password,
                                                    @RequestParam(value = "merchant", required = true) String merchant,
                                                    @RequestParam(value = "amount",required = true) String amount,
                                                    @RequestParam(value = "description") String description,
                                                    @RequestParam(value = "date", required = true) String date,
                                                    @RequestParam(value = "category", required = true) String category,
                                                    @RequestParam(value = "receipt", required = true) MultipartFile receipt,
                                                    @PathVariable Long id)
    {

        cloudWatchService.putMetricData("PutRequest","/transaction/{id}",++put_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}


        for(Transaction tran: userService.findUser(username).getTransactions())
        {
            if(id==tran.getId())
            {

                if(s3Service.updateAttachment(username,tran.getAttachments().get(0).getUrl(),receipt)==null){return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("attachment might not valid\n");}

                tran.setDescription(description);
                tran.setMerchant(merchant);
                tran.setDate(date);
                tran.setCategory(category);
                tran.setAmount(amount);
                transactionRepository.save(tran);
                return ResponseEntity.status(HttpStatus.CREATED).body("Update Success!!\n");
            }
        }

        return ResponseEntity.notFound().build();

    }





    @DeleteMapping("transaction/{id}")
    public ResponseEntity<Object> deleteTransaction(@RequestHeader(value="username",required = true) String username,
                                                    @RequestHeader(value="password",required = true) String password,
                                                    @PathVariable Long id)
    {

        cloudWatchService.putMetricData("DeleteRequest","/transaction/{id}",++delete_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}



        for(Transaction tran: userService.findUser(username).getTransactions())
        {
            if(id==tran.getId())
            {

                userService.findUser(username).getTransactions().remove(tran);
                for(Receipt r: tran.getAttachments())
                {
                    s3Service.deleteAttachment(username,r.getUrl());
                    receiptRepository.delete(r);
                }

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete_Success!!\n");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID NOT FOUND!!\n");

    }




}
