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

import java.util.List;
import java.util.Optional;

@RestController
public class ReceiptController {

    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CloudWatchService cloudWatchService;
    @Autowired
    private UserService userService;


    Double get_attachments=0.0;
    Double post_attachment=0.0;
    Double put_attachment=0.0;
    Double delete_attachment=0.0;


    @GetMapping("/transaction/{id}/attachments")
    public ResponseEntity<Object> getAttachments(@RequestHeader(value="username",required = true) String username,
                                                 @RequestHeader(value="password",required = true) String password,
                                                 @PathVariable(value = "id") long id)
    {


        cloudWatchService.putMetricData("GetRequest","/transaction/{id}/attachments",++get_attachments);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}



        for(Transaction tran: userService.findUser(username).getTransactions())
        {
            if(tran.getId()==id)
            {
                return ResponseEntity.ok().body(tran.getAttachments());
            }

        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID NOT FOUND");


    }


    @PostMapping("/transaction/{id}/attachment")
    public ResponseEntity<Object> postNewAttachment(@RequestHeader(value="username",required = true) String username,
                                                    @RequestHeader(value="password",required = true) String password,
                                                    @RequestBody Receipt receipt,@PathVariable long id)
    {


        cloudWatchService.putMetricData("PostRequest","/transaction/{id}/attachment",++post_attachment);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}


        for(Transaction tran: userService.findUser(username).getTransactions())
        {
            if(tran.getId()==id)
            {
                try
                {
                    receipt.setTransaction(tran);
                    tran.getAttachments().add(receipt);
                    receiptRepository.save(receipt);
                    return ResponseEntity.ok().body(receipt);

                } catch (Exception e)
                {
                    return ResponseEntity.badRequest().body(e);
                }


            }
         }


         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID NOT FOUND");


     
    }

    @PutMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> addNewAttachment(@RequestHeader(value="username",required = true) String username,
                                                   @RequestHeader(value="password",required = true) String password,
                                                   @RequestBody Receipt receipt,
                                                   @PathVariable(value="id") long id ,
                                                   @PathVariable(value="attachmentID") long attachID)
    {

        cloudWatchService.putMetricData("PutRequest","/transaction/{id}/attachment/{attachmentID}",++put_attachment);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");}

           for(Transaction tran: userService.findUser(username).getTransactions())
           {
               if(tran.getId()==id)
               {
                   try
                   {
                       receipt.setId(attachID);
                       receipt.setTransaction(tran);
                       receiptRepository.save(receipt);
                       return ResponseEntity.ok().body(receipt);

                   } catch (Exception e)
                   {
                       return ResponseEntity.badRequest().body(e);
                   }


               }
            }


            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID NOT FOUND");
           
    }





    @DeleteMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> deleteAttachment(@RequestHeader(value="username",required = true) String username,
                                                   @RequestHeader(value="password",required = true) String password,
                                                   @PathVariable(value = "id") long id,
                                                   @PathVariable(value="attachmentID") long attachID)
    {

        cloudWatchService.putMetricData("DeleteRequest","/transaction/{id}/attachment/{attachmentID}",++delete_attachment);
        if(!userService.userIsValid(username,password)){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password\n");}



        for(Transaction tran: userService.findUser(username).getTransactions())
        {

            if(tran.getId()==id)
            {
                try
                {


                    for(Receipt r:tran.getAttachments())
                    {
                        if (r.getId() == attachID)
                        {

                            tran.getAttachments().remove(r);
                            receiptRepository.deleteById(attachID);
                            return ResponseEntity.noContent().build();

                        }
                    }

                   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attachment ID NOT FOUND\n");

                } catch (Exception e)
                {
                    return ResponseEntity.badRequest().body(e);
                }


            }
         }


         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction ID NOT FOUND\n");

    }


}
