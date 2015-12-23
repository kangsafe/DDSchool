package com.ddschool.net;

import android.util.Xml;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ReturnData {
    private String html;
    public ReturnData(String temp, HttpConnProp updtedHttpConnProp, String encoding){
        html=temp;
    }
    public String getHtmlData(){
        return html;
    }
}
