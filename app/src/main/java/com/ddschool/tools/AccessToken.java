package com.ddschool.tools;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ddschool.bean.BwConst;
import com.ddschool.bean.BwResult;
import com.ddschool.bean.BwToken;
import com.ddschool.utils.HttpHelper;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccessToken {
    private static String accessstoken="";
    private static int exprise=0;
    private static Date date;

    public static String getAccessToken() {
        Date now = new Date();
        long between = (now.getTime() - date.getTime()) / 1000;//除以1000是为了转换成秒
        if (between > exprise - 10) {
            String str = "";
            Map<String, String> map = new HashMap<>();
            map.put("grant_type", "client_credential");
            map.put("appid", BwConst.AppId);
            map.put("appsecret", BwConst.AppSecret);
            for (String key : map.keySet()
                    ) {
                str += key + "=" + map.get(key) + "&";
            }
            str = str.substring(0, str.length() - 1);
            HttpHelper httpHelper = new HttpHelper();
            String data=httpHelper.getHtml("http://schoolapi2.wo-ish.com/OAuth/token", str, true, "utf-8");
            Gson gson = new Gson();
            BwToken tobj = gson.fromJson(data, BwToken.class);
            if(tobj.getErrcode()==0){
                exprise=tobj.getData().getExpires_in();
                accessstoken=tobj.getData().getAccess_token();
                date=new Date();
            }
            //httpHelper.getHtmlByThread("http://schoolapi2.wo-ish.com/OAuth/token", str, true, "utf-8", handler, 0);
        }
        return accessstoken;
    }
}
