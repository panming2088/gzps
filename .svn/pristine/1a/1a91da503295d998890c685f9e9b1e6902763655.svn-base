package com.augurit.am.cmpt.widget.treeview.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.widget.treeview.view.AndroidTreeView;
import com.augurit.am.cmpt.widget.treeview.view.TreeNodeWrapperView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bogdan Melnychuk on 2/10/15.
 */
/**
 *
 * 包名：com.augurit.am.cmpt.widget.treeview.bean
 * 文件描述：树形Listview
 * 创建人：liangshenghong
 * 创建时间：2016-08-31 9:25
 * 修改人：xuciluan
 * 修改时间：2016-08-31 9:25
 * 修改备注：
 * @version
 *
 */
public class TreeNode {
    public static final String NODES_ID_SEPARATOR = ":";

    private int mId;
    private int mLastId;
    private TreeNode mParent;
    private boolean mSelected;
    private boolean mSelectable = true;
    private final List<TreeNode> children;
    private BaseNodeViewHolder mViewHolder;
    private TreeNodeClickListener mClickListener;
    private TreeNodeLongClickListener mLongClickListener;
    private Object mValue;
    private boolean mExpanded;
    private boolean mExpandable = true;

    /**
     * 创建一个根节点
     * @return
     */
    public static TreeNode root() {
        TreeNode root = new TreeNode(null);
        root.setSelectable(false);
        return root;
    }

    /**
     * 返回节点ID
     * @return
     */
    private int generateId() {
        return ++mLastId;
    }

    public TreeNode(Object value) {
        children = new ArrayList<>();
        mValue = value;
    }

    /**
     * 添加子节点
     * @param childNode
     * @return
     */
    public TreeNode addChild(TreeNode childNode) {
        childNode.mParent = this;
        childNode.mId = generateId();
        children.add(childNode);
        return this;
    }

