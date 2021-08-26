package com.augurit.agmobile.photo.identify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.pmmobile.photoidentify.R;

import java.io.File;
import java.io.FileNotFoundException;
/**
 * 描述：身份证拍照识别出身份证号码
 * 测试
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.photo.identify
 * @createTime 创建时间 ：17/5/22
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/22
 * @modifyMemo 修改备注：
 */

@Deprecated
public class MainActivity extends AppCompatActivity {

    private static final int PHOTO_CAPTURE = 0x11;// 拍照
    private static final int PHOTO_RESULT = 0x12;// 结果

    private static String LANGUAGE = "eng";
    //    private static String IMG_PATH = getSDPath() + java.io.File.separator
//            + "ocrtest";
    private static String IMG_PATH = getSDPath();

    private static TextView tvResult;
    private static ImageView ivSelected;
    private static ImageView ivTreated;
    private static Button btnCamera;
    private static Button btnSelect;
    private static CheckBox chPreTreat;
    private static RadioGroup radioGroup;
    private static String textResult;
    private static String  number;
    private static Bitmap bitmapSelected;
    private static Bitmap bitmapTreated;
    private static final int SHOWRESULT = 0x101;
    private static final int SHOWTREATEDIMG = 0x102;

    // 该handler用于处理修改结果的任务
    public static Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWRESULT:
                    if (textResult.equals(""))
                        tvResult.setText("识别失败");
                    else
                        tvResult.setText(number);
                    break;
                case SHOWTREATEDIMG:
                    tvResult.setText("识别中......");
                    showPicture(ivTreated, bitmapTreated);
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private String filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);

        Intent intent=getIntent();
        filepath=intent.getStringExtra("filepath");



        // 若文件夹不存在 首先创建文件夹
        File path = new File(IMG_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }

        tvResult = (TextView) findViewById(R.id.tv_result);
        ivSelected = (ImageView) findViewById(R.id.iv_selected);
        ivTreated = (ImageView) findViewById(R.id.iv_treated);
        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnSelect = (Button) findViewById(R.id.btn_select);
        chPreTreat = (CheckBox) findViewById(R.id.ch_pretreat);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        btnCamera.setOnClickListener(new cameraButtonListener());
        btnSelect.setOnClickListener(new selectButtonListener());

        // 用于设置解析语言
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               /*
                switch (checkedId) {
                    case R.id.rb_en:
                        LANGUAGE = "eng";
                        break;
                    case R.id.rb_ch:
                        LANGUAGE = "chi_sim";
                        break;
                }
                */

                if(checkedId == R.id.rb_en){
                    LANGUAGE = "eng";
                }else if(checkedId == R.id.rb_ch){
                    LANGUAGE = "chi_sim";
                }
            }

        });

        if(filepath!=null){
            mUri=Uri.fromFile(new File(IMG_PATH, "ocr.jpg"));

            // 处理结果
            //        if (requestCode == PHOTO_RESULT) {
            bitmapSelected = decodeUriAsBitmap(mUri);
            if (chPreTreat.isChecked())
                tvResult.setText("预处理中......");
            else
                tvResult.setText("识别中......");
            // 显示选择的图片
            showPicture(ivSelected, bitmapSelected);

            // 新线程来处理识别
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (chPreTreat.isChecked()) {
                        bitmapTreated = ImgPretreatment
                                .doPretreatment(bitmapSelected);
                        Message msg = new Message();
                        msg.what = SHOWTREATEDIMG;
                        myHandler.sendMessage(msg);
                        textResult = doOcr(bitmapTreated, LANGUAGE);
                        number=getNumber(textResult);
                    } else {
                        bitmapTreated = ImgPretreatment
                                .converyToGrayImg(bitmapSelected);
                        Message msg = new Message();
                        msg.what = SHOWTREATEDIMG;
                        myHandler.sendMessage(msg);
                        textResult = doOcr(bitmapTreated, LANGUAGE);
                        number=getNumber(textResult);
                    }
                    Message msg2 = new Message();
                    msg2.what = SHOWRESULT;
                    myHandler.sendMessage(msg2);
                }

            }).start();
        }
        Log.d(TAG,"reciverfilepath="+filepath);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
   //     getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private Uri mUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED)
            return;

        if (requestCode == PHOTO_CAPTURE) {
            tvResult.setText("abc");
            mUri=Uri.fromFile(new File(IMG_PATH, "temp.jpg"));
        }else{
            mUri=data.getData();
        }
        Log.d(TAG,"filepath----="+filepath);
        if(filepath!=null){
            mUri=Uri.fromFile(new File(IMG_PATH, "ocr.jpg"));
        }

        // 处理结果
//        if (requestCode == PHOTO_RESULT) {
        bitmapSelected = decodeUriAsBitmap(mUri);
        if (chPreTreat.isChecked())
            tvResult.setText("预处理中......");
        else
            tvResult.setText("识别中......");
        // 显示选择的图片
        showPicture(ivSelected, bitmapSelected);

        // 新线程来处理识别
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (chPreTreat.isChecked()) {
                    bitmapTreated = ImgPretreatment
                            .doPretreatment(bitmapSelected);
                    Message msg = new Message();
                    msg.what = SHOWTREATEDIMG;
                    myHandler.sendMessage(msg);
                    textResult = doOcr(bitmapTreated, LANGUAGE);
                    number=getNumber(textResult);
                } else {
                    bitmapTreated = ImgPretreatment
                            .converyToGrayImg(bitmapSelected);
                    Message msg = new Message();
                    msg.what = SHOWTREATEDIMG;
                    myHandler.sendMessage(msg);
                    textResult = doOcr(bitmapTreated, LANGUAGE);
                    number=getNumber(textResult);
                }
                Message msg2 = new Message();
                msg2.what = SHOWRESULT;
                myHandler.sendMessage(msg2);
            }

        }).start();

//        }
        Log.d(TAG,"修改");
        super.onActivityResult(requestCode, resultCode, data);
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

    // 拍照识别
    class cameraButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                    Uri.fromFile(new File(IMG_PATH, "temp.jpg")));
//            startActivityForResult(intent, PHOTO_CAPTURE);

            Intent ninten = new Intent(MainActivity.this, RectCameraActivity.class);
//            ninten.setClass(MainActivity.this, RectCameraActivity.class);
            startActivity(ninten);
        }
    }

    private boolean isChoose=false;
    // 从相册选取照片并裁剪
    class selectButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d(TAG,"jinlaile");
            filepath=null;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
//            intent.putExtra("crop", "true");
//            intent.putExtra("scale", "true");
//            intent.putExtra("return-data", "false");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(IMG_PATH, "temp_cropped.jpg")));
//            intent.putExtra("outputFormat",
//                    Bitmap.CompressFormat.JPEG.toString());
//            intent.putExtra("noFaceDetection", true); // no face detection
            isChoose=true;
            startActivityForResult(intent, PHOTO_RESULT);
        }

    }

    // 将图片显示在view中
    public static void showPicture(ImageView iv, Bitmap bmp){
        iv.setImageBitmap(bmp);
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

    /**
     * 获取sd卡的路径
     *
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

    /**
     * 调用系统图片编辑进行裁剪
     */
    public void startPhotoCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMG_PATH, "temp_cropped.jpg")));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, PHOTO_RESULT);
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
