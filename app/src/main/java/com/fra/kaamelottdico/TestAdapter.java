package com.fra.kaamelottdico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public Cursor findRepliqueWithFilters(String keyword, List<String> characters, List<String> livres, List<String> episodes) {
        try {
            String characterFilter = convertListToString(characters,"PERSONNAGE");
            String livreFilter = convertListToString(livres, "LIVRE");
            String episodeFilter = convertListToString(episodes, "EPISODE");

            String sql = "SELECT * FROM DICO_REPLIQUE WHERE REPLIQUE LIKE '%" + keyword.replace("'","''") + "%'" + characterFilter + livreFilter + episodeFilter + " ORDER BY LIVRE, EPISODE, REPLIQUE_ID";
            return mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor findCharacter(String character) {
        try {
            String sql = "SELECT * FROM DICO_PERSONNAGE WHERE PERSONNAGE = '" + character + "'";
            return mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor findEpisode(int livre, int episode) {
        try {
            String sql ="SELECT * FROM DICO_EPISODE WHERE LIVRE = " + livre + " AND EPISODE = " + episode;
            Cursor cursor = mDb.rawQuery(sql, null);
            if (cursor != null) {
                cursor.moveToNext();
            }
            return cursor;
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

    private String convertListToString(List<String> list, String filterType) {
        if (list.size() > 0) {
            String sqlFilter = " AND " + filterType + " IN (";

            for (int i = 0 ; i < list.size() ; i++) {
                sqlFilter += "'" + list.get(i).replace("'","''")  + "'";

                if (i != list.size() - 1) {
                    sqlFilter += ",";
                }
            }

            return sqlFilter + ")";
        } else {
            return "";
        }
    }
}