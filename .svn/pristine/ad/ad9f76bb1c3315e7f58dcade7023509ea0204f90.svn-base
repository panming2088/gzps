package com.augurit.agmobile.patrolcore.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.CandidateContainer;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.ListAttributeCalloutManager;

import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.common.selectcomponent
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class ComponentAttributeListActivity extends AppCompatActivity {

    public static Map<String, Object> attributes = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = CandidateContainer.getMoreAttributesView(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attributes != null){
                    attributes.clear();
                }
                attributes = null;
                finish();
            }
        }, null);
        setContentView(contentView);

//        Map<String, Object> attributes = (Map<String, Object>) getIntent().getSerializableExtra(ListAttributeCalloutManager.INTENT_ATTRIBUTE_KEY);
        String title = getIntent().getStringExtra(ListAttributeCalloutManager.INTENT_FIELDNAME_KEY);
        ComponentAttributeListAdapter adapter = new ComponentAttributeListAdapter(this, attributes);

        CandidateContainer.setMoreDetailData(title, adapter, contentView);
    }

    @Override
    public void onBackPressed(){
        if(attributes != null){
            attributes.clear();
        }
        attributes = null;
        finish();
    }
}