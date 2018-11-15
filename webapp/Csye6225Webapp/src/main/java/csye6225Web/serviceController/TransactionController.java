package csye6225Web.serviceController;

import csye6225Web.models.Receipt;
import csye6225Web.models.Transaction;
import csye6225Web.repositories.ReceiptRepository;
import csye6225Web.repositories.TransactionRepository;
import csye6225Web.services.CloudWatchService;
import csye6225Web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    CloudWatchService cloudWatchService;

    @Autowired
    UserService userService;

    Double get_transactions=0.0;
    Double get_transaction=0.0;
    Double post_transaction=0.0;
    Double put_transaction=0.0;
    Double delete_transaction=0.0;

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions(@RequestHeader(value="username",required = true) String username,
                                                @RequestHeader(value="password",required = true) String password)
    {

        cloudWatchService.putMetricData("GetRequest","/transactions",++get_transactions);
        if(!userService.userIsValid(username,password)){return null;}
        return transactionRepository.findAll();

    }



    @GetMapping("/transaction/{id}")
    public ResponseEntity<Object> getTransaction(@RequestHeader(value="username",required = true) String username,
                                                 @RequestHeader(value="password",required = true) String password,
                                                 @PathVariable(value="id") Long id)
    {

        cloudWatchService.putMetricData("GetRequest","/transaction/{id}",++get_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

       Optional<Transaction> transaction=transactionRepository.findById(id);
        if (!transaction.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID NOT FOUND\n");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }

    }


    @PostMapping("/transaction")
    public ResponseEntity<Object> createNewTransaction(@RequestHeader(value="username",required = true) String username,
                                                       @RequestHeader(value="password",required = true) String password,
                                                       @RequestBody Transaction transaction)
    {

        cloudWatchService.putMetricData("PostRequest","/transaction",++post_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

        try {

            for(Receipt r:transaction.getAttachments())
            {
                r.setTransaction(transaction);
            }

            transactionRepository.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);

        } catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

    }


    @PutMapping("/transaction/{id}")
    public ResponseEntity<Object> updateTransaction(@RequestHeader(value="username",required = true) String username,
                                                    @RequestHeader(value="password",required = true) String password,
                                                    @RequestBody Transaction transaction ,@PathVariable Long id)
    {


        cloudWatchService.putMetricData("PutRequest","/transaction/{id}",++put_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

        Optional<Transaction> old_transaction=transactionRepository.findById(id);

        if(!old_transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {

            transaction.setId(id);
            for(Receipt r: transaction.getAttachments())
            {
                r.setTransaction(transaction);
            }

              transactionRepository.save(transaction);
              return ResponseEntity.status(HttpStatus.CREATED).body("Update Success!!\n");
        }
    }





    @DeleteMapping("transaction/{id}")
    public ResponseEntity<Object> deleteTransaction(@RequestHeader(value="username",required = true) String username,
                                                    @RequestHeader(value="password",required = true) String password,
                                                    @PathVariable Long id)
    {

        cloudWatchService.putMetricData("DeleteRequest","/transaction/{id}",++delete_transaction);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

        Optional<Transaction> transaction=transactionRepository.findById(id);
        if (!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            for(Receipt r:transaction.get().getAttachments())
            {
                receiptRepository.delete(r);
            }
            transactionRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete_Success!!\n");
        }

    }




}
