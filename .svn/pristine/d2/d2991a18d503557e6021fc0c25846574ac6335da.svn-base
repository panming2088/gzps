package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.mapengine.location.ILocationManager;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.model.OnSelectLocationFinishEvent;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.am.cmpt.loc.WGS84LocationTransform;
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

public class SelectLocationFragment extends Fragment {

    private LatLng mDestinationOrLastTimeSelectLocation;
    private String mLastSelectedAddress;
    private ISelectLocationView selectLocationView;
    private LegendPresenter legendPresenter;


    public static SelectLocationFragment newInstance(boolean ifReadOnly, LatLng location, double initScale, String destination) {

        SelectLocationFragment selectLocationFragment = new SelectLocationFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(SelectLocationConstant.IF_READ_ONLY, ifReadOnly);
        if (location != null) {
            bundle.putParcelable(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION, location);
        }
        bundle.putDouble(SelectLocationConstant.INITIAL_SCALE, initScale);
        if (destination != null) {
            bundle.putString(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS, destination);
        }

        selectLocationFragment.setArguments(bundle);
        return selectLocationFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_select_location, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //是否允许编辑位置,默认允许
        boolean mIfReadOnly = getArguments().getBoolean(SelectLocationConstant.IF_READ_ONLY, false);
        mDestinationOrLastTimeSelectLocation = getArguments().getParcelable(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION);
        mLastSelectedAddress = getArguments().getString(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS);
        double mInitialScale = getArguments().getDouble(SelectLocationConstant.INITIAL_SCALE, -1);


        ViewGroup ll_select_location_root_container =
                (ViewGroup) view.findViewById(R.id.ll_select_location_root_container);

        ILocationManager mLocationManager = new PatrolLocationManager(getActivity(), null);
        mLocationManager.setCoordinateSystem(new WGS84LocationTransform());
        PatrolLocationManager.setDefaultMinDistance(0);
        PatrolLocationManager.setDefaultMinTime(1000);

        selectLocationView = new SelectLocationVIew(getActivity(), ll_select_location_root_container, mLocationManager);
        selectLocationView.setDestinationOrLastTimeSelectLocation(mDestinationOrLastTimeSelectLocation);
        selectLocationView.setIfReadOnly(mIfReadOnly);
        selectLocationView.setInitialScale(mInitialScale);
        selectLocationView.setLastSelectedAddress(mLastSelectedAddress);
        selectLocationView.setFinishSelectLocationListener(new IFinishSelectLocationListener() {
            @Override
            public void onFinish(OnSelectLocationFinishEvent onSelectLocationFinishEvent) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_from_lefthide_to_rightshow, R.anim.slide_from_leftshow_to_righthide);
            }
        });
        ISelectLocationPresenter selectLocationPresenter =
                new SelectLocationPresenter(new SelectLocationService(getActivity(), Locator.createOnlineLocator()), selectLocationView);

        selectLocationPresenter.loadMap();


        //图层按钮
        view.findViewById(R.id.btn_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof IDrawerController) {
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
     *
     * @param container
     */
    public void showLayerList(ViewGroup container) {
        if (selectLocationView != null) {
            selectLocationView.showLayerList(container);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LayersService.clearInstance();
    }
}
