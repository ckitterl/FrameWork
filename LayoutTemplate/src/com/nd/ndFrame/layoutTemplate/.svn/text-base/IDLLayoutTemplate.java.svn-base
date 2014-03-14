package com.nd.ndFrame.layoutTemplate;

import android.view.View;

import com.nd.ndFrame.action.Action;
import com.nd.ndFrame.datalayer.interfaces.IDataLayer;

public interface IDLLayoutTemplate extends IDataLayer {
	/**
	 * 取得指定数据源中字段的值
	 * @param field
	 * @param datasource
	 * @return value
	 */
	public String valueForField(String field,String datasource);
	/**
	 * 取得指定数据源中字段的名称
	 * @param field
	 * @param datasource
	 * @return
	 */
	public String nameForField(String field,String datasource);
	/**
	 * 取得指定数据源中字段值所代表的路径
	 * @param field
	 * @param datasource
	 * @return
	 */
	public String pathForField(String field,String datasource);
	/**
	 * 取得指定数据源中的字段所代表的Action对象
	 * 如果字段值不可以转为Action，返回null
	 * @param field
	 * @param datasource
	 * @return
	 */
	public Action actionForField(String field,String datasource);
	/**
	 * 取得指定数据源中的字段需呈现的View
	 * @param field
	 * @param datasource
	 * @return
	 */
	public View viewForField(String field,String datasource);
}
