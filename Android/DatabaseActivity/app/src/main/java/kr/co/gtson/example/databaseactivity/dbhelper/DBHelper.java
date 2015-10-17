package kr.co.gtson.example.databaseactivity.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import kr.co.gtson.example.databaseactivity.dbhelper.model.Item;

/**
 * Created by genteelson on 2015-04-02.
 */
public class DBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Database.db";
    // Table name
    private static final String TABLE_NAME = "table_name";
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }
    // Upgrading Tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        DB 버전이 업그레이드 되었을 때 실행되는 메소드
        이 부분은 사용에 조심해야 하는 일이 많이 있다. 버전이 1인 사용자가 2로 바뀌면
        한번의 수정만 하면 되지만, 버전이 3이 되면 1인 사용자는 2,3을 거쳐야 하고,
        2인 사용자는 3만 거치면 된다. 이렇게 증가할수록 수정할 일이 많아지므로
        적절히 사용해야 하며 가능하면 최초 설계를 잘하는 것이 좋다.
         */
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    /**
     * CRUD 함수
     */

    // 새로운 Item 추가
    public void insertItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, item.getName()); // Name

        // Inserting Row
        db.insert(TABLE_NAME, null, contentValues);
        db.close(); // Closing database connection
    }

    // Item 정보 업데이트
    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, item.getName());

        // updating row
        return db.update(TABLE_NAME, contentValues, KEY_ID + " = ?", new String[] { String.valueOf(item.getId()) });
    }

    // Item 정보 삭제하기
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(item.getId()) });
        db.close();
    }

    // id 에 해당하는 Item 객체 가져오기
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,  KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        // return item
        return item;
    }

    // 모든 Item 정보 가져오기
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getString(1));
                // Adding contact to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return item list
        return itemList;
    }

    // Item 정보 숫자
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
