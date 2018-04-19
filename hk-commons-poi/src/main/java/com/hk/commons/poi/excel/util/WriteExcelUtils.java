package com.hk.commons.poi.excel.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hk.commons.poi.POIException;
import com.hk.commons.poi.excel.annotations.NestedProperty;
import com.hk.commons.poi.excel.annotations.WriteExcel;
import com.hk.commons.poi.excel.model.ExcelColumnInfo;
import com.hk.commons.poi.excel.model.StyleTitle;
import com.hk.commons.poi.excel.style.CustomCellStyle;
import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.FieldUtils;
import com.hk.commons.util.StringUtils;
import com.hk.commons.util.TypeUtils;

/**
 * 导出Excel工具类
 * 
 * @author huangkai
 * @date 2017年9月15日下午1:28:06
 */
public abstract class WriteExcelUtils {

	/**
	 * get method prefix
	 */
	public static final String GET_METHOD_PREFIX = "get";

	/**
	 * is method prefix
	 */
	public static final String IS_METHOD_PREFIX = "is";

	/**
	 * 嵌套集合属性
	 */
	public static final String NESTED_PROPERTY = "[%d].";

	/**
	 * 解析
	 * 
	 * @param clazz
	 *            beanClass
	 * @param titleRow
	 *            标题行
	 * @return
	 */
	public static List<ExcelColumnInfo> parse(Class<?> clazz, final int titleRow) {
		List<ExcelColumnInfo> result = Lists.newArrayList();
		addWriteExcel(titleRow, clazz, null, null, result);
		result.sort(Comparator.comparingInt(ExcelColumnInfo::getSortColumn));
		return result;
	}

	/**
	 * 使用递归解析
	 * 
	 * @param titleRow
	 *            标题所在行
	 * @param parameterizedTypeClass
	 *            参数类型class
	 * @param wrapClass
	 *            包装类型
	 * @param nestedPrefix
	 *            nested属性前缀
	 * @param result
	 *            结果list
	 */
	private static void addWriteExcel(final int titleRow, Class<?> parameterizedTypeClass, Class<?> wrapClass,
			String nestedPrefix, List<ExcelColumnInfo> result) {
		if (null != wrapClass) {
			if (ClassUtils.isAssignable(Iterable.class, wrapClass)) {
				nestedPrefix = StringUtils.isEmpty(nestedPrefix) ? StringUtils.EMPTY : nestedPrefix + NESTED_PROPERTY;
			} else {
				nestedPrefix = StringUtils.isEmpty(nestedPrefix) ? StringUtils.EMPTY
						: nestedPrefix + PropertyAccessor.NESTED_PROPERTY_SEPARATOR;
			}
		} else {
			nestedPrefix = StringUtils.isEmpty(nestedPrefix) ? StringUtils.EMPTY : nestedPrefix;
		}
		Map<String, WriteExcel> maps = getWriteExcelAnnotationList(parameterizedTypeClass);
		for (Entry<String, WriteExcel> entry : maps.entrySet()) {
			WriteExcel writeExcel = entry.getValue();
			ExcelColumnInfo info = new ExcelColumnInfo();
			info.setStatistics(writeExcel.isStatistics());
			info.setCommentAuthor(writeExcel.author());
			info.setCommentVisible(writeExcel.visible());

			CustomCellStyle titleStyle = StyleCellUtils.toCustomCellStyle(writeExcel.titleStyle());
			StyleTitle styleTitle = new StyleTitle(titleRow, writeExcel.index(), writeExcel.value(),
					nestedPrefix + entry.getKey());
			styleTitle.setColumnWidth(writeExcel.width());
			styleTitle.setStyle(titleStyle);
			info.setTitle(styleTitle);

			CustomCellStyle dataStyle = StyleCellUtils.toCustomCellStyle(writeExcel.dataStyle());
			info.setDataStyle(dataStyle);
			result.add(info);
		}
		List<Field> nestedFieldList = FieldUtils.getFieldsListWithAnnotation(parameterizedTypeClass,
				NestedProperty.class);
		if (nestedFieldList.size() > 1) {
			throw new POIException("暂不支持多个有NestedProperty注解标记的属性");
		}
		nestedFieldList.forEach(item -> {
			Class<?> ptclass = TypeUtils.getParameterizedTypeClass(parameterizedTypeClass, item.getName());
			if (null != ptclass && !BeanUtils.isSimpleProperty(ptclass)) {
				addWriteExcel(titleRow, ptclass, item.getType(), item.getName(), result);
			}
		});

	}

	/**
	 * 获取有ExportExcel注解修饰的属性，包括父类
	 * 
	 * @param cls
	 * @return
	 */
	private static List<Field> getFieldWithWriteExcelAnnotationList(Class<?> cls) {
		return FieldUtils.getFieldsListWithAnnotation(cls, WriteExcel.class);
	}

	/**
	 * 获取有ExportExcel注解修饰的方法，包括父类
	 * 
	 * @param cls
	 * @return
	 */
	private static List<Method> getMethodWithWriteExcelAnnotationList(Class<?> cls) {
		return MethodUtils.getMethodsListWithAnnotation(cls, WriteExcel.class);
	}

	/**
	 * 获取属性和Get方法有ExportExcel注解修饰Map集合，包括父类
	 * 
	 * @param cls
	 * @return
	 */
	private static Map<String, WriteExcel> getWriteExcelAnnotationList(Class<?> cls) {
		List<Field> fields = getFieldWithWriteExcelAnnotationList(cls);
		List<Method> methods = getMethodWithWriteExcelAnnotationList(cls);
		Map<String, WriteExcel> result = Maps.newHashMap();
		fields.forEach(item -> {
			result.put(item.getName(), item.getAnnotation(WriteExcel.class));
		});
		methods.forEach(item -> {
			String methodName = item.getName();
			if (StringUtils.startsWithAny(methodName, GET_METHOD_PREFIX, IS_METHOD_PREFIX)) {
				String name = StringUtils.uncapitalize(StringUtils.substring(methodName, GET_METHOD_PREFIX.length()));
				result.put(name, item.getAnnotation(WriteExcel.class));
			}
		});
		return result;
	}

}