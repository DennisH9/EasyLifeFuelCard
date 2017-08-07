package com.newland.payment.ui.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.newland.base.util.MainIconUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.TransType;
import com.newland.payment.ui.activity.App;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * 用于保存
 * 
 * @author CB
 * @date 2015-6-12
 * @time 下午11:50:30
 */
public class MainMenuData extends Observable {

	/** 菜单界面按钮图标数据集 */
	public final List<MainMenuItem> MAIN_MENU_ITEMS = new ArrayList<MainMenuItem>();

	public void checkAll() {
		initMenu();
		LoggerUtils.i("before :"+MAIN_MENU_ITEMS.toString());
		checkMenu(findDataByName(null, App.mainMenuData.MAIN_MENU_ITEMS));
		LoggerUtils.i("after :"+MAIN_MENU_ITEMS.toString());
		setChanged();
		notifyObservers();
	}

//	public static List<MainMenuItem> findDataByName(String name) {
	public static List<MainMenuItem> findDataByName(String name, List<MainMenuItem> items) {
		if (name == null) {
			return items;
		} else {

			if(items == null){
				return null;
			}
			for (MainMenuItem item : items) {
				if (item.getName().equals(name)) {
					return item.getChilds();
				}
				if (item.getChilds() != null) {
					List<MainMenuItem> tmp = findDataByName(name, item.getChilds());
					if(tmp != null){
						return tmp;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 检查菜单项是否需要显示
	 */
	private void checkMenu(List<MainMenuItem> mainMenuItems) {

		for (int i = mainMenuItems.size() - 1; i >= 0; i--) {
			MainMenuItem mainMenuItem = mainMenuItems.get(i);

			// 从配置文件检测是否需要加载该节点
			if ((!mainMenuItem.getParamsKey().equals(ParamsConst.PARAMS_NULL) && "0"
					.equals(ParamsUtils.getString(mainMenuItem.getParamsKey())))

					// 重打印结算总计单特殊处理
					|| (MainActivity
							.getInstance()
							.getString(
									R.string.common_reprint_clearing_total_list)
							.equals(mainMenuItem.getName()) && "0"
							.equals(ParamsUtils
									.getString(ParamsConst.PARAMS_KEY_ANEW_PRINT_STATEMENT)))) {

				mainMenuItems.remove(i);

			} else if (mainMenuItem.getChilds() != null) {
				checkMenu(mainMenuItem.getChilds());
				if (mainMenuItem.getChilds() == null
						|| mainMenuItem.getChilds().size() == 0) {
					mainMenuItems.remove(i);
				}
			}
		}
	}

	/**
	 * 初始化菜单
	 */
	private void initMenu() {
		MAIN_MENU_ITEMS.clear();
		addMenus(
		// 消费
				newMenu(R.string.common_consumption,
						ParamsConst.PARAMS_KEY_TRANS_SALE, TransType.TRANS_SALE),
				// 消费撤销
				newMenu(R.string.common_revocation,
						ParamsConst.PARAMS_KEY_TRANS_VOID,
						TransType.TRANS_VOID_SALE),
				// 退货
				newMenu(R.string.common_return_goods,
						ParamsConst.PARAMS_KEY_TRANS_REFUND,
						TransType.TRANS_REFUND),
				// 预授权
//				newMenu(R.string.common_pre_author,
//						ParamsConst.PARAMS_NULL,
//						TransType.TRANS_NULL,
//						// 预授权
//						newMenu(R.string.common_pre_author,
//
//						ParamsConst.PARAMS_KEY_TRANS_PREAUTH,
//								TransType.TRANS_PREAUTH),
//						// 预授权完成请求
//						newMenu(R.string.common_pre_author_finish,
//
//						ParamsConst.PARAMS_KEY_TRANS_AUTHSALE,
//								TransType.TRANS_AUTHSALE),
//						// 预授权完成通知
//						newMenu(R.string.common_pre_author_finish_notice,
//
//						ParamsConst.PARAMS_KEY_TRANS_AUTHSALEOFF,
//								TransType.TRANS_AUTHSALEOFF),
//						// 预授权撤销
//						newMenu(R.string.common_pre_author_revocation,
//
//						ParamsConst.PARAMS_KEY_TRANS_VOIDPREAUTH,
//								TransType.TRANS_VOID_PREAUTH),
//						// 预授权完成撤销
//						newMenu(R.string.common_pre_author_finish_revocation,
//
//						ParamsConst.PARAMS_KEY_TRANS_VOIDAUTHSALE,
//								TransType.TRANS_VOID_AUTHSALE)),
				// 打印
//				newMenu(R.string.common_print, ParamsConst.PARAMS_NULL,
//						TransType.TRANS_NULL,
//						// 重打最后一笔
//
//						newMenu(R.string.common_reprint_trait_last,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//						// 重打任意一笔
//
//						newMenu(R.string.common_reprint_trait_random,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//						// 打印交易明细
//
//						newMenu(R.string.common_print_consumption_detail,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//						// 重打印结算总计单
//
//						newMenu(R.string.common_reprint_clearing_total_list,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//						// 打印交易总汇
//
//						newMenu(R.string.common_print_consumption_all,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL)),
				// 管理

//				newMenu(R.string.common_manage,
//						ParamsConst.PARAMS_NULL,
//						TransType.TRANS_NULL,
//						// 签到
//
//						newMenu(R.string.common_sign_in,
//
//								ParamsConst.PARAMS_NULL,
//								TransType.TRANS_NULL,
//								// POS签到
//
//								newMenu(R.string.common_sign_in_pos,
//
//								ParamsConst.PARAMS_NULL, TransType.TRANS_LOGIN),
//								// 操作员签到
//
//								newMenu(R.string.common_operator_sign_in,
//
//								ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//								// 收银员积分签到
//
//								newMenu(R.string.common_operator_sign_in_score,
//
//								ParamsConst.PARAMS_NULL,
//										TransType.TRANS_CASHIER_LOGIN)),
//						// 签退
//
//						newMenu(R.string.common_sign_out,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_LOGOUT),
//						// 交易查询
//
//						newMenu(R.string.common_search_consumption,
//
//								ParamsConst.PARAMS_NULL,
//								TransType.TRANS_NULL,
//								// 查询交易明细
//
//								newMenu(R.string.common_search_consumption_detail,
//
//								ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//								// 查询交易总汇
//
//								newMenu(R.string.common_search_consumption_all,
//
//								ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//								// 按凭证号查询
//
//								newMenu(R.string.common_search_by_evidence,
//
//								ParamsConst.PARAMS_NULL, TransType.TRANS_NULL)),
//
//						// 柜员管理
//
//						newMenu(R.string.common_operator_manage,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//
//						// 结算
//
//						newMenu(R.string.common_settlement,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_SETTLE),
//						// 锁定终端
//
//						newMenu(R.string.common_lock_terminal,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL),
//						// 版本
//
//						newMenu(R.string.common_version,
//
//						ParamsConst.PARAMS_NULL, TransType.TRANS_NULL)
//
//				),

				newMenu(R.string.common_sign_in,

						ParamsConst.PARAMS_NULL,
						TransType.TRANS_NULL,
						// POS签到

						newMenu(R.string.common_sign_in_pos,

								ParamsConst.PARAMS_NULL, TransType.TRANS_LOGIN),
						// 操作员签到

						newMenu(R.string.common_operator_sign_in,

								ParamsConst.PARAMS_NULL, TransType.TRANS_NULL)
						// 收银员积分签到
				),
//				newMenu(R.string.common_settlement,ParamsConst.PARAMS_NULL, TransType.TRANS_SETTLE),
//
//						// 电子现金
//						newMenu(R.string.common_elec_cash,
//								ParamsConst.PARAMS_NULL,
//								TransType.TRANS_NULL,
//								// 快速支付
//								newMenu(R.string.common_trans_ecnotuchsale,
//
//								ParamsConst.PARAMS_KEY_TRANS_QPBOC,
//										TransType.TRANS_QPBOC),
//								// 普通支付
//								newMenu(R.string.normal_pay,
//
//								ParamsConst.PARAMS_KEY_TRANS_EC,
//										TransType.TRANS_EC_PURCHASE),
//								// 圈存
//								newMenu(R.string.load,
//
//										ParamsConst.PARAMS_NULL,
//										TransType.TRANS_NULL,
//										// 现金充值
//										newMenu(R.string.amount_recharge,
//
//												ParamsConst.PARAMS_KEY_TRANS_ECMONEYLOAD,
//												TransType.TRANS_EC_LOAD_CASH),
//										// 指定账户圈存
//										newMenu(R.string.trans_ecpointload,
//
//												ParamsConst.PARAMS_KEY_TRANS_ECPOINTLOAD,
//												TransType.TRANS_EC_LOAD),
//										// 非指定账户圈存
//										newMenu(R.string.trans_ecnopointload,
//
//												ParamsConst.PARAMS_KEY_TRANS_ECNOPOINTLOAD,
//												TransType.TRANS_EC_LOAD_NOT_BIND),
//										// 电子现金充值撤销
//										newMenu(R.string.amount_recharge_cancel,
//
//												ParamsConst.PARAMS_KEY_TRANS_ECVOIDMONEYLOAD,
//												TransType.TRANS_EC_VOID_LOAD_CASH)),
//								// 余额查询
//								newMenu(R.string.common_check_balance,
//
//								ParamsConst.PARAMS_NULL,
//										TransType.TRANS_EC_BALANCE),
//								// 明细查询
//								newMenu(R.string.check_detail,
//
//								ParamsConst.PARAMS_NULL,
//										TransType.TRANS_EC_DETAIL),
//								// 脱机退货
//								newMenu(R.string.offline_return,
//
//								ParamsConst.PARAMS_KEY_TRANS_ECREFUND,
//										TransType.TRANS_EMV_REFUND),
//								// 圈存日志
//								newMenu(R.string.check_ecload_log,
//										ParamsConst.PARAMS_NULL,
//										TransType.TRANS_LOAD_DETAIL)),
	
						// 余额查询
						newMenu(R.string.common_check_balance,ParamsConst.PARAMS_KEY_TRANS_BALANCE,
								TransType.TRANS_BALANCE));
	}

	private MainMenuItem newMenu(int resName, String resParam, int transType,
			MainMenuItem... items) {
		return new MainMenuItem(MainActivity.getInstance().getString(resName),
				MainIconUtils.getIcon(resName), resParam,
				transType/*TransUtils.getTransBean(transType)*/, items);
	}

	private void addMenus(MainMenuItem... items) {
		for (int i = 0; i < items.length; i++) {
			MAIN_MENU_ITEMS.add(items[i]);
		}
	}

}
