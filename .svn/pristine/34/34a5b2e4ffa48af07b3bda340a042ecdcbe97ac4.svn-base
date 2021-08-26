package com.augurit.am.cmpt.widget.treeview.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.augurit.am.cmpt.R;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.augurit.am.cmpt.widget.treeview.holder.SimpleViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class AndroidTreeView {
    private static final String NODES_PATH_SEPARATOR = ";";

    protected TreeNode mRoot;
    private Context mContext;
    private boolean applyForRoot;
    private int containerStyle = 0;
    private Class<? extends TreeNode.BaseNodeViewHolder> defaultViewHolderClass = SimpleViewHolder.class;
    private TreeNode.TreeNodeClickListener nodeClickListener;
    private TreeNode.TreeNodeLongClickListener nodeLongClickListener;
    private boolean mSelectionModeEnabled;
    private boolean mUseDefaultAnimation = false;
    private boolean use2dScroll = false;
    private boolean enableAutoToggle = true;

    public AndroidTreeView(Context context) {
        mContext = context;
    }

    /**
     * 设置根节点
     * @param mRoot
     */
    public void setRoot(TreeNode mRoot) {
        this.mRoot = mRoot;
    }

    public AndroidTreeView(Context context, TreeNode root) {
        mRoot = root;
        mContext = context;
    }

    /**
     * 使用默认动画
     * @param defaultAnimation
     */
    public void setDefaultAnimation(boolean defaultAnimation) {
        this.mUseDefaultAnimation = defaultAnimation;
    }

    /**
     * 设置默认节点样式
     * @param style
     */
    public void setDefaultContainerStyle(int style) {
        setDefaultContainerStyle(style, false);
    }

    /**
     * 设置默认节点样式，
     * @param style
     * @param applyForRoot
     */
    public void setDefaultContainerStyle(int style, boolean applyForRoot) {
        containerStyle = style;
        this.applyForRoot = applyForRoot;
    }

    /**
     * 设置是否启动水平垂直双向滚动
     * @param use2dScroll
     */
    public void setUse2dScroll(boolean use2dScroll) {
        this.use2dScroll = use2dScroll;
    }

    /**
     * 是否启动水平垂直双向滚动
     * @return
     */
    public boolean is2dScrollEnabled() {
        return use2dScroll;
    }

    public void setUseAutoToggle(boolean enableAutoToggle) {
        this.enableAutoToggle = enableAutoToggle;
    }

    public boolean isAutoToggleEnabled() {
        return enableAutoToggle;
    }

    /**
     * 设置节点的VeiwHolder
     * @param viewHolder
     */
    public void setDefaultViewHolder(Class<? extends TreeNode.BaseNodeViewHolder> viewHolder) {
        defaultViewHolderClass = viewHolder;
    }

    /**
     * 设置默认的节点单击事件监听器
     * @param listener
     */
    public void setDefaultNodeClickListener(TreeNode.TreeNodeClickListener listener) {
        nodeClickListener = listener;
    }

    /**
     * 设置默认的节点长按事件监听器
     * @param listener
     */
    public void setDefaultNodeLongClickListener(TreeNode.TreeNodeLongClickListener listener) {
        nodeLongClickListener = listener;
    }

    /**
     * 展开所有节点
     */
    public void expandAll() {
        expandNode(mRoot, true);
    }

    /**
     * 折叠所有节点
     */
    public void collapseAll() {
        for (TreeNode n : mRoot.getChildren()) {
            collapseNode(n, true);
        }
    }


    /**
     * 返回实际的树形列表控件的View
     * @param style 控件样式
     * @return
     */
    public View getView(int style) {
        final ViewGroup view;
        if (style > 0) {
            ContextThemeWrapper newContext = new ContextThemeWrapper(mContext, style);
            view = use2dScroll ? new TwoDScrollView(newContext) : new NestedScrollView(newContext);
        } else {
            view = use2dScroll ? new TwoDScrollView(mContext) : new NestedScrollView(mContext);
        }

        Context containerContext = mContext;
        if (containerStyle != 0 && applyForRoot) {
            containerContext = new ContextThemeWrapper(mContext, containerStyle);
        }
        final LinearLayout viewTreeItems = new LinearLayout(containerContext, null, containerStyle);

        viewTreeItems.setId(R.id.tree_items);
        viewTreeItems.setOrientation(LinearLayout.VERTICAL);
        view.addView(viewTreeItems);

        mRoot.setViewHolder(new TreeNode.BaseNodeViewHolder(mContext) {
            @Override
            public View createNodeView(TreeNode node, Object value) {
                return null;
            }

            @Override
            public ViewGroup getNodeItemsView() {
                return viewTreeItems;
            }
        });

        expandNode(mRoot, false);
        return view;
    }

    /**
     * 返回实际的树形列表控件的View
     * @return
     */
    public View getView() {
        return getView(-1);
    }


    /**
     * 展开给定层级
     * @param level
     */
    public void expandLevel(int level) {
        for (TreeNode n : mRoot.getChildren()) {
            expandLevel(n, level);
        }
    }

    private void expandLevel(TreeNode node, int level) {
        if (node.getLevel() <= level) {
            expandNode(node, false);
        }
        for (TreeNode n : node.getChildren()) {
            expandLevel(n, level);
        }
    }

    /**
     * 展开给定节点，不展开子节点
     * @param node
     */
    public void expandNode(TreeNode node) {
        expandNode(node, false);
    }


    /**
     * 折叠给定节点
     * @param node
     */
    public void collapseNode(TreeNode node) {
        collapseNode(node, false);
    }

    public String getSaveState() {
        final StringBuilder builder = new StringBuilder();
        getSaveState(mRoot, builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    public void restoreState(String saveState) {
        if (!TextUtils.isEmpty(saveState)) {
            collapseAll();
            final String[] openNodesArray = saveState.split(NODES_PATH_SEPARATOR);
            final Set<String> openNodes = new HashSet<>(Arrays.asList(openNodesArray));
            restoreNodeState(mRoot, openNodes);
        }
    }

    private void restoreNodeState(TreeNode node, Set<String> openNodes) {
        for (TreeNode n : node.getChildren()) {
            if (openNodes.contains(n.getPath())) {
                expandNode(n);
                restoreNodeState(n, openNodes);
            }
        }
    }

    private void getSaveState(TreeNode root, StringBuilder sBuilder) {
        for (TreeNode node : root.getChildren()) {
            if (node.isExpanded()) {
                sBuilder.append(node.getPath());
                sBuilder.append(NODES_PATH_SEPARATOR);
                getSaveState(node, sBuilder);
            }
        }
    }

    /**
     * 切换节点的折叠状态
     * @param node
     */
    public void toggleNode(TreeNode node) {
        if (node.isExpanded()) {
            collapseNode(node, false);
        } else {
            expandNode(node, false);
        }

    }

    public void forceCollapseNode(TreeNode node, final boolean includeSubnodes){
        doCollapseNode(node, includeSubnodes);
    }

    private void collapseNode(TreeNode node, final boolean includeSubnodes) {
        if (!node.isExpandable()) {
            return;
        }
        doCollapseNode(node, includeSubnodes);
    }

    private void doCollapseNode(TreeNode node, final boolean includeSubnodes) {
        if(!node.isExpandable()){
            return;
        }
        node.setExpanded(false);
        TreeNode.BaseNodeViewHolder nodeViewHolder = getViewHolderForNode(node);

        if (mUseDefaultAnimation) {
            collapse(nodeViewHolder.getNodeItemsView());
        } else {
            nodeViewHolder.getNodeItemsView().setVisibility(View.GONE);
        }
        nodeViewHolder.toggle(false);
        if (includeSubnodes) {
            for (TreeNode n : node.getChildren()) {
                collapseNode(n, includeSubnodes);
            }
        }
    }

    /**
     * 强制展开节点
     * @param node
     * @param includeSubnodes
     */
    public void forceExpandNode(final TreeNode node, boolean includeSubnodes){
        doExpandNode(node, includeSubnodes);
    }

    private void expandNode(final TreeNode node, boolean includeSubnodes){
        if(!node.isExpandable()){
            return;
        }
        doExpandNode(node, includeSubnodes);
    }

    private void doExpandNode(final TreeNode node, boolean includeSubnodes) {

        node.setExpanded(true);
        final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(node);
        parentViewHolder.getNodeItemsView().removeAllViews();


        parentViewHolder.toggle(true);

        for (final TreeNode n : node.getChildren()) {
            addNode(parentViewHolder.getNodeItemsView(), n);

            if (n.isExpanded() || includeSubnodes) {
                expandNode(n, includeSubnodes);
            }

        }
        if (mUseDefaultAnimation) {
            expand(parentViewHolder.getNodeItemsView());
        } else {
            parentViewHolder.getNodeItemsView().setVisibility(View.VISIBLE);
        }

    }

    private void addNode(ViewGroup container, final TreeNode n) {
        final TreeNode.BaseNodeViewHolder viewHolder = getViewHolderForNode(n);
        final View nodeView = viewHolder.getView();
        container.addView(nodeView);
        if (mSelectionModeEnabled) {
            viewHolder.toggleSelectionMode(mSelectionModeEnabled);
        }

        nodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n.getClickListener() != null) {
                    n.getClickListener().onClick(n, n.getValue());
                } else if (nodeClickListener != null) {
                    nodeClickListener.onClick(n, n.getValue());
                }
                if (enableAutoToggle) {
                    toggleNode(n);
                }
            }
        });

        nodeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (n.getLongClickListener() != null) {
                    return n.getLongClickListener().onLongClick(n, n.getValue());
                } else if (nodeLongClickListener != null) {
                    return nodeLongClickListener.onLongClick(n, n.getValue());
                }
                if (enableAutoToggle) {
                    toggleNode(n);
                }
                return false;
            }
        });
    }

    //------------------------------------------------------------
    //  Selection methods

    /**
     * 是否允许节点可选
     * @param selectionModeEnabled
     */
    public void setSelectionModeEnabled(boolean selectionModeEnabled) {
        if (!selectionModeEnabled) {
            // TODO fix double iteration over tree
            deselectAll();
        }
        mSelectionModeEnabled = selectionModeEnabled;

        for (TreeNode node : mRoot.getChildren()) {
            toggleSelectionMode(node, selectionModeEnabled);
        }

    }

    /**
     * 返回选中节点Value
     * @param clazz
     * @param <E>
     * @return
     */
    public <E> List<E> getSelectedValues(Class<E> clazz) {
        List<E> result = new ArrayList<>();
        List<TreeNode> selected = getSelected();
        for (TreeNode n : selected) {
            Object value = n.getValue();
            if (value != null && value.getClass().equals(clazz)) {
                result.add((E) value);
            }
        }
        return result;
    }

    /**
     * 是否可选择状态
     * @return
     */
    public boolean isSelectionModeEnabled() {
        return mSelectionModeEnabled;
    }

    /**
     * 切换给定节点的可选择状态
     * @param parent
     * @param mSelectionModeEnabled
     */
    private void toggleSelectionMode(TreeNode parent, boolean mSelectionModeEnabled) {
        toogleSelectionForNode(parent, mSelectionModeEnabled);
        if (parent.isExpanded()) {
            for (TreeNode node : parent.getChildren()) {
                toggleSelectionMode(node, mSelectionModeEnabled);
            }
        }
    }

    /**
     * 返回选中的节点
     * @return
     */
    public List<TreeNode> getSelected() {
        if (mSelectionModeEnabled) {
            return getSelected(mRoot);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 返回给定节点下的选中子节点
     * @param parent
     * @return
     */
    // TODO Do we need to go through whole tree? Save references or consider collapsed nodes as not selected
    private List<TreeNode> getSelected(TreeNode parent) {
        List<TreeNode> result = new ArrayList<>();
        for (TreeNode n : parent.getChildren()) {
            if (n.isSelected()) {
                result.add(n);
            }
            result.addAll(getSelected(n));
        }
        return result;
    }

    /**
     * 选中所有节点
     * @param skipCollapsed
     */
    public void selectAll(boolean skipCollapsed) {
        makeAllSelection(true, skipCollapsed);
    }

    /**
     * 取消选中的所有节点
     */
    public void deselectAll() {
        makeAllSelection(false, false);
    }

    private void makeAllSelection(boolean selected, boolean skipCollapsed) {
        if (mSelectionModeEnabled) {
            for (TreeNode node : mRoot.getChildren()) {
                selectNode(node, selected, skipCollapsed);
            }
        }
    }

    /**
     * 选中给定节点
     * @param node
     * @param selected 选中状态
     */
    public void selectNode(TreeNode node, boolean selected) {
        if (mSelectionModeEnabled) {
            node.setSelected(selected);
            toogleSelectionForNode(node, true);
        }
    }

    /**
     * 选中给定节点
     * @param parent 节点
     * @param selected 是否选中
     * @param skipCollapsed 是否同时选中其所有子节点
     */
    private void selectNode(TreeNode parent, boolean selected, boolean skipCollapsed) {
        parent.setSelected(selected);
        toogleSelectionForNode(parent, true);
        boolean toContinue = !skipCollapsed || parent.isExpanded();
        if (toContinue) {
            for (TreeNode node : parent.getChildren()) {
                selectNode(node, selected, skipCollapsed);
            }
        }
    }

    private void toogleSelectionForNode(TreeNode node, boolean makeSelectable) {
        TreeNode.BaseNodeViewHolder holder = getViewHolderForNode(node);
        if (holder.isInitialized()) {
            getViewHolderForNode(node).toggleSelectionMode(makeSelectable);
        }
    }

    private TreeNode.BaseNodeViewHolder getViewHolderForNode(TreeNode node) {
        TreeNode.BaseNodeViewHolder viewHolder = node.getViewHolder();
        if (viewHolder == null) {
            try {
                final Object object = defaultViewHolderClass.getConstructor(Context.class).newInstance(mContext);
                viewHolder = (TreeNode.BaseNodeViewHolder) object;
                node.setViewHolder(viewHolder);
            } catch (Exception e) {
                throw new RuntimeException("Could not instantiate class " + defaultViewHolderClass);
            }
        }
        if (viewHolder.getContainerStyle() <= 0) {
            viewHolder.setContainerStyle(containerStyle);
        }
        if (viewHolder.getTreeView() == null) {
            viewHolder.setTreeViev(this);
        }
        return viewHolder;
    }

    private static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    //-----------------------------------------------------------------
    //Add / Remove

    /**
     * 给节点添加子节点
     * @param parent 父节点
     * @param nodeToAdd 被添加子节点
     */
    public void addNode(TreeNode parent, final TreeNode nodeToAdd) {
        parent.addChild(nodeToAdd);
        if (parent.isExpanded()) {
            final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
            addNode(parentViewHolder.getNodeItemsView(), nodeToAdd);
        }
    }

    /**
     * 移除给定节点
     * @param node
     */
    public void removeNode(TreeNode node) {
        if (node.getParent() != null) {
            TreeNode parent = node.getParent();
            int index = parent.deleteChild(node);
            if (parent.isExpanded() && index >= 0) {
                final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
                parentViewHolder.getNodeItemsView().removeViewAt(index);
            }
        }
    }
}
