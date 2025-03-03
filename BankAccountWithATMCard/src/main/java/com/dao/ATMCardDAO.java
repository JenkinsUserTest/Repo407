package com.dao;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.bean.Account;
import com.repository.AccountRepository;
import com.repository.ATMCardRepository;
import org.slf4j.*;
import java.util.Optional;
import com.bean.ATMCard;
@Component
public class ATMCardDAO {
	private static final Logger logger=LoggerFactory.getLogger(ATMCardDAO.class);
	@Autowired
	private AccountRepository ar;
	@Autowired
	private ATMCardRepository cr;
	public void issueATMCardToAccount(int accountNumber, ATMCard atmCardObjet) {
			Optional<Account> optionalAccount=ar.findById(accountNumber);
			Account account=null;
			if(optionalAccount.isPresent()){
			   account= optionalAccount.get();
			   System.out.println("*****account"+account);
			}
			if(account!=null && account.getAtmCard()==null){
			    
			    atmCardObjet.setAccount(account);
			    cr.save(atmCardObjet);
			    logger.info("ATM card successfully issued to account number {}",accountNumber);
			    
			}else{
			    logger.error("ATM card not issued to account number {}",accountNumber);
			}
	}

}
