package com.augurit.agmobile.patrolcore.editmap;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.mapengine.location.ILocationManager;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.model.OnSelectLocationFinishEvent;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.agmobile.patrolcore.selectlocation.view.IFinishSelectLocationListener;
import com.augurit.am.cmpt.loc.WGS84LocationTransform;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.geocode.Locator;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/8/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/1
 * @modifyMemo 修改备注：
 */

public class EditPointFragment extends Fragment {

   // private String editMode;
    private Geometry geometry;
    private boolean ifReadOnly = false;
    private double initScale;
    private String mCurrentAddress;
    private EditMapFeatureView editMapFeatureView;
    private LegendPresenter legendPresenter;

    /**
     *
     * @return
     */
    public static EditPointFragment newInstance(Geometry geometry, double initScale , boolean ifReadOnly, String address) {

        EditPointFragment selectLocationFragment = new EditPointFragment();
        Bundle bundle = new Bundle();
       // bundle.putString("mode", editMode);
        bundle.putSerializable("geometry", geometry);
        bundle.putBoolean("read", ifReadOnly);
        bundle.putDouble("scale", initScale);
        bundle.putString("address",address);
        selectLocationFragment.setArguments(bundle);
        return selectLocationFragment;
    }


    private void getBundleData() {
        //editMode = getArguments().getString("mode");
        geometry = (Geometry) getArguments().getSerializable("geometry");
        ifReadOnly = getArguments().getBoolean("read");
        initScale = getArguments().getDouble("scale");
        mCurrentAddress = getArguments().getString("address");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在配置变化的时候将这个fragment保存下来
         setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(),R.layout.fragment_select_location,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();

        ViewGroup ll_select_location_root_container =
                (ViewGroup) view.findViewById(R.id.ll_select_location_root_container);

        ILocationManager mLocationManager = new PatrolLocationManager(getActivity(),null);
        mLocationManager.setCoordinateSystem(new WGS84LocationTransform());
        PatrolLocationManager.setDefaultMinDistance(0);
        PatrolLocationManager.setDefaultMinTime(1000);

        editMapFeatureView = new EditMapFeatureView(getActivity(),ll_select_location_root_container,mLocationManager);
        editMapFeatureView.setDestinationOrLastTimeSelectLocation(geometry);
        editMapFeatureView.setIfReadOnly(ifReadOnly);
        editMapFeatureView.setInitialScale(initScale);
        editMapFeatureView.setLastSelectedAddress(mCurrentAddress);
        final IEditMapFeaturePresenter selectLocationPresenter =
                new EditMapFeaturePresenter(new SelectLocationService(getActivity(), Locator.createOnlineLocator()), editMapFeatureView);

        selectLocationPresenter.loadMap();
        editMapFeatureView.setFinishSelectLocationListener(new IFinishSelectLocationListener() {
            @Override
            public void onFinish(OnSelectLocationFinishEvent onSelectLocationFinishEvent) {
                selectLocationPresenter.clearInstance();
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_from_lefthide_to_rightshow,R.anim.slide_from_leftshow_to_righthide);
            }
        });



        //图层按钮
        view.findViewById(R.id.btn_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof IDrawerController){
                    final IDrawerController drawerController = (IDrawerController) activity;
                    drawerController.openDrawer(new IDrawerController.OnDrawerOpenListener() {
                        @Override
                        public void onOpened(View drawerView) {
                            showLayerList(drawerController.getDrawerContainer());
                        }
                    });
                }
            }
        });
        /**
         * 图例
         */
        View ll_legend = view.findViewById(R.id.ll_legend);
        ll_legend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLegendPresenter();
                legendPresenter.showLegends();
            }
        });
    }


    private void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(getActivity());
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(getActivity()));
        }

    }

    /**
     * 显示图层列表
     * @param container
     */
    public void showLayerList(ViewGroup container){
        if (editMapFeatureView != null){
            editMapFeatureView.showLayerList(container);
        }
    }
}
