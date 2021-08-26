package com.augurit.agmobile.gzps.common.editmap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.editmap.EditLineFeatureFragment3;
import com.augurit.agmobile.patrolcore.editmap.EditLineReadStateMapListener;
import com.augurit.agmobile.patrolcore.editmap.OnGraphicChangedListener;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;


/**
 * 新增或编辑管线界面
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public class BottomsheetEditLineFeatureFragment4 extends EditLineFeatureFragment3 {


    private GridView gridView;
    private LayerAdapterWithoutPipeline layerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_editmapfeature4, null);
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        gridView = (GridView) view.findViewById(com.augurit.agmobile.gzps.R.id.gridview);
        layerAdapter = new LayerAdapterWithoutPipeline(getActivity());
        gridView.setAdapter(layerAdapter);
        layerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String selectedData) {
                if (editLineReEditStateMapListener != null && editLineReEditStateMapListener instanceof BottomSheetEditLineReEditStateMapListener) {
                    ((BottomSheetEditLineReEditStateMapListener) editLineReEditStateMapListener).setCurrComponentUrl(selectedData);
                }

                if (editLineEditStateMapListener != null && editLineEditStateMapListener instanceof BottomsheetEditLineEditStateMapListener) {
                    ((BottomsheetEditLineEditStateMapListener) editLineEditStateMapListener).setCurrComponentUrl(selectedData);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, String selectedData) {

            }
        });

    }


    /**
     * @param editMode 编辑线还是点；
     * @return
     */
    public static BottomsheetEditLineFeatureFragment4 newInstance(String editMode, Geometry geometry, double initScale, boolean ifReadOnly, String address) {

        BottomsheetEditLineFeatureFragment4 selectLocationFragment = new BottomsheetEditLineFeatureFragment4();
        Bundle bundle = new Bundle();
        bundle.putString("mode", editMode);
        bundle.putSerializable("geometry", geometry);
        bundle.putBoolean("read", ifReadOnly);
        bundle.putDouble("scale", initScale);
        bundle.putString("address", address);
        selectLocationFragment.setArguments(bundle);
        return selectLocationFragment;
    }

    @Override
    protected void initMapListener() {
        if (ifReadOnly) {
            /**
             * 只读模式
             */
            EditLineReadStateMapListener editLineReadStateMapListener = new EditLineReadStateMapListener(mapView, getActivity(), (Polyline) geometry, initScale);
            mapView.setOnSingleTapListener(editLineReadStateMapListener);
        } else if (geometry != null) {
            /**
             * 再次编辑模式
             */
            editLineReEditStateMapListener = new BottomSheetEditLineReEditStateMapListener(getActivity(), mapView, (Polyline) geometry, initScale, ll_component_list);
            editLineReEditStateMapListener.setOnGraphicChangedListener(new OnGraphicChangedListener() {
                @Override
                public void onGraphicChanged(Graphic graphic) {
                    BottomsheetEditLineFeatureFragment4.this.graphic = graphic;
                    if (graphic.getGeometry().getType() == Geometry.Type.POLYLINE) {
                        btn_sure.setVisibility(View.VISIBLE);
                    } else {
                        btn_sure.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAddressChanged(DetailAddress detailAddress) {
                    mCurrentAddress = detailAddress;
                    tv_address.setText("当前位置：" + detailAddress.getDetailAddress());
                }

                @Override
                public void onGraphicClear() {

                }
            });
            mapView.setOnTouchListener(editLineReEditStateMapListener);
        } else {
            /**
             * 编辑模式
             */
            editLineEditStateMapListener = new BottomsheetEditLineEditStateMapListener(mapView, getActivity(), initScale, ll_component_list);
            editLineEditStateMapListener.setOnGraphicChangedListener(new OnGraphicChangedListener() {
                @Override
                public void onGraphicChanged(Graphic graphic) {
                    BottomsheetEditLineFeatureFragment4.this.graphic = graphic;
                    if (graphic.getGeometry().getType() == Geometry.Type.POLYLINE) {
                        btn_sure.setVisibility(View.VISIBLE);
                    } else {
                        btn_sure.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAddressChanged(DetailAddress detailAddress) {
                    mCurrentAddress = detailAddress;
                    tv_address.setText("当前位置：" + detailAddress.getDetailAddress());
                }


                @Override
                public void onGraphicClear() {
                    btn_sure.setVisibility(View.GONE);
                }
            });
            mapView.setOnSingleTapListener(editLineEditStateMapListener);
        }



//        if (editLineReEditStateMapListener != null && editLineReEditStateMapListener instanceof BottomSheetEditLineReEditStateMapListener) {
//            ((BottomSheetEditLineReEditStateMapListener) editLineReEditStateMapListener).setCurrComponentUrl(LayerUrlConstant.newComponentUrls[0]);
//        }
//
//        if (editLineEditStateMapListener != null && editLineEditStateMapListener instanceof BottomsheetEditLineEditStateMapListener) {
//            ((BottomsheetEditLineEditStateMapListener) editLineEditStateMapListener).setCurrComponentUrl(LayerUrlConstant.newComponentUrls[0]);
//        }
    }
}