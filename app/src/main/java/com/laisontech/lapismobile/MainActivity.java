package com.laisontech.lapismobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.laisontech.lapismobile.tree.Node;
import com.laisontech.lapismobile.tree.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    protected List<Node> mDatas = new ArrayList<>();
    @BindView(R.id.lv_tree)
    ListView lvTree;
    private TreeListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        // id , pid , label , 其他属性
        mDatas.add(new Node(1, -1, "应用"));
        mDatas.add(new Node(2, 1, "WPS"));
        mDatas.add(new Node(3, 1, "Office"));
        mDatas.add(new Node(4, 1, "Android studio"));
        mDatas.add(new Node(5, 1, "V S"));

        mDatas.add(new Node(6, -2, "软件"));
        mDatas.add(new Node(7, 6, "QQ"));
        mDatas.add(new Node(8, 6, "AquaRadius"));
        mDatas.add(new Node(9, 6, "AquaMall"));
        mDatas.add(new Node(10, 6, "抖音"));
        mDatas.add(new Node(11, 6, "微信"));

        mDatas.add(new Node(12, -3, "游戏"));

        mDatas.add(new Node(13, 12, "畅销"));
        mDatas.add(new Node(14, 13, "王者荣耀"));
        mDatas.add(new Node(15, 13, "第五人格"));


        mDatas.add(new Node(16, 12, "新榜"));

        mDatas.add(new Node(17, 16, "国内"));
        mDatas.add(new Node(18, 17, "消消乐"));
        mDatas.add(new Node(19, 17, "贪吃蛇"));

        mDatas.add(new Node(20, 16, "国外"));
        mDatas.add(new Node(21, 20, "刺激战场"));
        mDatas.add(new Node(22, 20, "全军出击"));
        mDatas.add(new Node(23, 20, "英雄联盟"));


        mAdapter = new SimpleTreeAdapter(this, lvTree,
                mDatas, 1, R.mipmap.tree_ex, R.mipmap.tree_ec);
        lvTree.setAdapter(mAdapter);

    }

    public void touch(View view) {
        StringBuilder sb = new StringBuilder();
        //获取排序过的nodes
        //如果不需要刻意直接用 mDatas既可
        final List<Node> allNodes = mAdapter.getSelectNodeOnlyChildren();
        for (int i = 0; i < allNodes.size(); i++) {
                sb.append(allNodes.get(i).getName()+",");
        }
        String strNodesName = sb.toString();
        if (!TextUtils.isEmpty(strNodesName))
            Log.e("TAG", "touch: "+strNodesName.substring(0, strNodesName.length()-1));
    }

}
