package com.dachen.incomelibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.bean.BankInfo;
import com.dachen.incomelibrary.bean.FindBankNameResponse;
import com.dachen.incomelibrary.http.HttpCommClient;
import com.dachen.incomelibrary.utils.ExitSelHospital;
import com.dachen.incomelibrary.utils.UIHelper;
import com.dachen.incomelibrary.utils.UserInfo;
import com.dachen.incomelibrary.widget.CustomDialog;


public class BindBankCardActivity extends BaseActivity implements OnClickListener {
    private static final int FIND_BANK_NAME = 111;
    private EditText card_edittext, bank_edittext, account_bank_edittext, id_number_edittext;
    ;
    private TextView save, ower_edittext;
    private TextView back;
    private BankInfo bankInfo;
    private ImageView img_info;
    private String bankName = "";

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case FIND_BANK_NAME:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (msg.arg1 == 1) {
                        FindBankNameResponse res = (FindBankNameResponse) msg.obj;
                        if (res.isSuccess()) {
                            if (res.getData() != null && res.getData().getBankName() != null && !res.getData().getBankName().isEmpty()) {
                                bankName = res.getData().getBankName();
                            }
                            //							else {
                            //								showNonsupportDialog();
                            //							}
                            Intent intent = new Intent(BindBankCardActivity.this, SaveBankCardActivity.class);
                            intent.putExtra("bankName", bankName);
                            intent.putExtra("bankNum", card_edittext.getText().toString());
                            intent.putExtra("subBank", account_bank_edittext.getText().toString());
                            intent.putExtra("personNo", id_number_edittext.getText().toString());
                            intent.putExtra("userRealName", ower_edittext.getText().toString());
                            startActivity(intent);

                        } else {
                            //showNonsupportDialog();
                            UIHelper.ToastMessage(BindBankCardActivity.this, res.getResultMsg());
                        }
                    } else {
                        UIHelper.ToastMessage(BindBankCardActivity.this, (String) msg.obj);
                    }
                    break;
            }
        }
    };
    private String userId;
    private String userName;
    private String userType;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_bank_card);
        ExitSelHospital.getInstance().addActivity(this);
        initView();
        getData();
        String from = getIntent().getStringExtra("from");
        if (from != null) {
            initData();
        }
    }

    private void initData() {
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        userType = getIntent().getStringExtra("userType");
        access_token = getIntent().getStringExtra("access_token");
        UserInfo.getInstance(this).setToken(access_token);
        UserInfo.getInstance(this).setId(userId);
        UserInfo.getInstance(this).setUserName(userName);
        UserInfo.getInstance(this).setUserType(userType);
    }

    private void initView() {
        ower_edittext = (TextView) findViewById(R.id.ower_edittext);
        card_edittext = (EditText) findViewById(R.id.card_edittext);
        back = (TextView) findViewById(R.id.back);
        save = (TextView) findViewById(R.id.save);
        img_info = (ImageView) findViewById(R.id.img_info);
        account_bank_edittext = (EditText) findViewById(R.id.account_bank_edittext);
        id_number_edittext = (EditText) findViewById(R.id.id_number_edittext);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        img_info.setOnClickListener(this);

    }

    private void getData() {
        //ower_edittext.setText(DApplication.getInstance().mLoginUser.name);
        ower_edittext.setText(UserInfo.getInstance(context).getUserName());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save) {
            if (id_number_edittext.getText().toString() == null || "".equals(id_number_edittext.getText().toString())) {
                UIHelper.ToastMessage(this, "请输入身份证号");
                return;
            }
            if (!new IDCard().verify(id_number_edittext.getText().toString())){
                UIHelper.ToastMessage(this, "请输入正确的身份证号");
                return;
            }

//            if (!(id_number_edittext.getText().toString().length() == 15 || id_number_edittext.getText().toString().length() == 18)) {
//                UIHelper.ToastMessage(this, "请输入正确的身份证号");
//                return;
//            }
            if (account_bank_edittext.getText().toString() == null || "".equals(account_bank_edittext.getText().toString())) {
                UIHelper.ToastMessage(this, "请输入开户支行");
                return;
            }

            if (card_edittext.getText().toString() == null || "".equals(card_edittext.getText().toString())) {
                UIHelper.ToastMessage(this, "请输入银行卡卡号");
                return;
            }

            if (mDialog != null) {
                mDialog.show();
            }
            HttpCommClient.getInstance().findBankName(this, handler, FIND_BANK_NAME, UserInfo.getInstance(context).getToken(),
                    card_edittext.getText().toString());
        } else if (view.getId() == R.id.back) {
            finish();
        } else if (view.getId() == R.id.img_info) {
            showExplainDialog();
        }


    }

    public void showExplainDialog() {
        String name = "医生";
        if (UserInfo.getInstance(context).getUserType().equals("9")) {
            name = "店员";
        }
        CustomDialog customDialog = new CustomDialog.Builder(BindBankCardActivity.this, new CustomDialog.CustomClickEvent() {

            @Override
            public void onDismiss(CustomDialog customDialog) {
                customDialog.dismiss();
            }

            @Override
            public void onClick(CustomDialog customDialog) {
                customDialog.dismiss();
            }
        }).setTitle("持卡人说明").setMessage("为保证账户资金安全，只能绑定" + name + "用户本人的银行卡。如需修改，请联系客服处理。").setNegative("知道了").create();
        customDialog.show();
    }

    public void showNonsupportDialog() {
        CustomDialog customDialog = new CustomDialog.Builder(BindBankCardActivity.this, new CustomDialog.CustomClickEvent() {

            @Override
            public void onDismiss(CustomDialog customDialog) {

                customDialog.dismiss();
                card_edittext.setText("");
                //				card_edittext.setHint("");
            }

            @Override
            public void onClick(CustomDialog customDialog) {
                customDialog.dismiss();
            }
        }).setMessage("暂不支持添加该卡，请检查或添加其他卡片。").setNegative("知道了").create();
        customDialog.show();
    }

    public static void openUI(Context context, String userId, String userName, String userType, String access_token,
                              String from) {
        Intent intent = new Intent(context, BindBankCardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userType", userType);
        intent.putExtra("access_token", access_token);
        intent.putExtra("from", from);
        context.startActivity(intent);

    }

}
