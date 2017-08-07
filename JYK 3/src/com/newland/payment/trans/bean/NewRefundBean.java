package com.newland.payment.trans.bean;

import com.newland.pos.sdk.bean.CardBean;

/**
 * 脱货交易数据
 */
public class NewRefundBean {
    private CardBean cardBean;
    private String oldRefNo;    // 原参考号
    private long oldAmount;   // 原交易金额
    private String oldDate; //原交易日期
    private String currency = "156";

    public String getOldDate() {
        return oldDate;
    }

    public void setOldDate(String oldDate) {
        this.oldDate = oldDate;
    }

    public CardBean getCardBean() {
        return cardBean;
    }

    public void setCardBean(CardBean cardBean) {
        this.cardBean = cardBean;
    }

    public String getOldRefNo() {
        return oldRefNo;
    }

    public void setOldRefNo(String oldRefNo) {
        this.oldRefNo = oldRefNo;
    }

    public long getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(long oldAmount) {
        this.oldAmount = oldAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
