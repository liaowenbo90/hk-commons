/**
 * 
 */
package com.hk.commons.poi.excel.style;

import org.apache.poi.ss.usermodel.Font;

/**
 * @author huangkai
 *
 */
public interface StyleSheet {

	public enum Fonts {

		SONTTI("宋体"), 
		
		HEITI("黑体"),
		
		YAHEI("微软雅黑");

		private String fontName;

		private Fonts(String fontName) {
			this.fontName = fontName;
		}

		public String getFontName() {
			return fontName;
		}

	}

	/**
	 * 
	 * 无样式
	 */
	short NONE_STYLE = 0xffffffff;

	/*
	 * ********************************* 下划线 **************************************
	 */

	enum UnderLineStyle {

		/**
		 * 下划线 ：无
		 */
		U_NONE(Font.U_NONE),

		/**
		 * 下划线 ：单下划线与字体同宽
		 */
		U_SINGLE(Font.U_SINGLE),

		/**
		 * 下划线 :双下划线与字体同宽
		 */
		U_DOUBLE(Font.U_DOUBLE),

		/**
		 * 下划线 ：单下划线与单元格同宽
		 */
		U_SINGLE_ACCOUNTING(Font.U_SINGLE_ACCOUNTING),

		/**
		 * 下划线 :双下划线与单元格同宽
		 */
		U_DOUBLE_ACCOUNTING(Font.U_DOUBLE_ACCOUNTING);

		private byte value;

		UnderLineStyle(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}
	}

}