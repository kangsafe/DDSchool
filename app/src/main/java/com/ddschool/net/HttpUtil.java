//package com.ddschool.net;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
///**
// * HTTP通信的工具类
// */
//public final class HttpUtil {
//    /** 定义HTTP通信的对象 */
//    private static CloseableHttpClient httpClient = HttpClients.createDefault();
//    /** 定义基础的请求URL */
//    public static final String BASE_URL = "http://schoolapi2.wo-ish.com/";
//    /**
//     * 发送GET请求方法
//     * @param requestUrl 请求的URL
//     * @return 响应的数据
//     */
//    public static InputStream sendGetRequest(String requestUrl){
//        /** 创建get请求对象 */
//        HttpGet httpGet = new HttpGet(BASE_URL + requestUrl);
//        try {
//            /** 执行GET请求 */
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            // The underlying HTTP connection is still held by the response object
//            // to allow the response content to be streamed directly from the network socket.
//            // In order to ensure correct deallocation of system resources
//            // the user MUST either fully consume the response content  or abort request
//            // execution by calling CloseableHttpResponse#close().
//            //建立的http连接，仍旧被response1保持着，允许我们从网络socket中获取返回的数据
//            //为了释放资源，我们必须手动消耗掉response1或者取消连接（使用CloseableHttpResponse类的close方法）
//
//            /** 判断响应的状态码: 200代表响应成功 */
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//                /** 获取响应的实体 */
//                HttpEntity entity = response.getEntity();
//                httpClient.close();
//                /** 返回响应的数据 */
//                EntityUtils.consume(entity);
//                return entity.getContent();  //当需要返回为输入流InputStream时的返回值
//                //return EntityUtils.toString(entity); // 当返回的类型为Json数据时，调用此返回方法
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 发送post请求
//     * @param requestUrl 请求的URL
//     * @param params 请求的参数
//     * @return 响应的数据
//     */
//    public static InputStream sendPostRequest(String requestUrl, Map<String, String> params){
//        /** 创建post请求对象 */
//        HttpPost httpPost = new HttpPost(BASE_URL + requestUrl);
//        try {
//            /** 设置请求参数 */
//            if (params != null && params.size() > 0){
//                /** 将map转化成list集合 */
//                List<NameValuePair> paramLists = new ArrayList<>();
//                for (Map.Entry<String, String> map : params.entrySet()){
//                    paramLists.add(new BasicNameValuePair(map.getKey(), map.getValue()));
//                }
//                /** 为POST请设置请求参数 */
//                httpPost.setEntity(new UrlEncodedFormEntity(paramLists, "UTF-8"));
//            }
//            /** 执行post请求 */
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//
//              /** 对响应的状态做判断  */
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//                /** 服务器响应成功 , 获取响应实体*/
//                HttpEntity entity = response.getEntity();
//                // do something useful with the response body
//                // and ensure it is fully consumed
//                //消耗掉response
//                /** 返回响应数据 */
//                EntityUtils.consume(entity);
//                return entity.getContent();  //当需要返回为输入流InputStream时的返回值
//                //return EntityUtils.toString(entity);
//            }
//        } catch (Exception e) {
//            System.out.println(BASE_URL + requestUrl);
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 发送post请求
//     * @param requestUrl 请求的URL
//     * @param paramLists 请求的参数
//     * @return 响应的数据
//     */
//    public static String sendPostRequest(String requestUrl, List<NameValuePair> paramLists){
//        /** 创建post请求对象 */
//        HttpPost httpPost = new HttpPost(BASE_URL + requestUrl);
//        try {
//            /** 设置请求参数 */
//            if (paramLists != null && paramLists.size() > 0){
////                                /** 将map转化成list集合 */
////                                List<NameValuePair> paramLists = new ArrayList<NameValuePair>();
////                                for (Map.Entry<String, String> map : params.entrySet()){
////                                        paramLists.add(new BasicNameValuePair(map.getKey(), map.getValue()));
////                                }
////                                /** 为POST请设置请求参数 */
//                httpPost.setEntity(new UrlEncodedFormEntity(paramLists, "UTF-8"));
//            }
//            /** 执行post请求 */
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            /** 对响应的状态做判断  */
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//                /** 服务器响应成功 , 获取响应实体*/
//                HttpEntity entity = response.getEntity();
//                /** 返回响应数据 */
//                EntityUtils.consume(entity);
//                //return entity.getContent();  //当需要返回为输入流InputStream时的返回值
//                return EntityUtils.toString(entity);
//            }
//        } catch (Exception e) {
//            System.out.println(BASE_URL + requestUrl);
//            e.printStackTrace();
//        }
//        return null;
//    }
//}