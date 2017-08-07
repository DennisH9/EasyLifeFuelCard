package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.newland.base.util.MessageUtils;
import com.newland.base.util.ParamsUtils;
import com.newland.payment.R;
import com.newland.payment.common.ParamsConst;
import com.newland.payment.common.ParamsTrans;
import com.newland.payment.common.tools.SocketHelper;
import com.newland.payment.common.tools.SocketListener;
import com.newland.payment.trans.bean.CommunicationBean;
import com.newland.payment.trans.bean.CommunicationBean.CommunicationFailReason;
import com.newland.pos.sdk.bean.BaseBean;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 通讯界面
 * @author chenkh
 * @date 2015-5-17
 * @time 上午10:06:33
 *
 */
@SuppressLint({ "InflateParams", "ValidFragment" })
public class CommunicationFragment extends BaseFragment {
	
	private CommunicationBean communicationBean;
	private String[] tipContent;
	private Dialog dialog;
	/**
	 * socket是否连接成功
	 */
	private boolean isConnected;

	private SocketHelper socketHelper;
	
	private CommunicationFragment() {
		super(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_OUT_TIME, 60));
	}
	
	public static CommunicationFragment newInstance(CommunicationBean communicationBean) {
		CommunicationFragment fragment = new CommunicationFragment();
		fragment.communicationBean = communicationBean;
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.empty_view,
				null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;
	}

	@Override
	protected void initData() {
		if (communicationBean.getContent() == null) {
			tipContent = new String[]{getText(R.string.comm_waitting_network).toString(),
				//	getText(R.string.comm_send_data).toString(),
					getText(R.string.comm_receive_data).toString(),
					getText(R.string.comm_receive_data).toString()};
		} else {
			tipContent = new String[3];
			String[] msg = communicationBean.getContent();
			for (int i=0; i<3; i++) {
				if (msg.length > i) {
					tipContent[i] = msg[i];
				} else {
					tipContent[i] = null;
				}
			}
		}
		showCountProgress(tipContent[0]);
		isConnected = false;
		/** 开始通讯 */
		startCommunication();
	}
	
	/** 交易取消标志 */
	private boolean cancelFlag = false;
	
	/** 已收到返回报文标识 */
	private boolean isSucessFlag = false;
	
	/** 交易数据收发线程 */
	private void startCommunication(){
		new Thread(){
			public void run(){
				// 连接socket
				trySocketConnect();
				// 发送数据前，判断是否已经结束交易，若结束收发线程停止
				if (isTransCancel()) {
					LoggerUtils.d("连接结束，交易取消....");
					return;
				}
				showCountProgress(tipContent[1]);
				byte[] requst = communicationBean.getRequest();
				byte[] response = null;
				// 发送数据
				if (ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_USE_HTTP)
					? socketHelper.sendSLL(requst) : socketHelper.send(requst)) {
					if (isTransCancel()) {
						LoggerUtils.d("连接结束，交易取消....");
						return;
					}
					showCountProgress(tipContent[2]);
					// 接收数据
					response = ParamsUtils.getBoolean(ParamsConst.PARAMS_KEY_IS_USE_HTTP) 
					? socketHelper.receiveSLL():socketHelper.receive();
					socketHelper.closeComm();
					if (response == null) {
						LoggerUtils.e("数据接收为空...onFail()...");
						communicationBean.setData(null);
						communicationBean.setReason(CommunicationFailReason.RECEIVE_FAIL);

						ParamsUtils.setBoolean(ParamsConst.PARAMS_IS_USING_OTHER_IP_AND_PORT,
								!ParamsUtils.getBoolean(ParamsConst.PARAMS_IS_USING_OTHER_IP_AND_PORT));
						onFail();
						return;
					}else{
						isConnected = true;
					}
				} else {
					LoggerUtils.e("数据发送失败...onFail()...");
					communicationBean.setData(null);
					if (isConnected) {
						// 已连接，失败原因接收失败
						communicationBean.setReason(CommunicationFailReason.RECEIVE_FAIL);
					} else {
						// 未连接，失败原因连接失败
						communicationBean.setReason(CommunicationFailReason.CONNECT_FAIL);
					}

					ParamsUtils.setBoolean(ParamsConst.PARAMS_IS_USING_OTHER_IP_AND_PORT,
							!ParamsUtils.getBoolean(ParamsConst.PARAMS_IS_USING_OTHER_IP_AND_PORT));
					onFail();
					return;
				}
				if (isTransCancel()) {
					LoggerUtils.d("接收数据之后，交易取消....");
					return;
				}
				
				isSucessFlag = true;
				communicationBean.setResponse(response);
				//处理报文头请求
				dealCommHead(communicationBean.getHead());
				onSucess();
			}
		}.start();
	}
	
	/** 判断通讯是否已结束 */
	private boolean isTransCancel(){
		return cancelFlag;
	}

	private void trySocketConnect() {
		
		String ip;
		int port;
		if(communicationBean.getMposDwnm() == false){
			if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 0){
				ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_CDMA_SERVERIP1);
				port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_CDMA_PORT1, 8080);
			}else if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 1){
				ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_GPRS_SERVERIP1);
				port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_GPRS_PORT1, 8080);
			}else{
				ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_WIFI_SERVERIP1);
				port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_WIFI_PORT1, 8080);
			}

			if(ParamsUtils.getBoolean(ParamsConst.PARAMS_IS_USING_OTHER_IP_AND_PORT)){
				LoggerUtils.d("lxb 通讯使用备份IP和端口222");
                ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_WIFI_SERVERIP1);
                port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_WIFI__BACK_PORT2);
			}else{
				LoggerUtils.d("lxb 通讯使用正常IP和端口111");
			}
		}else{
			if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 0){
				ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_CDMA_SERVERIP3);
				port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_CDMA_PORT3, 8080);
			}else if(ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMMUNICATION_TYPE) == 1){
				ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_GPRS_SERVERIP3);
				port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_GPRS_PORT3, 8080);
			}else{
				ip = ParamsUtils.getString(ParamsConst.PARAMS_KEY_WIFI_SERVERIP3);
				port = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_WIFI_PORT3, 8080);
			}
		}

		int timeout = ParamsUtils.getInt(ParamsConst.PARAMS_KEY_COMM_TIMEOUT, 30);
		LoggerUtils.e("L132.......");
		socketHelper = SocketHelper.getSocketHelper(ip, port, timeout, new SocketListener() {
			
			@Override
			public void overSendData(boolean hasSenddata) {
				if (hasSenddata) {
					isConnected = hasSenddata;
				}
			}
		});
		LoggerUtils.e("L142.......");
		socketHelper.initSocket();
	}
	
	private Dialog progressCountDialog;
	private TextView tvTips;
	private View rootView;

	private void initCountProgress() {
		if (null == progressCountDialog) {
			progressCountDialog = new Dialog(activity, R.style.progress_dialog);
			progressCountDialog.setCancelable(false);
			rootView = LayoutInflater.from(activity).inflate(
					R.layout.progress_contain_cutdown_text_tip, null);
			tvTips = (TextView) rootView.findViewById(R.id.tv_dialog_tips);

			progressCountDialog.setContentView(rootView);
			// APP 获取 HOME keyEvent的设定关键代码。
			progressCountDialog.getWindow().addFlags(3);
			progressCountDialog.getWindow().addFlags(5);
			progressCountDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dg, int keyCode,
						KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_HOME) {
						return true;
					} else if (event.getAction()==KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
						dialog = MessageUtils.showCommonDialog(
								activity, 
								activity.getResources().getString(R.string.common_cancel_trans_tip_msg), 
								new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {
										if (isSucessFlag) {
											// 交易已收到应答，点击取消交易无效
											return;
										}
										// 确认
										cancelFlag = true;
										communicationBean.setData(null);
										if (isConnected) {
											// 已连接，失败原因接收失败
											communicationBean.setReason(CommunicationFailReason.RECEIVE_FAIL);
										} else {
											// 未连接，失败原因连接失败
											communicationBean.setReason(CommunicationFailReason.CONNECT_FAIL);
										}
										onBack();
									}
								}, 
								new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {
										// 取消
										if (dialog!=null && dialog.isShowing()){
											dialog.dismiss();
											dialog = null;
										}
									}
								});
						return true;
					}
					return false;
				}
			});
		}
	}

	private void showCountProgress(final String msg) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (activity.isFinishing()) {
					return;
				}
				if (null == progressCountDialog) {
					initCountProgress();
				}
				if (progressCountDialog != null
						&& !progressCountDialog.isShowing()) {
					if (!StringUtils.isEmpty(msg))
						tvTips.setText(msg);
					progressCountDialog.show();
				} else if (null != progressCountDialog
						&& progressCountDialog.isShowing()) {
					if (!StringUtils.isEmpty(msg))
						tvTips.setText(msg);
				}
			}
		});

	}
	
	@Override
	public BaseBean getBean() {
		return this.communicationBean;
	}

	private void dismissDialog(){
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (dialog!=null && dialog.isShowing()){
					dialog.dismiss();
					dialog = null;
				}
				if (null != progressCountDialog
						&& progressCountDialog.isShowing()) {
					progressCountDialog.dismiss();
					progressCountDialog = null;
				}
			}
		});
	}

	@Override
	protected void initEvent() {
	}

	@Override
	protected void initClickEvent(View view) {
	}
	
	@Override
	public void onFragmentHide() {
		dismissDialog();
		super.onFragmentHide();
	}

	private void dealCommHead(String resphead){
		LoggerUtils.d("响应报文头：" + resphead + " 处理要求:" + resphead.charAt(5));
		try{
			switch (resphead.charAt(5)){
			case '1':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_PARAM_DOWN, true);
				break;
			case '2':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_STATUS_SEND, true);
				break;
			case '3':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_RESIGN, true);
				break;
			case '4':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CAPK_DOWN, true);
				break;
			case '5':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_AID_DOWN, true);
				break;
			case '6':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_TMS_PARAM_DOWN, true);
				break;
			case '7':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_BALACKLIST_DOWN, true);
				break;
				
			case '9':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_RF_PARAM_DOWN, true);
				break;
				
			case 'b':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CARDBIN_B_UPDATE, true);
				break;
				
			case 'c':
				ParamsUtils.setBoolean(ParamsTrans.PARAMS_IS_CARDBIN_C_DOWN, true);
				break;
			default:
				break;				
			}
		}catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.d("后台返回的报文头异常");	
		}
	}
	
	
}