    /**
     * 添加子节点
     * @param nodes
     * @return
     */
    public TreeNode addChildren(TreeNode... nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    /**
     * 添加子节点
     * @param nodes
     * @return
     */
    public TreeNode addChildren(Collection<TreeNode> nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    /**
     * 删除子节点
     * @param child
     * @return
     */
    public int deleteChild(TreeNode child) {
        for (int i = 0; i < children.size(); i++) {
            if (child.mId == children.get(i).mId) {
                children.remove(i);
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回所有子节点
     * @return
     */
    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * 返回子节点的数量
     * @return
     */
    public int size() {
        return children.size();
    }

    /**
     * 返回父节点
     * @return
     */
    public TreeNode getParent() {
        return mParent;
    }

    /**
     * 返回节点id
     * @return
     */
    public int getId() {
        return mId;
    }

    /**
     * 是否叶子节点，即没有子节点
     * @return
     */
    public boolean isLeaf() {
        return size() == 0;
    }

    /**
     * 返回节点的数据value
     * @return
     */
    public Object getValue() {
        return mValue;
    }

    /**
     * 节点是否展开状态
     * @return
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * 设置节点的展开状态
     * @param expanded
     * @return
     */
    public TreeNode setExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    /**
     * 节点是否可展开或关闭
     * @return
     */
    public boolean isExpandable(){
        return mExpandable;
    }

    /**
     * 设置节点是否可展开或关闭状态
     * @param expandable
     * @return
     */
    public void setExpandable(boolean expandable){
        this.mExpandable = expandable;
    }

    /**
     * 设置节点的选中状态
     * @param selected
     */
    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    /**
     * 是否选中状态
     * @return
     */
    public boolean isSelected() {
        return mSelectable && mSelected;
    }

    /**
     * 设置是否可选中
     * @param selectable
     */
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    /**
     * 是否可选中
     * @return
     */
    public boolean isSelectable() {
        return mSelectable;
    }

    /**
     * 返回节点路径
     * @return
     */
    public String getPath() {
        final StringBuilder path = new StringBuilder();
        TreeNode node = this;
        while (node.mParent != null) {
            path.append(node.getId());
            node = node.mParent;
            if (node.mParent != null) {
                path.append(NODES_ID_SEPARATOR);
            }
        }
        return path.toString();
    }

    /**
     * 当前节点的层级
     * @return
     */
    public int getLevel() {
        int level = 0;
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
            level++;
        }
        return level;
    }

    /**
     * 是否父节点的最后一个子节点
     * @return
     */
    public boolean isLastChild() {
        if (!isRoot()) {
            int parentSize = mParent.children.size();
            if (parentSize > 0) {
                final List<TreeNode> parentChildren = mParent.children;
                return parentChildren.get(parentSize - 1).mId == mId;
            }
        }
        return false;
    }

    /**
     * 设置ViewHolder
     * @param viewHolder
     * @return
     */
    public TreeNode setViewHolder(BaseNodeViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (viewHolder != null) {
            viewHolder.mNode = this;
        }
        return this;
    }

    /**
     * 设置节点的单击事件监听器
     * @param listener
     * @return
     */
    public TreeNode setClickListener(TreeNodeClickListener listener) {
        mClickListener = listener;
        return this;
    }

    /**
     * 返回节点的单击监听器
     * @return
     */
    public TreeNodeClickListener getClickListener() {
        return this.mClickListener;
    }

    /**
     * 设置节点的长按事件监听器
     * @param listener
     * @return
     */
    public TreeNode setLongClickListener(TreeNodeLongClickListener listener) {
        mLongClickListener = listener;
        return this;
    }

    /**
     * 返回节点的长按监听器
     * @return
     */
    public TreeNodeLongClickListener getLongClickListener() {
        return mLongClickListener;
    }

    /**
     * 返回ViewHolder
     * @return
     */
    public BaseNodeViewHolder getViewHolder() {
        return mViewHolder;
    }

    /**
     * 是否父节点的第一个子节点
     * @return
     */
    public boolean isFirstChild() {
        if (!isRoot()) {
            List<TreeNode> parentChildren = mParent.children;
            return parentChildren.get(0).mId == mId;
        }
        return false;
    }

    /**
     * 是否根节点
     * @return
     */
    public boolean isRoot() {
        return mParent == null;
    }

    /**
     * 返回树形控件的根节点
     * @return
     */
    public TreeNode getRoot() {
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
        }
        return root;
    }

    public interface TreeNodeClickListener {
        void onClick(TreeNode node, Object value);
    }

    public interface TreeNodeLongClickListener {
        boolean onLongClick(TreeNode node, Object value);
    }

    /**
     * @param <E>
     */
    public static abstract class BaseNodeViewHolder<E> {
        protected AndroidTreeView tView;
        protected TreeNode mNode;
        private View mView;
        protected int containerStyle;
        protected Context context;

        public BaseNodeViewHolder(Context context) {
            this.context = context;
        }

        /**
         * 返回节点的用样式修饰过的实际View
         * @return
         */
        public View getView() {
            if (mView != null) {
                return mView;
            }
            final View nodeView = getNodeView();
            final TreeNodeWrapperView nodeWrapperView = new TreeNodeWrapperView(nodeView.getContext(), getContainerStyle());
            nodeWrapperView.insertNodeView(nodeView);
            mView = nodeWrapperView;

            return mView;
        }

        /**
         * 设置节点所在的AndroidTreeView
         * @param treeViev
         */
        public void setTreeViev(AndroidTreeView treeViev) {
            this.tView = treeViev;
        }

        public AndroidTreeView getTreeView() {
            return tView;
        }

        /**
         * 设置节点的样式
         * @param style
         */
        public void setContainerStyle(int style) {
            containerStyle = style;
        }

        /**
         * 返回节点的View
         * @return
         */
        public View getNodeView() {
            return createNodeView(mNode, (E) mNode.getValue());
        }

        public ViewGroup getNodeItemsView() {
            return (ViewGroup) getView().findViewById(R.id.node_items);
        }

        /**
         * 节点是否已初始化
         * @return
         */
        public boolean isInitialized() {
            return mView != null;
        }

        public int getContainerStyle() {
            return containerStyle;
        }


        /**
         * 创建节点的View，子类须重写此方法
         * @return
         */
        public abstract View createNodeView(TreeNode node, E value);

        /**
         * 节点的展开折叠状态发生改变时回调此方法
         * @param active
         */
        public void toggle(boolean active) {
            // empty
        }

        /**
         * 可选状态或选中状态发生改变时回调此方法，
         * 可以此方法中调用TreeNode.isSelected()方法判断节点的选中状态，
         * 然后自定义选中状态发生改变后的动作
         * @param editModeEnabled
         */
        public void toggleSelectionMode(boolean editModeEnabled) {
            // empty
        }
    }
}
