package com.dachen.common.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private final static Pattern email_pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static String[] DOC_ARRAY = new String[]{"doc", "docx", "txt", "xps", "pdf", "rtf", "xml", "xlsx", "ppt", "pptx", "xls"};
    private final static String[] IMAGE_ARRAY = new String[]{"jpg", "jpeg", "png", "gif", "bmp"};
    private final static String[] VIDEO_ARRAY = new String[]{"avi", "rmvb", "rm", "asf", "divx", "mpg", "mpeg", "mpe", "wmv", "mp4", "mkv", "vob", "mov"};
    private final static String[] AUDIO_ARRAY = new String[]{"mp3", "wma", "aac", "mid", "wav", "vqf", "cda"};


    /**
     * 将文本内容copy到剪切板
     *
     * @param context
     * @param text    要copy的内容
     */
    public static void copyToClipboard(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clipData);
    }

    public static void copyToClipboardURL(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        Uri copyUri = Uri.parse(text + "");
        ClipData clipData = ClipData.newUri(context.getContentResolver(), "URI", copyUri);
        clipboard.setPrimaryClip(clipData);
    }

    public static String getMimeType(String extension) {
        if (extension == null) {
            return "";
        }

        Set<String> sDocSet = new HashSet(Arrays.asList(DOC_ARRAY));
        Set<String> sImageSet = new HashSet(Arrays.asList(IMAGE_ARRAY));
        Set<String> sVideoSet = new HashSet(Arrays.asList(VIDEO_ARRAY));
        Set<String> sAudioSet = new HashSet(Arrays.asList(AUDIO_ARRAY));

        String mimeType = "";
        if (sDocSet.contains(extension)) {
            mimeType = "text/" + extension;
        } else if (sImageSet.contains(extension)) {
            mimeType = "image/" + extension;
        } else if (sVideoSet.contains(extension)) {
            mimeType = "video/" + extension;
        } else if (sAudioSet.contains(extension)) {
            mimeType = "audio/" + extension;
        }

        if (TextUtils.isEmpty(mimeType)) {
            //getMimeTypeFromExtension好像不识别大写后缀，getMimeTypeFromExtension返回的结果可能有误，比如rm文件返回为音频类型
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return mimeType;
    }

    public static String handleNull(String str) {
        return (str == null ? "" : str).replace("null", "");
    }

    public static String convertPrice(String fen) {
        if (TextUtils.isEmpty(fen) || !TextUtils.isDigitsOnly(fen)) {
            return "";
        }
        return String.format("%.2f", (float) Integer.parseInt(fen) / 100);
    }

    public static String convertPrice(int fen) {
        return convertPrice(fen + "");
    }

    /**
     * 将list拼接成str1,str2,str3形式的字符串
     *
     * @param list
     * @return
     */
    public static String spliceString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int len = list.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(list.get(i));
            if (i < len - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    /**
     * EditText显示Error
     *
     * @param context
     * @param resId
     * @return
     */
    public static CharSequence editTextHtmlErrorTip(Context context, int resId) {
        CharSequence html = Html.fromHtml("<font color='red'>" + context.getString(resId) + "</font>");
        return html;
    }

    public static CharSequence editTextHtmlErrorTip(Context context, String text) {
        CharSequence html = Html.fromHtml("<font color='red'>" + text + "</font>");
        return html;
    }

    static Pattern phoneNumberPat = Pattern.compile("^((13[0-9])|(147)|(15[0-3,5-9])|(17[0,6-8])|(18[0-9]))\\d{8}$");
    static Pattern nickNamePat = Pattern.compile("^[\u4e00-\u9fa5_a-zA-Z0-9_]{3,15}$");// 3-10个字符
    static Pattern searchNickNamePat = Pattern.compile("^[\u4e00-\u9fa5_a-zA-Z0-9_]*$");// 不限制长度，可以为空字符串
    static Pattern companyNamePat = Pattern.compile("^[\u4e00-\u9fa5_a-zA-Z0-9_]{3,50}$");// 3-50个字符
    static Pattern userNamePat = Pattern.compile("^[\u4e00-\u9fa5_a-zA-Z0-9_.-]{2,20}$");// 2-20个长度
    static Pattern passWordPat = Pattern.compile("^[^\\s\u4e00-\u9fa5]{6,18}$");//6-18位,不能为空格和中文
    static Pattern phonePat = Pattern.compile("^[0-9]{11}$");//11位数字
    static Pattern identifyingCodePat = Pattern.compile("^[0-9]{4}$");//4位数字
    //身份证号码
    static Pattern idCardNumberPat = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9])|[123][0-1])\\d{4}$");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /* 是否是手机号 */
    public static boolean isMobileNumber(String mobiles) {
        Matcher mat = phoneNumberPat.matcher(mobiles);
        return mat.matches();
    }

    /* 检测之前，最好检测是否为空。检测是否是正确的昵称格式 */
    public static boolean isNickName(String nickName) {
        if (TextUtils.isEmpty(nickName)) {
            return false;
        }
        Matcher mat = nickNamePat.matcher(nickName);
        return mat.matches();
    }

    /* 检测是否是正确的用户名格式 */
    public static boolean isUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return false;
        }
        Matcher mat = userNamePat.matcher(userName);
        return mat.matches();
    }

    /* 检测是否是正确的密码 */
    public static boolean isPassWord(String passWord) {
        if (TextUtils.isEmpty(passWord)) {
            return false;
        }
        Matcher mat = passWordPat.matcher(passWord);
        return mat.matches();
    }

    /* 检测是否是位11位电话*/
    public static boolean isPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        Matcher mat = phonePat.matcher(phone);
        return mat.matches();
    }

    /* 检测是否是4位数字验证码*/
    public static boolean isFourIdentifyingCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return false;
        }
        Matcher mat = identifyingCodePat.matcher(code);
        return mat.matches();
    }

    /* 检测是否是身份证号码*/
    public static boolean isIdCardNumber(String code) {
        if (TextUtils.isEmpty(code)) {
            return false;
        }
        Matcher mat = idCardNumberPat.matcher(code);
        return mat.matches();
    }

    public static boolean isSearchNickName(String nickName) {
        if (nickName == null) {// 防止异常
            return false;
        }
        Matcher mat = searchNickNamePat.matcher(nickName);
        return mat.matches();
    }

    public static boolean isCompanyName(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        Matcher mat = companyNamePat.matcher(name);
        return mat.matches();
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return email_pattern.matcher(email).matches();
    }

    public static boolean strEquals(String s1, String s2) {
        if (s1 == s2) {// 引用相等直接返回true
            return true;
        }
        boolean emptyS1 = s1 == null || s1.trim().length() == 0;
        boolean emptyS2 = s2 == null || s2.trim().length() == 0;
        if (emptyS1 && emptyS2) {// 都为空，认为相等
            return true;
        }
        if (s1 != null) {
            return s1.equals(s2);
        }
        if (s2 != null) {
            return s2.equals(s1);
        }
        return false;
    }

    /**
     * 去掉特殊字符
     */
    public static String replaceSpecialChar(String str) {
        if (str != null && str.length() > 0) {
            return str.replaceAll("&#39;", "’").replaceAll("&#039;", "’").replaceAll("&nbsp;", " ")
                    .replaceAll("\r\n", "\n").replaceAll("\n", "\r\n");
        }
        return "";
    }

    /**
     * 将字符串转为日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将Long时间转成 String
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(new Date(time));

    }

    /**
     * 获取头像url
     */
    public static String getAvatarUrl(String userId, String headPicFileName) {
//		String avatarUrl = null;
//		int t = Integer.parseInt(userId) % 1000;
//		if (!TextUtils.isEmpty(headPicFileName)) {
//			if (headPicFileName.contains("avatar/")) {
//				avatarUrl = headPicFileName;
//			} else {
//				avatarUrl = Constants.DOWNLOAD_AVATAR_BASE_URL + "avatar/o/" + t + "/" + headPicFileName;
//			}
//		}
        return headPicFileName;
    }

    public static String getSexStr(int sex) {
        String sexString = "";
        if (sex == 1) {
            sexString = "男";
        } else if (sex == 2) {
            sexString = "女";
        } else {
            sexString = "保密";
        }
        return sexString;
    }

    public static final String getFileType(String fileName) {
        if (fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".JPEG") || fileName.endsWith(".JPG")) {
            return "image/jpg";
        } else if (fileName.endsWith(".bmp") || fileName.endsWith(".BMP")) {
            return "image/bmp";
        } else {
            return "application/octet-stream";
        }
    }

    public static String convert(long mill) {
        Date date = new Date(mill);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    public static String convert1(long mill) {
        Date date = new Date(mill);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     * s缩略图url转原图url
     *
     * @param thumbnailUrl
     * @return
     */
    public static String thumbnailUrl2originalUrl(String thumbnailUrl) {
        if (thumbnailUrl == null) {
            return null;
        }
        String originalUrl = new String(thumbnailUrl);
        return originalUrl.replace("/t/", "/o/");
    }

    /**
     * 人民币元转为分
     *
     * @param yuan
     */
    public static String changeMoneyYuanToFen(String yuan) {
        int y = Integer.valueOf(yuan);
        yuan = String.valueOf(y * 100);
        return yuan;
    }

    /**
     * 人民币分转为元
     *
     * @param fen
     */
    public static String changeMoneyFenToYuan(String fen) {
        int y = Integer.valueOf(fen);
        fen = String.valueOf(y / 100);
        return fen;
    }

    public static String getAgeStr(long birthday, long createTime) {
        if (createTime == 0)
            return null;

        String ageStr = "";
        Calendar cal = Calendar.getInstance();
        int cMonth = cal.get(Calendar.MONTH) + 1;
        int cYear = cal.get(Calendar.YEAR);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        if (birthday != 0) {
            int bYear = Integer.parseInt(TimeUtils.s_long_2_str(birthday).substring(0, 4));
            int bMonth = Integer.parseInt(TimeUtils.s_long_2_str(birthday).substring(5, 7));
            int bDay = Integer.parseInt(TimeUtils.s_long_2_str(birthday).substring(8, 10));
            int tAge = cYear - bYear;
            if (tAge > 1) {
                if (cMonth <= bMonth) {
                    if (cMonth == bMonth) {
                        if (cDay < bDay)
                            tAge--;
                    } else {
                        tAge--;
                    }
                }
                ageStr = tAge + "岁";
            } else if (tAge == 1) {
                if (cMonth < bMonth) {
                    if (cDay < bDay)
                        ageStr = (12 - bMonth + cMonth - 1) + "个月";
                    else
                        ageStr = (12 - bMonth + cMonth) + "个月";

                } else {
                    if (cMonth == bMonth) {
                        if (cDay < bDay)
                            ageStr = "11个月";
                        else
                            ageStr = tAge + "岁";
                    } else {
                        ageStr = tAge + "岁";
                    }
                }
            } else if (tAge == 0) {
                if (cMonth > bMonth) {
                    if (cDay < bDay) {
                        if (cMonth - bMonth - 1 > 0)
                            ageStr = (cMonth - bMonth - 1) + "个月";
                        else
                            ageStr = "1个月";
                    } else {
                        ageStr = (cMonth - bMonth) + "个月";
                    }
                } else {
                    ageStr = "1个月";
                }
            }
        }
        return ageStr;
    }


    public static String getName(String trade_name, String general_name) {
        String name = general_name;
        if (null == name) {
            name = trade_name;
        }
        if (null != name && name.length() > 9) {
            name = name.substring(0, 6) + "..."
                    + name.substring(name.length() - 2);
        }
        return name;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int getStrlength(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static int getIntVal(String s) {
        int n = 0;
        try {
            n = Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;
    }
}