package com.augurit.agmobile.mapengine.common.widget.callout.attribute;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.augurit.agmobile.mapengine.common.widget.callout.SimpleListAdapter;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.widget
 * @createTime 创建时间 ：2016-11-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-18
 */

public class AttributeListActivity extends AppCompatActivity {

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
        SimpleListAdapter adapter = new SimpleListAdapter(this, attributes);

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

