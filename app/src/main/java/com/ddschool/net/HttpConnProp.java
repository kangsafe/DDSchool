package com.ddschool.net;

/**
 *
 * http链接的相关属性类
 */
public class HttpConnProp {

    private  String CONTENT_TYPE;
    private  String ACCEPT;
    private  String USER_AGENT;
    private  boolean IS_KEEP_ALIVE;
    private  boolean IS_USE_CACHE;
    private  boolean IS_INSTANCE_FOLLOW_REDIRECTS;
    private  int TIMEOUT;
    private  String REFERER;
    private Cookies COOKIES = null;

    /**
     * 以默认属性构造一个属性类
     * 详细修改可自己调用set函数进行逐个修改
     * 使用默认的话直接new就可以了
     */
    public HttpConnProp(){
        this.CONTENT_TYPE = "application/x-www-form-urlencoded";
        this.ACCEPT = "*/*";
        this.USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20100101 Firefox/15.0.1";
        this.IS_KEEP_ALIVE = true;
        this.IS_USE_CACHE = false;
        this.IS_INSTANCE_FOLLOW_REDIRECTS = true;
        this.TIMEOUT = 300000;
        this.REFERER = "";
        COOKIES = Cookies.getEmptyCookieObj();

    }

    /**
     * 获得一个以默认属性构造的本类对象
     * 也可以直接new 一个无参数的的本类对象，也是默认参数的
     * @return
     * httpConnProp对象
     */
    public static HttpConnProp getDefaultHttpConnProp() {
        return new HttpConnProp();
    }
    /**
     *
     * @param hcp
     * 待复制的对象
     * @return
     * 复制好后的对象
     */
    public static HttpConnProp copy(HttpConnProp hcp){
        HttpConnProp hcp2 = new HttpConnProp();
        hcp2.setAccept(hcp.getAccept());
        hcp2.setContentType(hcp.getContentType());
        hcp2.setCookies(hcp.getCookies());
        hcp2.setIsInstanceFollowRedirects(hcp.getIsInstanceFollowRedirects());
        hcp2.setIsKeepAlive(hcp.getIsKeepAlive());
        hcp2.setISUseCaches(hcp.getIsUseCaches());
        hcp2.setReferer(hcp.getReferer());
        hcp2.setTimeOut(hcp.getTimeOut());
        hcp2.setUserAgent(hcp.getUserAgent());
        return hcp2;

    }

    public String getContentType(){return this.CONTENT_TYPE;}
    public void setContentType(String ContentType){this.CONTENT_TYPE = ContentType;}

    public String getAccept(){return this.ACCEPT;}
    public void setAccept(String Accept){this.ACCEPT = Accept;}

    public String getUserAgent(){return this.USER_AGENT;}
    public void setUserAgent(String UserAgent){this.USER_AGENT = UserAgent;}

    public boolean getIsKeepAlive(){return this.IS_KEEP_ALIVE;}
    public void setIsKeepAlive(boolean isKeepAlive){this.IS_KEEP_ALIVE = isKeepAlive;}

    public boolean getIsUseCaches(){return this.IS_USE_CACHE;}
    public void setISUseCaches(boolean ISUseCaches){this.IS_USE_CACHE = ISUseCaches;}

    public boolean getIsInstanceFollowRedirects(){return this.IS_INSTANCE_FOLLOW_REDIRECTS;}
    public void setIsInstanceFollowRedirects(boolean ISInstanceFollowRedirects){this.IS_INSTANCE_FOLLOW_REDIRECTS = ISInstanceFollowRedirects;}

    public String getReferer(){return this.REFERER;}
    public void setReferer(String referer){this.REFERER = referer;}

    public int getTimeOut(){return this.TIMEOUT;}
    public void setTimeOut(int timeout){this.TIMEOUT = timeout; }

    public Cookies getCookies(){return this.COOKIES;}
    public void  setCookies(Cookies cookies) {this.COOKIES = cookies;}

}