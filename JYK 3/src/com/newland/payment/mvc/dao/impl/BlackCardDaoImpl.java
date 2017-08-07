package com.newland.payment.mvc.dao.impl;

import java.util.List;

import android.content.Context;

import com.newland.base.dao.BaseDao;
import com.newland.payment.mvc.dao.BlackCardDao;
import com.newland.payment.mvc.model.BlackCard;

public class BlackCardDaoImpl extends BaseDao<BlackCard> implements BlackCardDao{

	public BlackCardDaoImpl(Context context) {
		super(context);
	}

	@Override
	public List<BlackCard> findAll() {
		return super.findAll();
	}

	@Override
	public BlackCard findByCardBin(String cardBin) {
		List<BlackCard> list = super.query("CARD_BIN=?", new String[] {String.valueOf(cardBin)}, null);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public int deleteAll() {
		return super.delete("", new String[]{});
	}

	@Override
	public int getCount() {
		return super.getRowCount();
	}

}
