package com.repository;

import com.bean.ATMCard;
import com.bean.Account;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface ATMCardRepository extends CrudRepository<ATMCard,String> {
	List<ATMCard> findByCardType(String cardType);
}
