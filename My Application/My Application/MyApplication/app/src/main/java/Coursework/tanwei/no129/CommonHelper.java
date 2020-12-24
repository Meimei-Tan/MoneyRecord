package Coursework.tanwei.no129;

import android.widget.SimpleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CommonHelper {
    //public static int currentItemViewPager = 0;
    public static String[] text_item129 = new String[] {"一般","食品","住房","交通","医疗","工资","投资","奖励","礼物","服饰"};      //分类项目名称
    public static int[] icon_item129 = new int[]{R.drawable.ico_star,R.drawable.ico_food,R.drawable.ico_house,R.drawable.ico_traffic,R.drawable.ico_hospital,R.drawable.ico_salary,R.drawable.ico_amex,R.drawable.ico_reward,R.drawable.ico_present,R.drawable.ico_cloth};    //分类项目图标

    /*将date转换成指定格式的字符串*/
    public static  String dateToString(Date date){
        SimpleDateFormat formatter129 = new SimpleDateFormat("yyyy-MM-dd");
        String date_string = formatter129.format(date);
        return date_string;
    }

    public static String dateToStringEndWithMONTH(Date date){
        SimpleDateFormat formatter129 = new SimpleDateFormat("yyyy-MM");
        String date_string = formatter129.format(date);
        return date_string;
    }
    /*将stirng转换成指定格式的date*/
    public static Date stringToDate(String date_str) throws ParseException{
        SimpleDateFormat formatter129 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter129.parse(date_str);
        return date;
    }

}
