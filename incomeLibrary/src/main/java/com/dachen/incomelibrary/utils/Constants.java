package com.dachen.incomelibrary.utils;


/**
 * 服务器地址说明：
 * <p/>
 * 在浏览器中输入：http://192.168.3.7:8091/config 即可查看相关地址信息
 */
public class Constants {
    /**
     * 配置： 阿里云：120.24.94.126
     * <p/>
     * 内网：192.168.3.7
     */
    public static String IP = "xg.mediportal.com.cn";//默认康泽环境

    /**
     * apiUrl
     */
    public static String API_BASE_URL = "http://" + IP + ":80/health/";

    /**
     * web页面
     */
    public static String API_BASE_WEB_URL = "http://" + IP + "/health/web/";

    /**
     * 获取绑定银行卡列表
     */
    public static String GET_BANK_CARDS = API_BASE_URL + "pack/bank/getBankCards";

    /**
     * 获取银行信息
     */
    public static String GET_BANK_INFO = API_BASE_URL + "pack/bank/getBanks";

    /**
     * 添加银行卡
     */
    public static String ADD_BANK_CARD = API_BASE_URL + "pack/bank/addBankCard";

    /**
     * 删除银行卡
     */
    public static String DELET_EBANK_CARD = API_BASE_URL + "pack/bank/deleteBankCard";
    /**
     * 更新银行卡
     */
    public static String UPDATE_BANK_CARD = API_BASE_URL + "pack/bank/updateBankCard";

    /**
     * 设置指定银行卡为默认银行卡
     */
    public static String SET_BANK_DEAULT = API_BASE_URL + "pack/bank/setBankStatus";

    /**
     * 获取获取银行卡名称
     */
    public static String FIND_BANK_NAME = API_BASE_URL + "pack/bank/findBankName";


    /**
     * 获取总收入
     */
    public static String GET_INCOME = API_BASE_URL + "pack/income/getIncome";

    /**
     * 获取月收入
     */
    public static String GET_INCOME_MONTH = API_BASE_URL + "pack/income/getIncomeMonth";

    /**
     * 获取月收入详细
     */
    public static String GET_INCOME_MONTH_DETAIL = API_BASE_URL + "pack/income/getIncomeDetail";

    /**
     * 获取月收入基本信息
     */
    public static String GET_INCOME_INFO = API_BASE_URL + "income/info";

    /**
     * 获取历史收入明细
     */
    public static String GET_INCOME_HISTORY_LIST = API_BASE_URL + "income/hList";

    /**
     * 获取月收入明细
     */
    public static String GET_INCOME_MONTH_LIST = API_BASE_URL + "income/hDetails";

    /**
     * 获取打款记录
     */
    public static String GET_INCOME_PAID_LIST = API_BASE_URL + "income/hsettleList";

    /**
     * 获取单项打款记录详情
     */
    public static String GET_INCOME_PAID_ITEM_LIST = API_BASE_URL + "income/settleDetails";

    /**
     * 查询账户余额明细或者未完成订单余额明细
     */
    public static String GET_INCOME_DETAIL = API_BASE_URL + "income/details";


    /**
     * 收入规则介绍
     */
    public static String INCOME_RULE = API_BASE_WEB_URL + "attachments/finance/salesclerk/account_regulation.html";
    public static String INCOME_BLANCE_RULE = API_BASE_WEB_URL + "attachments/finance/salesclerk/balance_state.html";


    /**
     * 改变Ip,内网外网切换
     */
    public static void changeIp(String ip) {
        IP = ip;

        if (IP.equals("192.168.3.7") || IP.equals("120.24.94.126") || IP.equals("192.168.3.11")) {
            if (IP.equals("192.168.3.7")) {
                QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_3_7);
            } else if (IP.equals("120.24.94.126")) {
                QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_ALI_YUN);
            }
            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("xg.mediportal.com.cn")) {

            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("192.168.3.63")) {
            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("4")) {

            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("120.25.84.65")) {
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";
        }

        /**
         * 获取绑定银行卡列表
         */
        GET_BANK_CARDS = API_BASE_URL + "pack/bank/getBankCards";

        /**
         * 获取银行信息
         */
        GET_BANK_INFO = API_BASE_URL + "pack/bank/getBanks";

        /**
         * 添加银行卡
         */
        ADD_BANK_CARD = API_BASE_URL + "pack/bank/addBankCard";

        /**
         * 删除银行卡
         */
        DELET_EBANK_CARD = API_BASE_URL + "pack/bank/deleteBankCard";
        /**
         * 更新银行卡
         */
        UPDATE_BANK_CARD = API_BASE_URL + "pack/bank/updateBankCard";

        /**
         * 设置指定银行卡为默认银行卡
         */
        SET_BANK_DEAULT = API_BASE_URL + "pack/bank/setBankStatus";

        /**
         * 获取获取银行卡名称
         */
        FIND_BANK_NAME = API_BASE_URL + "pack/bank/findBankName";


        /**
         * 获取总收入
         */
        GET_INCOME = API_BASE_URL + "pack/income/getIncome";

        /**
         * 获取月收入
         */
        GET_INCOME_MONTH = API_BASE_URL + "pack/income/getIncomeMonth";

        /**
         * 获取月收入详细
         */
        GET_INCOME_MONTH_DETAIL = API_BASE_URL + "pack/income/getIncomeDetail";

        /**
         * 获取月收入基本信息
         */
        GET_INCOME_INFO = API_BASE_URL + "income/info";

        /**
         * 获取历史收入明细
         */
        GET_INCOME_HISTORY_LIST = API_BASE_URL + "income/hList";

        /**
         * 获取月收入明细
         */

        GET_INCOME_MONTH_LIST = API_BASE_URL + "income/hDetails";

        /**
         * 获取打款记录
         */
        GET_INCOME_PAID_LIST = API_BASE_URL + "income/hsettleList";

        /**
         * 获取单项打款记录详情
         */
        GET_INCOME_PAID_ITEM_LIST = API_BASE_URL + "income/settleDetails";

        /**
         * 查询账户余额明细或者未完成订单余额明细
         */
        GET_INCOME_DETAIL = API_BASE_URL + "income/details";

        INCOME_RULE = API_BASE_WEB_URL + "attachments/finance/salesclerk/account_regulation.html";
        INCOME_BLANCE_RULE = API_BASE_WEB_URL + "attachments/finance/salesclerk/balance_state.html";

    }

}
