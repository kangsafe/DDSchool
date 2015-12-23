package com.ddschool.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 非同步保存Cookie的键值
 *
 * @author SomeWind
 *
 */
public class Cookies {

    private HashMap<String, String> hashMap;

    public Cookies() {
        hashMap = new HashMap<String, String>();
    }

    /**
     * 清除 Cookies 里面的所有 Cookie 记录
     */
    public void clear() {
        hashMap.clear();
    }

    /**
     * 根据 key 获取对应的 Cookie 值
     *
     * @param key
     *            要获取的 Cookie 值的 key
     * @return 如果不存在 key 则返回 null
     */
    public String getCookie(String key) {
        return hashMap.get(key);
    }

    /**
     * 在 Cookies 里设置一个 Cookie
     *
     * @param key
     *            要设置的 Cookie 的 key
     * @param value
     *            要设置的 Cookie 的 value
     */
    public void putCookie(String key, String value) {
        hashMap.put(key, value);
    }

    /**
     * 在 Cookies 里面设置传入的 cookies
     *
     * @param cookies
     */
    public void putCookies(String cookies) {
        if (cookies == null)
            return;

        String[] strCookies = cookies.split(";");
        for (int i = 0; i < strCookies.length; i++) {
            for (int j = 0; j < strCookies[i].length(); j++) {
                if (strCookies[i].charAt(j) == '=') {
                    this.putCookie(
                            strCookies[i].substring(0, j),
                            strCookies[i].substring(j + 1,
                                    strCookies[i].length()));
                }
            }
        }
    }

    /**
     * 获取 Cookies 的字符串
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        if (hashMap.isEmpty())
            return "";

        Set<Entry<String, String>> set = hashMap.entrySet();
        StringBuilder sb = new StringBuilder(set.size() * 50);
        for (Entry<String, String> entry : set) {
            sb.append(String.format("%s=%s;", entry.getKey(), entry.getValue()));
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}