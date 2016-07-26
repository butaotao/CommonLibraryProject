package com.dachen.incomelibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.incomelibrary.R;
import com.dachen.incomelibrary.adapter.AccountAdapter;
import com.dachen.incomelibrary.bean.AccountInfo;
import com.dachen.incomelibrary.http.HttpCommClient;
import com.dachen.incomelibrary.utils.ConstantsApp;
import com.dachen.incomelibrary.utils.UIHelper;
import com.dachen.incomelibrary.utils.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class SettlementActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    private ListView accountLV;
    private TextView back, add_account;
    private AccountAdapter accountAdapter;
    private List<AccountInfo> accountInfos = new ArrayList<AccountInfo>();
    private ImageView empty_imageView;
    private View empty_view;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case ConstantsApp.HANDLER_GET_BANK_CARDS:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (msg.arg1 == 1) {
                        if (msg.obj != null && msg.obj instanceof List) {
                            List<AccountInfo> accountInfoList = (List<AccountInfo>) msg.obj;
                            AccountInfo accountInfo = null;
                            if (accountInfoList != null) {
                                for (int i = 0; i < accountInfoList.size(); i++) {
                                    if (accountInfoList.get(i).isIsDefault()) {
                                        accountInfo = accountInfoList.get(i);
                                        accountInfoList.remove(i);
                                    }
                                }
                                accountInfos.clear();
                                if (accountInfo != null)
                                    accountInfos.add(accountInfo);
                                if (accountInfoList.size() > 0)
                                    accountInfos.addAll(accountInfoList);
                                accountAdapter.notifyDataSetChanged();
                                if (accountInfos.size() > 0) {
                                    add_account.setVisibility(View.GONE);
                                } else {
                                    add_account.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    } else {
                        UIHelper.ToastMessage(SettlementActivity.this, (String) msg.obj);
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
        setContentView(R.layout.settlement);
        initView();
        initListView();
        initData();

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

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void initView() {
        accountLV = (ListView) findViewById(R.id.accountLV);
        back = (TextView) findViewById(R.id.back);
        add_account = (TextView) findViewById(R.id.add_account);
        back.setOnClickListener(this);
        add_account.setOnClickListener(this);
    }

    private void initListView() {
        accountAdapter = new AccountAdapter(this, accountInfos);
        accountLV.setAdapter(accountAdapter);
        empty_view = LayoutInflater.from(this).inflate(R.layout.empty, null);
        empty_imageView = (ImageView) empty_view.findViewById(R.id.empty);
        TextView empty_text = (TextView) empty_view.findViewById(R.id.empty_text);
        empty_text.setText("您还没有添加过结算账号");
        empty_imageView.setBackgroundResource(R.drawable.card);
        ((ViewGroup) accountLV.getParent()).addView(empty_view,
                new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        accountLV.setEmptyView(empty_view);
        accountLV.setOnItemClickListener(this);
    }

    private void getData() {
        if (mDialog != null) {
            mDialog.show();
        }

        HttpCommClient.getInstance().queryBindBankAccount(this, handler, ConstantsApp.HANDLER_GET_BANK_CARDS, access_token);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
        } else if (v.getId() == R.id.add_account) {
            Intent intent = new Intent(SettlementActivity.this, BindBankCardActivity.class);
            startActivityForResult(intent, ConstantsApp.GOTO_BIND_BANK_CARD);
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == ConstantsApp.GOTO_BIND_BANK_CARD || arg1 == ConstantsApp.GOTO_EDIT_BANK_CARD) {
            if (mDialog != null) {
                mDialog.show();
            }
            HttpCommClient.getInstance().queryBindBankAccount(this, handler, ConstantsApp.HANDLER_GET_BANK_CARDS, access_token);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        if (view.getTag() == null || !(view.getTag() instanceof AccountAdapter.Holder)) {
            return;
        }
        AccountInfo accountInfo = ((AccountAdapter.Holder) view.getTag()).accountInfo;
        if (accountInfo == null) {
            return;
        }
        Intent intent = new Intent(SettlementActivity.this, EditBankCardActivity.class);
        intent.putExtra("ACCOUNT_INFO", accountInfo);
        startActivityForResult(intent, ConstantsApp.GOTO_EDIT_BANK_CARD);
    }

    public static void openUI(Context context, String userId, String userName, String userType, String access_token) {
        Intent intent = new Intent(context, SettlementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userType", userType);
        intent.putExtra("access_token", access_token);
        context.startActivity(intent);

    }

}
