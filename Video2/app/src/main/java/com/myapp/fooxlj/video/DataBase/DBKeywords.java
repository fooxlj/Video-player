package com.myapp.fooxlj.video.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fooxlj on 23/04/2016.
 */
public class DBKeywords extends SQLiteOpenHelper {
    private static final String LOG = "Database_Keyword";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Keywords";

    // Table Names
    private static final String TABLE_KEYWORDS = "Keywords";

    private static final String KEY_KEYWORD = "keyword";
    private static final String KEY_DATE = "date";

    private static final String CREATE_TABLE_KEYWORDS = "CREATE TABLE "
            + TABLE_KEYWORDS + "("
            + KEY_KEYWORD + " VARCHAR,"
            + KEY_DATE + " VARCHAR)";

    public DBKeywords(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, CREATE_TABLE_KEYWORDS);
        // creating required tables
        db.execSQL(CREATE_TABLE_KEYWORDS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEYWORDS);
        // create new tables
        onCreate(db);
    }

    public void addKeyword(String keyword ) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        ContentValues values = new ContentValues();
        values.put(KEY_KEYWORD, keyword);
        values.put(KEY_DATE, currentDateandTime);

        db.insert(TABLE_KEYWORDS, null, values);
    }

    public boolean ifExist(String keyword){
        Boolean exist= false;
        String selectClause = KEY_KEYWORD + "= ?";
        String[] selectArgs = new String[] {
                keyword,
        };

        String[] columns = new String[]{
                KEY_KEYWORD
        };

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =db.query(TABLE_KEYWORDS, columns, selectClause, selectArgs, null, null, null);

        if (c.moveToFirst()){
            exist = true;
        }
        return exist;
    }

    public List<String> selectAllKeywords(){
        List<String> keywords = new ArrayList<String>();
        String selectQuery = "SELECT  "+KEY_KEYWORD+" FROM " + TABLE_KEYWORDS ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c!=null && c.moveToFirst()) {
            do {
                String keyword = c.getString(c.getColumnIndex(KEY_KEYWORD));
                keywords.add(keyword);
            } while (c.moveToNext());
        }
        return keywords;
    }

}
