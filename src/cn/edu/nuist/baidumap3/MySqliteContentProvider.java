package cn.edu.nuist.baidumap3;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MySqliteContentProvider extends ContentProvider {
	
	private SQLiteDatabase database=null;

	@Override
	public boolean onCreate() {
		MySqliteOpenHelper helpler = new MySqliteOpenHelper(this.getContext(), null, null, 1);
		database = helpler.getWritableDatabase();
		if(database==null)
			return false;
		return true;
	} 

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor=database.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, null);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//1.newId可以判定添加是否成功
		//2.重新呈现添加成功的信息，呈现数据的准确性
		//content://cn.edu.nuist.MySqliteContentProvider/user
		Uri newUri=uri;
		//content://cn.edu.nuist.MySqliteContentProvider/user/1
		long newId = database.insert(uri.getLastPathSegment(), null, values);//1
		newUri = ContentUris.withAppendedId(uri, newId);
		if(newId>0)
			return newUri;
		else
			return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.v("database sql:", uri.getLastPathSegment()+","+selection+","+selectionArgs);
		return database.delete(uri.getLastPathSegment(), selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return database.update(uri.getLastPathSegment(), values, selection, selectionArgs);
	}
}
