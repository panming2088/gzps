package com.augurit.agmobile.mapengine.layermanage.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ParentViewHolder extends TreeNode.BaseNodeViewHolder<String> {

    protected TextView node_value;
    protected TextView node_size;

    protected List<LayerInfo> mAllLayers;
    protected int isShowingNum = 0; //当前正在显示的图层的数量
    protected ViewGroup ll_half_check;

    protected boolean ifAllSelected = false;
    protected boolean ifAllUnselected = false;
    protected boolean ifHalfSelected = false;

    protected LinkedHashMap<Integer,LayerInfo> childLayer = new LinkedHashMap<>();
    protected ViewGroup ll_unselectall;
    protected ViewGroup ll_selectall;
    protected ImageView iv_halfCheck;
    protected View mView;
    private ImageView iv_symbol; //列表项前面的"+"/"-"图标
    private int parentLevel = 0;


    public ParentViewHolder(Context context, List<LayerInfo> members,int level){
        super(context);
        mAllLayers = members;
        parentLevel = level;
    }

    @Override
    public View createNodeView(final TreeNode node, final String value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        mView = getView(inflater);
        View rl_root = mView.findViewById(R.id.rl_root);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rl_root.getLayoutParams();
        layoutParams.leftMargin = 16 * parentLevel;

        iv_symbol = (ImageView) mView.findViewById(R.id.iv_symbol);

        iv_halfCheck = (ImageView) mView.findViewById(R.id.iv_halfcheck);
        ll_unselectall = (ViewGroup) mView.findViewById(R.id.ll_node_unselectall);
        ll_selectall = (ViewGroup) mView.findViewById(R.id.ll_node_selectall);
        ll_half_check = (ViewGroup) mView.findViewById(R.id.ll_node_halfcheck);

        node_value = (TextView) mView.findViewById(R.id.node_value);
        node_size = (TextView) mView.findViewById(R.id.node_size);
        node_size.setVisibility(View.GONE);
        node_value.setText(value);
        node_size.setText(String.valueOf(node.size()));
        ll_unselectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToogleAllCheckOrNot(true);
                node.setSelected(true);
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, true);
                }
            }
        });

        ll_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToogleAllCheckOrNot(false);
                node.setSelected(false);
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, false);
                    n.setSelected(false);
                }
            }
        });

        ll_half_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToogleAllCheckOrNot(true);
                node.setSelected(true);
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, true);
                    n.setSelected(true);
                }
                ll_selectall.setVisibility(View.VISIBLE);
                ll_unselectall.setVisibility(View.GONE);
                ll_half_check.setVisibility(View.GONE);
              //  ll_selectall.performClick();
            }
        });

        //得到一个子图层列表
        List<LayerInfo> temp = new ArrayList<>();
        for (LayerInfo amLayerInfo: mAllLayers){
            if (amLayerInfo.getType() == LayerType.Unsupported){
                continue;
            }
            temp.add(amLayerInfo);
            childLayer.put(amLayerInfo.getLayerId(),amLayerInfo);
        }
        updateAllLayer(temp);
        //更新checkbox的状态
        updateCheckBox();
        return mView;
    }

    protected View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layer_treeview_parentholder, null, false);
    }

    private void updateAllLayer(List<LayerInfo> temp) {
        mAllLayers.clear();
        mAllLayers.addAll(temp);
    }

    /**
     * 更新checkbox的状态
     */
    private void updateCheckBox() {
        isShowingNum =0;
       if (childLayer.size() == 0 ){
           //说明全部不支持加载
           ll_selectall.setVisibility(View.GONE);
           ll_half_check.setVisibility(View.GONE);
           ll_unselectall.setVisibility(View.GONE);
       }else {
           //查看是否是全选
           Set<Map.Entry<Integer, LayerInfo>> entries = childLayer.entrySet();
           for (Map.Entry<Integer, LayerInfo> entry: entries){
                 if (entry.getValue().isDefaultVisible()){
                     isShowingNum ++;
                 }
           }
           if (isShowingNum == childLayer.size()){
               //ll_unselectall.performClick();
               setToogleAllCheckOrNot(true);
               setIfAllSelected(true);
               setIfAllUnselected(false);
               setIfHalfSelected(false);
               mNode.setSelected(true);
           }else  if (isShowingNum == 0){
               //ll_selectall.performClick();
               setToogleAllCheckOrNot(false);
               setIfAllUnselected(true);
               setIfAllSelected(false);
               setIfHalfSelected(false);
               mNode.setSelected(false);
           }else {
               setToggleHalfCheck();
               setIfHalfSelected(true);
               setIfAllSelected(false);
               setIfAllUnselected(false);
               //mNode.setSelected(false);
           }
       }
    }


    /**
     * 设置此时是全选还是全不选
     * @param ifCheckAll
     */
    public void setToogleAllCheckOrNot(boolean ifCheckAll){
        if (ifCheckAll){
            ll_selectall.setVisibility(View.VISIBLE);
            ll_unselectall.setVisibility(View.GONE);
            ll_half_check.setVisibility(View.GONE);
        }else {
            ll_selectall.setVisibility(View.GONE);
            ll_unselectall.setVisibility(View.VISIBLE);
            ll_half_check.setVisibility(View.GONE);
        }
    }

    /**
     * 显示部分选择的图标
     */
    private void setToggleHalfCheck(){
        ll_selectall.setVisibility(View.GONE);
        ll_unselectall.setVisibility(View.GONE);
        ll_half_check.setVisibility(View.VISIBLE);
    }


    public void notifyForMemberStateChange(int layerId,boolean ifShow){
        if (childLayer.get(layerId) != null){
            childLayer.get(layerId).setDefaultVisibility(ifShow);
            updateCheckBox();
        }
    }

    /**
     * 可选状态或选中状态发生改变时回调此方法，
     * 可以此方法中调用TreeNode.isSelected()方法判断节点的选中状态，
     * 然后自定义选中状态发生改变后的动作
     * @param editModeEnabled
     */
    public void toggleSelectionMode(boolean editModeEnabled) {
        if (editModeEnabled){
            if (isIfHalfSelected()){
                return;
            }
            if (mNode.isSelected()){
                ll_unselectall.performClick(); //执行全选
            }else {
                ll_selectall.performClick(); //全不选
            }
        }
    }

    public boolean isIfAllSelected() {
        return ifAllSelected;
    }

    public void setIfAllSelected(boolean ifAllSelected) {
        this.ifAllSelected = ifAllSelected;
    }

    public boolean isIfAllUnselected() {
        return ifAllUnselected;
    }

    public void setIfAllUnselected(boolean ifAllUnselected) {
        this.ifAllUnselected = ifAllUnselected;
    }

    public boolean isIfHalfSelected() {
        return ifHalfSelected;
    }

    public void setIfHalfSelected(boolean ifHalfSelected) {
        this.ifHalfSelected = ifHalfSelected;
    }


    @Override
    public void toggle(boolean active) {
        super.toggle(active);
        if (active){
            iv_symbol.setImageResource(R.mipmap.common_ic_minus);
        }else {
            iv_symbol.setImageResource(R.mipmap.common_ic_plus);
        }
    }
}
