package Coursework.tanwei.no129;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity129 extends AppCompatActivity {
    private ViewPager viewPager129 = null;
    private FragmentPageAdapter129 fragmentPageAdapter129 = null;
    //private DetailFragment129 detailFragment129;     //明细Fragment
    //private FormFragment129 formFragment129;         //报表Fragment
    private String[] titles = new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
    private  ArrayList<Fragment> fragments129;
    private  FragmentManager fragmentManager129;
    private TabLayout tabs129;

    private static final int MODE_INSERT = 1;     //插入模式



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabs129 = findViewById(R.id.main_tabs129);
        Toolbar toolbar129 = findViewById(R.id.toolbar129);       //获取布局Toolbar
        viewPager129 = findViewById(R.id.main_viewpager129);



        setSupportActionBar(toolbar129);                        //使用Toolbar 代替actionbar
                //获取FloatingActionButton控件和TabLayout,ListView
        FloatingActionButton fab129 = findViewById(R.id.fab129);



        //初始化适配器
        fragmentManager129 = getSupportFragmentManager();
        fragments129 = new ArrayList<>();
        for(int i = 1;i <= 12;i++){
            DetailFragment129 fragment129 = DetailFragment129.newInstance(i);
            fragments129.add(fragment129);
        }
        fragmentPageAdapter129 = new FragmentPageAdapter129(fragmentManager129,fragments129,titles);
        viewPager129.setCurrentItem(12);
        viewPager129.setAdapter(fragmentPageAdapter129);
        tabs129.setupWithViewPager(viewPager129);
        viewPager129.setOffscreenPageLimit(11);

        //FloatingActionButton单击事件
        fab129.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  CommonHelper.currentItemViewPager = viewPager129.getCurrentItem();*/
                Intent record_act129 = new Intent(MainActivity129.this,RecordActivity129.class);
                record_act129.putExtra("MODE_TYPE",MODE_INSERT);
                startActivity(record_act129);
                MainActivity129.this.onStop();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
     /*   //返回时重新加载ListView;
        fragments129 = new ArrayList<>();
        for(int i = 1;i <= 12;i++){
            DetailFragment129 fragment129 = new DetailFragment129(i);
            fragments129.add(fragment129);
        }
        TabLayout tabs129 = findViewById(R.id.main_tabs129);
        fragmentPageAdapter129 = new FragmentPageAdapter129(fragmentManager129,fragments129,titles);
        viewPager129 = findViewById(R.id.main_viewpager129);
        viewPager129.setAdapter(fragmentPageAdapter129);
        tabs129.setupWithViewPager(viewPager129);
        viewPager129.setOffscreenPageLimit(12);     ///////////////////////////////////////////////
        Calendar c = Calendar.getInstance();
        *//*int currentMonth = c.get(Calendar.MONTH);
        viewPager129.setCurrentItem(currentMonth);*/
    }

    @Override
    protected void onRestart() {

        super.onRestart();


    }


    //初始
    public void  InitAdapter(){

    }
}
