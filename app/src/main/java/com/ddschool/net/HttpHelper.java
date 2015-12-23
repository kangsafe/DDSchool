package com.ddschool.net;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自己写的java用HttpHelper 方便http相关操作 提供一系列同步 异步的方法
 * 也有文件下载相关的异步、同步方法
 * 所有的异步方法都提供一系列的回调接口。方便快捷~
 *
 *
 */
public class HttpHelper {

    /**
     * 固定5个线程来执行类内异步方法(默认)
     */
    private static ExecutorService executorService = Executors
            .newFixedThreadPool(5);

    /**
     * 立刻停止线程池服务，若有线程在running，强制停止 PS：命令行程序最后必须执行shutdown停止线程池服务，否则程序结束不了
     * 警告：若一旦结束线程池服务，将不能再添加执行异步任务，除非运行本类中的openNewExecutorService()
     */
    public static void shutTheThreadPoolDownNow() {
        executorService.shutdownNow();
    }

    /**
     * 等待所有线程完成执行后关闭线程池服务 PS：命令行程序最后必须执行shutdown停止线程池服务，否则程序结束不了
     * 警告：若一旦结束线程池服务，将不能再添加执行异步任务,除非运行本类中的openNewExecutorService()
     */
    public static void shutTheThreadPoolDown() {

        executorService.shutdown();
    }

    /**
     * 关闭默认的线程池服务，新建一个新的线程池服务
     *
     * @param RunningThreadCount
     */
    public static void openNewExecutorService(int RunningThreadCount) {

        if (!executorService.isShutdown())
            executorService.shutdown();
        executorService = Executors.newFixedThreadPool(RunningThreadCount);
    }

    /**
     *
     * 用于异步通信的回调接口
     *
     */
    public interface AsynExecuteCallBack {
        public void beforeExecute();

        public void afterExecute(ReturnData rData);

        public void exceptionOccored(Exception e);
    }

    /**
     * 异步方法: 以get方式获得ReturnData对象，使用回调接口处理获得对象
     *
     * @param strUrl
     *            请求的URL地址
     * @param hcp
     *            连接的属性类
     * @param encoding
     *            编码字符串 例如 ASCII gb2312 utf-8 等
     * @param callback
     *            回调接口
     */
    public static void AysnGetHtml(final String strUrl, final HttpConnProp hcp,
                                   final String encoding, final AsynExecuteCallBack callback) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                callback.beforeExecute();

