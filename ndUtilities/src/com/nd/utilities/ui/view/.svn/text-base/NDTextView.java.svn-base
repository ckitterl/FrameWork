package com.nd.utilities.ui.view;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nd.utilities.ui.UIHelper;

import android.content.Context;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 使用SpannableString為textview設置各種效果
 * @author apple
 *
 */
public class NDTextView extends TextView {
	private SpannableString msp;
	private List<String> m_text;
	public static String FONT_NORMAL="NORMAL";
	public static String FONT_BOLD="BOLD";
	public static String FONT_ITALIC="ITALIC";
	public static String FONT_BOLD_ITALIC="BOLD_ITALIC";
	
	public NDTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NDTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setText(String text) {
		super.setText(format(text));
	}
	
	private String format(String s) {
		String regEx = "[`~!@#%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……*（）「」——+|{}【】‘；：”“’。，、？|-]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
		StringBuffer sbr = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sbr, m.group() + " ");
		}
		m.appendTail(sbr); 
		return sbr.toString();
	}

	/**
	 * @param text
	 *            順序傳入各段的string
	 */
	public void setTextMap(List<String> text) {
		m_text = text;
		String content = "";
		for (int i = 0; i < m_text.size(); i++) {
			content += m_text.get(i);
		}
		msp = new SpannableString(content);
	}

	private int getStartIndex(int index) {
		if(index==0)
			return 0;
		else
		{	int startIndex=0;
			for(int i=0;i<index;i++){
				String content=m_text.get(i);
				startIndex+=content.length();
			}
			return startIndex;
		}
	}
	/**
	 * index項設置前景色
	 * @param index
	 * @param color
	 */
	public void setTextColor(int index, String color) {
		setSpan(index, new ForegroundColorSpan(UIHelper.getColor(color)));
	}
	/**
	 * index項設置背景色
	 * @param index
	 * @param color
	 */
	public void setTextBgColor(int index,String color){
		setSpan(index, new BackgroundColorSpan(UIHelper.getColor(color)));
	}
	/**
	 * 設置字體大小，根據像素
	 * @param index
	 * @param size
	 */
	public void setTextSizeByPx(int index,int size){
		setSpan(index, new AbsoluteSizeSpan(size));
	}
	/**
	 * 設置字體大小，根據dip
	 * @param index
	 * @param size
	 */
	public void setTextSizeByDip(int index,int size){
		setSpan(index, new AbsoluteSizeSpan(size, true));
	}
	/**
	 * 設置字體大小
	 * @param index
	 * @param ratio 默認大小的倍數 
	 */
	public void setTextSizeByRatio(int index,float ratio){
		setSpan(index, new RelativeSizeSpan(ratio));
	}
	/**
	 * 設置字體  粗體，斜體，粗斜體
	 * @param index
	 * @param fontStyle
	 */
	public void setTextFontStyle(int index,String fontStyle){
		int style=android.graphics.Typeface.NORMAL;
		if(fontStyle.equalsIgnoreCase(FONT_BOLD)){
			style=android.graphics.Typeface.BOLD;
		}
		else if(fontStyle.equalsIgnoreCase(FONT_ITALIC)){
			style=android.graphics.Typeface.ITALIC;
		}
		else if(fontStyle.equalsIgnoreCase(FONT_BOLD_ITALIC)){
			style=android.graphics.Typeface.BOLD_ITALIC;
		}
		setSpan(index, new StyleSpan(style));
	}
	/**
	 * 設置下劃綫
	 * @param index
	 */
	public void setUnderLine(int index){
		setSpan(index, new UnderlineSpan());
	}
	/**
	 * 設置刪除線
	 * @param index
	 */
	public void setDeleteLine(int index){
		setSpan(index, new StrikethroughSpan());
	}
	private void setSpan(int index,Object span){
		if(index>=m_text.size())
			return;
		int startIndex = getStartIndex(index);
		String content = m_text.get(index);
		msp.setSpan(span, startIndex, startIndex + content.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		setText(msp);
	}
	private void setSpan(String text,Object span){
		msp = new SpannableString(text);
		msp.setSpan(span, 0, text.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		setText(msp);
	}
	/**
	 * 設置字體大小，根據dip
	 * @param text
	 * @param size
	 */
	public void setTextSizeByDip(String text,int size){
		setSpan(text, new AbsoluteSizeSpan(size, true));
	}
	/**
	 * 設置字體大小
	 * @param text
	 * @param ratio 默認大小的倍數 
	 */
	public void setTextSizeByRatio(String text,float ratio){
		setSpan(text, new RelativeSizeSpan(ratio));
	}
	/**
	 * 設置字體  粗體，斜體，粗斜體
	 * @param text
	 * @param fontStyle
	 */
	public void setTextFontStyle(String text,String fontStyle){
		int style=android.graphics.Typeface.NORMAL;
		if(fontStyle.equalsIgnoreCase(FONT_BOLD)){
			style=android.graphics.Typeface.BOLD;
		}
		else if(fontStyle.equalsIgnoreCase(FONT_ITALIC)){
			style=android.graphics.Typeface.ITALIC;
		}
		else if(fontStyle.equalsIgnoreCase(FONT_BOLD_ITALIC)){
			style=android.graphics.Typeface.BOLD_ITALIC;
		}
		setSpan(text, new StyleSpan(style));
	}
	/**
	 * 設置下劃綫
	 * @param text
	 */
	public void setUnderLine(String text){
		setSpan(text, new UnderlineSpan());
	}
	/**
	 * 設置刪除線
	 * @param text
	 */
	public void setDeleteLine(String text){
		setSpan(text, new StrikethroughSpan());
	}
}
