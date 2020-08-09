package fyp.water_delivery.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper {
    Context c;
    String Table_Name="shoppingcart";
    String[] pricecolumn={"Sum(price)"};
    String[] columns={"id","productname","price","image","quantity","userId","productid","weight"};
    String creat_table="create table shoppingcart (id integer primary key autoincrement,productname text,price integer,image blob,quantity integer,userId text,productid text,weight text);";
    public dbhelper(Context context) {
        super(context,"shoppingcartdb", null, 1);
        this.c=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(creat_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shoppingcart");
        onCreate(sqLiteDatabase);
    }
    public Boolean insert_product_toshoppingcart(String productname,int price,byte[] image,int quantity,String Username,String productid,String weight){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("productname ",productname);
        cv.put("price ",price);
        cv.put("image",image);
        cv.put("quantity",quantity);
        cv.put("userId",Username);
        cv.put("productid",productid);
        cv.put("weight",weight);
        long i= db.insert("shoppingcart",null,cv);
        if(i==-1){
            return false;
        }else{
            return true;
        }

    }
    public Cursor get_products_in_cart(String Username)throws SQLException {
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("shoppingcart",columns,"userId = ?",new String[]{Username},null,null,null,null);
        return products;
    }
    public int get_num_of_rows(String Username){
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("shoppingcart",columns,"userId = ?",new String[]{Username},null,null,null,null);
        return products.getCount();
    }
    public int check_if_already_exist(String Username,String productId){
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("shoppingcart",columns,"userId = ? and  productid = ?",new String[]{Username,productId},null,null,null,null);
        return products.getCount();
    }
    public int getTotalOfAmount(String Username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query("shoppingcart",pricecolumn,"userId = ?",new String[] {Username},null,null,null,null);
        cur.moveToFirst();
        int i = cur.getInt(0);
        cur.close();
        return i;
    }
    public Integer delete(String id){
        SQLiteDatabase sdb = this.getReadableDatabase();
        return sdb.delete("shoppingcart", "productid = ?", new String[] {id});
    }
    public void delete_all(String Username){
        SQLiteDatabase sdb = this.getReadableDatabase();

        sdb.delete("shoppingcart", "userId = ?", new String[] {Username});
    }
    public int updateProduct(String productId,int price,int quantity,String userId){
        SQLiteDatabase sdb = this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("quantity",quantity);
        cv.put("price",price);
        return sdb.update("shoppingcart",cv,"productid = ? and userId = ?",new String[]{productId,userId});
    }

}
