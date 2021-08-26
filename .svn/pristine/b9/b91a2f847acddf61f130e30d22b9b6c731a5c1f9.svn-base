package com.augurit.am.cmpt.media;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

public class MediaScannerFile {
    /**
     * 扫描指定的文件，把文件的媒体信息添加到媒体数据库中，
     * 选择图片时显示的图片列表就是从这个媒体数据库中读取的图片信息
     * 经测试，filePath必须为文件的详细路径，如果是文件夹路径是扫描不到里面的文件的
     * @param context 上下文
     * @param filePath 文件的详细路径
     * @param mineType 待扫描的文件的类型，可为null
     * @param sListener 扫描完成监听器
     */
    public static MediaScannerConnection scanFile(Context context, String[] filePath, String[] mineType,
                                                  MediaScannerConnection.OnScanCompletedListener sListener) {

        ClientProxy client = new ClientProxy(filePath, mineType, sListener);
        try {
            MediaScannerConnection connection = new MediaScannerConnection(
                    context.getApplicationContext(), client);
            client.mConnection = connection;
            connection.connect();
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    static class ClientProxy implements MediaScannerConnection.MediaScannerConnectionClient {
        final String[] mPaths;
        final String[] mMimeTypes;
        final MediaScannerConnection.OnScanCompletedListener mClient;
        MediaScannerConnection mConnection;
        int mNextPath;

        ClientProxy(String[] paths, String[] mimeTypes,
                    MediaScannerConnection.OnScanCompletedListener client) {
            mPaths = paths;
            mMimeTypes = mimeTypes;
            mClient = client;
        }

        public void onMediaScannerConnected() {
            scanNextPath();
        }

        public void onScanCompleted(String path, Uri uri) {
            if (mClient != null) {
                mClient.onScanCompleted(path, uri);
            }
            scanNextPath();
        }

        /**
         * 自动扫描下一个
         */
        void scanNextPath() {
            if (mNextPath >= mPaths.length) {
                mConnection.disconnect();
                return;
            }
            String mimeType = mMimeTypes != null ? mMimeTypes[mNextPath] : null;
            mConnection.scanFile(mPaths[mNextPath], mimeType);
            mNextPath++;
        }
    }
}
