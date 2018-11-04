package com.github.arocketman.controllers;


import com.github.arocketman.entities.Receipt;
import com.github.arocketman.entities.Transaction;
import com.github.arocketman.repositories.ReceiptRepository;
import com.github.arocketman.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/transaction/{id}/attachments")
    public ResponseEntity<Object> getAttachments(@PathVariable(value = "id") long id)
    {

        Optional<Transaction> transaction=transactionRepository.findById(id);

        if(!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok().body(transaction.get().getAttachments());
        }

    }


    @PostMapping("/transaction/{id}/attachment")
    public ResponseEntity<Object> postNewAttachment(@RequestBody Receipt receipt,@PathVariable long id)
    {

        Optional<Transaction> transaction=transactionRepository.findById(id);

        if(!transaction.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            try {
                receipt.setTransaction(transaction.get());
                transaction.get().getAttachments().add(receipt);
                receiptRepository.save(receipt);
                return ResponseEntity.ok().body(receipt);
            }catch (Exception e)
            {
                return ResponseEntity.badRequest().body(e);
            }
        }

    }

    @PutMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> addNewAttachment(@RequestBody Receipt receipt,@PathVariable(value="id") long id ,@PathVariable(value="attachmentID") long attachID)
    {
        Optional<Transaction> transaction=transactionRepository.findById(id);
        Optional<Receipt> old_receipt=receiptRepository.findById(attachID);

        if(!transaction.isPresent() || !old_receipt.isPresent())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            receipt.setId(attachID);
            receipt.setTransaction(transaction.get());
            receiptRepository.save(receipt);
            return ResponseEntity.ok().body(receipt);
        }



    }

    @DeleteMapping("transaction/{id}/attachment/{attachmentID}")
    public ResponseEntity<Object> deleteAttachment(@PathVariable(value = "id") long id, @PathVariable(value="attachmentID") long attachID)
    {

        Optional<Transaction> transaction= transactionRepository.findById(id);
        Optional<Receipt>     receipt=receiptRepository.findById(attachID);

        if(!transaction.isPresent()||!receipt.isPresent())
        {
            return ResponseEntity.notFound().build();

        }
        else
        {
            transaction.get().getAttachments().remove(receipt.get());
            receiptRepository.deleteById(attachID);
            return ResponseEntity.noContent().build();
        }

    }


}

