package com.newland.payment.mvc.service.impl;

import java.util.List;
import android.content.Context;
import com.newland.payment.mvc.model.CardBinC;
import com.newland.payment.mvc.dao.impl.CardBinCDaoImpl;


public class CardBinCServiceImpl{

	private CardBinCDaoImpl CardBinCDao;
	
	public CardBinCServiceImpl(Context context){
		this.CardBinCDao = new CardBinCDaoImpl(context);
	}
	
	public long addCardBin(String cardBin) {
		CardBinC cardBinC = new CardBinC();
		cardBinC.setCardBin(cardBin);
		return CardBinCDao.insert(cardBinC);
	}

	public List<CardBinC> findAll() {
		return CardBinCDao.findAll();
	}

	public CardBinC findByCardBin(String cardBin) {
		return CardBinCDao.findByCardBin(cardBin);
	}

	public long deleteAll() {
		return CardBinCDao.deleteAll();
	}

	public long getCount() {
		return CardBinCDao.getCount();
	}
}
