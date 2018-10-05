package com.github.arocketman.entities;


import javax.persistence.*;


@Entity
@Table(name="transaction_table")
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private String merchant;
    private String amount;
    private String date;
    private String category;



    public Transaction(){}


    public Transaction(Long id, String description, String merchant, String amount, String date, String category)
    {
        this.id=id;
        this.merchant=merchant;
        this.description=description;
        this.amount=amount;
        this.date=date;
        this.category=category;

    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public String getMerchant() {
        return merchant;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

}
