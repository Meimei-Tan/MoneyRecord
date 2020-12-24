package Coursework.tanwei.no129;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity129 extends AppCompatActivity {
    private String[] items = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private ArrayList<Map<String, Object>> list_data129 = new ArrayList<>();     //用于存储要加入到ListView中的Item
    private static String[] keys = {"icon", "type", "date", "money"};                  //数据Item在ListView中显示的内容的键
    private static int[] item_ids = {R.id.image_detail129, R.id.text_typeDetail129, R.id.text_dateDetail129, R.id.text_MoneyDetail129};    //对应的控件ID

    private Bill[] bills;   //用于存放从数据库查询到的数据
    private SimpleAdapter simpleAdapter129; //ListView适配器
    private Date date;

    private static final int MODE_INSERT = 1;     //插入模式
    private static final int MODE_ALTER = 2;        //修改模式

    //控件
    private ListView listView129 = null;
    private Spinner spinner129 = null;
    private TextView incomeToday129 = null;
    private TextView outcomeToday129 = null;
    private TextView incomeMonth129 = null;
    private TextView outcomeMonth129 = null;
    private CoordinatorLayout coordinatorLayout129 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar129 = findViewById(R.id.toolbar129);       //获取布局Toolbar
        setSupportActionBar(toolbar129);                          //使用Toolbar 代替actionbar
        FloatingActionButton fab129 = findViewById(R.id.fab129);  //获取FloatingActionButton控件和TabLayout,ListView
        spinner129 = findViewById(R.id.spinner129);
        listView129 = findViewById(R.id.list_view129);
        incomeToday129 = findViewById(R.id.income_today);
        outcomeToday129 = findViewById(R.id.outcome_today);
        incomeMonth129 = findViewById(R.id.income_month);
        outcomeMonth129 = findViewById(R.id.outcome_month);
        coordinatorLayout129 = findViewById(R.id.coordinator129);


        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity129.this, android.R.layout.simple_list_item_1, items);
        spinner129.setAdapter(adapter);


        spinner129.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int month = position + 1;
                listView129.setAdapter(null);
                data_display(month);
                String data_string = "2019-" + month + "-01";
                try{
                    date = CommonHelper.stringToDate(data_string);
                }catch (Exception e){
                    e.printStackTrace();
                }
                getAccountMonth(date);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView129.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int alter_id = Integer.parseInt(list_data129.get(position).get("id").toString());
                Intent alter_intent = new Intent(MainActivity129.this,RecordActivity129.class);
                alter_intent.putExtra("ALTER_ITEM_ID",alter_id);
                alter_intent.putExtra("MODE_TYPE",MODE_ALTER);
                startActivity(alter_intent);
                MainActivity129.this.onStop();
            }
        });
        //FloatingActionButton单击事件
        fab129.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*  CommonHelper.currentItemViewPager = viewPager129.getCurrentItem();*/
                Intent record_act129 = new Intent(MainActivity129.this, RecordActivity129.class);
                record_act129.putExtra("MODE_TYPE", MODE_INSERT);
                startActivity(record_act129);
                MainActivity129.this.onStop();
            }
        });

        this.registerForContextMenu(listView129);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE,11,Menu.NONE,"删除");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == 11){
            AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = menuinfo.position;
            int id_altere = Integer.parseInt(list_data129.get(position).get("id").toString());
            DBAdapter129 adapter129 = new DBAdapter129(MainActivity129.this);
            adapter129.openDatabase();
            adapter129.deleteBill(id_altere);
            list_data129.remove(position);
            simpleAdapter129.notifyDataSetChanged();
            Log.i("DELETE_ID",id_altere + "");
            adapter129.closeDatabase();
            Snackbar.make(coordinatorLayout129,"删除成功！！！",Snackbar.LENGTH_SHORT).show();
            getAccountToday();
            getAccountMonth(date);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Calendar  calendar = Calendar.getInstance();
        spinner129.setSelection(calendar.get(Calendar.MONTH));
        getAccountToday();

    }

    @Override
    protected void onResume() {
        super.onResume();
        data_display(12);
        getAccountToday();
    }

    @Override
    protected void onRestart() {

        super.onRestart();


    }

    private void data_display(int month) {
        DBAdapter129 adapter129 = new DBAdapter129(MainActivity129.this);
        adapter129.openDatabase();
        list_data129.clear();
        if (month < 10) {
            bills = adapter129.queryBills("time BETWEEN " + "'2019-0" + month + "-01' AND " + "'2019-0" + month + "-31'", null, "time DESC,id DESC");    //查询所有bills
        } else {
            bills = adapter129.queryBills("time BETWEEN " + "'2019-" + month + "-01' AND " + "'2019-" + month + "-31'", null, "time  DESC,id DESC");    //查询所有bills
        }

        if (bills != null) {
            getData(bills);
            simpleAdapter129 = new SimpleAdapter(MainActivity129.this, list_data129, R.layout.details_item129, keys, item_ids);
            listView129.setAdapter(simpleAdapter129);
        } else
            Log.d("Bills.Dataxxx", "Null " + month);
        adapter129.closeDatabase();
    }

    public void getData(Bill[] bills) {

        for (int i = 0; i < bills.length; i++) {
            Map<String, Object> itemMap = new HashMap<>();
            //设置图标

            for (int j = 0; j < CommonHelper.text_item129.length; j++) {
                if (CommonHelper.text_item129[j].equals(bills[i].type129.trim())) {
                    itemMap.put("icon", CommonHelper.icon_item129[j]);
                    break;
                }
            }
            itemMap.put("type", bills[i].type129);
            itemMap.put("date", CommonHelper.dateToString(bills[i].date129));
            itemMap.put("money", bills[i].money129);
            itemMap.put("id", bills[i].id129);
            list_data129.add(itemMap);
        }
    }


    private void getAccountToday(){
        DBAdapter129 adapter129 = new DBAdapter129(MainActivity129.this);
        adapter129.openDatabase();
        Date date = new Date();
        Log.i("timeDate",CommonHelper.dateToStringEndWithMONTH(date));
        float[] moneys;
        String[] columns = new String[]{ "SUM(money)"};
        //今日收入
        moneys = adapter129.queryByAttribute(columns,adapter129.KEY_DATE  + " LIKE '" + CommonHelper.dateToString(date) +"' AND " + adapter129.KEY_MONEY + " > 0;",null);
        incomeToday129.setText(Html.fromHtml("今日收入：<br/>   <font color='#4876FF'>" + moneys[0] + "</font>"));
        //今日支出
        moneys = adapter129.queryByAttribute(columns,adapter129.KEY_DATE  + " LIKE  '" + CommonHelper.dateToString(date) +"' AND " + adapter129.KEY_MONEY + " < 0;",null);
        outcomeToday129.setText(Html.fromHtml("今日支出：<br/>   <font color='#CD0000'>" + moneys[0] + "</font>"));
        adapter129.closeDatabase();
    }

    private  void getAccountMonth(Date date){
        DBAdapter129 adapter129 = new DBAdapter129(MainActivity129.this);
        adapter129.openDatabase();
        float[] moneys;
        String[] columns = new String[]{"SUM(money)"};
        //当前月收入
        moneys = adapter129.queryByAttribute(columns,adapter129.KEY_MONEY +" > 0"+ " AND " + adapter129.KEY_DATE + " LIKE '" + CommonHelper.dateToStringEndWithMONTH(date)+ "-%';",null);
        Log.d("month_day",CommonHelper.dateToStringEndWithMONTH(date));
        Log.d("moneyIncomeMonth",moneys[0] + "");
        incomeMonth129.setText(Html.fromHtml(CommonHelper.dateToStringEndWithMONTH(date) + "收入：<br/>   <font color='#4876FF'>" + moneys[0] + "</font>"));
        // 当前月支出
        moneys = adapter129.queryByAttribute(columns,adapter129.KEY_MONEY +" < 0"+ " AND " + adapter129.KEY_DATE + " LIKE '" + CommonHelper.dateToStringEndWithMONTH(date) + "-%';",null);
        outcomeMonth129.setText(Html.fromHtml(CommonHelper.dateToStringEndWithMONTH(date) + "支出：<br/>   <font color='#CD0000'>" + moneys[0] + "</font>"));
        adapter129.closeDatabase();
    }
}