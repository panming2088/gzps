package com.augurit.qrcodescan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.augurit.qrcodescan.Event.GetQRScanResultEvent;
import com.augurit.qrcodescan.Event.OnNotScanEvent;
import com.augurit.qrcodescan.R;
import com.augurit.qrcodescan.utils.CommonUtil;

import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QRCodeScanActivity extends AppCompatActivity{


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
        setContentView(R.layout.qrscan_activity_main);


        openQrCodeScan =(Button)findViewById(R.id.openQrCodeScan);
        openQrCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开二维码扫描界面
                if(CommonUtil.isCameraCanUse()){
                    Intent intent = new Intent(QRCodeScanActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    Toast.makeText(QRCodeScanActivity.this,"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        text =(EditText)findViewById(R.id.text);
        CreateQrCode =(Button)findViewById(R.id.CreateQrCode);
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
                            Toast.makeText(QRCodeScanActivity.this,"二维码生成成功！",Toast.LENGTH_SHORT).show();
                            QrCode.setImageBitmap(mBitmap);
                        }
                    }else{
                        Toast.makeText(QRCodeScanActivity.this,"文本信息不能为空！",Toast.LENGTH_SHORT).show();
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        QrCode =(ImageView)findViewById(R.id.QrCode);
        qrCodeText=(TextView)findViewById(R.id.qrCodeText);


        //打开二维码扫描界面
        if(CommonUtil.isCameraCanUse()){
            Intent intent = new Intent(QRCodeScanActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }else{
            Toast.makeText(QRCodeScanActivity.this,"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
        }

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
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
          //  qrCodeText.setText(scanResult);

            String str[] = scanResult.split("\n\r");

            int length = str.length;

            List<Map<String,String>> list = new ArrayList<>();
            for(int i=0;i<length;i++){
                Map<String,String> map = new HashMap<>();
                String string = str[i];
                String str2[] = string.split("：");
                if(str2.length ==2) {
                    map.put(str2[0],str2[1]);
                }
                if(!map.isEmpty())
                    list.add(map);
            }

            int len = list.size();
            if(len > 0){
                EventBus.getDefault().post(new GetQRScanResultEvent(list));
            }

            finish();
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onNoScanEvent( OnNotScanEvent event) {
        finish();
    }
}
