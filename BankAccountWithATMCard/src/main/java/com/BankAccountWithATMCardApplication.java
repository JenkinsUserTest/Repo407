package com;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.dao.*;
import java.util.*;
import com.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication(scanBasePackages="com.*")
@EntityScan(basePackages="com.bean")
@EnableJpaRepositories(basePackages="com.repository")
@PropertySource({"classpath:application.properties"})
public class BankAccountWithATMCardApplication implements CommandLineRunner
{
    @Autowired
    private AccountDAO accountDAO;
     @Autowired
    private ATMCardDAO atmCardDAO;

	public static void main(String[] args) {
		SpringApplication.run(BankAccountWithATMCardApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
    	System.out.println("Invoke the methods");
    	SavingsAccount account=new SavingsAccount();
    	account.setAccountNumber(1001);
    	account.setHolderName("test");
    	account.setBalance(45000);
    	account.setMinimumBalance(5000);
    	
    	ATMCard atmCard=new ATMCard();
    	atmCard.setCardNumber("xyz");
    	atmCard.setCardType("abc");
    	atmCard.setExpiryDate(new Date());
    	
    	accountDAO.openAccount(account);
    	atmCardDAO.issueATMCardToAccount(1001,atmCard);
    	accountDAO.retrieveAccountBasedOnCardType("abc");
    	

    }


}
