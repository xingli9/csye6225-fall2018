package com.github.arocketman.repositories;

import com.github.arocketman.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReceiptRepository extends JpaRepository<Receipt,Long> {

}
