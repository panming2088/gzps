package com.augurit.qrcodescan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augurit.qrcodescan.R;
import com.augurit.qrcodescan.utils.CommonUtil;
import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;


public class QRCodeWebViewActivity extends AppCompatActivity{


    Button openQrCodeScan;

    EditText text;

    Button CreateQrCode;

    ImageView QrCode;

    TextView qrCodeText;

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscan_activity_webview_main);

        /*
        openQrCodeScan =(Button)findViewById(R.id.openQrCodeScan);
        openQrCodeScan.setVisibility(View.GONE);
        openQrCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开二维码扫描界面
                if(CommonUtil.isCameraCanUse()){
                    Intent intent = new Intent(QRCodeWebViewActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    Toast.makeText(QRCodeWebViewActivity.this,"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        text =(EditText)findViewById(R.id.text);
        CreateQrCode =(Button)findViewById(R.id.CreateQrCode);
        CreateQrCode.setVisibility(View.GONE);
        CreateQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //获取输入的文本信息
                    String str = text.getText().toString().trim();
                    if(str != null && !"".equals(str.trim())){
                        //根据输入的文本生成对应的二维码并且显示出来
                        Bitmap mBitmap = EncodingHandler.createQRCode(text.getText().toString(), 500);
                        if(mBitmap != null){
                            Toast.makeText(QRCodeWebViewActivity.this,"二维码生成成功！",Toast.LENGTH_SHORT).show();
                            QrCode.setImageBitmap(mBitmap);
                        }
                    }else{
                        Toast.makeText(QRCodeWebViewActivity.this,"文本信息不能为空！",Toast.LENGTH_SHORT).show();
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        QrCode =(ImageView)findViewById(R.id.QrCode);
        qrCodeText=(TextView)findViewById(R.id.qrCodeText);
        */

        //打开二维码扫描界面
        if(CommonUtil.isCameraCanUse()){
            Intent intent = new Intent(QRCodeWebViewActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }else{
            Toast.makeText(QRCodeWebViewActivity.this,"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
      //      qrCodeText.setText(scanResult);

            if(scanResult != null){
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(scanResult);
                intent.setData(content_url);
                startActivity(intent);

//                Intent intent = new Intent(this,WebViewActivity.class);
//                intent.putExtra("url",scanResult);
//                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this,"扫描失败!",Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            finish();   // 没有结果则退出
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
