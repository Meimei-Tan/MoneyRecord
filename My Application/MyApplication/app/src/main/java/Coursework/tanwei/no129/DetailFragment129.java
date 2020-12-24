package Coursework.tanwei.no129;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailFragment129 extends Fragment {
    public  ArrayList<Map<String,Object>> list_items129;
    private static String[] keys = {"icon","type","date","money"};
    private static int[] item_ids = {R.id.image_detail129,R.id.text_typeDetail129,R.id.text_dateDetail129,R.id.text_MoneyDetail129};
    private int month = 0;          //传入一个值表示该Fragment记录的月份
    private static final int MODE_ALTER = 2;      //修改模式
    public  Bill[] bills;
    private SimpleAdapter simpleadapter129; // ListView数据适配器

    private  ListView listView129;

    private static int id_alter;       //要改删的记录id；
    private static int position_delete; //删除的item的postion
    private Context context;
    private boolean isInit = false;

    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            final int month = Integer.parseInt(bundle.get("month").toString()) ;
            this.month = month;
        }
        isInit = true;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail129, container, false);



        listView129 = view.findViewById(R.id.list_view129);




        list_items129 = new ArrayList<>();

        DBAdapter129 adapter129 = new DBAdapter129(context);
        adapter129.openDatabase();
        if(month < 10){
            bills = adapter129.queryBills("time BETWEEN " + "'2019-0" + month + "-01' AND " + "'2019-0" + month + "-31'" ,null,"time DESC");    //查询所有bills
        }else{
            bills = adapter129.queryBills("time BETWEEN " + "'2019-" + month + "-01' AND " + "'2019-" + month + "-31'" ,null,"time DESC");    //查询所有bills
        }



      listView129.setOnItemClickListener(new ListView.OnItemClickListener(){
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
              //获取所选Item对应的记录的id
              id_alter = Integer.parseInt(list_items129.get(position).get("id").toString());
              Log.i("id_alterxx",id_alter+ "");
              //打开编辑页
              Intent alter_intent = new Intent(DetailFragment129.this.getContext(),RecordActivity129.class);
              alter_intent.putExtra("MODE_TYPE",MODE_ALTER);
              alter_intent.putExtra("ALTER_ITEM_ID",id_alter);
              startActivity(alter_intent);

          }
      });



        this.registerForContextMenu(listView129);
        return view;
    }

    public static DetailFragment129 newInstance(int month) {

        Bundle args = new Bundle();
        args.putInt("month",month);
        DetailFragment129 fragment = new DetailFragment129();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 11, Menu.NONE, "删除");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint() && isInit){
            //明细列表显示;
            requestData();
        }else{
            isInit = false;
        }

    }



    public void requestData(){

        if(bills != null){
            getData(bills);
            simpleadapter129 = new SimpleAdapter(DetailFragment129.this.getContext(),list_items129,R.layout.details_item129,keys,item_ids);
            listView129.setAdapter(simpleadapter129);
        }else
            Log.d("Bills.Data","Null");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getItemId() == 11 && list_items129.size() != 0){
            DBAdapter129 dbAdapter129 = new DBAdapter129(DetailFragment129.this.getContext());
            dbAdapter129.openDatabase();
            Log.i("FRAGMENT",month + "");
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            position_delete = menuInfo.position;
            id_alter = Integer.parseInt(list_items129.get(position_delete).get("id").toString());
            dbAdapter129.deleteBill(id_alter);
            list_items129.remove(position_delete);
            simpleadapter129 = new SimpleAdapter(DetailFragment129.this.getContext(),list_items129,R.layout.details_item129,keys,item_ids);
            listView129.setAdapter(simpleadapter129);
            }


            Log.i("DeleteSuccess","success");


        return true;
    }



    @Override
    public void onResume() {
        super.onResume();
        isInit = true;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }










    public void getData(Bill[] bills){

        for(int i = 0;i < bills.length;i++){
            Map<String,Object> itemMap = new HashMap<>();
            //设置图标

            for (int j = 0;j < CommonHelper.text_item129.length;j++ ) {
                if(CommonHelper.text_item129[j].equals(bills[i].type129.trim())){
                    itemMap.put("icon",CommonHelper.icon_item129[j]);
                    break;
                }
            }
            itemMap.put("type",bills[i].type129);
            itemMap.put("date",CommonHelper.dateToString(bills[i].date129));
            itemMap.put("money",bills[i].money129);
            itemMap.put("id",bills[i].id129);
            list_items129.add(itemMap);
        }

    }


    }

