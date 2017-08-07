package com.newland.base.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lidroid.xutils.exception.DbException;
import com.newland.base.dao.ann.Column;
import com.newland.base.dao.ann.Table;
import com.newland.payment.common.dao.DbHelper;
import com.newland.pos.sdk.util.LoggerUtils;
import com.newland.pos.sdk.util.StringUtils;

/**
 * 
 * @author linchunhui
 * @since 20150417
 * @param <T>
 */
public abstract class BaseDao<T> {
//	private static final Logger logger = Logger.getLogger(BaseDao.class);

	protected DbHelper dbHelper;

	protected String tableName;

	protected String idColumn;
	
	protected String idProperty;

	private T t;
	
	private Class<?> clazz;

	@SuppressWarnings("static-access")
	public BaseDao(Context context) {
		dbHelper = dbHelper.getInstance(context);
		try {
			t = generateEntity();
			clazz = t.getClass();
			Table table = t.getClass().getAnnotation(Table.class);
			tableName = table.name();
			
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				Column c = field.getAnnotation(Column.class);
				if (c != null && c.primaryKey()) {
					idColumn = c.name();
					break;
				}
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private T generateEntity() throws InstantiationException,
			IllegalAccessException {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Class<T> entityClass = (Class<T>) params[0];
		return entityClass.newInstance();
	}

	protected String getTableName() {
		return tableName;
	}
	
	
	public List<T> findAll() {
		return findAll("");
	}
	
	public List<T> findAll(String orderby) {
		return this.query("", null, orderby);
	}
	
	
	/**
	 * 插入一条记录
	 * @param t
	 * @return
	 */
	public long insert(T t) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = getColumsObject(t);
		return db.insert(getTableName(), null, cv);
	}
	
	/**
	 * 更新记录（以主键为关键字段）
	 * @param t
	 * @return
	 */
	public int update(T t) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = getColumsObject(t);
		if (idColumn == null || idColumn.trim().equals("")) {
			throw new RuntimeException("未指定主键");
		}
		String where = "", keyValue = "";
		try {
			Object obj = forceGetProperty(t, idProperty);
			if (obj != null) {
				keyValue = obj.toString();
			} else {
				keyValue = null;
			}
			where = idColumn + "= ?";
			return db.update(getTableName(), cv, where, new String[]{ keyValue });
		} catch (Exception e) {
			LoggerUtils.e("获取主键值失败" + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("获取主键值失败");
		} finally {
			db.close();
		}
	}
	
//	/**
//	 * 更新记录
//	 * @param t
//	 * @param where 过滤条件
//	 * @param args 取值
//	 * @return
//	 */
//	public int update(T t, String where, String[] args) {
//		SQLiteDatabase db = openHelper.getWritableDatabase();
//		ContentValues cv = getColumsObject(t);
//		return db.update(getTableName(), cv, where, args);
//	}
	
	public void execSql(String sql) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql);
	}
	
	/**
	 * 通过主键ID删除记录 
	 * @param id
	 * @return
	 */
	public int delete(long id) {
		return delete(idColumn, String.valueOf(id));
	}
	
	public int delete(String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db.delete(getTableName(), where, whereArgs);
	}
	
	public int delete(String columnName, String value) {
		return delete(columnName + "= ?", new String[]{ value });
	}
	

	/**
	 * 获取对象集合
	 * 
	 * @param t
	 * @return
	 */
	protected ContentValues getColumsObject(T t) {
		ContentValues cv = new ContentValues();
		Field[] fds = t.getClass().getDeclaredFields();
		for (Field field : fds) {
			Column cl = field.getAnnotation(Column.class);
			if (cl != null) {
				String cl_name = cl.name();
				boolean key = cl.primaryKey();
				if (key) {
					this.idColumn = cl_name;
					this.idProperty = field.getName();
				}
				Object value = null;
				try {
					value = forceGetProperty(t, field.getName());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				if (value != null) {
					cv.put(cl_name, value.toString());
				}
			}
		}
		return cv;
	}
	private String findKey() {
		Field[] fds = t.getClass().getDeclaredFields();
		for (Field field : fds) {
			Column cl = field.getAnnotation(Column.class);
			if (cl != null && cl.primaryKey()) {
				return field.getName();
			}
		}
		return null;
	}
	
	protected T findById(long id) {
		String key = findKey();
		if (key == null) {
			throw new RuntimeException("表对象不存在主键");
		}
		
		List<T> list = this.query(key + "=?", new String[]{ String.valueOf(id) }, "");
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 统计记录数
	 * @return
	 */
	public int getRowCount() {
		return this.getRowCount("", null);
	}
	
	/**
	 * 统计记录数
	 * @param where 条件语句
	 * @param args 参数值
	 * @return
	 */
	public int getRowCount(String where, String[] args) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from " + this.getTableName() + (StringUtils.isEmpty(where)?"":(" where " + where)), args);
		if (!cursor.moveToNext()) {
			return 0;
		}
		Object obj = cursor.getString(0);
		return Integer.parseInt(obj.toString());
	}
	
	/**
	 * 获取对象集合
	 * 
	 * @param selection
	 * @param args
	 * @param orderby
	 * @return
	 */
	protected List<T> query(String orderby, String limit) {
		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, null, null, null, null, null,
				orderby, limit);
		for (int i = 0; i < cursor.getCount(); i++) {
			@SuppressWarnings("unchecked")
			T o = (T) cursor2VO(cursor, t.getClass());
			list.add(o);
		}
		
		cursor.close();
		return list;
	}
	
	/**
	 * 获取对象集合
	 * 
	 * @param selection
	 * @param args
	 * @param orderby
	 * @return
	 */
	protected List<T> query(String selection, String[] args, String orderby) {
		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, null, selection, args, null, null,
				orderby);
		for (int i = 0; i < cursor.getCount(); i++) {
			@SuppressWarnings("unchecked")
			T o = (T) cursor2VO(cursor, t.getClass());
			list.add(o);
		}
		
		cursor.close();
		return list;
	}
	
	/**
	 * 带翻页的查询
	 * @param selection
	 * @param args
	 * @param orderby
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	protected List<T> query(String selection, String[] args, String orderby, int pageSize, int pageNo) {
		String limit = (pageSize * (pageNo - 1)) + "," + pageSize;
		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, null, selection, args, null, null, orderby, limit);
		for (int i = 0; i < cursor.getCount(); i++) {
			@SuppressWarnings("unchecked")
			T o = (T) cursor2VO(cursor, t.getClass());
			list.add(o);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 带翻页的查询
	 * @param t
	 * @param orderby
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	@SuppressWarnings("static-access")
	protected List<T> query(T t, String orderby, int pageSize, int pageNo) {
		StringBuilder sb = new StringBuilder();
		Field[] fields = t.getClass().getDeclaredFields();
		List<String> list = new ArrayList<String>();
		
		for (Field field : fields) {
			try {
				
				Column c = field.getAnnotation(Column.class);
				if (c == null) {
					continue;
				}
				
				
				Object obj = this.forceGetProperty(t, field.getName());
				if (obj == null) {
					continue;
				}
				
				if (sb.length() > 1) {
					sb.append(" and ");
				}

				sb.append(c.name()).append("=?");
				list.add(obj.toString());
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				continue;
			}
		}
		
		String[] values = new String[list.size()];
		for (int index = 0; index < list.size(); index++) {
			values[index] = list.get(index);
		}
		
		return query(sb.toString(), values, orderby, pageSize, pageNo);
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private static Object cursor2VO(Cursor c, Class clazz) {
		if (c == null) {
			return null;
		}
		Object obj;
		int i = 1;
		try {
			c.moveToNext();
			obj = setValues2Fields(c, clazz);

			return obj;
		} catch (Exception e) {
			LoggerUtils.d("ERROR @：cursor2VO");
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private static Object setValues2Fields(Cursor c, Class clazz)
			throws Exception {
//		String[] columnNames = c.getColumnNames();// 字段数组
		Object obj = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();

		for (Field _field : fields) {
			try{
			Class<? extends Object> typeClass = _field.getType();// 属性类型
			Column column = _field.getAnnotation(Column.class);
			if (column == null) {
				continue;
			}
			String _str = c.getString(c.getColumnIndex(column.name()));
			if (_str == null) {
				continue;
			}
			_str = _str == null ? "" : _str;
			Constructor<? extends Object> cons = typeClass
					.getConstructor(String.class);
			Object attribute = cons.newInstance(_str);
			_field.setAccessible(true);
			_field.set(obj, attribute);
			}catch(Exception e){
				e.printStackTrace();
			}
//			for (int j = 0; j < columnNames.length; j++) {
//				String columnName = columnNames[j];
//				typeClass = getBasicClass(typeClass);
//				boolean isBasicType = isBasicType(typeClass);
//
//				if (isBasicType) {
//					if (columnName.equalsIgnoreCase(column.name())) {// 是基本类型
//						String _str = c.getString(c.getColumnIndex(columnName));
//						if (_str == null) {
//							break;
//						}
//						_str = _str == null ? "" : _str;
//						Constructor<? extends Object> cons = typeClass
//								.getConstructor(String.class);
//						Object attribute = cons.newInstance(_str);
//						_field.setAccessible(true);
//						_field.set(obj, attribute);
//						break;
//					}
//				} else {
//					Object obj2 = setValues2Fields(c, typeClass);// 递归
//					_field.set(obj, obj2);
//					break;
//				}
//
//			}
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private static boolean isBasicType(Class typeClass) {
		if (typeClass.equals(Integer.class) || typeClass.equals(Long.class)
				|| typeClass.equals(Float.class)
				|| typeClass.equals(Double.class)
				|| typeClass.equals(Boolean.class)
				|| typeClass.equals(Byte.class)
				|| typeClass.equals(Short.class)
				|| typeClass.equals(String.class)) {

			return true;

		} else {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	private static Map<Class, Class> basicMap = new HashMap<Class, Class>();
	static {
		basicMap.put(int.class, Integer.class);
		basicMap.put(long.class, Long.class);
		basicMap.put(float.class, Float.class);
		basicMap.put(double.class, Double.class);
		basicMap.put(boolean.class, Boolean.class);
		basicMap.put(byte.class, Byte.class);
		basicMap.put(short.class, Short.class);
	}

	@SuppressWarnings("all")
	public static Class<? extends Object> getBasicClass(Class typeClass) {
		Class _class = basicMap.get(typeClass);
		if (_class == null)
			_class = typeClass;
		return _class;
	}

	
	public static Object forceGetProperty(Object object, String propertyName)
			throws NoSuchFieldException {

		Field field = getDeclaredField(object, propertyName);

		if (field == null) {
			return null;
		}
		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {

		}
		field.setAccessible(accessible);
		return result;
	}
	
	public static Field getDeclaredField(Object object, String propertyName)
			throws NoSuchFieldException {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equalsIgnoreCase(propertyName)) {
				return field;
			}
		}
		return null;
//		return getDeclaredField(object.getClass(), propertyName);
	}
	
	
	private String getDBColumnType(Field field) {
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


	public void createTableIfNotExist() throws DbException {
		StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuffer.append(tableName);
        sqlBuffer.append(" ( ");

        sqlBuffer.append("\"").append(idColumn).append("\"  ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");

        Field[] fields = clazz.getDeclaredFields();
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
        LoggerUtils.d(sqlBuffer.toString());
        this.execSql(sqlBuffer.toString());
    }
	
	public void dropTable(){
		String sql = "DROP TABLE IF EXISTS " + tableName;
		this.execSql(sql);
	}
	
	public void revertSeq(){
		String sql = "update sqlite_sequence set seq=0 where name='"+tableName+"'";
		this.execSql(sql);
	}
}
