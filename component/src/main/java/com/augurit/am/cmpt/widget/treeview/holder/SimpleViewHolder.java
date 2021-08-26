package com.augurit.am.cmpt.widget.treeview.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.augurit.am.cmpt.widget.treeview.model.TreeNode;

/**
 *
 * 包名：com.augurit.am.cmpt.widget.treeview.holder
 * 文件描述：树形Listview中的Item视图
 * 创建人：liangshenghong
 * 创建时间：2016-08-31 9:28
 * 修改人：xuciluan
 * 修改时间：2016-08-31 9:28
 * 修改备注：
 * @version
 *
 */
public class SimpleViewHolder extends TreeNode.BaseNodeViewHolder<Object> {

    public SimpleViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, Object value) {
        final TextView tv = new TextView(context);
        tv.setText(String.valueOf(value));
        return tv;
    }

    @Override
    public void toggle(boolean active) {

    }
}
