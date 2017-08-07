package com.newland.payment.mvc.service.impl;

import java.util.List;
import android.content.Context;
import com.newland.payment.mvc.dao.impl.CardBinBDaoImpl;
import com.newland.payment.mvc.model.CardBinB;

public class CardBinBServiceImpl{

	private CardBinBDaoImpl CardBinBDao;
	
	public CardBinBServiceImpl(Context context){
		this.CardBinBDao = new CardBinBDaoImpl(context);
	}
	
	public long addCardBin(String cardBin) {
		CardBinB cardBinB = new CardBinB();
		cardBinB.setCardBin(cardBin);
		return CardBinBDao.insert(cardBinB);
	}

	public List<CardBinB> findAll() {
		return CardBinBDao.findAll();
	}

	public CardBinB findByCardBin(String cardBin) {
		return CardBinBDao.findByCardBin(cardBin);
	}

	public long deleteAll() {
		return CardBinBDao.deleteAll();
	}

	public long getCount() {
		return CardBinBDao.getCount();
	}
}
