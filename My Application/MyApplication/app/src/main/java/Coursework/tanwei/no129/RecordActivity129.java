package Coursework.tanwei.no129;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RecordActivity129 extends AppCompatActivity {
    private List<Fragment> mfrglist129;
    private ViewPager mviewPager129;
    private GridView gridview129 = null;
    private ArrayList<HashMap<String,Object>>  list_item129 = new ArrayList<>();


    //变量
    private Date date129 = new Date();        //日期
    private float money129 = 0.0f;            //金额
    private String description129 = "";       //备注
    private String type129 = "一般";          //类型

    private String[] titles129 = new String[]{"支出","收入"};


    //控件
    /*private static IncomeFragment129 incomeFragment129 = null; //收入Fragment
    private static  OutcomeFragment129 outcomeFragment129 = null;//支出Fragment*/

    private  TabLayout tabs129 = null;          //选项卡
    private  TextView btn_dateSet129 = null;    //日期设置
    private  TextView btn_descSet129 = null;    //备注设置



    final DBAdapter129 dbAdapter129 = new DBAdapter129(RecordActivity129.this);          //DBAdapter适配器

    private static int fragment_state129 = 2;
    private static final int INCOME_MODE = 1;
    private static final int OUTCOME_MODE = 2;

    private static final int MODE_INSERT = 1;     //插入模式
    private static final int MODE_ALTER = 2;      //修改模式
    private static int operationMode129 = 0;

    public RecordActivity129(){
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record129);
                        //使用Toolbar 代替actionbar
        operationMode129 = this.getIntent().getExtras().getInt("MODE_TYPE");




        //选项卡
        tabs129 = findViewById(R.id.record_tabs129);
        Log.d("BBBB","AADXEF");

        //初始化fragment适配器
        final FragmentManager fragmentManager129 = getSupportFragmentManager();
        mfrglist129 = new ArrayList<>();
        final IncomeFragment129 incomeFragment129 = new IncomeFragment129();
        final OutcomeFragment129 outcomeFragment129 = new OutcomeFragment129();
        mfrglist129.add(outcomeFragment129);
        mfrglist129.add(incomeFragment129);
        final FragmentPageAdapter129 frgAdapter129 = new FragmentPageAdapter129(fragmentManager129,mfrglist129,titles129);

        Log.d("CCC","AADXEF");

        //绑定viewPager
        mviewPager129 = findViewById(R.id.record_viewpager129);
        mviewPager129.setAdapter(frgAdapter129);
        tabs129.setupWithViewPager(mviewPager129);      //tab与viewPager绑定.


        //tab选择监听
        tabs129.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        //edit_money129 = outcomeFragment129.getView().findViewById(R.id.out_money129);    //获取outcomeFragment129的editText
                        fragment_state129 = OUTCOME_MODE;
                        Log.d("tabMode",tab.getPosition() + "");
                        break;
                    case 1:
                       // edit_money129 = incomeFragment129.getView().findViewById(R.id.in_money129);    //获取outcomeFragment129的editText
                        fragment_state129 = INCOME_MODE;
                        Log.d("tabMode",tab.getPosition() + "");
                        break;
                    default:
                        break;
                }
            }

        });


        gridview129 = findViewById(R.id.outcome_gridview129);
        //获取数据
        getData();
        String[] keys129 = {"icon","text"};
        int[] item_id129 = {R.id.image_type129,R.id.text_type129};
        SimpleAdapter simpleAdapter129 = new SimpleAdapter(RecordActivity129.this,list_item129,R.layout.type_item129,keys129,item_id129);
        gridview129.setAdapter(simpleAdapter129);

        //控件获取
        btn_dateSet129 = findViewById(R.id.out_date_set129);              //时间设置按钮
        btn_descSet129 = findViewById(R.id.out_description_set129);       //备注设置
        Button  btn_save = findViewById(R.id.out_button_save129);                  //保存

        //类型设置
        gridview129.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {

                type129 = CommonHelper.text_item129[position];
                EditText edit_moneya129 = outcomeFragment129.getView().findViewById(R.id.out_money129);
                EditText edit_moneyb129 = incomeFragment129.getView().findViewById(R.id.in_money129);
                Drawable ico129 = getResources().getDrawable(CommonHelper.icon_item129[position],null);
                ico129.setBounds(0,0,ico129.getMinimumWidth(),ico129.getMinimumHeight());          //设置图标大小
                edit_moneya129.setCompoundDrawables(ico129,null,null,null);
                edit_moneyb129.setCompoundDrawables(ico129,null,null,null);
            }
        });


        //日期设置
        btn_dateSet129.setText("  日期                                       " + CommonHelper.dateToString(date129));
        btn_dateSet129.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("Button","Click");
                Calendar c129 = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener  ds_listener = new DatePickerDialog.OnDateSetListener() {
                    @Override

                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Calendar calendar129 = Calendar.getInstance();
                        calendar129.setTimeInMillis(System.currentTimeMillis());
                        calendar129.set(Calendar.YEAR,year);
                        calendar129.set(Calendar.MONTH,month);
                        calendar129.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        date129.setTime(calendar129.getTimeInMillis());
                        Log.d("Time",date129.toString());
                    }
                };
                new DatePickerDialog(RecordActivity129.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,ds_listener,c129.get(Calendar.YEAR),c129.get(Calendar.MONTH),c129.get(Calendar.DAY_OF_MONTH)).show();
                btn_dateSet129.setText("  日期                                       " + CommonHelper.dateToString(date129));
            }
        });


        //备注设置
        btn_descSet129.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                final EditText editText129 = new EditText(RecordActivity129.this);
                editText129.setLines(5);
                editText129.setGravity(Gravity.START);
                editText129.setBackgroundColor(getResources().getColor(R.color.colorEdit_bgc));
                final AlertDialog.Builder dialog = new AlertDialog.Builder(RecordActivity129.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog.setTitle("备注");
                dialog.setView(editText129);
                dialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        description129 = editText129.getText().toString();
                        btn_descSet129.setText("  备注                                        " + description129);
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });

        //保存
        btn_save.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                dbAdapter129.openDatabase();
                switch(operationMode129){
                    case MODE_INSERT:
                        EditText edit_in129 = incomeFragment129.getView().findViewById(R.id.in_money129);
                        EditText edit_out129 = outcomeFragment129.getView().findViewById(R.id.out_money129);
                        Log.d("Fmode",fragment_state129 + "");
                        //根据所选模式从不同的fragment中获取金额值
                        switch (fragment_state129){
                            case INCOME_MODE:
                                money129 = Float.parseFloat(edit_in129.getText().toString());
                                break;
                            case OUTCOME_MODE:
                                money129 = -1f * Float.parseFloat(edit_out129.getText().toString());
                                break;
                            default:
                                break;

                        }

                        Log.d("money",money129 + " ");
                        Bill  bill = new Bill();
                        bill.money129 = money129;
                        bill.type129 = type129;                             ////////////////////////////////////////////////////////////////////////
                        bill.date129 = date129;
                        bill.description129 = description129;

                        dbAdapter129.insertBill(bill);
                        break;
                    case MODE_ALTER:
                            int ALTER_ID = getIntent().getExtras().getInt("ALTER_ITEM_ID");


                        break;
                    default:
                            break;
                }



            }
        });

        Log.d("OnCreate结束","FFFF");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("OnStart结束","FFFF");

    }


    public void getData(){

        for(int i = 0;i < CommonHelper.icon_item129.length;i++){
            HashMap<String,Object> item_map129 = new HashMap<>();  //Map是抽象类 此表记录两个键值对  name 和 icon
            item_map129.put("icon",CommonHelper.icon_item129[i]);
            item_map129.put("text",CommonHelper.text_item129[i]);
            list_item129.add(item_map129);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter129.closeDatabase();
    }

    @Override
    public void onBackPressed() {

        /*Log.i("CountOFbackStack", getSupportFragmentManager().getBackStackEntryCount()+"");
        getSupportFragmentManager().popBackStack();*/
        this.finish();


    }
}
