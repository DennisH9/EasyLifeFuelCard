package com.newland.payment.mvc.service.impl;

import java.util.List;

import android.content.Context;

import com.newland.payment.mvc.dao.BlackCardDao;
import com.newland.payment.mvc.dao.impl.BlackCardDaoImpl;
import com.newland.payment.mvc.model.BlackCard;
import com.newland.payment.mvc.service.BlackCardService;

public class BlackCardServiceImpl implements BlackCardService{

	private BlackCardDao blackCardDao;
	
	public BlackCardServiceImpl(Context context){
		this.blackCardDao = new BlackCardDaoImpl(context);
	}
	
	@Override
	public long addBlackCard(String cardBin) {
		BlackCard blackCard = new BlackCard();
		blackCard.setCardBin(cardBin);
		return blackCardDao.insert(blackCard);
	}

	@Override
	public List<BlackCard> findAll() {
		return blackCardDao.findAll();
	}

	@Override
	public BlackCard findByCardBin(String cardBin) {
		return blackCardDao.findByCardBin(cardBin);
	}

	@Override
	public long deleteAll() {
		return blackCardDao.deleteAll();
	}

	@Override
	public long getCount() {
		return blackCardDao.getCount();
	}

}
