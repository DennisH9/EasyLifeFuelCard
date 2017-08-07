package com.newland.payment.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newland.base.util.MessageUtils;
import com.newland.payment.R;
import com.newland.payment.ui.view.CommonDialog;
import com.newland.pos.sdk.bean.BaseBean;

/**
 * 母pos导密钥界面
 * @author lst
 * @date 20150610
 */
public class MPOSLoadKeyFragment extends BaseSettingFragment{
//	@ViewInject (R.id.txt_load_key_info)
	TextView txtLoadKeyInfoText;
	private boolean isStartDownload;
	//private String infoString = "";
	//private String newInfoString = null;
	private CommonDialog dialog = null;
	Message msg = new Message();
	int line = 0;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:	
				txtLoadKeyInfoText.setText(context.getString(R.string.setting_mpos_load_key_connect));
				//newInfoString = context.getString(R.string.setting_mpos_load_key_connect)+"\n";
				//infoString = newInfoString+infoString;
				//txtLoadKeyInfoText.setText(infoString);
				break;
			case 2:
				getDialog(R.string.setting_mpos_load_key_open_port_fail);
				dialog.show();
				break;
			case 3:
				getDialog(R.string.setting_mpos_load_key_write_data_fail);
				dialog.show();
				break;
			case 4:
				getDialog(R.string.setting_mpos_load_key_read_data_error);
				dialog.show();
				break;
			case 5:
				txtLoadKeyInfoText.setText("\r\n" + msg.obj.toString());
				break;
			default:
				break;

			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.setting_mpos_load_key_fragment, null);
		super.onCreateView(inflater, container, savedInstanceState);
		return mFragmentView;

	}
	
	@Override
	protected void initData() {
		activity.setTitle(R.string.setting_secret_key_pos);
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				isStartDownload = true;
//				byte[] buf = new byte[2];
//				byte[] bcdMainKey = new byte[16];
//				byte keyIndex = 0;
//				int keyLen;
//				
//				if (handler != null) {
//					handler.sendEmptyMessage(1);
//				}
//				NLUART3Manager uart3Manager = (NLUART3Manager) activity.getSystemService(Activity.UART3_SERVICE);
//				uart3Manager.open();
//				if (!uart3Manager.isValid()) {
//					if (handler!=null) {
//						handler.sendEmptyMessage(2);
//						return;
//					}
//				}
//				int ret = uart3Manager.setconfig(9600, 0,"8N1NN".getBytes());
//				while(isStartDownload) {
//					
//					ret = uart3Manager.write(new byte[]{0x04},2,1);
//					LoggerUtils.i("write ret: " + ret); 
//					if(ret < 0) {
//						if (handler!=null) {
//							handler.sendEmptyMessage(3);
//						}
//						return;
//					}
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					ret = uart3Manager.read(buf,2,1);
//					//BytesImpl.DebugHex("buf", buf);
//					LoggerUtils.i("read ret: " + ret); 
//					if(ret <= 0) {
//						continue;
//					} else {
//						if(buf[0]==0x03 && buf[1]<=9) {
//							keyIndex = buf[1];
//						}
//						break;
//					}
//				} 
//				isStartDownload = false;
//				if ("1".equals(ParamsUtils.getString(ParamsConst.PARAMS_KEY_ENCRYPT_MODE))) {
//					keyLen = 16;
//				} else {
//					keyLen = 8;
//				}
//				//System.arraycopy(buf, 2, bcdMainKey, 0, keyLen);
//				ret = uart3Manager.read(bcdMainKey,keyLen,1);
//				//BytesImpl.DebugHex("bcdMainKey", bcdMainKey);
//				uart3Manager.close();
//				if(ret != keyLen) {
//					if (handler!=null) {
//						handler.sendEmptyMessage(4);
//					}
//					return;
//				}
//				String strMainKey = new String(bcdMainKey);
//				SecurityModule module = SecurityModule.getInstance();
//				module.loadMainKey(keyIndex,strMainKey);
//				msg.what = 5;
//				msg.obj = getString(R.string.setting_mpos_load_key_suss, keyIndex + "");
//				handler.sendMessage(msg);
//			}
//		}).start();
		
	}
	
	private void backFragment(){
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				activity.backFragment();
			}
		});
	}
	private void getDialog(int resourcesId) {

		dialog = MessageUtils.getCommonDialog(activity,resourcesId, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				backFragment();
			}
		});
	}
	@Override
	public void onFragmentHide() {
		isStartDownload = false;
		super.onFragmentHide();
	}

	/**
	 * 监听返回事件
	 */
	@Override
	protected boolean onBackKeyDown() {
		handler = null;
		if (isStartDownload) {
			isStartDownload = false;
			backFragment();
			return true;
		} else {
			return super.onBackKeyDown();
		}
	}

	@Override
	protected void initClickEvent(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected BaseBean getBean() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

}
