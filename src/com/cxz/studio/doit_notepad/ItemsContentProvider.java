package com.cxz.studio.doit_notepad;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ItemsContentProvider extends ContentProvider {
	
	public static final String AUTHORITY_STR = 
			"com.cxz.studio.doit_notepad.itemsprovider";
	public static final String PATH_STR = "items";
	public static final Uri CONTENT_URI = 
			Uri.parse("content://"+AUTHORITY_STR + "/" + PATH_STR);
	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DETAILS = "details";
	public static final String KEY_STATUS = "status";
	public static final String KEY_CREATE_TIME = "create_time";
	public static final String KEY_FINISH_TIME = "finish_time";
	public static final String KEY_ALARM = "alarm";
	public static final String KEY_CATEGORY = "category";
	
	private MySQLiteOpenHelper myOpenHelper;
	
	@Override
	public boolean onCreate() {
		myOpenHelper = new MySQLiteOpenHelper(
				getContext(), 
				MySQLiteOpenHelper.DATABASE_NAME, 
				null, 
				MySQLiteOpenHelper.DATABASE_VERSION);
		return true;
	}
	
	private static final int ALL_ITMES = 1;
	private static final int SINGLE_ITEM = 2;
	
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY_STR, PATH_STR, ALL_ITMES);
		uriMatcher.addURI(AUTHORITY_STR, PATH_STR + "/#", SINGLE_ITEM);
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
	      case ALL_ITMES: return "vnd.android.cursor.dir/vnd.cxz.studio.items";
	      case SINGLE_ITEM: return "vnd.android.cursor.item/vnd.cxz.studio.items";
	      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
	    }
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		
		String groupBy = null;
	    String having = null;
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    queryBuilder.setTables(MySQLiteOpenHelper.ITEMS_TABLE);
	    
	    switch (uriMatcher.match(uri)) {
		case SINGLE_ITEM:
			String rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(KEY_ID + "=" + rowID);
			break;

		default:
			break;
		}
	    
	    Cursor cursor = queryBuilder.query(
	    		db, 
	    		projection, 
	    		selection, 
	    		selectionArgs, 
	    		groupBy, 
	    		having, 
	    		sortOrder);
		return cursor;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Open a read / write database to support the transaction.
	    SQLiteDatabase db = myOpenHelper.getWritableDatabase();
	    
	    // If this is a row URI, limit the deletion to the specified row.
	    switch (uriMatcher.match(uri)) {
	      case SINGLE_ITEM : 
	        String rowID = uri.getPathSegments().get(1);
	        selection = KEY_ID + "=" + rowID
	            + (!TextUtils.isEmpty(selection) ? 
	              " AND (" + selection + ')' : "");
	      default: break;
	    }
	    
	    // To return the number of deleted items, you must specify a where
	    // clause. To delete all rows and return a value, pass in "1".
	    if (selection == null)
	      selection = "1";

	    // Execute the deletion.
	    int deleteCount = db.delete(MySQLiteOpenHelper.ITEMS_TABLE, selection, selectionArgs);
	    
	    // Notify any observers of the change in the data set.
	    getContext().getContentResolver().notifyChange(uri, null);
	    
	    return deleteCount;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Open a read / write database to support the transaction.
	    SQLiteDatabase db = myOpenHelper.getWritableDatabase();
	    
	    // To add empty rows to your database by passing in an empty Content Values
	    // object, you must use the null column hack parameter to specify the name of
	    // the column that can be set to null.
	    String nullColumnHack = null;
	    
	    // Insert the values into the table
	    long id = db.insert(MySQLiteOpenHelper.ITEMS_TABLE, 
	        nullColumnHack, values);
	    
	    if (id > -1) {
	      // Construct and return the URI of the newly inserted row.
	      Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
	      
	      // Notify any observers of the change in the data set.
	      getContext().getContentResolver().notifyChange(insertedId, null);
	      
	      return insertedId;
	    }
	    else
	      return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// Open a read / write database to support the transaction.
	    SQLiteDatabase db = myOpenHelper.getWritableDatabase();
	    
	    // If this is a row URI, limit the deletion to the specified row.
	    switch (uriMatcher.match(uri)) {
	      case SINGLE_ITEM : 
	        String rowID = uri.getPathSegments().get(1);
	        selection = KEY_ID + "=" + rowID
	            + (!TextUtils.isEmpty(selection) ? 
	              " AND (" + selection + ')' : "");
	      default: break;
	    }

	    // Perform the update.
	    int updateCount = db.update(MySQLiteOpenHelper.ITEMS_TABLE, 
	      values, selection, selectionArgs);

	    // Notify any observers of the change in the data set.
	    getContext().getContentResolver().notifyChange(uri, null);

	    return updateCount;
	}

	private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "doit_notepad.db";
		private static final int DATABASE_VERSION = 1;
	    private static final String ITEMS_TABLE = "tb_items";
	    
	    private static final String CREATE_TABLE_SQL = "create table "
	    		+ ITEMS_TABLE + " (" 
	    		+ KEY_ID + " integer primary key autoincrement, "
	    		+ KEY_TITLE + " text not null, " 
	    		+ KEY_DETAILS + " text, " 
	    		+ KEY_STATUS + " integer,"
	    		+ KEY_CREATE_TIME + " bigint,"
	    		+ KEY_FINISH_TIME + " bigint,"
	    		+ KEY_ALARM + " bigint,"
	    		+ KEY_CATEGORY + " integer);"; 

		public MySQLiteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
			db.execSQL("DROP TABLE IF IT EXIST " + ITEMS_TABLE);
			onCreate(db);
		}
		
	}
	
}