                try {
                    ReturnData rd = getHtmlBytes(strUrl, hcp, null, false,
                            encoding);
                    callback.afterExecute(rd);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    callback.exceptionOccored(e);

                }

            }
        });

    }

    /**
     * 异步方法: 添加一个任务 以post方式获取页面
     *
     * @param strUrl
     *            网页的URL地址
     * @param strPost
     *            POST的数据 注意POST属性的值如果是中文或含有特殊符号根据相应编码URLencode
     * @param hcp
     *            http连接相应的属性类
     * @param encoding
     *            编码字符串 例如 ASCII gb2312 utf-8 等
     * @param callback
     *            一系列回调接口
     */
    public static void AysnPostHtml(final String strUrl, final String strPost,
                                    final HttpConnProp hcp, final String encoding,
                                    final AsynExecuteCallBack callback) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                callback.beforeExecute();

                try {
                    ReturnData rd = getHtmlBytes(strUrl, hcp, strPost, true,
                            encoding);
                    callback.afterExecute(rd);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    callback.exceptionOccored(e);

                }

            }
        });

    }

    /**
     * 同步方法: 以gb2312编码，默认的链接属性get页面
     *
     * @param strUrl
     *            访问的URL地址
     * @return 网页的html
     */
    public static String getHtml(String strUrl) {

        return getHtml(strUrl, new HttpConnProp(), "gb2312");

    }

    /**
     * 同步方法: get方式取页面，返回html页面
     *
     * @param strUrl
     *            网页URL地址
     * @param hcp
     *            访问属性的类对象
     * @param encoding
     *            编码字符串 例如 ASCII gb2312 utf-8 等
     * @return 如果出现错误返回null,否则返回页面的String对象
     */
    public static String getHtml(String strUrl, HttpConnProp hcp,
                                 String encoding) {

        try {
            return getHtmlBytes(strUrl, hcp, null, false, encoding)
                    .getHtmlData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }

    }

    /**
     * 同步方法: 以post方式获取页面
     *
     * @param strUrl
     *            网页URL地址
     * @param strPost
     *            POST的数据 注意POST属性的值如果是中文或含有特殊符号根据相应编码URLencode
     * @param hcp
     *            http连接的属性对象
     * @param encoding
     *            返回的数据存储的编码和post的编码，例如'gb2312'、'utf-8'
     * @return 返回得到的html
     */
    public static String postHtml(String strUrl, String strPost,
                                  HttpConnProp hcp, String encoding) {
        try {
            return getHtmlBytes(strUrl, hcp, strPost, true, encoding)
                    .getHtmlData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 同步方法:
     *
     * 本Helper中最主要的方法 其他的一些方法都是根据此方法进行包装
     *
     * @param strUrl
     *            网页URL地址
     * @param httpConnProp
     *            http连接的属性
     * @param strPost
     *            POST的数据 注意POST属性的值如果是中文或含有特殊符号根据相应编码URLencode，如果是get
     *            isPost设置为false，请设置null
     * @param isPost
     *            是否是POST方法
     * @param encoding
     *            返回的数据存储的编码和post的编码，例如'gb2312'、'utf-8'
     * @return 一个返回信息的包装，内部类ReturnData,里面有HttpConnProp的更新和请求得到的数据
     * @throws IOException
     */

    public static ReturnData getHtmlBytes(String strUrl,
                                          HttpConnProp httpConnProp, String strPost, boolean isPost,
                                          String encoding) throws IOException {
        HttpURLConnection.setFollowRedirects(true);// 静态变量设置 防止某些服务出错，特别是IIS的
        HttpURLConnection httpCon = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        String temp = "";

        // System.out.println(httpConnProp.getCookies().toString());
        try {
            URL url = new URL(strUrl);
            httpCon = (HttpURLConnection) url.openConnection();

            // 设置相关属性
            httpCon.setReadTimeout(httpConnProp.getTimeOut());
            httpCon.setConnectTimeout(httpConnProp.getTimeOut());
            httpCon.setUseCaches(httpConnProp.getIsUseCaches());// 设置缓存
            if (httpConnProp.getIsKeepAlive())
                httpCon.setRequestProperty("connection", "Keep-Alive");
            httpCon.setInstanceFollowRedirects(httpConnProp
                    .getIsInstanceFollowRedirects());// 设置跳转跟随
            httpCon.setRequestProperty("Referer", httpConnProp.getReferer());
            httpCon.setRequestProperty("Content-Type",
                    httpConnProp.getContentType());
            httpCon.setRequestProperty("Accept", httpConnProp.getAccept());
            httpCon.setRequestProperty("User-Agent",
                    httpConnProp.getUserAgent());
            httpCon.setRequestProperty("Cookie", httpConnProp.getCookies()
                    .toString());
            httpCon.setRequestProperty("Accept-Language",
                    "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");

            if (isPost) {// 如果是post 设置post信息
                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);
                httpCon.setUseCaches(false);// POST请求不能使用缓存
                httpCon.setRequestProperty("Context-Length",
                        String.valueOf(strPost.getBytes(encoding).length));
                httpCon.setRequestMethod("POST");
                httpCon.connect();
                OutputStream os = null;// 输出post信息的流
                try {
                    os = httpCon.getOutputStream();
                    os.write(strPost.getBytes(encoding));
                    os.flush();
                } finally {
                    if (os != null)
                        os.close();
                }
            }
            // 获取数据

            is = httpCon.getInputStream();
            bis = new BufferedInputStream(is);
            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = bis.read(buffer)) != -1) {
                temp += (new String(buffer, 0, count, encoding));
            }

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HttpConnProp updtedHttpConnProp = HttpConnProp.copy(httpConnProp);
        if (httpCon != null) {

            updtedHttpConnProp.getCookies().putCookies(
                    httpCon.getHeaderField("Set-Cookie"));
            // 更新 referer
            updtedHttpConnProp.setReferer(strUrl);

            // System.out.println("输出号：--" + httpCon.getResponseCode());

            httpCon.disconnect();
            httpCon = null;
        }

        return new ReturnData(temp, updtedHttpConnProp, encoding);

    }
    /**
     * 同步方法：
     * 从网络上下载文件 缓存4kb
     * @param strUrl
     * 文件URL地址
     * @param fileURL
     * 保存地址 如g://1.rar (注意是覆蓋的)
     */
    public static void downLoadFile(final String strUrl, final String fileURL) {
        downLoadFile(strUrl, fileURL,4096);
    }
    /**
     * 同步方法: 从网络上下载文件
     *
     * @param strUrl
     *文件URL地址
     * @param fileURL
     *保存地址 如g://1.rar (注意是覆蓋的)
     * @bufferLength
     * 下载缓存的大小，byte单位 ，建议4096 (4KB)
     */
    public static void downLoadFile(final String strUrl, final String fileURL,final int bufferLength) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {

            in = new BufferedInputStream(new URL(strUrl).openStream());
            File img = new File(fileURL);
            out = new BufferedOutputStream(new FileOutputStream(img));
            byte[] buf = new byte[bufferLength];
            int count = in.read(buf);
            while (count != -1) {
                out.write(buf, 0, count);
                count = in.read(buf);
            }
            in.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     *
     * 文件异步下载的接口
     *
     */
    interface asynDownLoadCallBack {
        public void beforeDownLoad();// 下载文件之前
        /**
         * 如果服务器不指定文件的长度，则fileLength = 0  要用到这几个参数的话要自己在回调接口处判断
         * @param hasDownedByte
         * 已经下载的字节数
         * @param FileLength
         * 文件总字节数 若服务器无此信息即为0
         */
        public void duringDownLoad(long hasDownedByte, long FileLength);// 下载文件之中

        public void downLoadFinished();// 下载文件之后

        public void downLoadErrorOccured(Exception e);// 出错后
    }

    /**
     * 异步方法:
     * 以4KB缓存从网上下载文件
     * @param strUrl
     * 文件URL地址
     * @param fileURL
     * 保存的地址 如g://1.rar
     * @param callback
     * 回调接口
     */
    public static void AsynDownLoadFile(final String strUrl,
                                        final String fileURL, final asynDownLoadCallBack callback) {
        AsynDownLoadFile(strUrl, fileURL, callback,4096);
    }
    /**
     * 异步方法:
     * 从网络上异步下载文件
     * @param strUrl
     * 文件URL地址
     * @param fileURL
     * @param callback
     * @param bufferLength
     */
    public static void AsynDownLoadFile(final String strUrl,
                                        final String fileURL, final asynDownLoadCallBack callback,final int bufferLength) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                URLConnection urlConnection = null;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                long fileLength = 0;
                long hasDowned = 0;
                try {
                    urlConnection = new URL(strUrl).openConnection();
                    fileLength = urlConnection.getContentLength();//getContentLengthLong();// 获取报文里给的数据长度
                    bis = new BufferedInputStream(urlConnection
                            .getInputStream());
                    bos = new BufferedOutputStream(new FileOutputStream(
                            new File(fileURL)));

                    callback.beforeDownLoad();// 执行回调函数

                    byte[] buffer = new byte[bufferLength];
                    int count = bis.read(buffer);
                    while (count != -1) {
                        bos.write(buffer, 0, count);
                        hasDowned += count;
                        // 如果服务器不指定文件的长度，则fileLength = 0  要用到这几个参数的话要自己在回调接口处判断
                        callback.duringDownLoad(hasDowned, fileLength);
                        count = bis.read(buffer);

                    }
                    callback.downLoadFinished();

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.downLoadErrorOccured(e);
                }
                finally{

                    try {
                        bis.close();
                        bos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}