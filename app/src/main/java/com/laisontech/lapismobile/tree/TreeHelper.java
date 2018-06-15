package com.laisontech.lapismobile.tree;

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
 * 树的帮助类
 */

public class TreeHelper {
    public static List<Node> getSortNodes(List<Node> datas, int defaultExpandLevel) {
        if (datas == null) return null;
        if (datas.size() < 2) return datas;
        List<Node> result = new ArrayList<>();
        List<Node> nodes = covertData2Node(datas);//设置父子节点关系
        List<Node> rootNodes = getRootNodes(nodes);//获取根节点
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 设置父子关系,让每两个节点比较一次
     */
    private static List<Node> covertData2Node(List<Node> nodes) {
        //如果node的size小于2，则不可设树级
        for (int i = 0; i < nodes.size(); i++) {
            Node nodeI = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node nodeJ = nodes.get(j);
                if (nodeJ.getPId() instanceof String) {
                    if (nodeJ.getPId().equals(nodeI.getId())) {//是一个下面的
                        nodeI.getChildren().add(nodeJ);
                        nodeJ.setParent(nodeI);
                    } else if (nodeJ.getId().equals(nodeI.getPId())) {
                        nodeJ.getChildren().add(nodeI);
                        nodeI.setParent(nodeJ);
                    }
                } else {
                    if (nodeJ.getPId() == nodeI.getId()) {
                        nodeI.getChildren().add(nodeJ);
                        nodeJ.setParent(nodeI);
                    } else if (nodeJ.getId() == nodeI.getPId()) {
                        nodeJ.getChildren().add(nodeI);
                        nodeI.setParent(nodeJ);
                    }
                }
            }
        }
        return nodes;
    }


    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                rootNodes.add(node);
            }
        }
        return rootNodes;
    }

    /**
     * 使用递归的方法获取数据
     */
    private static <T, B> void addNode(List<Node> nodes, Node<T, B> node, int defaultExpandLevel, int currentLevel) {
        nodes.add(node);
        if (node.isNewAdd && defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        }
        if (node.isLeaf()) return;
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(nodes, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
        }
    }

    /**
     * 过滤节点
     */
    public static List<Node> filterVisibleNode(List<Node> nodes) {
        if (nodes == null || nodes.size() < 1) return null;
        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

    private static void setNodeIcon(Node node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(node.iconExpand);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(node.iconUnExpand);
        } else {
            node.setIcon(-1);
        }
    }

}
