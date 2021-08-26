package com.augurit.agmobile.mapengine.edit.service;

import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.augurit.agmobile.mapengine.edit.model.AttributeItem;
import com.augurit.agmobile.mapengine.edit.util.DateFormatUtils;
import com.augurit.agmobile.mapengine.edit.util.FeatureLayerUtils;
import com.augurit.agmobile.mapengine.edit.view.EditDataCallback;
import com.augurit.am.fw.utils.log.LogUtil;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.am.map.arcgis.edit.model
 * @createTime 创建时间 ：16/11/22
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/22
 */

public class EditAttrDataService  implements IEditAttrDataService{

    //几何编辑 ->删除要素
    @Override
    public void deleteFeature(ArcGISFeatureLayer featureLayer, Object featureId, EditDataCallback callback){
        Map<String, Object> attrs = new HashMap<>();
        boolean updateMapLayer = true;
        attrs.put(featureLayer.getObjectIdField(), featureId);
        Graphic oldGraphic = new Graphic(null, null, attrs);
        featureLayer.applyEdits(null, new Graphic[]{oldGraphic}, null, deleteEditCallbackListener(featureLayer,callback));
    }

    //删除要素的回调监听器
    public CallbackListener<FeatureEditResult[][]> deleteEditCallbackListener(final ArcGISFeatureLayer featureLayer,final EditDataCallback callback) {
        return new CallbackListener<FeatureEditResult[][]>() {
            @Override
            public void onCallback(FeatureEditResult[][] result) {
                if (result[1] != null && result[1][0] != null && result[1][0].isSuccess()) {
                    //    Log.d("Editing:", "Success deleting feature with id=" + result[1][0].getObjectId());
                    LogUtil.i("Success deleting feature with id=" + result[1][0].getObjectId());
               //     AbsAttrFeatureDialogFragment.this.dismiss();
               //     Toast.makeText(mContext,"成功删除要素"+result[1][0].getObjectId(),Toast.LENGTH_LONG).show();
                    callback.onSuccess(result);
                }
                featureLayer.refresh();

            }

            @Override
            public void onError(Throwable e) {
                Log.d("Editing:", "Fail deleting feature :" + e.getLocalizedMessage());
                callback.onFail(e);
            }
        };

    }

    /**
     * 提交属性编辑
     * @param featureLayer
     * @param listAdapter
     */
    @Override
    public void updateFeature(ArcGISFeatureLayer featureLayer, BaseAdapter listAdapter, FeatureSet featureSet, EditDataCallback callback){
        boolean isTypeField = false;
        boolean hasEdits = false;
        boolean updateMapLayer = false;
        Graphic newGraphic;
        Map<String, Object> attrs = new HashMap<>();

        //遍历当前点的属性,如果其属性值发生改变了就设置新值给它
        for (int i = 0; i < listAdapter.getCount(); i++) {
            AttributeItem item = (AttributeItem) listAdapter.getItem(i);
            String value;

            //属性值对应的视图项是否有创建
            if (item.getView() != null) {

                //判断字段属性和其对应的视图
                if (item.getField().getName().equals(featureLayer.getTypeIdField())) {
                    Spinner spinner = (Spinner) item.getView();
                    // 获取该字段属性值
                    String typeName = spinner.getSelectedItem().toString();
                    value = FeatureLayerUtils.returnTypeIdFromTypeName(featureLayer.getTypes(), typeName);

                    //由于该属性改变对应的属性符号也会发生改变,所以要记录以便更新该图层
                    isTypeField = true;

                } else if (FeatureLayerUtils.FieldType.determineFieldType(item.getField()) == FeatureLayerUtils.FieldType.DATE) {
                    // date 日期
                    Button dateButton = (Button) item.getView();
                    value = dateButton.getText().toString();
                } else {
                    // edit text
                    EditText editText = (EditText) item.getView();
                    value = editText.getText().toString();
                }

                // try to set the attribute value on the graphic and see if it has
                // been changed
                boolean hasChanged = FeatureLayerUtils.setAttribute(attrs, featureSet.getGraphics()[0],
                        item.getField(), value, DateFormatUtils.formatter);

                // if a value has for this field, log this and set the hasEdits
                // boolean to true
                if (hasChanged) {
                    //      Log.d(TAG, "Change found for field=" + item.getField().getName() + " value = " + value
                    //              + " applyEdits() will be called");
                    hasEdits = true;

                    // If the change was from a Type field then set the dynamic map
                    // service to update when the edits have been applied, as the
                    // renderer of the feature will likely change
                    if (isTypeField) {
                        updateMapLayer = true;
                    }
                }

                // check if this was a type field, if so set boolean back to false
                // for next field
                if (isTypeField) {
                    isTypeField = false;
                }
            }
        }

        // check there have been some edits before applying the changes
        if (hasEdits) {
            // set objectID field value from graphic held in the featureset
            attrs.put(featureLayer.getObjectIdField(), featureSet.getGraphics()[0].getAttributeValue(featureLayer.getObjectIdField()));
            newGraphic = new Graphic(null, null, attrs);
            featureLayer.applyEdits(null, null, new Graphic[]{newGraphic}, updateEditCallbackListener(updateMapLayer,featureLayer,callback));
        }

     //   Toast.makeText(mContext, "编辑已保存并且上传到服务!", Toast.LENGTH_LONG).show();
        //   mHandler.sendEmptyMessage(HIDE_EDIT_DIALOG);
   //     AbsAttrFeatureDialogFragment.this.dismiss();

    }

    //编辑提交更新到服务的回调监听器
    public CallbackListener<FeatureEditResult[][]> updateEditCallbackListener(final boolean updateLayer, final ArcGISFeatureLayer featureLayer, final EditDataCallback callback) {
        return new CallbackListener<FeatureEditResult[][]>() {
            public void onCallback(FeatureEditResult[][] result) {
                // check the response for success or failure
                if (result[2] != null && result[2][0] != null && result[2][0].isSuccess()) {
                    Log.d("Editing:", "Success updating feature with id=" + result[2][0].getObjectId());
                    // see if we want to update the dynamic layer to get new symbols for
                    // updated features
                    if (updateLayer) {
                        featureLayer.refresh();
                        //   editListener.OnEditSuccess();
                    }
                    callback.onSuccess(result);

                }
            }

            public void onError(Throwable e) {
                Log.d("Update ", "error updating feature: " + e.getLocalizedMessage());
                //  editListener.OnEditFail();
                callback.onFail(e);
            }
        };
    }

/*
    public interface EditDataCallback{
        void onSuccess(FeatureEditResult[][] result);
        void onFail(Throwable e);
    }
    */
}
