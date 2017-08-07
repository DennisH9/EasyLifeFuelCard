package com.newland.payment.ui.adapter;

import android.util.SparseArray;
import android.view.View;

import com.newland.payment.R;

/**
 * Holder工具类
 * @author CB 
 * @version 创建时间：2014年8月21日 下午1:51:57  
 * 
 */
public class HolderUtils {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view,int id){
        Object tag = view.getTag(R.id.holder_util_tag_key);
        SparseArray<View> viewHolder;
        if(tag == null){
            viewHolder = new SparseArray<View>();
            view.setTag(R.id.holder_util_tag_key,viewHolder);
        }
        else{
            viewHolder = (SparseArray<View>) tag;
        }
        View childView = viewHolder.get(id);
        if(childView == null){
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T)childView;
    }
}
