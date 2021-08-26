package com.augurit.agmobile.gzps.journal;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.journal.model.Journal;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;


/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal
 * @createTime 创建时间 ：17/11/4
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/4
 * @modifyMemo 修改备注：
 */
@Deprecated
public class ReadOnlyJounalActivity extends BaseActivity {

    private EditText et_address;
    private EditText et_road;
    private EditText et_content;
    private EditText et_parent_organization;
    private EditText et_direct_organization;
    private EditText et_team;
    private EditText et_user;
    private EditText et_date;

    private Journal journal;
    private EditText et_team_memeber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal);

        journal = (Journal) getIntent().getSerializableExtra("journal");

        initView();

        initData();
    }

    private void initData() {

        if (journal == null) {
            return;
        }

        et_address.setText(journal.getAddr());
        et_road.setText(journal.getRoad());
        et_content.setText(journal.getDescription());
        et_parent_organization.setText(journal.getParentOrgName());
        et_direct_organization.setText(journal.getDirectOrgName());
        et_team.setText(journal.getTeamOrgName());
        et_user.setText(journal.getWriterName());
        et_date.setText(TimeUtil.getStringTimeYMDMChines(new Date(journal.getRecordTime())));
        et_team_memeber.setText(journal.getTeamMember());
    }

    protected void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("日志详情");

        et_address = (EditText) findViewById(R.id.et_address);
        et_road = (EditText) findViewById(R.id.et_road);
        et_content = (EditText) findViewById(R.id.et_content);
        et_parent_organization = (EditText) findViewById(R.id.et_parent_organization);
        et_direct_organization = (EditText) findViewById(R.id.et_direct_organization);
        et_team = (EditText) findViewById(R.id.et_team);
        et_user = (EditText) findViewById(R.id.et_user);
        et_date = (EditText) findViewById(R.id.et_date);
        et_team_memeber = (EditText) findViewById(R.id.et_team_memeber);
        findViewById(R.id.btn_upload_journal).setVisibility(View.GONE);
        findViewById(R.id.ll_team_memeber).setVisibility(View.GONE);
        findViewById(R.id.action_open_photo).setVisibility(View.GONE);

        et_address.setEnabled(false);
        et_road.setEnabled(false);
        et_content.setEnabled(false);
        et_parent_organization.setEnabled(false);
        et_direct_organization.setEnabled(false);
        et_team.setEnabled(false);
        et_user.setEnabled(false);
        et_date.setEnabled(false);
        et_team_memeber.setEnabled(false);
        et_team_memeber.setVisibility(View.VISIBLE);


        findViewById(R.id.ll_organization).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_team).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_parent_organization).setVisibility(View.VISIBLE);
        findViewById(R.id.view_team).setVisibility(View.VISIBLE);
        findViewById(R.id.view_organizaation).setVisibility(View.VISIBLE);
        findViewById(R.id.view_direct_organization).setVisibility(View.VISIBLE);

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
