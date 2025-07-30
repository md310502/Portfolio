package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;
// Import required files
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {
// Create Database tables
    public static final String DATABASE_NAME = "dinnerdash.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1 = "USERID";
    public static final String COL_2 = "PHONE_NUMBER";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "ORDER_PACKAGE";
    private Context context;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override // Local database creation
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1 + " TEXT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT)");
        Log.d("MyDatabaseHelper", "Database created with table: " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.d("MyDatabaseHelper", "Database upgraded, old table dropped and new table created.");
    }
// inserts the phone number and password given
    public boolean insertData(String userId, String phoneNumber, String password, String orderPackage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, userId);
        contentValues.put(COL_2, phoneNumber);
        contentValues.put(COL_3, password);
        contentValues.put(COL_4, orderPackage);
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d("MyDatabaseHelper", "Data insertion: userId=" + userId + ", phoneNumber=" + phoneNumber + ", result=" + result);
        return result != -1;
    }
          // used to grab the phone number for sms text
    public String getPhoneNumber(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_2 + " FROM " + TABLE_NAME + " WHERE " + COL_1 + "=?", new String[]{userId});
        if (cursor != null && cursor.moveToFirst()) {
            String phoneNumber = cursor.getString(0);
            cursor.close();
            Log.d("MyDatabaseHelper", "Phone number fetched for userId=" + userId + ": " + phoneNumber);
            return phoneNumber;
        } else {
            Log.d("MyDatabaseHelper", "No phone number found for userId=" + userId);
            return null;
        }
    }
      // used to delete database on application shutdown
    public void deleteDatabase() {
        context.deleteDatabase(DATABASE_NAME);
        Log.d("MyDatabaseHelper", "Database deleted: " + DATABASE_NAME);
    }
}











