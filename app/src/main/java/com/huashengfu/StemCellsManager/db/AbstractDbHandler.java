package com.huashengfu.StemCellsManager.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractDbHandler {
	
	/**
	 * 退出时调用此方法销毁数据库连接
	 */
	public abstract void sayByeBye();
	
	/**
	 * 保存数据
	 * @param table
	 * @param obj
	 */
	public abstract void save(String table, Object obj);
	
	/**
	 * 清除数据
	 * @param table
	 */
	public abstract void clear(String table);
	
	/**
	 * 从数据列表中获取一条记录的实体
	 * 
	 * @param c
	 * @param t
	 * @return
	 */
	protected Object getEntity(Cursor c, Class<?> t){
		try {
			Object obj = t.newInstance();
			
			Field[] declaredFields = t.getDeclaredFields();
			List<String> fields = new ArrayList<String>();
			for(Field field : declaredFields)
				fields.add(field.getName().toLowerCase());
			
			List<String> columns = new ArrayList<String>();
			for(int i=0; i<c.getColumnCount(); i++)
				columns.add(c.getColumnName(i));
			
			Method[] methods = t.getMethods();
			for(Method method : methods){
				String methodName = method.getName();
				if(methodName.startsWith("set")){
					String filedName = methodName.substring(3, methodName.length()).toLowerCase();
					
					if(fields.contains(filedName)){
						String typeName = getFieldType(method.getParameterTypes()[0].getName().toLowerCase());
						
						if(columns.contains(filedName)){

							if(typeName.equals("string")){
								String value = c.getString(c.getColumnIndexOrThrow(filedName));
								if(StringUtils.isNullOrBlank(value))
									value = "";
								
								method.invoke(obj, value);
							}else if(typeName.equals("float")){
								method.invoke(obj, c.getFloat(c.getColumnIndex(filedName)));
							}else if(typeName.equals("int")){
								method.invoke(obj, c.getInt(c.getColumnIndex(filedName)));
							}else if(typeName.equals("boolean")){
								method.invoke(obj, c.getInt(c.getColumnIndex(filedName)) == 1);
							}else if(typeName.equals("long")){
								method.invoke(obj, c.getLong(c.getColumnIndex(filedName)));
							}else if(typeName.equals("double")){
								method.invoke(obj, c.getDouble(c.getColumnIndex(filedName)));
							}else if(typeName.equals("short")){
								method.invoke(obj, c.getShort(c.getColumnIndex(filedName)));
							}
						}
					}
				}
			}

			return obj;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 将实体类转换成数据库存储用的ContentValues类
	 * 
	 * @param t
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected ContentValues getContentValues(Object t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Field[] declaredFields = t.getClass().getDeclaredFields();
		
		List<String> fields = new ArrayList<String>();
		for(Field field : declaredFields)
			fields.add(field.getName().toLowerCase());
		
		ContentValues values = new ContentValues();
		Method[] methods =  t.getClass().getMethods();
		
		for(Method method : methods){
			String methodName = method.getName();
			String filedName = null;
			
			if(methodName.startsWith("get"))
				filedName = methodName.substring(3, methodName.length()).toLowerCase();
			else if(methodName.startsWith("is"))
				filedName = methodName.substring(2, methodName.length()).toLowerCase();
			
			if(!StringUtils.isNullOrBlank(filedName)){
				if(fields.contains(filedName)){
					String typeName = getFieldType(method.getReturnType().getName().toLowerCase());
					
					if(typeName.equals("string")){
						values.put(filedName, (String)method.invoke(t));
					}else if(typeName.equals("float")){
						values.put(filedName, (Float)method.invoke(t));
					}else if(typeName.equals("int")){
						values.put(filedName, (Integer)method.invoke(t));
					}else if(typeName.equals("boolean")){
						values.put(filedName, ((Boolean)method.invoke(t)) ? 1 : 0);
					}else if(typeName.equals("long")){
						values.put(filedName, (Long)method.invoke(t));
					}else if(typeName.equals("double")){
						values.put(filedName, (Double)method.invoke(t));
					}else if(typeName.equals("short")){
						values.put(filedName, (Short)method.invoke(t));
					}
					
				}
			}
		}
		
		return values;
	}
	
	/**
	 * 获取数据类型
	 * 
	 * @param type
	 * @return
	 */
	private String getFieldType(String type){
		if(type.lastIndexOf('.') >= 0)
			type = type.substring(type.lastIndexOf('.') + 1);
		return type;
	}
}
