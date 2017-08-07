package com.newland.payment.mvc.dao.impl;

import java.util.List;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.BlackCardDao;
import com.newland.payment.mvc.model.CardBinC;

public class CardBinCDaoImpl extends BaseDao<CardBinC> {

	public CardBinCDaoImpl(Context context) {
		super(context);
	}

	public List<CardBinC> findAll() {
		return super.findAll();
	}

	public CardBinC findByCardBin(String cardBin) {
		List<CardBinC> list = super.query("CARD_BIN=?", new String[] {String.valueOf(cardBin)}, null);
		return list.size() > 0 ? list.get(0) : null;
	}

	public int deleteAll() {
		return super.delete("", new String[]{});
	}

	public int getCount() {
		return super.getRowCount();
	}

}
