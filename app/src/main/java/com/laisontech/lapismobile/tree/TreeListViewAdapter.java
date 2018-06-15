package com.laisontech.lapismobile.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * ..................................................................
 * .         The Buddha said: I guarantee you have no bug!          .
 * .                                                                .
 * .                            _ooOoo_                             .
 * .                           o8888888o                            .
 * .                           88" . "88                            .
 * .                           (| -_- |)                            .
 * .                            O\ = /O                             .
 * .                        ____/`---'\____                         .
 * .                      .   ' \\| |// `.                          .
 * .                       / \\||| : |||// \                        .
 * .                     / _||||| -:- |||||- \                      .
 * .                       | | \\\ - /// | |                        .
 * .                     | \_| ''\---/'' | |                        .
 * .                      \ .-\__ `-` ___/-. /                      .
 * .                   ___`. .' /--.--\ `. . __                     .
 * .                ."" '< `.___\_<|>_/___.' >'"".                  .
 * .               | | : `- \`.;`\ _ /`;.`/ - ` : | |               .
 * .                 \ \ `-. \_ __\ /__ _/ .-` / /                  .
 * .         ======`-.____`-.___\_____/___.-`____.-'======          .
 * .                            `=---='                             .
 * ..................................................................
 * Created by SDP on 2018/6/15.
 */

public abstract class TreeListViewAdapter extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;
    //默认不展开
    private int defaultExpandLevel = 0;
    //图片
    private int iconExpand = -1, iconUnExpand = -1;
    //所有可见的nodes
    protected List<Node> mVisibleNodes = new ArrayList<>();
    //所有的nodes
    protected List<Node> mAllNodes = new ArrayList<>();
    //接口
    private OnTreeNodeClickListener mListener;

    public void setListener(OnTreeNodeClickListener listener) {
        this.mListener = listener;
    }

    //构造函数
    public TreeListViewAdapter(Context context, ListView tree,
                               List<Node> nodes, int defaultExpandLevel,
                               int iconExpand, int iconUnExpand) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.iconExpand = iconExpand;
        this.iconUnExpand = iconUnExpand;
        for (Node node : nodes) {
            node.getChildren().clear();
            node.iconExpand = iconExpand;
            node.iconUnExpand = iconUnExpand;
        }
        this.defaultExpandLevel = defaultExpandLevel;
        this.mAllNodes = TreeHelper.getSortNodes(nodes, defaultExpandLevel);
        this.mVisibleNodes = TreeHelper.filterVisibleNode(mAllNodes);
        tree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);
                if (mListener != null) {
                    mListener.onClick(mVisibleNodes.get(position), position);
                }
            }
        });
    }

    public TreeListViewAdapter(Context context, ListView mTree, List<Node> datas,
                               int defaultExpandLevel) {
        this(context, mTree, datas, defaultExpandLevel, -1, -1);
    }

    /**
     * 添加所有数据
     */
    public void addDataAll(List<Node> data, int defaultExpandLevel) {
        mAllNodes.clear();
        addData(-1, data, defaultExpandLevel);
    }

    public void addData(int index, List<Node> data, int defaultExpandLevel) {
        this.defaultExpandLevel = defaultExpandLevel;
        notifyData(index, data);
    }

    public void addData(int index, List<Node> data) {
        notifyData(index, data);
    }

    public void addData(List<Node> data) {
        addData(data, defaultExpandLevel);
    }

    public void addData(List<Node> data, int defaultExpandLevel) {
        this.defaultExpandLevel = defaultExpandLevel;
        notifyData(-1, data);
    }

    public void addData(Node node) {
        addData(node, defaultExpandLevel);
    }

    public void addData(Node node, int defaultExpandLevel) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(node);
        this.defaultExpandLevel = defaultExpandLevel;
        notifyData(-1, nodes);
    }

    private void notifyData(int index, List<Node> data) {
        if (data == null || data.size() < 1) return;
        for (int i = 0; i < data.size(); i++) {
            Node node = data.get(i);
            node.getChildren().clear();
            node.iconExpand = iconExpand;
            node.iconUnExpand = iconUnExpand;
        }
        for (int i = 0; i < mAllNodes.size(); i++) {
            Node node = mAllNodes.get(i);
            node.getChildren().clear();
            node.isNewAdd = false;
        }
        if (index == -1) {
            mAllNodes.addAll(data);
        } else {
            mAllNodes.addAll(index, data);
        }
        mAllNodes = TreeHelper.getSortNodes(mAllNodes, defaultExpandLevel);
        mVisibleNodes = TreeHelper.filterVisibleNode(mAllNodes);
        notifyDataSetChanged();
    }

    /**
     * 获取排序的节点
     */
    public List<Node> getAllNodes() {
        if (mAllNodes == null) {
            mAllNodes = new ArrayList<>();
        }
        return mAllNodes;
    }

    /**
     * 获取所有选中的节点,包含父节点
     */
    public List<Node> getSelectNodeIncludeParent() {
        List<Node> allNodes = getAllNodes();
        if (allNodes.size() < 1) return null;
        List<Node> result = new ArrayList<>();
        for (Node node : allNodes) {
            if (node.isChecked()) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 获取所有选中的节点,只要子节点
     */
    public List<Node> getSelectNodeOnlyChildren() {
        List<Node> allNodes = getAllNodes();
        if (allNodes.size() < 1) return null;
        List<Node> result = new ArrayList<>();
        addLeafNode(result, allNodes);
        return result;
    }

    private void addLeafNode(List<Node> result, List<Node> nodes) {
        for (Node node : nodes) {
            if (node.isLeaf()) {
                if (node.isChecked()) {
                    if (!result.contains(node)){
                        result.add(node);
                    }
                }
            } else {
                addLeafNode(result, node.getChildren());
            }
        }
    }

    private void expandOrCollapse(int position) {
        Node visibleNode = mVisibleNodes.get(position);
        if (visibleNode != null) {
            if (!visibleNode.isLeaf()) {
                visibleNode.setExpand(!visibleNode.isExpand());
                mVisibleNodes = TreeHelper.filterVisibleNode(mAllNodes);//重新过滤显示信息
                notifyDataSetChanged();//刷新视图
            }
        }
    }

    @Override
    public int getCount() {
        return mVisibleNodes == null ? 0 : mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        //设置内边距
        convertView.setPadding(node.getLevel() * DisplayUtil.dip2px(mContext, 10),
                DisplayUtil.dip2px(mContext, 3),
                DisplayUtil.dip2px(mContext, 3),
                DisplayUtil.dip2px(mContext, 3));
        return convertView;
    }


    protected abstract View getConvertView(Node node, int position,
                                           View convertView, ViewGroup parent);

    /**
     * 设置多选
     */
    protected void setChecked(Node node, boolean checked) {
        node.setChecked(checked);
        setChildChecked(node, checked);
        if (node.getParent() != null) {
            setNodeParentChecked(node.getParent(), checked);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置是否选中
     */
    private <T, B> void setChildChecked(Node<T, B> node, boolean checked) {
        if (!node.isLeaf()) {
            node.setChecked(checked);
            for (Node childNode : node.getChildren()) {
                setChildChecked(childNode, checked);
            }
        } else {
            node.setChecked(checked);
        }
    }

    private void setNodeParentChecked(Node node, boolean checked) {
        if (checked) {
            node.setChecked(checked);
            if (node.getParent() != null) {
                setNodeParentChecked(node.getParent(), checked);
            }
        } else {
            List<Node> children = node.getChildren();
            boolean isChecked = false;
            for (Node child : children) {
                if (child.isChecked()) {
                    isChecked = true;
                }
            }
            //如果所有自节点都没有被选中 父节点也不选中
            if (!isChecked) {
                node.setChecked(checked);
            }
            if (node.getParent() != null) {
                setNodeParentChecked(node.getParent(), checked);
            }
        }
    }
}
