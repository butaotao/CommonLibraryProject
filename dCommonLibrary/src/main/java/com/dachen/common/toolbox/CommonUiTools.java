package com.dachen.common.toolbox;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import com.dachen.common.widget.dialog.MessageDialog;

/**
 * Created by Administrator on 2016/5/24.
 */
public class CommonUiTools {

    public static CommonUiTools commonUiTools;
    private  static MessageDialog messageDialog;
    private CommonUiTools(){

    }
    public static CommonUiTools getInstance(){
        if(commonUiTools == null){
            synchronized (CommonUiTools.class){
                commonUiTools=new CommonUiTools();
            }
        }
        return commonUiTools;
    }


    public  void appVersionUpdate(final Context context, String resultMsg) {

        if(null!=context&&context instanceof  Activity){
            if(messageDialog == null){
                messageDialog = new MessageDialog(context, "忽略", "去下载", resultMsg);
            }else{
                messageDialog.setMessage(resultMsg);
            }
            messageDialog.setBtn1ClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageDialog.dismiss();
                }
            });
            messageDialog.setBtn2ClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageDialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(getDownLoadUrl(context));
                    intent.setData(content_url);
                    context.startActivity(intent);
                }
            });
            Activity activity = (Activity) context;
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            String className = cn.getClassName();
            if (null!=activity.getComponentName()&&
                    activity.getComponentName().getClassName().equals(className)){
                if (!messageDialog.isShowing()){
                    messageDialog.show();
                }
            }
        }
    }

    public static String getDownLoadUrl(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (pInfo.packageName.equals("com.dachen.dgroupdoctor")) {
                return "http://xg.mediportal.com.cn/health/web/app/downDoctor.html";
            } else if (pInfo.packageName.equals("com.dachen.dgrouppatient")) {
                return "http://xg.mediportal.com.cn/health/web/app/downPatient.html";
            }else if(pInfo.packageName.equals("com.bestunimed.dgroupdoctor")){
                return "http://xg.mediportal.com.cn/health/web/app/downDoctor.html?app=bd&";
            }else if(pInfo.packageName.equals("com.bestunimed.dgrouppatient")){
                return "http://xg.mediportal.com.cn/health/web/app/downPatient.html?app=bd&";
            }else if (pInfo.packageName.equals("com.dachen.dgroupdoctorcompany")){
                return "http://xg.mediportal.com.cn/health/web/app/downDrug.html";
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }
}
