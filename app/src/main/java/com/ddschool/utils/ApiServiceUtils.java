package com.ddschool.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ddschool.bean.BwConst;
import com.ddschool.bean.BwToken;
import com.ddschool.networks.ServiceFactory;
import com.ddschool.networks.UpdateInfo;
import com.ddschool.networks.UpdateService;
import com.ddschool.service.ApiService;
import com.ddschool.utils.PrefsConsts;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 更新管理
 * <p>
 * Created by wangchenlong on 16/1/6.
 */
@SuppressWarnings("unused")
public class ApiServiceUtils {

    @SuppressWarnings("unused")
    private static final String TAG = "DEBUG-ApiService: " + ApiServiceUtils.class.getSimpleName();

    /**
     * 检测更新
     */
    @SuppressWarnings("unused")
    public static void getToken(TokenCallback tokenCallback) {
        ApiService apiService =
                ServiceFactory.createServiceFrom(ApiService.class, ApiService.ENDPOINT);

        apiService.getToken(BwConst.AppId, BwConst.AppSecret, "client_credential")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bwToken -> onNext(bwToken, tokenCallback),
                        throwable -> onError(throwable, tokenCallback));
    }

    // 显示信息
    private static void onNext(BwToken bwToken, TokenCallback tokenCallback) {
        Log.e(TAG, "返回数据: " + bwToken.toString());
        if (bwToken.getErrcode()==0) {
            tokenCallback.onSuccess(bwToken);
        } else {
            tokenCallback.onError();
        }
    }

    // 错误信息
    private static void onError(Throwable throwable, TokenCallback tokenCallback) {
        tokenCallback.onError();
    }

    /**
     * 下载Apk, 并设置Apk的地址，
     * 默认位置: /storage/sdcard0/Download
     *
     * @param context    上下文
     * @param updateInfo 更新信息
     * @param infoName   通知名称
     * @param storeApk   存储的Apk
     */
    @SuppressWarnings("unused")
    public static void downloadApk(
            Context context, UpdateInfo updateInfo,
            String infoName, String storeApk
    ) {
        if (!isDownloadManagerAvailable()) {
            return;
        }

        String description = updateInfo.data.description;
        String appUrl = updateInfo.data.appURL;

        if (appUrl == null || appUrl.isEmpty()) {
            Log.e(TAG, "版本检测地址为空");
            return;
        }

        appUrl = appUrl.trim(); // 去掉首尾空格

        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // 添加http信息
        }

        Log.e(TAG, "appUrl: " + appUrl);

        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        request.setTitle(infoName);
        request.setDescription(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, storeApk);

        Context appContext = context.getApplicationContext();
        DownloadManager manager = (DownloadManager)
                appContext.getSystemService(Context.DOWNLOAD_SERVICE);

        // 存储下载Key
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        sp.edit().putLong(PrefsConsts.DOWNLOAD_APK_ID_PREFS, manager.enqueue(request)).apply();
    }

    // 系统版本号大于9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    // 错误回调
    public interface TokenCallback {
        void onSuccess(BwToken bwToken);

        void onError();
    }
}