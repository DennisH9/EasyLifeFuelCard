package com.newland.payment.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.trans.bean.CommonBean;
import com.newland.payment.trans.bean.PubBean;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.FormatUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;


import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 消费信息
 */

public class NewConsumeInfoFragment extends BaseFragment {

    private TextView cardNo;    // 卡号
    private TextView cardType;  // 卡类别
    private TextView cardName;  // 卡类别名称
    private TextView balance;   // 余额
    private TextView carNo;     // 车牌号
    private TextView cardOilKind;    // 卡片限用油品
    private TextView orderOilKind;   // 订单使用油品
    private TextView price;     // 单价
    private TextView liter;     // 升数
    private TextView amount;    // 金额
    private TextView pay_amount;    // 实付金额
    private TextView dicount_amount;    // 实付金额
    private TextView tv_err_msg;    // 错误信息
    private Button   btn_do_trade;    // 确认
    private CommonBean <PubBean> commonBean;
    private boolean isSupportPay = false;
    private  String errMsg = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.layout_consume_info, null);
        super.onCreateView(inflater, container, savedInstanceState);
        return mFragmentView;
    }
    public  NewConsumeInfoFragment(CommonBean <PubBean> commonBean){
        super(0);
        this.commonBean = commonBean;
    }
    public static NewConsumeInfoFragment newInstance(CommonBean <PubBean> commonBean) {
        NewConsumeInfoFragment fragment = new NewConsumeInfoFragment(commonBean);
        return fragment;
    }
    @Override
    protected void initData() {
        activity.lock();
        initView(mFragmentView);
    }
    private void initView(View layout) {
        cardNo = (TextView)layout.findViewById(R.id.tv_card_no);
        cardType = (TextView)layout.findViewById(R.id.tv_card_type);
        cardName = (TextView)layout.findViewById(R.id.tv_card_name);
        balance = (TextView)layout.findViewById(R.id.tv_balance);
        carNo = (TextView)layout.findViewById(R.id.tv_car_no);
        cardOilKind = (TextView)layout.findViewById(R.id.tv_card_oil_kind);
        orderOilKind = (TextView)layout.findViewById(R.id.tv_order_oil_kind);
        price = (TextView) layout.findViewById(R.id.tv_price);
        liter = (TextView) layout.findViewById(R.id.tv_liter);
        amount = (TextView) layout.findViewById(R.id.tv_amount);
        pay_amount = (TextView) layout.findViewById(R.id.pay_amount);
        tv_err_msg = (TextView) layout.findViewById(R.id.tv_err_msg);
        dicount_amount = (TextView) layout.findViewById(R.id.discount_amount);
        btn_do_trade = (Button) layout.findViewById(R.id.btn_do_trade);



        PubBean pubBean = commonBean.getValue();
        commonBean.setResult(false);
        Map<String, String> cardInfo = pubBean.getCardInfo();
        cardNo.setText(FormatUtils.formatCardNoWithStar(cardInfo.get("5A")));
        cardType.setText(Integer.parseInt(cardInfo.get("BF04"))+"");
        cardName.setText(getCardName(Integer.parseInt(cardInfo.get("BF03"))));
        try {
            carNo.setText(StringUtils.hexToStr(cardInfo.get("BF09")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cardOilKind.setText(getOilType(cardInfo.get("BF10")));
        orderOilKind.setText(pubBean.getOilType());
        price.setText(FormatUtils.formatAmount(pubBean.getPrice()+""));
        liter.setText(FormatUtils.formatAmount(pubBean.getLiter()+""));
        amount.setText(FormatUtils.formatAmount(pubBean.getOrderAmount()+""));
        pay_amount.setText("￥"+FormatUtils.formatAmount(pubBean.getAmount()+""));

        btn_do_trade.setOnClickListener(clickListener);
        if(pubBean.getDiscountAmount() >= 0){
            dicount_amount.setText("已优惠 : "+FormatUtils.formatAmount(pubBean.getDiscountAmount()+"")+"元");
            dicount_amount.setVisibility(View.VISIBLE);
        }
        if(pubBean.isPay()){
            btn_do_trade.setText("确认支付");
        }
        isSupportPay = checkPay(cardInfo.get("BF10"));
        if(!isSupportPay){
            tv_err_msg.setText(errMsg);
            tv_err_msg.setVisibility(View.VISIBLE);
            btn_do_trade.setText("退出交易");
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initClickEvent(v);
        }
    };
    @Override
    protected void initClickEvent(View view) {
        LoggerUtils.d("initClickEvent view "+ view  + "   ID:"+view.getId());
        if( view.getId() == R.id.btn_do_trade){
            if(!isSupportPay){
                commonBean.setResult(false);
                onBack();
            }else{
                commonBean.setResult(true);
                onSucess();
            }
        }
    }


    @Override
    protected void initEvent() {
    }


    @Override
    public BaseBean getBean() {
        return commonBean;
    }

    /**
     * 登陆
     */



//    @Override
//    public void onFragmentShow() {
//        LoggerUtils.e("hjh  onFragmentShow");
//        activity.hideTitle();
//        activity.hideTimeOut();
//
//    }

    @Override
    public void onFragmentHide() {
        LoggerUtils.e("hjh  onFragmentHide");
        activity.hideProgress();
        super.onFragmentHide();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private String getOilType(String oilType){
        String[] oilTypes ;
        oilTypes = ParamsUtils.getString(ParamsConst.PARAMS_KEY_OILTYPE_BIN).split(",");
        String res = "";
        int index = 0;
        boolean isFirst = true;
        LoggerUtils.d("hjh  oilType : "+oilType);
        while(index < oilTypes.length){
            if(oilType.charAt(index) == '1'){
                LoggerUtils.d("hjh  oilTypes[index] : "+oilTypes[index]);
                if(isFirst){
                    res += oilTypes[index];
                    isFirst = false;
                }else{
                    res += ","+oilTypes[index];
                }
            }
            index++;
        }
        return res;
    }
    private int getOilIndex(String oilType,String cardOilType){
        String[] oilTypes ;
        oilTypes = ParamsUtils.getString(ParamsConst.PARAMS_KEY_OILTYPE_BIN).split(",");
        int index = 0;
        int ret = -1;
        while(index < oilTypes.length){
            if(oilTypes[index].equals(oilType) && cardOilType.charAt(index) == '1'){
                ret = index;
                break;
            }
            index++;
        }
        return ret;
    }
    private boolean checkPay(String cardOilType){
        if(getOilIndex(orderOilKind.getText()+"",cardOilType) == -1){
            errMsg = "本加油卡不允许加"+orderOilKind.getText();
            return false;
        }
        return true;
    }

    public String getCardName(int index){
        if (index>=6){
            return "未知卡";
        }
        String[] cardTypes = {"个人卡","出租卡","内部卡","公务车卡","单位卡","调试卡"};
        return cardTypes[index];
    }



}
