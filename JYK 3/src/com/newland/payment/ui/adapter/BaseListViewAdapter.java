package com.newland.payment.ui.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;
/**
 * 列表式适配器
 * @author jby
 * @param <T>
 *
 */
public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    
    protected List<T> mDataList;
    protected Context mActiviy;

    public BaseListViewAdapter(Context context, List<T> mDataList) {
        super();
        this.mActiviy = context; 
        this.mDataList = mDataList;
    }

	@Override
    public int getCount() {
        return (null == mDataList) ? 0 : mDataList.size();
    }

    @Override
	public T getItem(int position) {
		return (null == mDataList) ? null : mDataList.get(position);
	}


	@Override
	public long getItemId(int position) {
		return 0;
	}


	/**
     * 增加
     * @param collection
     * @return
     */
    public boolean addAll(Collection<? extends T> collection){
        return mDataList.addAll(collection);
    }
    
    /**
     * 删除
     * @param collection
     * @return
     */
    public boolean removeAll(Collection<? extends T> collection){
        return mDataList.removeAll(collection);
    }
    
    /**
     * 清除所有
     */
    public void clear(){
        mDataList.clear();
    }
    
    /**
     * 拷贝一份当前的list
     * <br/>
     * @return
     */
    public List<T> copyList(){
        synchronized (mDataList) {
            List<T> destination = new ArrayList<T>();
            destination.addAll(mDataList);
            return destination;
        }
    }
    

}
