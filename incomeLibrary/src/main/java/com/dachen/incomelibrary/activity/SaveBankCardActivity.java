package com.dachen.incomelibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.bean.BankInfo;
import com.dachen.incomelibrary.http.HttpCommClient;
import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.incomelibrary.utils.ExitSelHospital;
import com.dachen.incomelibrary.utils.UIHelper;
import com.dachen.incomelibrary.utils.UserInfo;


public class SaveBankCardActivity extends BaseActivity implements OnClickListener {
    private static final int FIND_BANK_NAME = 111;
    private TextView save, ower_bank_edittext;
    private RelativeLayout owner_bank_layout;
    private TextView back;
    private String bankName;
    private String bankId;
    private String bankNum, subBank, personNo, userRealName;
    private BankInfo bankInfo;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantsApp.HANDLER_ADD_BANK_CARD:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (msg.arg1 == 1) {
                        UIHelper.ToastMessage(SaveBankCardActivity.this, "添加成功");
                        ExitSelHospital.getInstance().exit();
                    } else {
                        UIHelper.ToastMessage(SaveBankCardActivity.this, (String) msg.obj);
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_bank_card);
        ExitSelHospital.getInstance().addActivity(this);
        initData();
        initView();
    }

    private void initData() {
        bankName = getIntent().getStringExtra("bankName");
        bankId = getIntent().getStringExtra("bankId");
        bankNum = getIntent().getStringExtra("bankNum");
        subBank = getIntent().getStringExtra("subBank");
        personNo = getIntent().getStringExtra("personNo");
        userRealName = getIntent().getStringExtra("userRealName");
    }

    private void initView() {
        ower_bank_edittext = (TextView) findViewById(R.id.ower_bank_edittext);
        owner_bank_layout = (RelativeLayout) findViewById(R.id.owner_bank_layout);
        back = (TextView) findViewById(R.id.back);
        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        owner_bank_layout.setOnClickListener(this);

        if (bankName != null && !bankName.isEmpty()) {
            ower_bank_edittext.setText(bankName);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save) {
            if (ower_bank_edittext.getText().toString() == null || "".equals(ower_bank_edittext.getText().toString())) {
                UIHelper.ToastMessage(this, "请选择所属银行");
                return;
            }
            if (mDialog != null) {
                mDialog.show();

            }
            HttpCommClient.getInstance().addBankCard(this, handler, ConstantsApp.HANDLER_ADD_BANK_CARD, UserInfo.getInstance
                    (context).getToken(), bankNum, ower_bank_edittext.getText().toString(), subBank, personNo, userRealName);

        } else if (view.getId() == R.id.owner_bank_layout) {
            Intent intent = new Intent(SaveBankCardActivity.this, ChooseBankActivity.class);
            startActivityForResult(intent, ConstantsApp.GOTO_CHOOSE_BANK_RESULT);
        } else if (view.getId() == R.id.back) {
            finish();
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, intent);
        if (arg1 == ConstantsApp.GOTO_CHOOSE_BANK_RESULT) {
            bankInfo = (BankInfo) intent.getSerializableExtra(ConstantsApp.BANK_INFO);
            if (bankInfo != null) {
                ower_bank_edittext.setText(bankInfo.getBankName());
                bankName = bankInfo.getBankName();
            }
        }
    }
}
