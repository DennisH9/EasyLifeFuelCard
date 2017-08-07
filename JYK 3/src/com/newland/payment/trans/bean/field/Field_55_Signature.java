package com.newland.payment.trans.bean.field;

import com.newland.base.util.ParamsUtils;
import com.newland.base.util.TransUtils;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransConst;
import com.newland.payment.common.TransConst.EmvStatus;
import com.newland.payment.common.TransType;
import com.newland.payment.mvc.model.Water;
import com.newland.pos.sdk.common.EmvTag;
import com.newland.pos.sdk.common.TransConst.TransAttr;
import com.newland.pos.sdk.util.BytesUtils;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;
import com.newland.pos.sdk.util.TLVUtils;
import com.newland.pos.sdk.util.TLVUtils.PackTLV;
import com.newland.pos.sdk.util.TimeUtils;
import com.newland.pos.sdk.emv.EmvTLV;

/**
 * 电子签名 55 域
 * @version 1.0
 * @author spy
 * @date 2015年5月25日
 * @time 下午9:57:29
 */
public class Field_55_Signature {

	private Water water;
	
	public Field_55_Signature(Water water){
		this.water = water;
	}
	
	public byte[] pack(){
		try{
			PackTLV pack = TLVUtils.newPackTLV();
			
			/**
			 * FF00	商户名称	Ans, var. up to 40	POS终端	M
			 */
			LoggerUtils.d("dd FF00:" + ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME));
			pack.append(0xFF00, ParamsUtils.getString(ParamsConst.PARAMS_KEY_BASE_MERCHANTNAME).getBytes("GBK"));
			/**
			 * FF01	交易类型	Ans,var. up to 25	POS终端	M，见附录1
			 */

			pack.append(0xFF01, TransUtils.getTransType(water.getTransType())[0].getBytes("GBK"));
			
			/**
			 * FF02	操作员号	CN,1	POS终端	M，包含两位数字
			 */
			LoggerUtils.d("dd FF02:bcd" + water.getOper());
			pack.append(0xFF02, BytesUtils.str2bcd(water.getOper(), true));
			/**
			 * FF03	收单机构	An,11	44域	C，可以获取时上送
			 */
			if (!StringUtils.isNullOrEmpty(water.getAcqCode())){
				LoggerUtils.d("dd FF03:" + water.getAcqCode());
				pack.append(0xFF03, water.getAcqCode());
			}
			/**
			 * FF04	发卡机构	An,11	44域	C，可以获取时上送
			 */
			if (!StringUtils.isNullOrEmpty(water.getIisCode())){
				LoggerUtils.d("dd FF04:" + water.getIisCode());
				pack.append(0xFF04, water.getIisCode());
			}
			/**
			 * FF05	有效期	CN,2	14域	C，格式为YYMM，可以获取时上送
			 */
			if (!StringUtils.isNullOrEmpty(water.getExpDate())){
				LoggerUtils.d("dd FF05:bcd" + water.getExpDate());
				pack.append(0xFF05, BytesUtils.str2bcd(water.getExpDate(), true));
			}
			/**
			 * FF06	日期时间	CN,7	12域、13域	M，格式为YYYYMMDD hhmmss
			 */
			LoggerUtils.d("FF06日期时间:bcd" + TimeUtils.getCurrentYear() + water.getDate() + water.getTime());
			pack.append(0xFF06, BytesUtils.str2bcd(TimeUtils.getCurrentYear() + water.getDate() + water.getTime(), true));
			
			/**
			 * FF07	授权码	AN,6	38域	C，38域存在时出现
			 */
			if (!StringUtils.isNullOrEmpty(water.getAuthCode())){
				LoggerUtils.d("dd FF07:" + water.getAuthCode());
				pack.append(0xFF07,water.getAuthCode());
			}
			
//			/**
//			 * FF08	小费金额	CN,6	48域	C，在追加小费交易时出现，包含12位数字
//			 */
//			if (water.getTransType() == TransType.TRANS_ADJUST && water.getOldTransType() == TransType.TRANS_SALE){
//				pack.append(0xFF08, BytesUtils.str2bcd(String.format("%012d", water.getTipAmount()), true));
//			}
			
			/**
			 * FF09	卡组织	AN,3	63域	C，通知交易和离线交易出现
			 */
			if( water.getTransType() == TransType.TRANS_AUTHSALEOFF 
//					water.getTransType() == TransType.TRANS_OFFLINE 
//					|| water.getTransType() == TransType.TRANS_ADJUST 
//					|| water.getTransType() == TransType.TRANS_ORDER_AUTHSALEOFF 
//					|| water.getTransType() == TransType.TRANS_ALLIANCE_REFUND
//					|| water.getTransType() == TransType.TRANS_REFUND_PHONE_SALE 
//					|| water.getTransType() == TransType.TRANS_ORDER_REFUND
				|| water.getTransType() == TransType.TRANS_EMV_REFUND
				|| water.getTransType() == TransType.TRANS_REFUND){ 
				if (!StringUtils.isNullOrEmpty(water.getInterOrg())){
					pack.append(0xFF09,water.getInterOrg());
				}
			}
			
			/**
			 * FF0A	交易币种	AN,3	49域	C，可以获取时上送
			 */
			LoggerUtils.d("dd FF0A:" + water.getCurrency());
			pack.append(0xFF0A, water.getCurrency() == null ? TransConst.CURRENCY_CODE : water.getCurrency());
			/**
			 * FF0B	持卡人手机号码	CN，6	终端	C，可以获取时上送
			 */
			if (!StringUtils.isNullOrEmpty(water.getCardHolderTelphone())){
				LoggerUtils.d("dd FF0B持卡人手机号码:" + water.getCardHolderTelphone());
				pack.append(0xFF0B, BytesUtils.str2bcd(StringUtils.fill(water.getCardHolderTelphone(), "0", 12, true), true));
			}
			
			/**
			 * IC卡相关交易
			 */
			if (!StringUtils.isNullOrEmpty(water.getEmvAddition())){
				
				EmvTLV[] list = TLVUtils.getTLVList(BytesUtils.hexToBytes(water.getEmvAddition()));
				/**
				 * FF30 应用标签
				 */
				pack.append(0xFF30, TLVUtils.getValueFromTLVlist(EmvTag.TAG_50_IC_APPLABEL, list));
				
				/**
				 * FF31 应用名称
				 */
				pack.append(0xFF31, TLVUtils.getValueFromTLVlist(EmvTag.TAG_9F12_IC_APNAME, list));
				
				/**
				 *	FF22	应用标识	B,var.up to,16	PBOC借/贷记卡	C，IC卡交易获取时出现
				 */
				pack.append(0xFF22, TLVUtils.getValueFromTLVlist(EmvTag.TAG_4F_IC_AID, list));
				
				/**
				 * FF23 应用密文 B,8 PBOC借/贷记卡 C，当存在时出现，
				 * 基于借贷记应用的小额支付时表示TC，
				 * 基于借贷记应用的联机交易时表示ARQC
				 */
				if (water.getTransAttr() != TransAttr.ATTR_EMV_PREDIGEST_RF){
					pack.append(0xFF23, TLVUtils.getValueFromTLVlist(EmvTag.TAG_9F26_IC_AC, list));
				}
				
				if (water.getEmvStatus() == EmvStatus.EMV_STATUS_OFFLINE_SUCC){
					
					/**
					 * FF26	不可预知数	B,4	55域 Tag9F37	C，IC卡脱机消费交易时出现
					 */
					pack.append(0xFF26, TLVUtils.getValueFromTLVlist(EmvTag.TAG_9F37_TM_UNPNUM, list));
					
					/**
					 * FF27	应用交互特征	B,2	55域Tag9F82	C，IC卡脱机消费交易时出现
					 */
					pack.append(0xFF27, TLVUtils.getValueFromTLVlist(EmvTag.TAG_82_IC_AIP, list));
					
					/**
					 * FF28	终端验证结果	B,5	PBOC借/贷记卡	C，IC卡脱机消费交易时出现
					 */
					pack.append(0xFF28, TLVUtils.getValueFromTLVlist(EmvTag.TAG_95_TM_TVR, list));
					
					/**
					 * FF29	交易状态信息	B,2	PBOC借/贷记卡	C，IC卡脱机消费交易时出现
					 */
					pack.append(0xFF29, TLVUtils.getValueFromTLVlist(EmvTag.TAG_9B_TM_TSI, list));
					
					/**
					 * FF2A	应用交易计数器	B,2	PBOC借/贷记卡	C，IC卡脱机消费交易时出现
					 */
					pack.append(0xFF2A, TLVUtils.getValueFromTLVlist(EmvTag.TAG_9F36_IC_ATC, list));
					
					/**
					 * 	FF2B	发卡应用数据	B,32	PBOC借/贷记卡	C，IC卡脱机消费交易时出现
					 */
					
					pack.append(0xFF2B, TLVUtils.getValueFromTLVlist(EmvTag.TAG_9F10_IC_ISSAPPDATA, list));
				}
			}
			
			/**
			 * FF24	充值后卡片余额	CN,6	PBOC借/贷记卡	
			 * C，IC卡圈存类交易出现，包含12位数字
			 */
			if (water.getTransType() == TransType.TRANS_EC_LOAD 
				|| water.getTransType() == TransType.TRANS_EC_LOAD_CASH
				|| water.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
				pack.append(0xFF24, BytesUtils.str2bcd(String.format("%012d", water.getEcBalance()), true));
			}
			
			/**
			 * FF25	转入卡卡号	cn,var. up to 10	
			 * 62域	C，当IC卡非指定帐户圈存时或磁条卡账户充值时出现（不需要卡号屏蔽）， 
			 */
			if (/*water.getTransType() == TransType.TRANS_MAG_LOAD_ACCOUNT 
				||*/ water.getTransType() == TransType.TRANS_EC_LOAD_NOT_BIND){
				
				pack.append(0xFF25,BytesUtils.str2bcd(water.getCardNoTransIn(), true));
			}
			
			
			
			/**
			 * 	FF40	备注信息	ans,var.up to 60	
			 * 交易应答63.2.1、63.2.2和63.2.3子域有效数据（空格除外）	
			 * C，原交易应答63.2.1,63.2.2和63.2.3返回有效数据（空格除外）时出现
			 */
			if (water.getIisInfo() != null 
				|| water.getCupInfo() != null
				|| water.getMerAcqInfo() != null){
				pack.append(0xFF40, StringUtils.paddingString(water.getIisInfo() == null ? "" : water.getIisInfo(), 20, " ", 1) 
						+ StringUtils.paddingString(water.getCupInfo() == null ? "" : water.getCupInfo() , 20, " ", 1) 
						+ StringUtils.paddingString(water.getMerAcqInfo()==null ? "" : water.getMerAcqInfo(), 20, " ", 1));
			}

//			if (water.getTransType() == TransType.TRANS_INSTALMENT 
//				|| water.getTransType() == TransType.TRANS_VOID_INSTALMENT){
//				
//				ISOField62_Installment isoField62_Installment = new ISOField62_Installment(
//						water.getAddition());
//				/**
//				 * 	FF41	分期付款期数	Cn,1	分期付款消费交易请求	
//				 * C，原交易应答返回时出现，包含两位数字
//				 */
//				pack.append(0xFF41, BytesUtils.str2bcd(isoField62_Installment.getInstallmentNum(), true));
//				
//				/**
//				 * FF42 FF42	分期付款首期金额	Cn,6	分期付款消费交易应答	
//				 * C，原交易应答返回时出现，包含12位数字
//				 */
//				pack.append(0xFF42, BytesUtils.str2bcd(isoField62_Installment.getFirstAmt(), true));			
//	
//				/**
//				 * FF43	分期付款还款币种	Cn,2	分期付款消费交易应答	
//				 * C，原交易应答返回时出现，包含3位数字
//				 */
//				pack.append(0xFF43, BytesUtils.str2bcd(StringUtils.paddingString(isoField62_Installment.getCurrency(), 4, " ", 1), true));
//				
//				/**
//				 * FF44	持卡人手续费	Cn,6	分期付款消费交易应答	
//				 * C，原交易应答返回时出现,包含12位数字
//				 */
//				pack.append(0xFF44, BytesUtils.str2bcd(isoField62_Installment.getFee(), true));			
//			}
			
//			if (water.getTransType() == TransType.TRANS_BONUS_IIS_SALE 
//					|| water.getTransType() == TransType.TRANS_BONUS_ALLIANCE){
//					ISOField62_Bonus isoField62_Bonus = new ISOField62_Bonus(water.getAddition());
//					
//					/**
//					 * FF45	商品代码	Ans，var.up.to 30	积分消费交易请求	
//					 * C，原交易应答返回时出现
//					 */
//					pack.append(0xFF45, isoField62_Bonus.getItemCode());
//					
//					/**
//					 * FF46	兑换积分数	Cn,5	积分消费交易应答	
//					 * C，原交易应答返回时出现，包含10位数字
//					 */
//					pack.append(0xFF46, BytesUtils.str2bcd(isoField62_Bonus.getBonusPoint(), true));
//					
//					/**
//					 * FF57	积分余额	Cn,6	
//					 * 54域	C，原交易应答返回时出现 
//					 */
//					pack.append(0xFF57, BytesUtils.str2bcd(isoField62_Bonus.getBonusBalance(), true));
//					
//					/**
//					 * FF48	自付金额	Cn,6	积分消费交易应答	
//					 * C，部分承兑时出现包含12位数字
//					 */
//					pack.append(0xFF45, BytesUtils.str2bcd(isoField62_Bonus.getSelfAmt(), true));
//					
//					/**
//					 * FF4A	54域余额,下面还有消费的FF4A
//					 */
//					pack.append(0xFF4A, BytesUtils.str2bcd(isoField62_Bonus.getBonusBalance(), true));
//			
//			}
			
			/**
			 * FF49	承兑金额	cN,6	部分承兑交易应答报文第4域	
			 * C，部分扣款时出现
			 */
			
			if ((water.getTransType() == TransType.TRANS_SALE)
//					|| water.getTransType() == TransType.TRANS_BONUS_IIS_SALE
//					|| water.getTransType() == TransType.TRANS_BONUS_ALLIANCE) 
					&& "10".equals(water.getRespCode())){
				pack.append(0xFF49, BytesUtils.str2bcd(String.format("%012d",water.getAmount()), true));
			}
			
			/**
			 * FF4A	可用余额	cn,6	
			 * 54域	C，原交易应答返回时出现
			 */
			if (water.getTransType() == TransType.TRANS_SALE 
				&& water.getBalance() != null){
				pack.append(0xFF4A, BytesUtils.str2bcd(String.format("%012d",water.getBalance()), true));
			}
			
//			/**
//			 * FF4B	手机号码	an,11	
//			 * 62域	C，原交易应答返回时出现，手机号码屏蔽，除前三位和后三位以外全部显示”*”
//			 */
//			if (water.getTransType() == TransType.TRANS_APPOINTMENT_SALE){
//				if (null != water.getTelNo() && water.getTelNo().length() > 6){
//					int len = water.getTelNo().length();
//					String c1=water.getTelNo().substring(0,3);
//					String c3=water.getTelNo().substring(len-3, len);
//					String c2="*****";
//					pack.append(0xFF4B, c1+c2+c3);
//					
//				}
//			}
			
			if (water.getTransType() == TransType.TRANS_VOID_SALE 
					|| water.getTransType() == TransType.TRANS_VOID_AUTHSALE
					|| water.getTransType() == TransType.TRANS_EC_VOID_LOAD_CASH
					|| water.getTransType() == TransType.TRANS_VOID_PREAUTH
					|| water.getTransType() == TransType.TRANS_EMV_REFUND){
			/**
			 * FF60	原凭证号	Cn,3	
			 * 61域	C，撤销类交易和电子现金退货交易时出现
			 */
			if (!StringUtils.isNullOrEmpty(water.getOldTrace())){
				pack.append(0xFF60,BytesUtils.str2bcd(water.getOldTrace(), true));
			}
			
			/**
			 * FF61	原批次号	Cn,3	
			 * 61域	C，电子现金退货交易时出现
			 */
			if (water.getTransType() == TransType.TRANS_EMV_REFUND){
				pack.append(0xFF61,BytesUtils.str2bcd(water.getOldBatch(), true));
			}
			
			if (water.getTransType() == TransType.TRANS_REFUND
				|| water.getTransType() == TransType.TRANS_EMV_REFUND){
			/**
			 * FF62	原参考号	Cn,6	
			 * 37域	C，退货类（不含电子现金退货）交易出现
			 */
				if (water.getTransType() != TransType.TRANS_EMV_REFUND){
				if (!StringUtils.isNullOrEmpty(water.getOldRefNum())){
					pack.append(0xFF62,BytesUtils.str2bcd(water.getOldRefNum(), true));
				}
}
			}
			/**
			 * FF63	原交易日期	Cn,2	
			 * 61域	C，退货类交易（包括电子现金退货）出现
			 */
			if (!StringUtils.isNullOrEmpty(water.getOldDate())){
				pack.append(0xFF63,BytesUtils.str2bcd(water.getOldDate(), true));
			}
			}
			if (water.getTransType() == TransType.TRANS_VOID_AUTHSALE 
					|| water.getTransType() == TransType.TRANS_AUTHSALE 
					|| water.getTransType() == TransType.TRANS_VOID_PREAUTH ){
			/**
			 * FF64	原授权码	aN,6	
			 * 38域	C，预授权撤销、预授权完成（请求）、预授权完成（请求）撤销时输入的原预授权码
			 */
			if (!StringUtils.isNullOrEmpty(water.getOldAuthCode())){
					pack.append(0xFF64,water.getOldAuthCode());
				}
			}
			
			if (water.getTransType() == TransType.TRANS_EMV_REFUND){
			/**
			 * FF65	原终端号	Ans，8	
			 * 62域	C，电子现金退货上送
			 */
			if (!StringUtils.isNullOrEmpty(water.getOldTerminalId())){
					pack.append(0xFF65,water.getOldTerminalId());
				}
			}
			
			/**
			 * FF70	当前交易打印张数	CN，1	
			 * 终端	C，若终端获得时上送，表明终端侦测到当笔签字完成后POS终端打印的张数
			 */
			int printCount = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_BASE_PRTCOUNT, 1);
			pack.append(0xFF70,BytesUtils.str2bcd(String.format("%02d", printCount), true));

			return pack.pack();
		}catch (Exception e){
			e.printStackTrace();
			LoggerUtils.d("电子签名上送打包55域出错");
			return null;
		}
	}
	
}
