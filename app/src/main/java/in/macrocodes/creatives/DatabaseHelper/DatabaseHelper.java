package in.macrocodes.creatives.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import in.macrocodes.creatives.Models.TrendingProducts;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "liked";
    private static final String TABLE_NAME = "products";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " + TABLE_NAME + "(id TEXT ,pid TEXT PRIMARY KEY, name TEXT," +
                "description TEXT,price TEXT,amazon TEXT,flipkart TEXT,image TEXT,category TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean addText(TrendingProducts trendingProducts){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pid", trendingProducts.getId());
        contentValues.put("name", trendingProducts.getName());
        contentValues.put("description", trendingProducts.getDescription());
        contentValues.put("price", trendingProducts.getPrice());
        contentValues.put("amazon", trendingProducts.getAmazon());
        contentValues.put("flipkart", trendingProducts.getFlipkart());
        contentValues.put("image", trendingProducts.getImage());
        contentValues.put("category", trendingProducts.getCategory());

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return true;
    }

    public boolean deleteRow(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "pid=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        db.delete(TABLE_NAME, whereClause, whereArgs);
        return true;
    }
    public ArrayList<TrendingProducts> getAllData(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<TrendingProducts> arrayList= new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TrendingProducts trendingProducts = new TrendingProducts(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("price")),cursor.getString(cursor.getColumnIndex("image")),
                            cursor.getString(cursor.getColumnIndex("pid")),cursor.getString(cursor.getColumnIndex("amazon")),
                                    cursor.getString(cursor.getColumnIndex("flipkart")),cursor.getString(cursor.getColumnIndex("category")));
            arrayList.add(trendingProducts);
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }
}
