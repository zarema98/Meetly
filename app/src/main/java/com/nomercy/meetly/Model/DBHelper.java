package com.nomercy.meetly.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nomercy.meetly.Model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String DATABASE_NAME = "meetlyDb.db";
    public static final int DATABASE_VERSION = 2;

    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";

    private final String TBL_USERS = "users";

    private final String COL_ID = "id";
    private final String COL_TOKEN = "token";
    private final String COL_AUTH = "auth";
    private final String COL_SEEN = "seen";
    private final String COL_NAME = "name";
    private final String COL_SURNAME = "surname";

    public SQLiteDatabase mDB;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
        DATABASE_LOCATION = "data/data/" + mContext.getPackageName() + "/databases/";
        // DATABASE_LOCATION = mContext.getFilesDir().getPath() + mContext.getPackageName() + "/databases/";
        // DATABASE_LOCATION = mContext.getFilesDir().getPath();
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;

        if(!isExistingDB()) {
            try {
                File dbLocation = new File(DATABASE_LOCATION);
                dbLocation.mkdirs();
                extractAssetToDatabaseDirectory(DATABASE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);

    }

    boolean isExistingDB() {
        File file  = new File(DATABASE_FULL_PATH);
        return file.exists();
    }


    public void extractAssetToDatabaseDirectory(String fileName) throws IOException{
        int length;
        InputStream sourceDatabase = this.mContext.getAssets().open(fileName);
        File destinationPath = new File(DATABASE_FULL_PATH);
        OutputStream destination = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[32768];
        while ((length = sourceDatabase.read(buffer)) > 0 ) {
            destination.write(buffer, 0, length);
        }
        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    public boolean isEmpty () {
        String q = "SELECT count(*) FROM users";
        Cursor result = mDB.rawQuery(q, null);
        result.moveToFirst();
        int count = result.getInt(0);
        if(count > 0) return false;
        else return true;
    }

        public void addUser(int id, String token, int auth) {
            try {
                String q = "INSERT INTO users(["+COL_ID+"], ["+COL_TOKEN+"], ["+COL_AUTH+"]) VALUES (?, ?, ?);";
                mDB.execSQL(q, new Object[]{id, token, true});
            } catch (SQLException ex) {

            }
        }



        public String getUserName (int id) {
            String q = "SELECT name FROM users WHERE upper([id]) = "+id+" ";
            Cursor result = mDB.rawQuery(q, null);
           String name = "";
            while(result.moveToNext()) {
                name = result.getString(result.getColumnIndex(COL_ID));
            }
            return name;
        }

    public String getUserSurname (int id) {
        String q = "SELECT surname FROM users WHERE upper([id]) = "+id+" ";
        Cursor result = mDB.rawQuery(q, null);
        String surname = "";
        while(result.moveToNext()) {
            surname = result.getString(result.getColumnIndex(COL_ID));
        }
        return surname;
    }

        public void deleteUser(int id) {
            try {
                String q = "DELETE FROM users WHERE upper([id]) = upper(?)";
                mDB.execSQL(q, new Object[]{id});
            } catch (SQLException ex) {

            }
        }

    public void addInfo (String name, String surname, int id) {
        try {
            String q = "UPDATE  users SET "+COL_NAME+" = (?), "+COL_SURNAME+" = (?) WHERE id = (?)";
            mDB.execSQL(q, new Object[]{name, surname, id});
        } catch (SQLException ex) {

        }
    }

        public void updateSeen (int id) {
            try {
                String q = "UPDATE  users SET "+COL_SEEN+" = (?) WHERE id = "+id+" ";
               mDB.execSQL(q, new String[]{String.valueOf(1)});
            } catch (SQLException ex) {

            }
        }

        public int getAuth () {
            String q = "SELECT `auth` FROM users";
            Cursor result = mDB.rawQuery(q, null);
            int  auth = 0;
            if(result.getCount() <= 0) {
                auth = 0;
            } else {
                while(result.moveToNext()) {
                    auth = result.getInt(result.getColumnIndex(COL_AUTH));
                }
            }
            return auth;
        }

        public User getUser (int id) {
            String q = "SELECT * FROM users WHERE upper([id]) = upper(?)";
            Cursor result = mDB.rawQuery(q, new String[]{String.valueOf(id)});
            User user = null;
            while(result.moveToNext()) {
                user = new User();
                user.id = result.getInt(result.getColumnIndex(COL_ID));
                user.token = result.getString(result.getColumnIndex(COL_TOKEN));
            }
            return user;

        }

    public String  getToken (int id) {
        String q = "SELECT token FROM users WHERE upper([id]) = upper(?)";
        Cursor result = mDB.rawQuery(q, new String[]{String.valueOf(id)});
        String token="";
        while(result.moveToNext()) {
            token = result.getString(result.getColumnIndex(COL_TOKEN));
        }
        return token;

    }

    public void updateAuth () {
        try {
            String q = "UPDATE  users SET "+COL_AUTH+" = uppper(?)";
            Cursor result = mDB.rawQuery(q, new String[]{String.valueOf(1)});
        } catch (SQLException ex) {

        }
    }

    public int getId () {
        String q = "SELECT id FROM users";
        Cursor result = mDB.rawQuery(q, null);
        int  id = 0;
        while(result.moveToNext()) {
            id = result.getInt(result.getColumnIndex(COL_ID));
        }
        return id;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
