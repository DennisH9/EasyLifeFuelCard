package com.newland.payment.mvc.service.impl;

import java.util.List;
import android.content.Context;
import com.newland.payment.mvc.dao.impl.CardBinADaoImpl;
import com.newland.payment.mvc.model.CardBinA;

public class CardBinAServiceImpl{

	private CardBinADaoImpl cardBinADao;
	
	public CardBinAServiceImpl(Context context){
		this.cardBinADao = new CardBinADaoImpl(context);
	}
	
	public long addCardBin(String cardBin) {
		CardBinA cardBinA = new CardBinA();
		cardBinA.setCardBin(cardBin);
		return cardBinADao.insert(cardBinA);
	}

	public List<CardBinA> findAll() {
		return cardBinADao.findAll();
	}

	public CardBinA findByCardBin(String cardBin) {
		return cardBinADao.findByCardBin(cardBin);
	}

	public long deleteAll() {
		return cardBinADao.deleteAll();
	}

	public long getCount() {
		return cardBinADao.getCount();
	}
}
