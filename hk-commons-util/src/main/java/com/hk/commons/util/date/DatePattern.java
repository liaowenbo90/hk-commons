package com.hk.commons.util.date;

/**
 * 时间格式
 * 
 * @author huangkai
 * @date 2017年9月15日下午5:46:30
 */
public enum DatePattern {

	YYYY("yyyy"),

	MM_DD("MM-dd"),

	YYYY_MM("yyyy-MM"),

	YYYY_MM_DD("yyyy-MM-dd"),

	MM_DD_HH_MM("MM-dd HH:mm"),

	MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),

	YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),

	YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),

	MM_DD_EN("MM/dd"),

	YYYY_MM_EN("yyyy/MM"),

	YYYY_MM_DD_EN("yyyy/MM/dd"),

	MM_DD_HH_MM_EN("MM/dd HH:mm"),

	MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),

	YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),

	YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),

	MM_DD_CN("MM月dd日"),

	YYYY_MM_CN("yyyy年MM月"),

	YYYY_MM_DD_CN("yyyy年MM月dd日"),

	MM_DD_HH_MM_CN("MM月dd日 HH:mm"),

	MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),

	YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),

	YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),

	HH_MM("HH:mm"),

	HH_MM_SS("HH:mm:ss");

	private String pattern;

	private DatePattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

}