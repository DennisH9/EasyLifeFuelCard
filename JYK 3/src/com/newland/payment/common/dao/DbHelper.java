package com.newland.payment.common.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;
import com.newland.payment.common.Const;
import com.newland.payment.mvc.model.BlackCard;
import com.newland.payment.mvc.model.CardBinA;
import com.newland.payment.mvc.model.CardBinB;
import com.newland.payment.mvc.model.CardBinC;
import com.newland.payment.mvc.model.EmvFailWater;
import com.newland.payment.mvc.model.ReverseWater;
import com.newland.payment.mvc.model.ScriptResult;
import com.newland.payment.mvc.model.Settlement;
import com.newland.payment.mvc.model.User;
import com.newland.payment.mvc.model.Water;
import com.newland.pos.sdk.util.LoggerUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{
	
	public static final String TAG = DbHelper.class.getName();
	
	public static final String DB_NAME = "payment.db";
	
	private static DbHelper helper;
	
	public static final int DB_VERSION = 1; // 数据库版本号

	private Context context;
	
	public static synchronized DbHelper getInstance(Context context) {
		if (helper == null) {
			helper = new DbHelper(context);
		}
		return helper;
	}

	private DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);//创建数据库
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "onCreate");
		try{

			db.execSQL(getCreateTableSql(User.class));
			db.execSQL(getCreateTableSql(Water.class));
			db.execSQL(getCreateTableSql(ScriptResult.class));
			db.execSQL(getCreateTableSql(ReverseWater.class));
			db.execSQL(getCreateTableSql(Settlement.class));
			db.execSQL(getCreateTableSql(EmvFailWater.class));
			db.execSQL(getCreateTableSql(BlackCard.class));
			db.execSQL(getCreateTableSql(CardBinA.class));
			db.execSQL(getCreateTableSql(CardBinB.class));
			db.execSQL(getCreateTableSql(CardBinC.class));
			
			initUersTable(db);
			initSettlementTable(db);
			initCarBinA(db);
			initCarBinB(db);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "数据库升级 old = "+oldVersion+", new = "+newVersion);
		switch (oldVersion) {
		case 2:
			
		}
	}

	/**
	 * 初始化操作员表中默认操作员
	 * @param db
	 */
	private void initUersTable(SQLiteDatabase db){
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('99','20100322','4')");
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('00','123456','2')");
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('99','00000000','3')");
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('01','0000','1')");
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('02','0000','1')");
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('03','0000','1')");
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('04','0000','1')");
		db.execSQL("INSERT INTO T_USER(USER_NO,PASSWORD,USER_TYPE) VALUES('05','0000','1')");
	}
	
	/**
	 * 初始化结算表数据
	 * @param db
	 */
	private void initSettlementTable(SQLiteDatabase db){
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('sale','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_sale','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale_off','0','0','0')");
		
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_auth_sale','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('refund','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('offline','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('adjust','0','0','0')");
		
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('ec_sale','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('emv_refund','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('cash_ecload','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_cash_ecload','0','0','0')");
		
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('not_bin_ecload','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('mag_cash_load','0','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('sale','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_sale','1','0','0')");
		
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('auth_sale_off','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_auth_sale','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('refund','1','0','0')");
		
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('offline','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('adjust','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('ec_sale','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('emv_refund','1','0','0')");
		
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('cash_ecload','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('void_cash_ecload','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('not_bin_ecload','1','0','0')");
		db.execSQL("INSERT INTO T_SETTLEMENT(TYPE,ORGANIZATION,AMOUNT,NUM) VALUES('mag_cash_load','1','0','0')");
	}
	
	private void initCarBinA(SQLiteDatabase db){
		
		try {
			Reader reader = new FileReader(Const.PathConst.SDCARD_PATH
						+ Const.FileConst.BINA);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String valueString = null;
			while((valueString = bufferedReader.readLine()) != null){
				LoggerUtils.e("BINA VALUE:" + valueString);
				LoggerUtils.e("BINA Len:" + valueString.length());
				String sql = "INSERT INTO T_Card_BIN_A(CARD_BIN) VALUES('" + valueString + "')";
				db.execSQL(sql);
			}
			bufferedReader.close();
			
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initCarBinB(SQLiteDatabase db){
		
		try {
			Reader reader = new FileReader(Const.PathConst.SDCARD_PATH
						+ Const.FileConst.BINB);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String valueString = null;
			while((valueString = bufferedReader.readLine()) != null){
				LoggerUtils.e("BINB VALUE:" + valueString);
				LoggerUtils.e("BINB Len:" + valueString.length());
				String sql = "INSERT INTO T_Card_BIN_B(CARD_BIN) VALUES('" + valueString + "')";
				db.execSQL(sql);
			}
			bufferedReader.close();
			
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getCreateTableSql(Class<?> clazz){
		Table table = clazz.getAnnotation(Table.class);
		String tableName = table.name();
		String idColumn = "";
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Column c = field.getAnnotation(Column.class);
			if (c != null && c.primaryKey()) {
				idColumn = c.name();
				break;
			}
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuffer.append(tableName);
        sqlBuffer.append(" ( ");

        sqlBuffer.append("\"").append(idColumn).append("\"  ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");

        fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Column c = field.getAnnotation(Column.class);
			if (c != null && !c.primaryKey()) {
				sqlBuffer.append("\"").append(c.name()).append("\"  ");
				
	            sqlBuffer.append(getDBColumnType(field));
	            
	            if (c.unique()) {
	                sqlBuffer.append(" UNIQUE");
	            }
	            sqlBuffer.append(",");
			}
		}
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(" )");
        
		return sqlBuffer.toString();
	}
	
	private static String getDBColumnType(Field field) {
		Class<?> javaType = field.getType();
		if (javaType == Integer.class) {
			return "INTEGER";
		} else if (javaType == Long.class) {
			return "INTEGER";
		} else if (javaType == String.class) {
			return "VARCHAR(" + field.getAnnotation(Column.class).len() + ")";
		} else {
			return "VARCHAR(" + field.getAnnotation(Column.class).len() + ")";
		}
	}
}
