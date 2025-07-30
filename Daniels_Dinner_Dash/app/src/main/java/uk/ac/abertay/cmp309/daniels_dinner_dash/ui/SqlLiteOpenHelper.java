package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class SqlLiteOpenHelper extends SQLiteOpenHelper {

    public SqlLiteOpenHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
    }

    // Abstract methods for onCreate and onUpgrade which must be implemented by subclasses
    @Override
    public abstract void onCreate(SQLiteDatabase db);

    @Override
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}

