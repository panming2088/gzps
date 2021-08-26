package me.nereo.multi_image_selector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.adapter.ImageFolderAdapter;
import me.nereo.multi_image_selector.adapter.ImageGridAdapter;
import me.nereo.multi_image_selector.bean.Folder;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.utils.FileUtils;
import me.nereo.multi_image_selector.utils.ScreenUtils;
import me.nereo.multi_image_selector.utils.TimeUtils;

/**
 * Multi image selector Fragment
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/5/18.
 */
public class MultiImageSelectorFragment extends Fragment {

    public static final String TAG = "MultiImageSelectorFragment";

    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110;
    private static final int REQUEST_CAMERA = 100;

    private static final String KEY_TEMP_FILE = "key_temp_file";

    // Single choice
    public static final int MODE_SINGLE = 0;
    // Multi choice
    public static final int MODE_MULTI = 1;

    /**
     * Max image size，int，
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * Select mode，{@link #MODE_MULTI} by default
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * Whether show camera，true by default
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * Original data set
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    // loaders
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    // image result data set
    private ArrayList<Image> resultList = new ArrayList<>();
    // folder result data set
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    private GridView mGridView;
    private Callback mCallback;

    private ImageGridAdapter mImageAdapter;
    private ImageFolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;
    private ListPopupWindow mSortPopupWindow;
    private TextView mCategoryText,sort_btn;
    private View mPopupAnchorView;

    private boolean hasFolderGened = false;

    private File mTmpFile;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mis_fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int mode = selectMode();
//        if (mode == MODE_MULTI) {
//            Image image;
//            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
//            if (tmp != null && tmp.size() > 0) {
//                for (int i = 0; i < tmp.size(); i++) {
//                    image = FileUtils.registOpenPhotoButton(getContext(), tmp.get(i));
//                    resultList.add(image);
//                }
//            }
//        }
        mImageAdapter = new ImageGridAdapter(getActivity(), showCamera(), 3);
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);

        mPopupAnchorView = view.findViewById(R.id.footer);

        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        sort_btn = (TextView) view.findViewById(R.id.sort_btn);
        sort_btn.setText("时间倒序");
        mCategoryText.setText(R.string.mis_folder_all);
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        sort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mSortPopupWindow == null){
                    createSortPopuMenu();
                }

                if (mSortPopupWindow.isShowing()) {
                    mSortPopupWindow.dismiss();
                } else {
                    mSortPopupWindow.show();
                    int index =mSortPopupWindow.getSelectedItemPosition();
                    index = index == 0 ? index : index - 1;
                    mSortPopupWindow.getListView().setSelection(index);
                }
            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    if (i == 0) {
                        showCameraAction();
                    } else {
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image, mode);
                    }
                } else {
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_FLING) {
//                    Glide.with(view.getContext()).pauseRequests();
////                    Picasso.with(view.getContext()).pauseTag(TAG);
//                } else {
//                    Glide.with(view.getContext()).resumeRequests();
////                    Picasso.with(view.getContext()).resumeTag(TAG);
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mFolderAdapter = new ImageFolderAdapter(getActivity(),null);
    }


    private void  createSortPopuMenu(){
        Point point = ScreenUtils.getScreenSize(getActivity());
        int width = point.x/3;
        int height = (int) (point.y * (4.5f/8.0f));
        final String srtArray[] = new String[]{"名称顺序","名称倒序","大小顺序","大小倒序","时间顺序","时间倒序"};
        mSortPopupWindow = new ListPopupWindow(getActivity());
        mSortPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mSortPopupWindow.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.mis_sort_item,R.id.sort_tv,srtArray));
        mSortPopupWindow.setContentWidth(width);
        mSortPopupWindow.setWidth(width);
        mSortPopupWindow.setHeight(height);
        mSortPopupWindow.setDropDownGravity(Gravity.CENTER_VERTICAL);
        mSortPopupWindow.setAnchorView(sort_btn);
        mSortPopupWindow.setModal(true);
        mSortPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                List<Image> images = mImageAdapter.getImages();
                Collections.sort(images, new Comparator<Image>() {
                    @Override
                    public int compare(Image o1, Image o2) {
                        if(i==0){
                            return o1.getFilePath().substring(o1.getFilePath().lastIndexOf("/")+1,o1.getFilePath().length()).compareTo(o2.getFilePath().substring(o2.getFilePath().lastIndexOf("/")+1,o2.getFilePath().length()));
                        }else if(i == 1){
                            return o2.getFilePath().substring(o2.getFilePath().lastIndexOf("/")+1,o2.getFilePath().length()).compareTo(o1.getFilePath().substring(o1.getFilePath().lastIndexOf("/")+1,o1.getFilePath().length()));
                        }else if(i==2){
                            return (int) (o2.size - o1.size);
                        }else if(i==3){
                            return (int) (o1.size - o2.size);
                        }else if(i==4){
                            return (int) (o1.getPhotoTime().getTime() - o2.getPhotoTime().getTime());
                        }else if(i==5){
                            return (int) (o2.getPhotoTime().getTime() - o1.getPhotoTime().getTime());
                        }
                        return 0;
                    }
                });
                sort_btn.setText(srtArray[i]);
                mSortPopupWindow.dismiss();
                mImageAdapter.notifyDataSetChanged();
            }
        });

    }
    /**
     * Create popup ListView
     */
    private void createPopupFolderList() {
        Point point = ScreenUtils.getScreenSize(getActivity());
        int width = point.x;
        int height = (int) (point.y * (4.5f / 8.0f));
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText(R.string.mis_folder_all);
                            if (showCamera()) {
                                mImageAdapter.setShowCamera(true);
                            } else {
                                mImageAdapter.setShowCamera(false);
                            }
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                mCategoryText.setText(folder.name);
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }

                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_TEMP_FILE, mTmpFile);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mTmpFile = (File) savedInstanceState.getSerializable(KEY_TEMP_FILE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // load image data
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            } else {
                // delete tmp file
                while (mTmpFile != null && mTmpFile.exists()) {
                    boolean success = mTmpFile.delete();
                    if (success) {
                        mTmpFile = null;
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Open camera
     */
    private void showCameraAction() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale_write_storage),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                try {
                    mTmpFile = FileUtils.createTmpFile(getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mTmpFile != null && mTmpFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(getActivity(), R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_WRITE_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * notify callback
     *
     * @param image image data
     */
    private void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            if (mode == MODE_MULTI) {
                if (resultList.contains(image)) {
                    resultList.remove(image);
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image);
                    }
                } else {
                    if (selectImageCount() == resultList.size()) {
                        Toast.makeText(getActivity(), R.string.mis_msg_amount_limit, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    resultList.add(image);
                    if (mCallback != null) {
                        mCallback.onImageSelected(image);
                    }
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE) {
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image);
                }
            }
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = null;
            if (id == LOADER_ALL) {
                String excludePath = "/layer/layer";
//                cursorLoader = new CursorLoader(getActivity(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
////                        IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? ",
////                        "(" + IMAGE_PROJECTION[0] + "  like '%Pictures%' or " + IMAGE_PROJECTION[0] + " like '%DCIM%')and (" + IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? )",
//                        "(" +IMAGE_PROJECTION[0] + " not like '%/layer/layer%' )and (" +IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? )",
//                        new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                cursorLoader = new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " not like '%"+excludePath+"%'", null, IMAGE_PROJECTION[2] + " DESC");
            } else if (id == LOADER_CATEGORY) {
//                cursorLoader = new CursorLoader(getActivity(),
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
//                        IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
//                        null, IMAGE_PROJECTION[2] + " DESC");
                cursorLoader = new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
            }
            return cursorLoader;
        }

        private boolean fileExist(String path) {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (!file.exists() || file.length() <= 0) {
                    return false;
                }else{
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                if (data.getCount() > 0) {
                    List<Image> images = new ArrayList<>();
                    data.moveToFirst();
                    Date photoTime = new Date();
                    String dateString = null;
                    String path = null;
                    String name = null;
                    long dateTime = 0l;
                    Date date;
                    File mImageFile;
                    Image image = null;
                    int id = 0;

                    File folderFile = null;
                    File f1 = null;

                    String fp = null;
                    Folder f = null;
                    Folder folder = null;
                    List<Image> imageList = null;
                    long size  = 0l;
                    do {
                        dateString = TimeUtils.getStringTimeYYMMDDSS(photoTime);
                        path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        //todo
                        name = dateString + "_img.jpg";
//                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        date = new Date(dateTime);
                        size = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
//                         mImageFile = new File(path);
//                        id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                        //Android 7.0采用了StrictMode API政策,私有目录被限制访问
                        //判断Android版本是否是Android7.0以上
//                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//                            bitmapDataUri= FileProvider.getUriForFile(getContext(),getContext().getPackageName()+".FileProvider",mImageFile);
//                        }else{
//                            bitmapDataUri=Uri.fromFile(mImageFile);
//                        }
                        if (!fileExist(path) ) {
                            continue;
                        }
                        if (!TextUtils.isEmpty(name)) {
                            image = new Image(name, path, date,size);
                            images.add(image);
                        }
                        if (!hasFolderGened) {
                            // get all folder data
                            f1 = new File(path);
                            folderFile = f1.getParentFile();
                            if (folderFile != null && folderFile.exists()) {
                                fp = folderFile.getAbsolutePath();
                                f = getFolderByPath(fp);
                                if (f == null) {
                                    folder = new Folder();
                                    folder.name = folderFile.getName();
                                    folder.path = fp;
                                    folder.cover = image;
                                    imageList = new ArrayList<>();
                                    imageList.add(image);
                                    folder.images = imageList;
                                    mResultFolder.add(folder);
                                } else {
                                    f.images.add(image);
                                }
                            }
                        }
                    } while (data.moveToNext());
                    mImageAdapter.setData(images);
                    if (resultList != null && resultList.size() > 0) {
                        mImageAdapter.setDefaultSelected(resultList);
                    }
                    if (!hasFolderGened) {
                        mFolderAdapter.refreshData(mResultFolder);
                        hasFolderGened = true;
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private Folder getFolderByPath(String path) {
        if (mResultFolder != null) {
            for (Folder folder : mResultFolder) {
                if (TextUtils.equals(folder.path, path)) {
                    return folder;
                }
            }
        }
        return null;
    }

    private boolean showCamera() {
        return getArguments() == null || getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
    }

    private int selectMode() {
        return getArguments() == null ? MODE_MULTI : getArguments().getInt(EXTRA_SELECT_MODE);
    }

    private int selectImageCount() {
        return getArguments() == null ? 9 : getArguments().getInt(EXTRA_SELECT_COUNT);
    }

    /**
     * Callback for host activity
     */
    public interface Callback {
        void onSingleImageSelected(Image image);

        void onImageSelected(Image image);

        void onImageUnselected(Image image);

        void onCameraShot(File imageFile);
    }
}
