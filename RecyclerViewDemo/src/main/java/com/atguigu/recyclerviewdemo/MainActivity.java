package com.atguigu.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.btn_add)
    Button btnAdd;
    @InjectView(R.id.btn_remove)
    Button btnRemove;
    @InjectView(R.id.btn_list)
    Button btnList;
    @InjectView(R.id.btn_grid)
    Button btnGrid;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    private ArrayList<String> datas;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //准备数据和设置适配器
        initData();

        //设置适配器
        adapter = new MyAdapter(this,datas);
        recyclerview.setAdapter(adapter);
        //设置布局管理器
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
//        recyclerview.setLayoutManager(new GridLayoutManager(this, 3,GridLayoutManager.HORIZONTAL,false));
//        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        //添加分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i=0;i<1000;i++){
            datas.add("Data_"+i);
        }
    }

    @OnClick({R.id.btn_add, R.id.btn_remove, R.id.btn_list, R.id.btn_grid})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Toast.makeText(MainActivity.this, "添加", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_remove:
                Toast.makeText(MainActivity.this, "移除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_list:
                Toast.makeText(MainActivity.this, "List", Toast.LENGTH_SHORT).show();

                recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                break;
            case R.id.btn_grid:
                Toast.makeText(MainActivity.this, "Grid", Toast.LENGTH_SHORT).show();
                recyclerview.setLayoutManager(new GridLayoutManager(this, 3,GridLayoutManager.VERTICAL,false));
                break;
        }
    }
}
