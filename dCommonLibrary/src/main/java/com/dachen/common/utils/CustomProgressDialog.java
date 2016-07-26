package com.dachen.common.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.common.R;

public class CustomProgressDialog extends Dialog {
	
	  private Context context = null; 
	  private  TextView textview;
	  private static CustomProgressDialog customProgressDialog = null;  
	  
	  public CustomProgressDialog(Context context){        
		  super(context);        
		  this.context = context;    
	  }         
	  
	  public CustomProgressDialog(Context context, int theme) {
		  super(context, theme);   
	  }
	  
	  public static CustomProgressDialog createDialog(Context context){
		  customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
		  customProgressDialog.setContentView(R.layout.loading_dialog);   
		  customProgressDialog.setCancelable(false);
		  customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;          
		  return customProgressDialog; 
	  }
	  
	  
	  public void onWindowFocusChanged(boolean hasFocus){  
		  if (customProgressDialog == null){          
			  return;      
		  }                 
		  ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.img);  
		  AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();      
		  animationDrawable.start();   
	  }
	  
	  public CustomProgressDialog setMessage(String strTitle){ 
		  textview=(TextView)customProgressDialog.findViewById(R.id.tipTextView);
		  textview.setText(strTitle);
		  return customProgressDialog;    
	  }
	  
	  
	  
}
