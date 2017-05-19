package com.atguigu.mobileplayer0224;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.atguigu.mobileplayer0224.fragment.BaseFragment;
import com.atguigu.mobileplayer0224.pager.LocalAudioPager;
import com.atguigu.mobileplayer0224.pager.LocalVideoPager;
import com.atguigu.mobileplayer0224.pager.NetAudioPager;
import com.atguigu.mobileplayer0224.pager.NetVideoPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    /**
     * 缓存当前显示的Fragment
     */
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainActivity","onCreate");
        setContentView(R.layout.activity_main);
        isGrantExternalRW(this);
        //初始化控件
        rg_main = (RadioGroup)findViewById(R.id.rg_main);
        initFragment();
        //设置监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选择本地视频
        rg_main.check(R.id.rb_local_video);
    }


    private void initFragment() {
        //把各个页面实例化放入集合中
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetAudioPager());
        fragments.add(new NetVideoPager());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_local_video:
                    position = 0;
                    break;
                case R.id.rb_local_audio:
                    position = 1;
                    break;

                case R.id.rb_net_audio:
                    position = 2;
                    break;

                case R.id.rb_net_video:
                    position  = 3;
                    break;
            }
            //根据位置得到对应的Fragment
            BaseFragment currentFragment = fragments.get(position);//要显示的Fragment
            addFragment(currentFragment);


        }
    }

    private void addFragment(Fragment currentFragment) {
        if(tempFragment != currentFragment){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            //判断是否添加过-没有添加
            if(!currentFragment.isAdded()){
                //把之前的隐藏
                if(tempFragment != null){
                    ft.hide(tempFragment);
                }
                //添加当前的
                ft.add(R.id.fl_content,currentFragment);
            }else{
//       当前Fragment已经添加过
                //把之前的隐藏
                if(tempFragment != null){
                    ft.hide(tempFragment);
                }
                //显示当前的
                ft.show(currentFragment);

            }

            ft.commit();//提交事务
            //把当前的缓存起来
            tempFragment = currentFragment;

        }


    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MainActivity","onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity","onDestroy");
    }
}
