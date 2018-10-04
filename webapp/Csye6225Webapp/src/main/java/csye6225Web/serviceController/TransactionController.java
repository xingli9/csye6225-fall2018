package csye6225Web.serviceController;
import csye6225Web.models.Transaction;
import csye6225Web.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;


@RestController
public class TransactionController {


    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {

        List<Transaction> allTransList = new ArrayList<>();
        transactionRepository.findAll().forEach(allTransList::add);

        return allTransList;

    }


 //   @GetMapping("/transactions/{id}")
    @RequestMapping(value="/transactions/{id}", method = RequestMethod.GET,produces ="application/json")
    @ResponseBody
    public ResponseEntity<Object> getTransaction(@PathVariable(value="id") long id) {

       Transaction transaction=transactionRepository.findOne(id);

        if (transaction==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id NOT FOUND\n");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(transaction);
        }

    }


    @PostMapping("/transaction")
    public ResponseEntity<Object> createNewTransaction(@RequestBody Transaction transaction) {

        try {
            transactionRepository.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);

        } catch (Exception e)
        {

            return ResponseEntity.badRequest().build();
        }

    }


    @PutMapping("/transaction/{id}")
    public ResponseEntity<Object> updateTransaction(@RequestBody Transaction transaction ,@PathVariable long id)
    {

        Transaction old_transaction=transactionRepository.findOne(id);

        if(old_transaction==null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
              transaction.setId(id);
              transactionRepository.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body("Update Success!!\n");
        }
    }





    @DeleteMapping("transaction/{id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable Long id)
    {

        Transaction transaction=transactionRepository.findOne(id);
        if (transaction==null)
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            transactionRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete_Success!!\n");
        }

    }




}
