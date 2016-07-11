package com.myapp.fooxlj.video.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myapp.fooxlj.video.Model.VideoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fooxlj on 26/04/2016.
 */
public class DBVideo extends SQLiteOpenHelper {
    private static final String LOG = "Database_Video";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Video";

    // Table Names
    private static final String TABLE_VIDEO_HISTORY = "Video_History";
    private static final String TABLE_VIDEO_FAVORITE = "Video_Favorite";

    //For VIDEO_HISTORY
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_THUMBNAILURL = "thumbnailURL";
    private static final String KEY_DATE_PUBLICATION = "date_publication";
    private static final String KEY_DATE_WATCHED = "date_watched";

    private static final String CREATE_TABLE_VIDEO_HISTORY = "CREATE TABLE "
            + TABLE_VIDEO_HISTORY + "("
            + KEY_ID + " VARCHAR,"
            + KEY_TITLE + " VARCHAR,"
            + KEY_DESCRIPTION + " VARCHAR,"
            + KEY_THUMBNAILURL + " VARCHAR,"
            + KEY_DATE_PUBLICATION + " VARCHAR,"
            + KEY_DATE_WATCHED + " VARCHAR)";


    private static final String CREATE_TABLE_VIDEO_FOVORATE = "CREATE TABLE "
            + TABLE_VIDEO_FAVORITE + "("
            + KEY_ID + " VARCHAR,"
            + KEY_TITLE + " VARCHAR,"
            + KEY_DESCRIPTION + " VARCHAR,"
            + KEY_THUMBNAILURL + " VARCHAR,"
            + KEY_DATE_PUBLICATION + " VARCHAR,"
            + KEY_DATE_WATCHED + " VARCHAR)";

    public DBVideo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, CREATE_TABLE_VIDEO_HISTORY);
        Log.d(LOG, CREATE_TABLE_VIDEO_FOVORATE);
        // creating required tables

        db.execSQL(CREATE_TABLE_VIDEO_HISTORY);
        db.execSQL(CREATE_TABLE_VIDEO_FOVORATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VIDEO_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VIDEO_FOVORATE);
        // create new tables
        onCreate(db);
    }

    public void deleteAll(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table,null,null);
    }

    public void addVideo(String table,VideoItem videoItem) {
    //    SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        ContentValues values = new ContentValues();
        if (ifExist(table,videoItem.getId()))
        {
            updateDate(table,currentDateandTime , videoItem.getId());
        }else{
            values.put(KEY_ID, videoItem.getId());
            values.put(KEY_TITLE, videoItem.getTitle());
            values.put(KEY_DESCRIPTION, videoItem.getDescription());
            values.put(KEY_THUMBNAILURL, videoItem.getThumbnailURL());
            values.put(KEY_DATE_PUBLICATION, videoItem.getDate_publication());
            values.put(KEY_DATE_WATCHED, currentDateandTime);

            db.insert(table, null, values);
        }
    }


    public List<VideoItem> getAllVideos(String table)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<VideoItem> videoItemList = new ArrayList<VideoItem>();

        String selectQuery = "SELECT  * FROM " + table ;
     //   SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c!=null && c.moveToFirst()) {
            do {
                VideoItem videoItem = new VideoItem();
                videoItem.setId(c.getString(c.getColumnIndex(KEY_ID)));;
                videoItem.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                videoItem.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                videoItem.setThumbnailURL(c.getString(c.getColumnIndex(KEY_THUMBNAILURL)));
                videoItem.setDate_publication(c.getString(c.getColumnIndex(KEY_DATE_PUBLICATION)));
                videoItem.setDate_watched(c.getString(c.getColumnIndex(KEY_DATE_WATCHED)));
                videoItemList.add(videoItem);
            } while (c.moveToNext());
        }
        return videoItemList;
    }

    private void updateDate(String table,String time,String id)
    {
        //update(String table, ContentValues values, String whereClause, String[] whereArgs)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_DATE_WATCHED, time);
        db.update(table, args, KEY_ID + "= ?" , new String[]{id}) ;
    }

    public boolean ifExist(String table,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean exist= false;
        String selectClause = KEY_ID+ "= ?";
        String[] selectArgs = new String[] {
                id,
        };

        String[] columns = new String[]{
                KEY_ID
        };
//        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =db.query(table, columns, selectClause, selectArgs, null, null, null);

        if (c.moveToFirst()){
            exist = true;
        }
        return exist;
    }
}
