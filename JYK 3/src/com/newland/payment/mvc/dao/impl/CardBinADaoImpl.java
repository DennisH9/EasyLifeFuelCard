package com.newland.payment.mvc.dao.impl;

import java.util.List;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.BlackCardDao;
import com.newland.payment.mvc.model.CardBinA;

public class CardBinADaoImpl extends BaseDao<CardBinA> {

	public CardBinADaoImpl(Context context) {
		super(context);
	}

	public List<CardBinA> findAll() {
		return super.findAll();
	}

	public CardBinA findByCardBin(String cardBin) {
		List<CardBinA> list = super.query("CARD_BIN=?", new String[] {String.valueOf(cardBin)}, null);
		return list.size() > 0 ? list.get(0) : null;
	}

	public int deleteAll() {
		return super.delete("", new String[]{});
	}

	public int getCount() {
		return super.getRowCount();
	}

}
