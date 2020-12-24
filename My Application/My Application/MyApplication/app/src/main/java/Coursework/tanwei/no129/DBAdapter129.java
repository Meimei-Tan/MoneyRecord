package Coursework.tanwei.no129;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.FontsContract;

import androidx.annotation.Nullable;

import java.text.ParseException;

public  class DBAdapter129 {
    private static final String DB_NAME = "Account.db";
    private static final String TB_NAME = "Account";
    private static final int VERSION = 1;


    public  final String KEY_ID = "id";                       //id主键，自增  int
    public  final String KEY_TYPE = "type";                   //分类          String
    public  final String KEY_DATE = "time";                   //记录时间      date
    public  final String KEY_MONEY = "money";                 //金额          float
    public  final String KEY_DESCIPTION = "description";      //备注          text


    private final Context context129;
    private SQLiteDatabase database129;
    private DBOpenHelper dbOpenHelper129;
    public DBAdapter129(Context context){
        this.context129 = context;
    }

    class DBOpenHelper extends SQLiteOpenHelper{
            private String TB_CREATE = "CREATE TABLE " + TB_NAME + "(" +
                                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                        KEY_TYPE + " TEXT NOT NULL," +
                                        KEY_DATE + " TEXT NOT NULL," +
                                        KEY_MONEY + " FLOAT NOT NULL," +
                                        KEY_DESCIPTION + " TEXT NOT NULL);";
            public DBOpenHelper(Context context, String basename, SQLiteDatabase.CursorFactory cursor, int version){
                super(context,basename,cursor,version);
            }
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                        sqLiteDatabase.execSQL(TB_CREATE);
             }
            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
    }

    /*打开数据库*/
    public void openDatabase(){
        dbOpenHelper129 = new DBOpenHelper(context129,DB_NAME,null,VERSION);
        if(database129 == null){
            try{
                database129 = dbOpenHelper129.getWritableDatabase();
            }catch(SQLException e){
                database129 = dbOpenHelper129.getReadableDatabase();
            }
        }
    }

    /*关闭数据库*/
    public void closeDatabase(){
        if(database129 != null)
            database129.close();
    }


    /*添加记录*/
    public long insertBill(Bill bill){
            ContentValues newBill129 = new ContentValues();

            //newBill129.put(KEY_ID,bill.id129);
            newBill129.put(KEY_TYPE,bill.type129);
            newBill129.put(KEY_DATE, CommonHelper.dateToString(bill.date129));
            newBill129.put(KEY_MONEY,bill.money129);
            newBill129.put(KEY_DESCIPTION,bill.description129);



        return database129.insert(TB_NAME,null,newBill129);
    }

    /*删除指定记录*/
    public long deleteBill(int id){
       return  database129.delete(TB_NAME,KEY_ID + "=" + id,null);
    }

    /*查询指定记录*/
    public Bill[] queryBill(int id){
        String[] columns129 = new String[]{
                KEY_ID,KEY_TYPE,KEY_DATE,KEY_MONEY,KEY_DESCIPTION
        };
        Cursor cursor129 = database129.query(TB_NAME,columns129,KEY_ID + "=" + id,null,null,null,null);

        return ConvertToBill(cursor129);
    }

    /*查询具有指定条件的记录
    * selection: 条件语句  类似"A=? AND B=?"
    * args:填充selection中的?
    * */
    public Bill[] queryBills(@Nullable String selection,@Nullable String[] args,@Nullable String orderby){
        String[] columns129 = new String[]{
                KEY_ID,KEY_TYPE,KEY_DATE,KEY_MONEY,KEY_DESCIPTION
        };
        Cursor cursor129 = database129.query(TB_NAME,columns129,selection,args,null,null,orderby );
        if(cursor129 == null)
            return null;
        else
             return ConvertToBill(cursor129);
    }

    //按条件查询指定字段的值   (额度)
    public float[] queryByAttribute(@Nullable String[] columns, @Nullable String selection, @Nullable String[] args){
        Cursor cursor129 = database129.query(TB_NAME,columns,selection,args,null,null,null);

        int count = cursor129.getCount();
        if(count == 0 || !cursor129.moveToFirst())
            return new float[]{0.0f};
        float[] moneys = new float[count];

        for(int i = 0; i < count;i++){
            moneys[i] = cursor129.getFloat(0);
            cursor129.moveToNext();
        }
        return moneys;
    }

    public long updateBill(int id,Bill bill){
        ContentValues upBill129 = new ContentValues();
        upBill129.put(KEY_TYPE,bill.type129);

            upBill129.put(KEY_DATE, CommonHelper.dateToString(bill.date129));


        upBill129.put(KEY_MONEY,bill.money129);
        upBill129.put(KEY_DESCIPTION,bill.description129);

        return database129.update(TB_NAME,upBill129,KEY_ID + "=" + id,null);
    }



    public Bill[] ConvertToBill(Cursor cursor){
        int result129 = cursor.getCount();
        if(result129 == 0 || !cursor.moveToFirst())
            return null;
        Bill[] bills129 = new Bill[result129];
        for(int i = 0; i < result129;i++){
            Bill bill129 = new Bill();
            bill129.id129 = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            bill129.description129 = cursor.getString(cursor.getColumnIndex(KEY_DESCIPTION));
            bill129.money129 = cursor.getFloat(cursor.getColumnIndex(KEY_MONEY));
            bill129.type129 = cursor.getString(cursor.getColumnIndex(KEY_TYPE));
            try{
                bill129.date129 = CommonHelper.stringToDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            }catch(ParseException e){
                e.printStackTrace();
            }

            bills129[i] = bill129;
            cursor.moveToNext();
        }
        return bills129;
    }

}



