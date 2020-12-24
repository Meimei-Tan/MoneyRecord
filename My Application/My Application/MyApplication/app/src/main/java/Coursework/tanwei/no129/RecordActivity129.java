package Coursework.tanwei.no129;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RecordActivity129 extends AppCompatActivity {
    private ArrayList<HashMap<String,Object>>  list_item129 = new ArrayList<>();
    //变量
    private Date date129 = new Date();        //日期
    private float money129 = 0.0f;            //金额
    private String description129 = "";       //备注
    private String type129 = "一般";          //类型



    //控件
    private GridView gridview129 = null;
    private  TextView btn_dateSet129 = null;    //日期设置
    private  TextView btn_descSet129 = null;    //备注设置
    private Switch switch129 = null;
    private EditText edit_money129 = null;
    private CoordinatorLayout coordinatorLayout129 = null;



    final DBAdapter129 dbAdapter129 = new DBAdapter129(RecordActivity129.this);          //DBAdapter适配器

    private static int type_state129 = 2;
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


        //获取控件
        switch129 = findViewById(R.id.switch_mode);
        edit_money129 = findViewById(R.id.edit_money);
        gridview129 = findViewById(R.id.outcome_gridview129);
        btn_dateSet129 = findViewById(R.id.out_date_set129);              //时间设置按钮
        btn_descSet129 = findViewById(R.id.out_description_set129);       //备注设置
        Button  btn_save = findViewById(R.id.out_button_save129);                  //保存
        coordinatorLayout129 = findViewById(R.id.coordinator1292);

        //初始化控件状态
        btn_dateSet129.setText("  日期                                       " + CommonHelper.dateToString(date129));

        switch129.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if(state)
                    type_state129 = INCOME_MODE;        //打开表示收入模式
                else
                    type_state129 = OUTCOME_MODE;       //关闭表示支出模式
            }
        });



        //获取数据并给GridView设置适配器
        getData();
        String[] keys129 = {"icon","text"};
        int[] item_id129 = {R.id.image_type129,R.id.text_type129};
        SimpleAdapter simpleAdapter129 = new SimpleAdapter(RecordActivity129.this,list_item129,R.layout.type_item129,keys129,item_id129);
        gridview129.setAdapter(simpleAdapter129);



        //类型设置
        gridview129.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                type129 = CommonHelper.text_item129[position];
                Drawable ico129 = getResources().getDrawable(CommonHelper.icon_item129[position],null);
                ico129.setBounds(0,0,ico129.getMinimumWidth(),ico129.getMinimumHeight());          //设置图标大小
                edit_money129.setCompoundDrawables(ico129,null,null,null);
            }
        });


        //日期设置
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
                String money_string = edit_money129.getText().toString();
                if(money_string.equals("") || Float.parseFloat(money_string) == 0){
                    Snackbar.make(coordinatorLayout129,"金额需大于0",Snackbar.LENGTH_SHORT).show();
                    return ;
                }
                money129 =  Float.parseFloat(money_string);
                if(type_state129 == OUTCOME_MODE)
                    money129 = -1.0f * money129;
                Log.d("money",money129 + " ");
                Bill  bill = new Bill();
                bill.money129 = money129;
                bill.type129 = type129;                             ////////////////////////////////////////////////////////////////////////
                bill.date129 = date129;
                bill.description129 = description129;
                switch(operationMode129){
                    case MODE_INSERT:
                            dbAdapter129.insertBill(bill);
                            Snackbar.make(coordinatorLayout129,"添加成功！！！",Snackbar.LENGTH_SHORT).show();
                            break;
                    case MODE_ALTER:
                        final int ALTER_ID = getIntent().getExtras().getInt("ALTER_ITEM_ID");
                            dbAdapter129.updateBill(ALTER_ID,bill);
                            Snackbar.make(coordinatorLayout129,"修改成功！！！",Snackbar.LENGTH_SHORT).show();
                            break;
                    default:
                            break;
                }

            dbAdapter129.closeDatabase();
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        onBackPressed();
                    }
                }, 1000);


            }
        });
        if(operationMode129 == MODE_ALTER)
            ItemDisplay();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
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
        this.finish();
    }

    public void ItemDisplay(){
        final int ALTER_ID = getIntent().getExtras().getInt("ALTER_ITEM_ID");
        Log.i("ALTER_ID",ALTER_ID + "");
        DBAdapter129 adapter129 = new DBAdapter129(RecordActivity129.this);
        adapter129.openDatabase();
        Bill[] billx = adapter129.queryBill(ALTER_ID);
        money129 = billx[0].money129;
        date129 = billx[0].date129;
        description129 = billx[0].description129;
        type129 = billx[0].type129;

        btn_dateSet129.setText("  日期                                       " + CommonHelper.dateToString(date129));
        btn_descSet129.setText("  备注                                        " + description129);

        int position = 0;
        for (int i = 0;i < CommonHelper.text_item129.length;i++){
            if(type129.equals(CommonHelper.text_item129[i])){
                position = i;
                break;
            }
        }
        if(money129 >= 0){
            switch129.setChecked(true);
        }else{
            switch129.setChecked(false);
            money129 = money129 * (-1.0f);

        }
        edit_money129.setText(money129 + "");
        Drawable ico129 = getResources().getDrawable(CommonHelper.icon_item129[position],null);
        ico129.setBounds(0,0,ico129.getMinimumWidth(),ico129.getMinimumHeight());          //设置图标大小
        edit_money129.setCompoundDrawables(ico129,null,null,null);
        adapter129.closeDatabase();
    }
}
