package com.nd.utilities.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

public class UIHelper {
	
	/**
	 * 将 #rrggbbaa 颜色值转为整数
	 * @param strColor
	 * @return
	 */
	public static int getColor(String strColor) {
		if (strColor != null) {
			if (strColor.startsWith("#"))
				strColor = strColor.substring(1);
			int length = strColor.length();
			if (length == 6) {
				return Color.rgb(Integer.valueOf(strColor.substring(0, 2), 16),
						Integer.valueOf(strColor.substring(2, 4), 16),
						Integer.valueOf(strColor.substring(4, 6), 16));
			} else if (length == 8) {
				return Color.argb(
						Integer.valueOf(strColor.substring(0, 2), 16),
						Integer.valueOf(strColor.substring(2, 4), 16),
						Integer.valueOf(strColor.substring(4, 6), 16),
						Integer.valueOf(strColor.substring(6, 8), 16));
			}
		}
		return Color.argb(0, 0, 0, 0);
	}
	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}

	/**
	 * 将dip转换成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 将px转换成dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static float px2dip(Context context, int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return pxValue / scale;
	}

	/**
	 * 将dip转换成sp
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static float dip2sp(Context context, float dpValue) {
		return px2sp(context, dip2px(context, dpValue));
	}

	/**
	 * 将sp转换成dip
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static float sp2dip(Context context, float spValue) {
		return px2dip(context, sp2px(context, spValue));
	}


	/**
	 * 重新计算对齐方式，使多种对齐方式可同时生效
	 * 
	 * @param old
	 * @param gravity
	 * @return
	 */
	public static int getGravity(int old, int gravity) {
		if (gravity == Gravity.CENTER)
			return gravity;
		if (gravity == Gravity.LEFT || gravity == Gravity.RIGHT
				|| gravity == Gravity.CENTER_HORIZONTAL) {
			if ((old & Gravity.START) == Gravity.START)
				old &= ~Gravity.START;
			if ((old & Gravity.LEFT) == Gravity.LEFT)
				old &= ~Gravity.LEFT;
			if ((old & Gravity.RIGHT) == Gravity.RIGHT)
				old &= ~Gravity.RIGHT;
			if (gravity == Gravity.CENTER_HORIZONTAL) {
				if ((old & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL)
					old &= ~Gravity.CENTER_HORIZONTAL;
			}
		} else if (gravity == Gravity.TOP || gravity == Gravity.BOTTOM
				|| gravity == Gravity.CENTER_VERTICAL) {
			if ((old & Gravity.END) == Gravity.END)
				old &= ~Gravity.END;
			if ((old & Gravity.TOP) == Gravity.TOP)
				old &= ~Gravity.TOP;
			if ((old & Gravity.BOTTOM) == Gravity.BOTTOM)
				old &= ~Gravity.BOTTOM;
			if (gravity == Gravity.CENTER_VERTICAL) {
				if ((old & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL)
					old &= ~Gravity.CENTER_VERTICAL;
			}
		}
		return old | gravity;
	}
}
