package com.augurit.agmobile.patrolcore.common.preview.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

import java.util.ArrayList;
import java.util.List;
/**
 * 描述：
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.preview.view
 * @createTime 创建时间 ：17/8/1
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：17/8/1
 * @modifyMemo 修改备注：
 */
public class PhotoPagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";

	private HackyViewPager mPager;
	private int pagerPosition;
	private int type;
	private TextView indicator;
	private ArrayList<Photo> listPath;
	LinearLayout main_lin;
	private TextView tv_title;

	private int curPosition = 0;
	int current_circle = 0;

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_photo_pager);
//		if(Build.VERSION.SDK_INT>=19){
//			View decorView = getWindow().getDecorView();
//			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//			decorView.setSystemUiVisibility(option);
//			getWindow().setNavigationBarColor(Color.TRANSPARENT);
//			getWindow().setStatusBarColor(Color.TRANSPARENT);
//		}
		listPath = (ArrayList<Photo>) getIntent().getSerializableExtra("BITMAPLIST");
		pagerPosition = getIntent().getIntExtra("POSITION", 0);
		main_lin = (LinearLayout) findViewById(R.id.main_lin);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("图片预览");
		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), listPath);
		mPager.setAdapter(mAdapter);
		findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					finishAfterTransition();
				}else {
					finish();
				}
			}
		});
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageSelected(int i) {
				View v = main_lin.getChildAt(i);
				View cuview = main_lin.getChildAt(current_circle);

				if(v != null && cuview != null){
					ImageView pointView = (ImageView) v;
					ImageView curpointView = (ImageView) cuview;
					curpointView
							.setBackgroundResource(R.drawable.preview_circle_white);
					pointView
							.setBackgroundResource(R.drawable.preview_circle_green);
					current_circle = i;
				}
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
		current_circle = pagerPosition;
		mPager.setCurrentItem(pagerPosition);
		setCircle();
	}

	//设置滚动图片的小圆点
	private void setCircle() {
		if(listPath.size() <= 1){
			return;
		}
		for(int i = 0;i<listPath.size();i++){
			ImageView iv = new ImageView(PhotoPagerActivity.this);
			//循环创建小圆点，判断第一个小圆点为白色的，其他的都是透明的
			if(i == current_circle){
				iv.setBackgroundResource(R.drawable.preview_circle_green);
			}else{
				iv.setBackgroundResource(R.drawable.preview_circle_white);
			}
			main_lin.addView(iv);

			//设置小圆点的margin值
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//			lp.setMargins(2, 10, 2, 10);
			iv.setLayoutParams(lp);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<Photo> fileList;

		public ImagePagerAdapter(FragmentManager fm, List<Photo> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position).getPhotoPath();
			return PhotoPreviewFragment.newInstance(url);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				finishAfterTransition();
			}else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}