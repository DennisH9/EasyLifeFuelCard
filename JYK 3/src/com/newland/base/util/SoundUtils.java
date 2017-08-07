package com.newland.base.util;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.newland.payment.R;
import com.newland.payment.common.Const.SoundType;
import com.newland.pos.sdk.util.LoggerUtils;

/**
 * android原生音效
 * @author chenkh
 * @date 2015-7-29
 * @time 上午9:24:18
 *
 */
public class SoundUtils {

	private static SoundUtils INSTANCE;
	
	private SoundPool soundPool;
	
	private HashMap<Integer, Integer> soundId;
	
	private SoundUtils(){
	}
	
	public static SoundUtils getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new SoundUtils();
		}
		return INSTANCE;
	}
	
	@SuppressLint("UseSparseArrays")
	public void load(Context context){
		soundPool= new SoundPool(6,AudioManager.STREAM_SYSTEM,5);
		soundId = new HashMap<Integer, Integer>();
		soundId.put(SoundType.BEEP, soundPool.load(context, R.raw.shortbeep, 1));
		soundId.put(SoundType.LONG_BEEP, soundPool.load(context, R.raw.longbeep, 1));
		soundId.put(SoundType.TIP, soundPool.load(context, R.raw.systemtip, 1));
		soundId.put(SoundType.WARNING, soundPool.load(context, R.raw.longwarning, 1));
	}
	
	public void play(int soundType){
		switch (soundType){
		case SoundType.BEEP:
		case SoundType.LONG_BEEP:
		case SoundType.TIP:
		case SoundType.WARNING:
			soundPool.play(soundId.get(soundType),1, 1, 0, 0, 1);
			break;
		default:
			LoggerUtils.d("没有对应的声音文件~~~~");
			break;
		}
		
	}
}
