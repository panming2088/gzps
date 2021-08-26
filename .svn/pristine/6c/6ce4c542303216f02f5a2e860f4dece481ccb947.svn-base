package com.augurit.agmobile.patrolcore.common.preview.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.augurit.agmobile.patrolcore.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

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
public class PhotoPreviewFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static PhotoPreviewFragment newInstance(String imageUrl) {
		final PhotoPreviewFragment f = new PhotoPreviewFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.preview_photo_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().onBackPressed();
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		progressBar.setVisibility(View.VISIBLE);
		Glide.with(getActivity()).load(mImageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(new GlideDrawableImageViewTarget(mImageView){

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
				super.onResourceReady(resource, animation);
				mAttacher.update();
				progressBar.setVisibility(View.GONE);
			}
		});
	}

}
