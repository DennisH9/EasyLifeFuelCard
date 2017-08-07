package com.newland.base.util;

import com.newland.payment.R;

/**
 * 
 * 
 * @author CB
 * @date 2015-61
 * @time 下午10:07:50
 */
public class MainIconUtils {
	public static int getIcon(int resName) {

		int icon = R.drawable.activity_bg;

		if (resName == R.string.common_consumption) {
			icon = R.drawable.main_icon_1;

			// 消费撤销
		} else if (resName == R.string.common_revocation) {
			icon = R.drawable.main_icon_2;

			// 退货
		} else if (resName == R.string.common_return_goods) {
			icon = R.drawable.main_icon_3;

			// 预授权
		} else if (resName == R.string.common_pre_author) {
			icon = R.drawable.main_icon_4;


			// 打印
		} else if (resName == R.string.common_print) {
			icon = R.drawable.main_icon_6;


			// 管理
		} else if (resName == R.string.common_manage) {
			icon = R.drawable.main_icon_7;


			// 其它
		} else if (resName == R.string.common_other) {
			icon = R.drawable.main_icon_8;


			// 电子现金
		} else if (resName == R.string.common_elec_cash) {
			icon = R.drawable.main_icon_9;


			// 余额查询
		} else if (resName == R.string.common_check_balance) {
			icon = R.drawable.main_icon_11;

			// 预授权完成请求

			icon = R.drawable.main_icon_17;

			// 预授权完成通知
		} else if (resName == R.string.common_pre_author_finish) {
			icon = R.drawable.main_icon_17;

			// 预授权完成通知
		} else if (resName == R.string.common_pre_author_finish_notice) {
			icon = R.drawable.main_icon_18;

			// 预授权撤销
		} else if (resName == R.string.common_pre_author_revocation) {
			icon = R.drawable.main_icon_19;

			// 预授权完成撤销
		} else if (resName == R.string.common_pre_author_finish_revocation) {
			icon = R.drawable.main_icon_20;

			// 重打最后一笔
		} else if (resName == R.string.common_reprint_trait_last) {
			icon = R.drawable.main_icon_23;

			// 重打任意一笔
		} else if (resName == R.string.common_reprint_trait_random) {
			icon = R.drawable.main_icon_24;

			// 打印交易明细
		} else if (resName == R.string.common_print_consumption_detail) {
			icon = R.drawable.main_icon_25;

			// 重打印结算总计单
		} else if (resName == R.string.common_reprint_clearing_total_list) {
			icon = R.drawable.main_icon_26;

			// 打印交易总汇
		} else if (resName == R.string.common_print_consumption_all) {
			icon = R.drawable.main_icon_27;

			// 签到
		} else if (resName == R.string.common_sign_in) {
			icon = R.drawable.main_icon_28;


			// 签退
		} else if (resName == R.string.common_sign_out) {
			icon = R.drawable.main_icon_29;

			// 交易查询
		} else if (resName == R.string.common_search_consumption) {
			icon = R.drawable.main_icon_30;

			// 柜员管理
		} else if (resName == R.string.common_operator_manage) {
			icon = R.drawable.main_icon_31;

			// 结算
		} else if (resName == R.string.common_settlement) {
			icon = R.drawable.main_icon_32;

			// 锁定终端
		} else if (resName == R.string.common_lock_terminal) {
			icon = R.drawable.main_icon_33;

			// 版本
		} else if (resName == R.string.common_version) {
			icon = R.drawable.main_icon_34;

			// POS签到
		} else if (resName == R.string.common_sign_in_pos) {
			icon = R.drawable.main_icon_35;

			// 操作员签到
		} else if (resName == R.string.common_operator_sign_in) {
			icon = R.drawable.main_icon_36;

			// 收银员积分签到
		} else if (resName == R.string.common_operator_sign_in_score) {
			icon = R.drawable.main_icon_37;

			// 查询交易明细
		} else if (resName == R.string.common_search_consumption_detail) {
			icon = R.drawable.main_icon_38;

			// 查询交易总汇
		} else if (resName == R.string.common_search_consumption_all) {
			icon = R.drawable.main_icon_39;

			// 按凭证号查询
		} else if (resName == R.string.common_search_by_evidence) {
			icon = R.drawable.main_icon_40;

			// 快速支付
		} else if (resName == R.string.common_trans_ecnotuchsale) {
			icon = R.drawable.main_icon_52;

			// 普通支付
		} else if (resName == R.string.normal_pay) {
			icon = R.drawable.main_icon_53;

			// 圈存
		} else if (resName == R.string.load) {
			icon = R.drawable.main_icon_54;


			// 明细查询
		} else if (resName == R.string.check_detail) {
			icon = R.drawable.main_icon_56;

			// 脱机退货
		} else if (resName == R.string.offline_return) {
			icon = R.drawable.main_icon_57;

			// 指定账户圈存
		} else if (resName == R.string.trans_ecpointload) {
			icon = R.drawable.main_icon_59;

			// 非指定账户圈存
		} else if (resName == R.string.trans_ecnopointload) {
			icon = R.drawable.main_icon_60;


			// 现金充值撤销
		} else if (resName == R.string.amount_recharge_cancel) {
			icon = R.drawable.main_icon_61;

		} else {
			icon = R.drawable.main_icon_8;

		}

		return icon;

	}
}
