package com.dachen.incomelibrary.utils;

public class ConstantsApp {

    /**
     * 结算方式界面
     */
    public static final int HANDLER_GET_BANK_CARDS = 0;

    /**
     * 选择银行界面
     */
    public static final int HANDLER_GET_BANK_INFO = 0;
    /**
     * 添加银行卡界面
     */
    public static final int HANDLER_ADD_BANK_CARD = 0;

    /**
     * 编辑银行卡界面
     */
    public static final int HANDLER_DELETE_BANK_CARD = 0;

    public static final int HANDLER_UPDATE_BANK_CARD = 1;

    /**
     * 收入报表界面
     */
    public static final int HANDLER_GET_INCOME = 0;
    public static final int HANDLER_GET_INCOME_MONTH = 1;

    /**
     * 我的收入界面
     */
    public static final int HANDLER_GET_INCOME_INFO = 0;

    /**
     * 历史收入明细界面
     */
    public static final int HANDLER_GET_INCOME_HISTORY_LIST = 0;

    /**
     * 月收入明细界面
     */
    public static final int HANDLER_GET_INCOME_MONTH_LIST = 0;

    /**
     * 打款记录页面
     */
    public static final int HANDLER_GET_INCOME_PAID_LIST = 0;

    /**
     * 单项打款记录详情
     */
    public static final int HANDLER_GET_INCOME_PAID_ITEM_LIST = 0;


    /**
     * 收入详细报表界面
     */
    public static final int HANDLER_GET_INCOME_MONTH_DETAIL = 0;

    /**
     * 常量
     */

    public static final int GOTO_CHOOSE_BANK_RESULT = 1;
    public static final String BANK_INFO = "BANK_INFO";
    public static final int GOTO_BIND_BANK_CARD = 1;
    public static final String INCOME_MONTH_DETAIL = "IncomeDetail";
    public static final int GOTO_EDIT_BANK_CARD = 2;

    /**
     * 订单界面
     */
    public static final int HANDLER_QUERY_NEW_ORDER_LIST = 0;
    public static final int HANDLER_QUERY_NEW_ORDER_LIST_UP = 1;
    public static final int HANDLER_QUERY_WAIT_PAY_ORDER_LIST = 2;
    public static final int HANDLER_QUERY_WAIT_PAY_ORDER_LIST_UP = 3;
    public static final int HANDLER_QUERY_PAYED_ORDER_LIST = 4;
    public static final int HANDLER_QUERY_PAYED_ORDER_LIST_UP = 5;
    public static final int HANDLER_QUERY_FINISH_ORDER_LIST = 6;
    public static final int HANDLER_QUERY_FINISH_ORDER_LIST_UP = 7;
    public static final int HANDLER_QUERY_CANCEL_ORDER_LIST = 8;
    public static final int HANDLER_QUERY_CANCEL_ORDER_LIST_UP = 9;

    /**
     * 订单详情界面
     */
    public static final int HANDLER_GET_ORDER_DETAIL = 0;

    public static final int HANDLER_RESERVE_TIME = 1;


    /**
     * 医生和患者聊天界面
     */

    public static final int HANDLER_BEGIN_SERVER = 0;

    public static final int HANDLER_OVER_SERVER = 1;


    /**
     * 填写诊疗记录界面
     */

    public static final int HANDLER_QUERY_TREATMENT_RECORD = 0;
    public static final int HANDLER_SAVE_TREATMENT = 1;
    public static final int HANDLER_SAVE_TREATMENT_END_SERVER = 2;
    public static final int HANDLER_UPDATE_TREATMENT_RECORD = 3;


    //小米推送alias规则

    public static final String pushAlias = "DaChen@_";

    public static final int GS_BANK = 1001;  //工商银行
    public static final int GD_BANK = 1002;//光大银行
    public static final int XY_BANK = 1003;//兴业银行
    public static final int JS_BANK = 1004;//建设银行
    public static final int JT_BANK = 1005;//交通银行
    public static final int MS_BANK = 1006;//民生银行
    public static final int NY_BANK = 1007;//农业银行
    public static final int PA_BANK = 1008;//平安银行
    public static final int PF_BANK = 1009;//浦发银行
    public static final int YZ_BANK = 1010;//邮政银行
    public static final int ZS_BANK = 1011;//招商银行
    public static final int ZX_BANK = 1012;//中信银行
    public static final int ZG_BANK = 1013;//中国银行
    public static final int GF_BANK = 1014;//广发银行

    /**
     * 调用网页界面
     */
    public static final String GOTO_WEBACTIVITY = "WebActivity";
    public static final String GOTO_WEBFROM = "WebActivityFrom";
    public static final int FROM_GROUP_NOTICE = 1;//集团通知
    public static final int FROM_COMPANY_NOTICE = 2;//公司通知
    public static final int FROM_PIC_SERVICE = 3;//图文套餐
    public static final int FROM_TEL_SERVICE = 4;//电话套餐
    public static final int FROM_SERVICE_ARTICEL = 5; //服務和隐私条款
    public static final int FROM_INCOME_RULE = 6; //收入规则
    public static final int FROM_EXPERT_CONSULT = 7; //会诊咨询
    public static final int FROM_INCOME_BALANCE_INTRO = 8;//余额说明

}
