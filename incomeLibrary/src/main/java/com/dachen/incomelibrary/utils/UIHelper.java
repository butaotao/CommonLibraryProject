package com.dachen.incomelibrary.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 
* @ClassName: UIHelper 
* @Description: TODO(UI帮助类) 
* @author yehj  V1.0.0
* @date 2015-7-31 下午4:48:51 
*
 */

public class UIHelper {
	
	
	
	//弹出Toast
	public static void ToastMessage(Context cont, int msg) {
		if(cont!=null){
			Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
		}
	}

	public static void ToastMessage(Context cont, String msg) {
		if(cont!=null){
			Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
		}
	}	
	
	/**
	 * 隐藏软键盘
	 * @param cont
	 * @param v
	 */
	public static void hideSoftInput(Context cont,View v){
		try{
			InputMethodManager inputMethodManager = (InputMethodManager) cont.getSystemService(Context.INPUT_METHOD_SERVICE);  
			if(inputMethodManager.isActive()){  
				inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);  
			} 		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void openSoftInput(Context context){
		try{
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void setForegroundColorSpan(SpannableString spanStr,Context context,
											  int start,int end,int colorResId){
		ForegroundColorSpan coloSpan = new ForegroundColorSpan(context.getResources().getColor(colorResId));
		spanStr.setSpan(coloSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);		
	}
	
	/**
	 * 获取颜色selector
	 * @author luopeng
	 * @param context
	 * @param idNormal
	 * @param idPressed
	 * @return
	 */
	public static StateListDrawable newColorSelector(Context context, int idNormal, int idPressed){
		return newColorSelector( context, idNormal,  idPressed, -1,-1);
	}
	
	/**
	 * 获取颜色selector
	 * @author luopeng
	 * @param context
	 * @param idNormal
	 * @param idPressed
	 * @param idFocused
	 * @param idUnable
	 * @return
	 */
    public static StateListDrawable newColorSelector(Context context, int idNormal, int idPressed, int idFocused,  
            int idUnable) {  
        return newSelector(context, idNormal, idPressed, idFocused, idUnable, 1);  
    } 		
	
	/**
	 * 获取图片selector
	 * @author luopeng  
	 * @param context
	 * @param idNormal
	 * @param idPressed
	 * @param idFocused
	 * @param idUnable
	 * @return
	 */
    public static StateListDrawable newDrawableSelector(Context context, int idNormal, int idPressed, int idFocused,  
            int idUnable) {  
        return newSelector(context, idNormal, idPressed, idFocused, idUnable, 0);  
    } 	
    
    /**
	 * 获取图片selector
	 * @author luopeng  
     * @param context
     * @param idNormal
     * @param idPressed
     * @param idFocused
     * @param idUnable
     * @param type 0:图片；1：color
     * @return
     */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused,  
            int idUnable, int type) {  
    	
        StateListDrawable bg = new StateListDrawable();  
        Drawable normal = getSelectorDrawalbe(context,idNormal,type);

        Drawable pressed = getSelectorDrawalbe(context,idPressed,type);  
        Drawable focused = getSelectorDrawalbe(context,idFocused,type);  
        Drawable unable = getSelectorDrawalbe(context,idUnable,type);
    	//注意该处的顺序，只要有一个状态与之相配，背景就会被换掉  
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果    	          
        // View.PRESSED_ENABLED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);  
        // View.ENABLED_FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);  
        // View.ENABLED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);  
        // View.FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_focused }, focused);  
        // View.WINDOW_FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);  
        // View.EMPTY_STATE_SET  
        bg.addState(new int[] {}, normal);  
        return bg;  
    }  
    
    public static StateListDrawable newDrawableSelector(Context context, Drawable normal,Drawable pressed,Drawable selected) { 

//        StateListDrawable bg = new StateListDrawable();  
//        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_window_focused }, pressed);
//        bg.addState(new int[] { android.R.attr.state_selected }, selected);
//        bg.addState(new int[] {}, normal);  
//        return bg;
    	 return newDrawableSelector(context, normal, pressed, null, null, selected);
    }    
    
    public static StateListDrawable newDrawableSelector(Context context, Drawable normal,Drawable pressed) { 
        return newDrawableSelector(context, normal, pressed, null, null, null);  
    }     
    
    public static StateListDrawable newDrawableSelector(Context context, Drawable normal,Drawable pressed, Drawable focused, Drawable unable, Drawable selected) {  
        StateListDrawable bg = new StateListDrawable();  
    	//注意该处的顺序，只要有一个状态与之相配，背景就会被换掉  
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果    	        
        // View.PRESSED_ENABLED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed); 
     // View.ENABLED_FOCUSED_STATE_SET 
        if(selected != null)
        	bg.addState(new int[] { android.R.attr.state_selected }, selected);
        if(focused != null)
        	bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);  
        // View.FOCUSED_STATE_SET  
        if(focused != null)
        	bg.addState(new int[] { android.R.attr.state_focused }, focused);  
        // View.WINDOW_FOCUSED_STATE_SET  
        if(unable != null)
        	bg.addState(new int[] { android.R.attr.state_window_focused }, unable);  
        // View.EMPTY_STATE_SET  
        bg.addState(new int[] {}, normal);  
        return bg;  
    }    
    
    private static Drawable getSelectorDrawalbe(Context context, int value, int type){
    	Drawable drawable = null;
    	try{
    		if(type == 0 ){
    			drawable = context.getResources().getDrawable(value);
    		}else if(type == 1){
    			drawable = new ColorDrawable(value);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return drawable;
    }
    

	
	public static void showPopAsDropdown(final View v,final PopupWindow pop, final int xoff, final int yoff){
		v.post(new Runnable() {
			public void run() {
				pop.showAsDropDown(v, xoff, yoff);
			}
		});
	}
	
}
