package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.bean.ATMCard;
import com.bean.Account;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.bean.Account;
import com.repository.ATMCardRepository;
import com.repository.AccountRepository;
import org.slf4j.*;
@Component
public class AccountDAO {
	private static final Logger logger=LoggerFactory.getLogger(AccountDAO.class);
	@Autowired
	private ATMCardRepository ar;
	@Autowired
	private AccountRepository ap;
	public void openAccount(Account account) {
		ap.save(account);
		logger.info("Account with id {} added successfully",account.getAccountNumber());
	}
	
	public List<Account> retrieveAccountBasedOnCardType(String cardType){
		List<ATMCard> atmCards=ar.findByCardType(cardType);
		List<Account> accountList=new ArrayList<>();
		Account account=null;
		for(ATMCard atmcard:atmCards) {
			account=atmcard.getAccount();
			if(account==null){
			    logger.error("No account with this card type {} ",cardType);
			}else{
			    logger.info("Account details with card type {} retrieved successfully ",cardType);
			}
		}
		accountList.add(account);
		
		return accountList;
		
	}
}
