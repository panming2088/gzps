package com.augurit.agmobile.photo.identify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.pmmobile.photoidentify.R;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 描述：身份证拍照识别出身份证号码
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.photo.identify
 * @createTime 创建时间 ：17/5/22
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/22
 * @modifyMemo 修改备注：
 */

public class RectCameraActivity extends Activity implements OnCaptureCallback {
    private MaskSurfaceView surfaceview;
    private ImageView imageView;
    //	拍照
    private Button btn_capture;
    //	重拍
    private Button btn_recapture;
    //	取消
    private Button btn_cancel;
    //	确认
    private Button btn_ok;

    //	拍照后得到的保存的文件路径
    private String filepath;

    /*
    private static PhotoIdentifyManager.OnIdetifyCallback mCallback;

    public static void setOnIdentifyCallback(PhotoIdentifyManager.OnIdetifyCallback callback){
        mCallback = callback;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rect_camera);

        this.surfaceview = (MaskSurfaceView) findViewById(R.id.surface_view);
        this.imageView = (ImageView) findViewById(R.id.image_view);
        btn_capture = (Button) findViewById(R.id.btn_capture);
        btn_recapture = (Button) findViewById(R.id.btn_recapture);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        //		设置矩形区域大小
        this.surfaceview.setMaskSize(1000, 170);

        //		拍照
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_capture.setEnabled(false);
                btn_ok.setEnabled(true);
                btn_recapture.setEnabled(true);
                CameraHelper.getInstance().tackPicture(RectCameraActivity.this);
            }
        });

        //		重拍
        btn_recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_capture.setEnabled(true);
                btn_ok.setEnabled(false);
                btn_recapture.setEnabled(false);
                imageView.setVisibility(View.GONE);
                surfaceview.setVisibility(View.VISIBLE);
                deleteFile();
                CameraHelper.getInstance().startPreview();
            }
        });

        //		确认
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*
                Intent ninten = new Intent(RectCameraActivity.this, MainActivity.class);
                ninten.putExtra("filepath",filepath);
                startActivity(ninten);
                RectCameraActivity.this.finish();
                */
                identifyPhoto();
            }
        });

        //		取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleteFile();
                RectCameraActivity.this.finish();
            }
        });
    }

    /**
     * 删除图片文件呢
     */
    private void deleteFile(){
        if(this.filepath==null || this.filepath.equals("")){
            return;
        }
        File f = new File(this.filepath);
        if(f.exists()){
            f.delete();
        }
    }

    @Override
    public void onCapture(boolean success, String filepath) {
        this.filepath = filepath;
        Log.d("weid","文件路径--"+filepath);
        String message = "拍照成功";
        if(!success){
            message = "拍照失败";
            CameraHelper.getInstance().startPreview();
            this.imageView.setVisibility(View.GONE);
            this.surfaceview.setVisibility(View.VISIBLE);
        }else{
            this.imageView.setVisibility(View.VISIBLE);
            this.surfaceview.setVisibility(View.GONE);
            this.imageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    private static String LANGUAGE = "chi_sim";//语料库类型
    private static String IMG_PATH = getSDPath();
    private Uri mUri;
    private static String textResult;
    private static String  number;
    private static Bitmap bitmapSelected;
    private static Bitmap bitmapTreated;
    private ProgressDialog mProgressDialog;

    /**
     * 获取sd卡的路径
     * @return 路径的字符串
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        }
        return sdDir.toString();
    }

    private static final int SHOWRESULT = 0x101;
    private static final int SHOWTREATEDIMG = 0x102;

    // 该handler用于处理修改结果的任务
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWRESULT:
                    if (textResult.equals("")) {
                        Toast.makeText(RectCameraActivity.this, "识别失败,请手动输入", Toast.LENGTH_LONG).show();
                        PhotoIdentifyManager.getInstance(RectCameraActivity.this).onHandleIdentifyFail("识别失败");
                        RectCameraActivity.this.finish();
                    /*
                        if(mCallback != null){
                            mCallback.onFail("识别失败");
                            RectCameraActivity.this.finish();
                        }
                        */
                    }
                    else {
                    /*
                        if(mCallback != null){
                            mCallback.onSuccess(number);
                            RectCameraActivity.this.finish();
                        }
                        */
                        PhotoIdentifyManager.getInstance(RectCameraActivity.this).onHandleIdentifySuccess(number);
                        RectCameraActivity.this.finish();
                    }
                    break;
                case SHOWTREATEDIMG:
                    /*
                    tvResult.setText("识别中......");
                    showPicture(ivTreated, bitmapTreated);
                    */
                    mProgressDialog = new ProgressDialog(RectCameraActivity.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                    mProgressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                    mProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    mProgressDialog.setTitle("提示:");
                    mProgressDialog.setMessage("数据识别中...");
                    mProgressDialog.show();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public void identifyPhoto(){
        // 若文件夹不存在 首先创建文件夹
        File path = new File(IMG_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        if(filepath != null){
            mUri=Uri.fromFile(new File(IMG_PATH, "ocr.jpg"));
            bitmapSelected = decodeUriAsBitmap(mUri);
            // 新线程来处理识别
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmapTreated = ImgPretreatment.converyToGrayImg(bitmapSelected);
                    Message msg = new Message();
                    msg.what = SHOWTREATEDIMG;
                    myHandler.sendMessage(msg);
                        textResult = doOcr(bitmapTreated, LANGUAGE);
                    number=getNumber(textResult);
                    Message msg2 = new Message();
                    msg2.what = SHOWRESULT;
                    myHandler.sendMessage(msg2);

                    //识别后删掉图片
                    deleteFile();

                }

            }).start();
        }
    }

    private String TAG="weid";
    /**
     * 进行图片识别
     *
     * @param bitmap
     *            待识别图片
     * @param language
     *            识别语言
     * @return 识别结果字符串
     */
    public String doOcr(Bitmap bitmap, String language) {
        TessBaseAPI baseApi = new TessBaseAPI();

        Log.d(TAG,"路径是"+getSDPath());
        baseApi.init(getSDPath(), language);

        // 必须加此行，tess-two要求BMP必须为此配置
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        baseApi.setImage(bitmap);

        String text = baseApi.getUTF8Text();

        baseApi.clear();
        baseApi.end();

        return text;
    }

    private String getNumber(String textResult) {
        String number_s="";
        if(textResult.contains("号码")){
            number_s=textResult.substring(textResult.indexOf("号码"));
            Log.d(TAG,"是否含有o"+number_s);

        }else{
            number_s=textResult;
        }
        if(number_s.contains("o")){
            number_s=number_s.replace("o","0");
            Log.d(TAG,"替换后"+number_s);
        }

        return number_s;
    }

    /**
     * 根据URI获取位图
     *
     * @param uri
     * @return 对应的位图
     */
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


}
