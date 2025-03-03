package com.bean;

import java.util.Date;
import jakarta.persistence.*;
import jakarta.persistence.*;
@Entity
@Table(name="atmcard")
public class ATMCard {
	@Id
	@Column(name="card_number")
	private String cardNumber;
	@Column(name="card_type")
	private String cardType;
	@Column(name="expiry_date")
	private Date expiryDate;
	@Column(name="cvv_number")
	private int cvvNumber;
	@OneToOne
	@JoinColumn(name="account_number",referencedColumnName="account_number",unique=true)
	private Account account;
	
	public ATMCard() {
		
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public int getCvvNumber() {
		return cvvNumber;
	}
	public void setCvvNumber(int cvvNumber) {
		this.cvvNumber = cvvNumber;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

}
