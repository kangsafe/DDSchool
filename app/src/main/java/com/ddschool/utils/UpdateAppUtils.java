package com.ddschool.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ddschool.networks.ServiceFactory;
import com.ddschool.networks.UpdateInfo;
import com.ddschool.networks.UpdateService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ���¹�����
 * <p/>
 * Created by wangchenlong on 16/1/6.
 */
@SuppressWarnings("unused")
public class UpdateAppUtils {

    @SuppressWarnings("unused")
    private static final String TAG = "DEBUG-WCL: " + UpdateAppUtils.class.getSimpleName();

    /**
     * ������
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(String appCode, String curVersion, UpdateCallback updateCallback) {
        UpdateService updateService =
                ServiceFactory.createServiceFrom(UpdateService.class, UpdateService.ENDPOINT);

        updateService.getUpdateInfo(appCode, curVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateInfo->onNext(updateInfo, updateCallback),
                        throwable -> onError(throwable, updateCallback));
    }

    // ��ʾ��Ϣ
    private static void onNext(UpdateInfo updateInfo, UpdateCallback updateCallback) {
        Log.e(TAG, "��������: " + updateInfo.toString());
        if (updateInfo.error_code != 0 || updateInfo.data == null ||
                updateInfo.data.appURL == null) {
            updateCallback.onError(); // ʧ��
        } else {
            updateCallback.onSuccess(updateInfo);
        }
    }

    // ������Ϣ
    private static void onError(Throwable throwable, UpdateCallback updateCallback) {
        updateCallback.onError();
    }

    /**
     * ����Apk, ������Apk��ַ,
     * Ĭ��λ��: /storage/sdcard0/Download
     *
     * @param context    ������
     * @param updateInfo ������Ϣ
     * @param infoName   ֪ͨ����
     * @param storeApk   �洢��Apk
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
            Log.e(TAG, "����д\"App���ص�ַ\"");
            return;
        }

        appUrl = appUrl.trim(); // ȥ����β�ո�

        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // ���Http��Ϣ
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

        // �洢����Key
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        sp.edit().putLong(PrefsConsts.DOWNLOAD_APK_ID_PREFS, manager.enqueue(request)).apply();
    }

    // ��С�汾�Ŵ���9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    // ����ص�
    public interface UpdateCallback {
        void onSuccess(UpdateInfo updateInfo);

        void onError();
    }
}