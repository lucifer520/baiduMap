package cn.edu.nuist.baidumap3;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
public class MySqliteOpenHelper extends SQLiteOpenHelper {
	private static String DB_NAME="baidumap2.db";
	private String userSql="create table if not exists user" +
			"(_id integer primary key autoincrement," +
			"username text not null," +
			"pwd text," +
			"sex text," +
			"interest text," +
			"address text," +
			"birthday text)";
	private String editorSql="create table if not exists editor"+
			"(diaryId integer primary key autoincrement,"+
			"username text not null," +
			"diaryDay text," +
			"diaryAddress text," +
			"diaryAuthor text," +
			"diaryContent text," +
			"cate text)";
	private String cateSql="create table if not exists cates"+
			"(diaryId integer primary key autoincrement,"+
			"cate text not null)";
	public MySqliteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DB_NAME, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(userSql);
		db.execSQL(editorSql);
		db.execSQL(cateSql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql="drop table if exists user";
		db.execSQL(sql);
		onCreate(db);
	}
}
