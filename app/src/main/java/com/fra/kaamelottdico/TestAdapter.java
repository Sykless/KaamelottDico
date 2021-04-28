package com.fra.kaamelottdico;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestAdapter {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public TestAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public TestAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public TestAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor findRepliqueWithKeyword(String keyword) {
        try {
            String sql ="SELECT * FROM DICO_REPLIQUE WHERE REPLIQUE LIKE '%" + keyword + "%'";
            return mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor findAllCharacters() {
        try {
            String sql ="SELECT * FROM DICO_PERSONNAGE ORDER BY PERSONNAGE";
            return mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "findAllCharacters >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}