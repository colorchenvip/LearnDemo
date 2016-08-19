package com.colorchen.utils;

/**
 * Created by color on 16/8/18 17:23.
 */

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具
 * Created by clark on 14/12/5.
 */
public class StringUtils {
    private static final String TAG = "StringUtils";

    public static final int MAX_NICK_LENGTH = 30;
    public static final int MAX_NICKLOGIN_LENGTH = 32;
    public static final int MAX_SIGNATURE_LENGTH = 60;
    public static final int MAX_WEIBO_LENGTH = 280;
    public static final int MAX_INTRO_LENGTH = 255;
    public static final int MAX_FEEDBACK_LEN = 140;
    public static final int MAX_MESSAGE_LEN = 600;
    public static final int MAX_CHECKFRIEND_LENGTH = 40;

    public static final String ENCODE_UTF8 = "UTF-8";
    public static final String ALGO_RSA = "RSA";
    // algorithm/mode/padding
    public static final String CIPHER_RSA = "RSA/ECB/PKCS1PADDING";
    private static final String PREFIX_OF_MULTICHAT = "multichats:";
    public static int PIECE_SIZE = 1048576;
    public static int LIMIT_FILE_SIZE = PIECE_SIZE * 10;

    public static ArrayList<String> splitString(String content) {
        ArrayList<String> revalue = new ArrayList<String>();
        String[] strings = content.split(",");
        for (String str : strings) {
            if (!TextUtils.isEmpty(str)) {
                revalue.add(str);
            }
        }
        return revalue;
    }

    public static String newMsgCount(int count) {
        if (count < 0) {
            return "0";
        } else if (count > 99) {
            return "99+";
        } else {
            return "" + count;
        }
    }



