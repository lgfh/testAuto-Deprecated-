package cn.unionstech.Utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author WXY
 * @version 创建时间：2018/11/12
 */
public class JsonUtil {

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }
}
