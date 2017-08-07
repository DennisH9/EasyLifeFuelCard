package com.newland.payment.service;

import com.newland.base.util.TransUtils;
import com.newland.payment.aidl.IManagerService;
import com.newland.payment.mvc.service.WaterService;
import com.newland.payment.mvc.service.impl.WaterServiceImpl;
import com.newland.payment.ui.activity.MainActivity;
import com.newland.payment.ui.fragment.SelfCheckFragment;
import com.newland.pos.sdk.util.LoggerUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class ManagerService extends Service{
	
	@Override
	public IBinder onBind(Intent arg0) {
		return new ManagerServiceStub();
	}
	
	private class ManagerServiceStub extends IManagerService.Stub{

		@Override
		public boolean reset() throws RemoteException {
			// TODO Auto-generated method stub
						
			try {
				WaterService waterService = new WaterServiceImpl(getApplication());
				int nCount = waterService.getWaterCount();
				
				
				if (nCount > 0) {
					
					return false;
//					TransController controller = new TransController(getApplication());
//					controller.start(new Settle());
					
				} else {
					LoggerUtils.d("lxb reset没有流水文件");
				}
				
				
			} catch (Exception e) {	
				// TODO: handle exception
				LoggerUtils.d("lxb reset异常退出");				
				return false;
			}
		
			//清流水
			TransUtils.clearWater(getApplication());
			//初始化
			SelfCheckFragment.initExtendParamsToSharedPreferences(MainActivity.getInstance());
			return true;
		}

		@Override
		public boolean activate() throws RemoteException {
			// TODO Auto-generated method stub
			return true;
		}
		
	
	}
	



	

}