    public static boolean containsString(String[] array, String element) {
        if (array == null)
            return false;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(element))
                return true;
        }
        return false;
    }

    public static void addStringArrayList(ArrayList<String> arrayList, String[] array) {
        if (arrayList == null) {
            arrayList = new ArrayList<String>();
        }
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                arrayList.add(array[i]);
            }
        }

    }

    public static void removeStringArrayList(ArrayList<String> arrayList, String[] array) {
        if (arrayList == null)
            arrayList = new ArrayList<String>();
        if (array != null) {
            for (int i = 0; i < array.length; i++)
                arrayList.remove(array[i]);
        }
    }

    public static int getFunctionPosition(String fun) {
        int result = -1;
        for (int i = 0; i < FUNCTION_TEXT.length; i++) {
            if (FUNCTION_TEXT[i].contains(fun)) {
                result = i;
                break;
            }
        }
        return result;
    }
    public static String[] FUNCTION_TEXT = {"拍照", "拍摄视频", "照片/视频", "表情",
            // "涂鸦"
    };

    public static String[] EMOTION_TEXT = {"[兔子]", "[熊猫]", "[给力]", "[神马]", "[浮云]", "[威武]", "[围观]", "[呵呵]", "[嘻嘻]", "[哈哈]", "[爱你]", "[晕]", "[泪]", "[馋嘴]", "[抓狂]", "[哼]", "[可爱]",
            "[怒]", "[汗]", "[害羞]", "[睡觉]", "[钱]", "[偷笑]", "[酷]", "[衰]", "[吃惊]", "[闭嘴]", "[鄙视]", "[挖鼻屎]", "[花心]", "[鼓掌]", "[悲伤]", "[思考]", "[生病]", "[亲亲]", "[怒骂]", "[太开心]", "[懒得理你]",
            "[右哼哼]", "[左哼哼]", "[嘘]", "[委屈]", "[吐]", "[可怜]", "[打哈气]", "[做鬼脸]", "[失望]", "[顶]", "[疑问]", "[书呆子]", "[困]", "[感冒]", "[拜拜]", "[黑线]", "[阴险]", "[愤怒]", "[男孩儿]", "[女孩儿]",
            "[奥特曼]", "[猪头]", "[握手]", "[耶]", "[good]", "[弱]", "[不要]", "[ok]", "[赞]", "[来]", "[haha]", "[拳头]", "[最差]", "[月亮]", "[太阳]", "[微风]", "[沙尘暴]", "[下雨]", "[雪]", "[雪人]",
            "[落叶]", "[鲜花]", "[心]", "[伤心]", "[爱心传递]", "[互粉]", "[萌]", "[囧]", "[织]", "[帅]", "[喜]", "[围脖]", "[温暖帽子]", "[手套]", "[绿丝带]", "[红丝带]", "[蛋糕]", "[咖啡]", "[西瓜]", "[冰棍]", "[干杯]",
            "[蜡烛]", "[发红包]", "[汽车]", "[飞机]", "[自行车]", "[礼物]", "[照相机]", "[手机]", "[风扇]", "[话筒]", "[钟]", "[足球]", "[电影]", "[音乐]", "[实习]"};

    public static String[] EMOTION_TEXT_PINYIN = {"[d_tuzi]", "[d_xiongmao]", "[f_geili]", "[f_shenma]", "[w_fuyun]", "[f_v5]", "[o_weiguan]", "[d_hehe]", "[d_xixi]", "[d_haha]",
            "[d_aini]", "[d_yun]", "[d_lei]", "[d_chanzui]", "[d_zhuakuang]", "[d_heng]", "[d_keai]", "[d_nu]", "[d_han]", "[d_haixiu]", "[d_shuijiao]", "[d_qian]", "[d_touxiao]",
            "[d_ku]", "[d_shuai]", "[d_chijing]", "[d_bizui]", "[d_bishi]", "[d_wabishi]", "[d_huaxin]", "[d_guzhang]", "[d_beishang]", "[d_sikao]", "[d_shengbing]", "[d_qinqin]",
            "[d_numa]", "[d_taikaixin]", "[d_landelini]", "[d_youhengheng]", "[d_zuohengheng]", "[d_xu]", "[d_weiqu]", "[d_tu]", "[d_kelian]", "[d_dahaqi]", "[d_zuoguilian]",
            "[d_shiwang]", "[d_ding]", "[d_yiwen]", "[d_shudaizi]", "[d_kun]", "[d_ganmao]", "[d_baibai]", "[d_heixian]", "[d_yinxian]", "[d_fennu]", "[d_nanhaier]",
            "[d_nvhaier]", "[d_aoteman]", "[d_zhutou]", "[h_woshou]", "[h_ye]", "[h_good]", "[h_ruo]", "[h_buyao]", "[h_ok]", "[h_zan]", "[h_lai]", "[h_haha]", "[h_quantou]",
            "[h_zuicha]", "[w_yueliang]", "[w_taiyang]", "[w_weifeng]", "[w_shachenbao]", "[w_xiayu]", "[w_xue]", "[w_xueren]", "[w_luoye]", "[w_xianhua]", "[l_xin]",
            "[l_shangxin]", "[l_aixinchuandi]", "[f_hufen]", "[f_meng]", "[f_jiong]", "[f_zhi]", "[f_shuai]", "[f_xi]", "[o_weibo]", "[o_wennuanmaozi]", "[o_shoutao]",
            "[o_lvsidai]", "[o_hongsidai]", "[o_dangao]", "[o_kafei]", "[o_xigua]", "[o_bingun]", "[o_ganbei]", "[o_lazhu]", "[o_fahongbao]", "[o_qiche]", "[o_feiji]",
            "[o_zixingche]", "[o_liwu]", "[o_zhaoxiangji]", "[o_shouji]", "[o_fengshan]", "[o_huatong]", "[o_zhong]", "[o_zuqiu]", "[o_dianying]", "[o_yinyue]", "[o_shixi]"};



    public static String[] FACE_LXH_TEXT = {"[笑哈哈]", "[好爱哦]", "[噢耶]", "[偷乐]", "[泪流满面]", "[巨汗]", "[抠鼻屎]", "[求关注]", "[好喜欢]", "[崩溃]", "[好囧]", "[震惊]", "[别烦我]", "[不好意思]", "[羞嗒嗒]",
            "[得意地笑]", "[纠结]", "[给劲]", "[悲催]", "[甩甩手]", "[好棒]", "[瞧瞧]", "[不想上班]", "[困死了]", "[许愿]", "[丘比特]", "[有鸭梨]", "[想一想]", "[躁狂症]", "[转发]", "[互相膜拜]", "[雷锋]", "[杰克逊]", "[玫瑰]",
            "[hold住]", "[群体围观]", "[推荐]", "[赞啊]", "[被电]", "[霹雳]"};

    public static String[] FACE_LXH_PINYIN = {"[lxh_xiaohaha]", "[lxh_haoaio]", "[lxh_oye]", "[lxh_toule]", "[lxh_leiliumanmian]", "[lxh_juhan]", "[lxh_koubishi]",
            "[lxh_qiuguanzhu]", "[lxh_haoxihuan]", "[lxh_bengkui]", "[lxh_haojiong]", "[lxh_zhenjing]", "[lxh_biefanwo]", "[lxh_buhaoyisi]", "[lxh_xiudada]", "[lxh_deyidexiao]",
            "[lxh_jiujie]", "[lxh_feijin]", "[lxh_beicui]", "[lxh_shuaishuaishou]", "[lxh_haobang]", "[lxh_qiaoqiao]", "[lxh_buxiangshangban]", "[lxh_kunsile]", "[lxh_xuyuan]",
            "[lxh_qiubite]", "[lxh_youyali]", "[lxh_xiangyixiang]", "[lxh_kuangzaozheng]", "[lxh_zhuanfa]", "[lxh_xianghumobai]", "[lxh_leifeng]", "[lxh_jiekexun]",
            "[lxh_meigui]", "[lxh_holdzhu]", "[lxh_quntiweiguan]", "[lxh_tuijian]", "[lxh_zana]", "[lxh_beidian]", "[lxh_pili]"};


    public static boolean isNotBlank(String draft) {
        if (draft == null)
            draft = "";
        return (draft.trim().length() > 0);
    }

    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    public static String parseNull(String str) {
        if (str == null)
            str = "";
        return str;
    }

    public static String cutTextMaxLength(String str, int maxChinese, int maxEnglish) {
        if (str == null || maxChinese <= 0 || maxEnglish <= 0)
            return str;

        boolean hasChinese = hasChinese(str);
        if (hasChinese) {
            return str.substring(0, Math.min(maxChinese, str.length()));
        } else {
            return str.substring(0, Math.min(maxEnglish, str.length()));
        }
    }

    public static int isEditableTextExceedsMax(Editable s, int max, boolean noDiff) {
        int len = 0;
        return -1;
    }

    public static boolean hasChinese(String str) {
        if (str == null)
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (isChinese(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static String trimNull(String str) {
        if (str == null) {
            str = "";
        }
        return str.trim();
    }

    public static String customTrim(String str) {
        str = str.trim();
        if (str.length() == 0)
            str = " ";
        return str;
    }

    public static int parseNull(Integer int1) {
        if (int1 == null) {
            return 0;
        } else {
            return int1;
        }
    }

    public static double parseNullToDouble(String string) {
        if (string == null || string.length() == 0) {
            return 0.0;
        } else {
            return Double.parseDouble(string);
        }
    }

    public static Long parseNull(Long asLong) {
        if (asLong == null) {
            return 0l;
        } else {
            return asLong;
        }
    }

    public static String parseNull(CharSequence cs) {
        if (cs == null) {
            return "";
        }
        return cs.toString();
    }

    public static byte[] getByteArrayMD5(byte[] array) {
        try {
            byte md5[];
            md5 = MessageDigest.getInstance("MD5").digest(array);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] changeMD5StringToByteArray(String MD5) {
        byte[] result = new byte[MD5.length() / 2];
        for (int i = 0; i < MD5.length(); i = i + 2) {
            result[i / 2] = (byte) (Integer.parseInt(MD5.substring(i, i + 2), 16) & 0xFF);
        }
        return result;
    }

    public static String changeToString(byte[] array) {
        if (array == null) {
            return "";
        }
        return changeToString(array, array.length);
    }

    public static String changeToString(byte[] array, int length) {
        if (array == null) {
            return "";
        }

        StringBuffer strA = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if ((int) (array[i] & 0xff) < 16) {
                strA.append('0');
            }
            strA.append(Integer.toHexString((int) (array[i] & 0xff)));
        }
        return strA.toString();
    }

    public static String getDecimalString(byte[] array) {
        return getDecimalString(array, array.length);
    }

    public static String getDecimalString(byte[] array, int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(String.valueOf((int) (array[i] & 0xff)));
            sb.append(",");
        }
        return sb.toString();
    }

    public static String getDecimalString(long[] array) {
        return getDecimalString(array, array.length);
    }

    public static String getDecimalString(long[] array, int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(String.valueOf(array[i]));
            sb.append(",");
        }
        return sb.toString();
    }

    public static String getByteArrayMD5String(byte[] array) {
        try {
            byte md5[];
            md5 = MessageDigest.getInstance("MD5").digest(array);
            StringBuffer strA = new StringBuffer();
            for (int i = 0; i < md5.length; i++) {
                if ((int) (md5[i] & 0xff) < 16) {
                    strA.append('0');
                }
                strA.append(Integer.toHexString((int) (md5[i] & 0xff)));
            }
            return strA.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generalMD5ByString(String string) {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte tmp[] = md.digest();

            char str[] = new char[16 * 2];

            int k = 0;
            for (int i = 0; i < 16; i++) {

                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];

                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static byte[] readByteArrayFromFile(String path) throws IOException {
        File imageFile = new File(path);
        FileInputStream fis = new FileInputStream(imageFile);
        byte[] buffer = new byte[4096];
        long size = imageFile.length();
        long done = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (done < size) {
            int read = fis.read(buffer);
            if (read == -1) {
                fis.close();
                throw new IOException("Something went horribly wrong");
            }
            Log.d(TAG, "send size:" + done);
            if (done == 3129344l) {
                Log.d(TAG, "here");
            }
            baos.write(buffer, 0, read);
            done += read;
        }
        byte[] content = baos.toByteArray();
        baos.close();
        fis.close();
        return content;
    }

    public static String getByteArraySHA1String(byte[] array) {
        try {
            byte encode[];
            encode = MessageDigest.getInstance("SHA1").digest(array);
            StringBuffer strA = new StringBuffer();
            for (int i = 0; i < encode.length; i++) {
                if ((int) (encode[i] & 0xff) < 16) {
                    strA.append('0');
                }
                strA.append(Integer.toHexString((int) (encode[i] & 0xff)));
            }
            return strA.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getByteArraySHA1String(String filename) throws IOException {
        // byte[] content = readByteArrayFromFile(filename);
        // return getByteArraySHA1String(content);
        File file = new File(filename);
        FileInputStream in = new FileInputStream(file);
        MessageDigest messagedigest;
        byte[] buffer = null;
        try {
            messagedigest = MessageDigest.getInstance("SHA-1");
            buffer = new byte[1024 * 50];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                // 该对象通过使用 update（）方法处理数据 防止因为文件过大崩溃
                messagedigest.update(buffer, 0, len);
            }
            byte encode[] = messagedigest.digest();
            StringBuffer strA = new StringBuffer();
            for (int i = 0; i < encode.length; i++) {
                if ((int) (encode[i] & 0xff) < 16) {
                    strA.append('0');
                }
                strA.append(Integer.toHexString((int) (encode[i] & 0xff)));
            }
            return strA.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } finally {
            in.close();
        }
    }

    public static boolean parseNull(Boolean asBoolean, boolean defaultValue) {
        if (asBoolean == null)
            return defaultValue;
        else
            return asBoolean;
    }

    public static int parseNull(Integer asInteger, int defaultValue) {
        if (asInteger == null) {
            return defaultValue;
        }
        return asInteger;
    }

    public static long parseNull(Long asLong, long l) {
        if (asLong == null) {
            return l;
        }
        return asLong;
    }

    public static String getJsonString(JSONObject obj, String key) throws JSONException {
        if (obj.isNull(key)) {
            return "";
        } else {
            return obj.getString(key);
        }
    }

    public static long getJsonLong(JSONObject obj, String key) throws JSONException {
        if (obj.isNull(key)) {
            return 0;
        } else {
            return obj.getLong(key);
        }
    }

    public static JSONArray getJsonArray(JSONObject obj, String key) throws JSONException {
        if (obj.isNull(key)) {
            return new JSONArray();
        } else {
            return obj.getJSONArray(key);
        }
    }

    public static int getJsonInt(JSONObject obj, String key) throws JSONException {
        if (obj.isNull(key)) {
            return -1;
        } else {
            return obj.getInt(key);
        }
    }

    public static JSONObject getJsonObject(JSONObject obj, String key) throws JSONException {
        if (obj.isNull(key)) {
            return new JSONObject();
        } else {
            return obj.getJSONObject(key);
        }
    }

    public static String checkPinyinHeader(String pinyin) {
        // TODO Auto-generated method stub
        if (StringUtils.isNotBlank(pinyin) && ((pinyin.charAt(0) >= 'A' && pinyin.charAt(0) <= 'Z') || (pinyin.charAt(0) >= 'a' && pinyin.charAt(0) <= 'z')))
            return pinyin;
        else
            return ("#" + pinyin);
    }

    public static boolean isValidPhoneNumber(String userPhone) {
        if (null == userPhone || "".equals(userPhone))
            return false;
        Pattern p = Pattern.compile("^0?1[0-9]{10}");
        Matcher m = p.matcher(userPhone);
        return m.matches();
    }

    public static StringBuffer needEscapeSqlite(String mFilterStr) {
        StringBuffer mLikepattern = new StringBuffer("%");
        for (int i = 0; i < mFilterStr.length(); i++) {
            switch (mFilterStr.charAt(i)) {
                case '_':
                    mLikepattern.append("/_").append("%");
                    break;
                case '/':
                    mLikepattern.append("//").append("%");
                    break;
                case '[':
                    mLikepattern.append("/[").append("%");
                    break;
                case ']':
                    mLikepattern.append("/]").append("%");
                    break;
                case '%':
                    mLikepattern.append("/%").append("%");
                    break;
                case '&':
                    mLikepattern.append("/&").append("%");
                    break;
                case '(':
                    mLikepattern.append("/(").append("%");
                    break;
                case ')':
                    mLikepattern.append("/)").append("%");
                    break;
                case '\'':
                    mLikepattern.append("\'\'").append("%");
                    break;
                default:
                    mLikepattern.append(mFilterStr.charAt(i)).append("%");
                    break;
            }
        }
        return mLikepattern;
    }

    public static StringBuffer wholeWordNeedEscapeSqlite(String mFilterStr) {
        StringBuffer mLikepattern = new StringBuffer("%");
        for (int i = 0; i < mFilterStr.length(); i++) {
            switch (mFilterStr.charAt(i)) {
                case '_':
                    mLikepattern.append("/_");
                    break;
                case '/':
                    mLikepattern.append("//");
                    break;
                case '[':
                    mLikepattern.append("/[");
                    break;
                case ']':
                    mLikepattern.append("/]");
                    break;
                case '%':
                    mLikepattern.append("/%");
                    break;
                case '&':
                    mLikepattern.append("/&");
                    break;
                case '(':
                    mLikepattern.append("/(");
                    break;
                case ')':
                    mLikepattern.append("/)");
                    break;
                case '\'':
                    mLikepattern.append("\'\'");
                    break;
                default:
                    mLikepattern.append(mFilterStr.charAt(i));
                    break;
            }
        }
        mLikepattern.append("%");
        return mLikepattern;
    }

    public static String FormatFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.0");
        DecimalFormat df1 = new DecimalFormat("#");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df1.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df1.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String FormatPlayTime(int seconds) {
        int m = seconds / 60;
        String mString = m > 9 ? String.valueOf(m) : (0 + String.valueOf(m));
        int s = seconds % 60;
        String sString = s > 9 ? String.valueOf(s) : (0 + String.valueOf(s));
        return mString + ":" + sString;
    }

    public static String distanceFormat(int distance) {
        if (distance < 1000) {
            return String.valueOf(distance) + " m";
        } else if (distance >= 1000) {
            DecimalFormat df = new DecimalFormat("#.00");
            return String.valueOf(df.format((float) distance / 1000f)) + " km";
        } else if (distance >= 1000000) {
            return "很远...";
        }
        return null;
    }

    /**
     * Compare two avatar urls, return true even if avatar size is different
     *
     * @param url1
     * @param url2
     * @return
     */
    public static boolean compareAvatarUrl(String url1, String url2) {
        // http://tp4.sinaimg.cn/1655095247/180/1288767645/0
        if (url1.equals(url2)) {
            return true;
        }

        if (url1.replace("/180/", "/50/").equals(url2)) {
            return true;
        }

        return false;
    }

    public static String getConstellation(int month, int day) {
        String constellation = "";

        if (month == 1) {
            if (day < 21) {
                constellation = "摩羯座";
            } else {
                constellation = "水瓶座";
            }
        } else if (month == 2) {
            if (day < 20) {
                constellation = "水瓶座";
            } else {
                constellation = "双鱼座";
            }
        } else if (month == 3) {
            if (day < 21) {
                constellation = "双鱼座";
            } else {
                constellation = "白羊座";
            }
        } else if (month == 4) {
            if (day < 21) {
                constellation = "白羊座";
            } else {
                constellation = "金牛座";
            }
        } else if (month == 5) {
            if (day < 22) {
                constellation = "金牛座";
            } else {
                constellation = "双子座";
            }
        } else if (month == 6) {
            if (day < 22) {
                constellation = "双子座";
            } else {
                constellation = "巨蟹座";
            }
        } else if (month == 7) {
            if (day < 23) {
                constellation = "巨蟹座";
            } else {
                constellation = "狮子座";
            }
        } else if (month == 8) {
            if (day < 24) {
                constellation = "狮子座";
            } else {
                constellation = "处女座";
            }
        } else if (month == 9) {
            if (day < 24) {
                constellation = "处女座";
            } else {
                constellation = "天秤座";
            }
        } else if (month == 10) {
            if (day < 24) {
                constellation = "天秤座";
            } else {
                constellation = "天蝎座";
            }
        } else if (month == 11) {
            if (day < 23) {
                constellation = "天蝎座";
            } else {
                constellation = "射手座";
            }
        } else if (month == 12) {
            if (day < 22) {
                constellation = "射手座";
            } else {
                constellation = "摩羯座";
            }
        }
        return constellation;
    }


    public static boolean checkStringAllNumbers(String s) {

        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c < 58 && c > 47) {
                return true;
            }
        }
        return false;
    }


}
