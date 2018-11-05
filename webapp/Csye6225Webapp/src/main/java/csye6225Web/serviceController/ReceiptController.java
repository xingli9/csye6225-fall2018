package csye6225Web.serviceController;


import csye6225Web.daos.AWSS3Impl;
import csye6225Web.daos.LocalImpl;
import csye6225Web.daos.ReceiptImpl;
import csye6225Web.daos.UserImpl;
import csye6225Web.models.Receipt;
import csye6225Web.models.Transaction;
import csye6225Web.repositories.ReceiptRepository;
import csye6225Web.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
public class ReceiptController {

    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transaction/{id}/attachments")
    public ResponseEntity<Object> getAttachments(@RequestHeader(value="username", required = true) String username,
                                                 @RequestHeader(value="password", required = true) String password,
                                                 @PathVariable(value = "id") String id)
    {


        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Register failed!\n");
        }
        //-----------------

        Optional<Transaction> transaction=transactionRepository.findById(id);
        if(!transaction.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            if (!transaction.get().getUser_id().equals(user_id)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("You could only get your own transaction!\n");
            }
            return ResponseEntity.ok().body(transaction.get().getAttachments());
        }



    }


    @PostMapping("/transaction/{id}/attachment")
    public ResponseEntity<Object> postNewAttachment(@RequestHeader(value="username", required = true) String username,
                                                    @RequestHeader(value="password", required = true) String password,
                                                    @RequestParam(value = "receipt") MultipartFile receipt,
                                                    @PathVariable String id)
    {

        Optional<Transaction> transaction=transactionRepository.findById(id);
        ReceiptImpl receiptImpl = ReceiptImpl.getInstance();
        AWSS3Impl awss3 = AWSS3Impl.getInstance();
        LocalImpl local = LocalImpl.getInstance();

        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Register failed!\n");
        }
        //-----------------


        if(!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        } else {
            if (!transaction.get().getUser_id().equals(user_id)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("You could only modify your own transaction!\n");
            }
            try {
                String url;

                //save to local
                //url = local.saveFile(receipt);

                //save to aws s3
                url = awss3.uploadToS3(receipt);
                Receipt receipt1 = new Receipt(UUID.randomUUID().toString(), url);

                receiptImpl.insertReceipt(id, receipt1);

                transaction.get().getAttachments().add(receipt1);
                return ResponseEntity.ok().body("Post successful!");
            }catch (Exception e)
            {
                return ResponseEntity.badRequest().body(e);
            }
        }

    }

    @PutMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> addNewAttachment(@RequestHeader(value="username", required = true) String username,
                                                   @RequestHeader(value="password", required = true) String password,
                                                   @RequestParam(value = "receipt") MultipartFile newReceipt,
                                                   @PathVariable(value="id") String transactionId ,
                                                   @PathVariable(value="attachmentID") String attachID)
    {
        Optional<Transaction> transaction=transactionRepository.findById(transactionId);
        Optional<Receipt> old_receipt=receiptRepository.findById(attachID);
        Receipt receipt = old_receipt.get();
        ReceiptImpl receiptImpl = ReceiptImpl.getInstance();
        AWSS3Impl awss3 = AWSS3Impl.getInstance();

        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Register failed!\n");
        }
        //-----------------

        if(!transaction.isPresent() || receipt == null) {
            return ResponseEntity.notFound().build();
        } else {
            if (!transaction.get().getUser_id().equals(user_id)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("You could only modify your own transaction!\n");
            }
            String url = receipt.getUrl();
            System.out.println(url);
            awss3.deleteToS3(url);
            url = awss3.uploadToS3(newReceipt);
            receipt.setTransaction(transaction.get());
            receipt.setUrl(url);
            receiptImpl.updateReceipt(receipt);
         return ResponseEntity.ok().body(receipt);
        }



    }

    @DeleteMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> deleteAttachment(@RequestHeader(value="username", required = true) String username,
                                                   @RequestHeader(value="password", required = true) String password,
                                                   @PathVariable(value = "id") String id,
                                                   @PathVariable(value="attachmentID") String attachID)
    {

        Optional<Transaction> transaction= transactionRepository.findById(id);
        Optional<Receipt>     receipt=receiptRepository.findById(attachID);
        AWSS3Impl awss3 = AWSS3Impl.getInstance();

        //authorization------
        UserImpl userImpl = UserImpl.getInstance();
        String user_id = userImpl.register(username, password);
        if (user_id.equals("")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Register failed!\n");
        }
        //-----------------

        if(!transaction.isPresent()||!receipt.isPresent()) {
            return ResponseEntity.notFound().build();

        } else {
            if (!transaction.get().getUser_id().equals(user_id)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("You could only modify your own transaction!\n");
            }
            String url = receipt.get().getUrl();
            awss3.deleteToS3(url);

            transaction.get().getAttachments().remove(receipt.get());
            receiptRepository.deleteById(attachID);
            return ResponseEntity.noContent().build();
        }

    }


}
