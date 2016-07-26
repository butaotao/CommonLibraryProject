package com.dachen.incomelibrary.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.bean.AccountInfo;
import com.dachen.incomelibrary.http.HttpCommClient;
import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.incomelibrary.utils.UIHelper;
import com.dachen.incomelibrary.utils.UserInfo;
import com.dachen.incomelibrary.widget.CustomDialog;


public class EditBankCardActivity extends BaseActivity implements OnClickListener {
    public static final int SET_BANK_DEFAULT = 112;
    private TextView ower_edittext, ower_bank_edittext, card_edittext, id_card_num_textview_tx, bank_tx;
    private TextView delete;
    private TextView back;
    private AccountInfo accountInfo;
    private int bankId;
    private ImageView img_info;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsApp.HANDLER_DELETE_BANK_CARD:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (msg.arg1 == 1) {
                        UIHelper.ToastMessage(EditBankCardActivity.this, "解绑成功");
                        setResult(ConstantsApp.GOTO_EDIT_BANK_CARD);
                        finish();

                    } else {
                        UIHelper.ToastMessage(EditBankCardActivity.this, (String) msg.obj);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_bank_card);
        initView();
        getData();
    }

    private void initView() {
        ower_edittext = (TextView) findViewById(R.id.ower_edittext);
        ower_bank_edittext = (TextView) findViewById(R.id.ower_bank_edittext);
        card_edittext = (TextView) findViewById(R.id.card_edittext);
        back = (TextView) findViewById(R.id.back);
        delete = (TextView) findViewById(R.id.delete);
        id_card_num_textview_tx = (TextView) findViewById(R.id.id_card_num_textview_tx);
        bank_tx = (TextView) findViewById(R.id.bank_tx);
        img_info = (ImageView) findViewById(R.id.img_info);

        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        img_info.setOnClickListener(this);
    }

    private void getData() {
        accountInfo = (AccountInfo) getIntent().getSerializableExtra("ACCOUNT_INFO");
        if (accountInfo == null) {
            return;
        }
        ower_edittext.setText(UserInfo.getInstance(context).getUserName());
        ower_bank_edittext.setText(accountInfo.getBankName());
        String bankNum = accountInfo.getBankNo();
        if (bankNum != null && !bankNum.isEmpty()) {
            card_edittext.setText("************" + bankNum.substring(bankNum.length() - 4));
        }

        id_card_num_textview_tx.setText(accountInfo.getPersonNo());
        if (accountInfo.getSubBank() != null) {
            bank_tx.setText(accountInfo.getSubBank());
        }
        bankId = accountInfo.getId();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            finish();
        } else if (view.getId() == R.id.delete) {

            showDeleteDialog();


        } else if (view.getId() == R.id.img_info) {
            showExplainDialog();
        }

    }

    public void showExplainDialog() {
        String name = "医生";
        if (UserInfo.getInstance(context).getUserType().equals("9")) {
            name = "店员";
        }
        CustomDialog customDialog = new CustomDialog.Builder(EditBankCardActivity.this, new CustomDialog.CustomClickEvent() {

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

    public void showDeleteDialog() {
        CustomDialog customDialog = new CustomDialog.Builder(EditBankCardActivity.this, new CustomDialog.CustomClickEvent() {

            @Override
            public void onDismiss(CustomDialog customDialog) {
                customDialog.dismiss();
            }

            @Override
            public void onClick(CustomDialog customDialog) {

                if (accountInfo == null) {
                    customDialog.dismiss();
                    return;
                }
                if (mDialog != null) {
                    mDialog.show();
                }
                String token = UserInfo.getInstance(context).getToken();
                HttpCommClient.getInstance().deleteBankCard(context, handler, ConstantsApp.HANDLER_DELETE_BANK_CARD, UserInfo
                        .getInstance(context).getToken(), accountInfo.getId() + "");

                customDialog.dismiss();
            }
        }).setMessage("是否解除绑定此银行卡").setNegative("否").setPositive("是").create();
        customDialog.show();
    }
}
