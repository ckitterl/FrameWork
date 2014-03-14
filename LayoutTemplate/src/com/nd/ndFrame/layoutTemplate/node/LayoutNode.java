package com.nd.ndFrame.layoutTemplate.node;

import java.util.Map;

import org.w3c.dom.Element;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

import com.nd.ndFrame.layoutTemplate.IDLLayoutTemplate;
import com.nd.utilities.css.CssApply;
import com.nd.utilities.css.CssParser;
import com.nd.utilities.execption.FrameNoSupportMethodException;
import com.nd.utilities.ui.UIHelper;

/**
 * LayoutTemplate中显示节点的基类
 * 
 * @author wangjg
 * 
 */
public abstract class LayoutNode {
	Element mElement;
	CssParser mCssParser;
	Context mContext;
	IDLLayoutTemplate mDataLayer;
	int cellSpacing;

	public LayoutNode(Context context, Element e, CssParser cssParser,
			IDLLayoutTemplate dataLayer) {
		mContext = context;
		mElement = e;
		mCssParser = cssParser;
		mDataLayer = dataLayer;
	}

	public void addChildLayoutNode(LayoutNode child)
			throws FrameNoSupportMethodException {
		if (!isViewGroup()) {
			throw new FrameNoSupportMethodException(
					"This layout node is not support add");
		}

		ViewGroup viewGroup = (ViewGroup) getView();
		View childView = child.getView();
		if (cellSpacing != 0) {
			LayoutParams childLp = (LayoutParams) childView.getLayoutParams();
			childLp.setMargins(0, 0, cellSpacing, cellSpacing);
			childView.setLayoutParams(childLp);
		}
		viewGroup.addView(childView);
	}

	protected boolean isViewGroup() {
		return getView() instanceof ViewGroup;
	}

	protected abstract View buildView();

	public abstract View getView();

	/**
	 * TODO 转义字符串字符，如果需要从数据源获取就从数据源获取
	 * 
	 * @param rawContent
	 * @return
	 */
	protected Object translateContent(String rawContent) {
		return rawContent;
	}

	/**
	 * 设置布局属性。一般的情况下，布局属性作为节点属性呈现，其他的style 用样式来表示
	 * 
	 */
	protected void setStyle() {
		View view = getView();
		String strWidth = mElement.getAttribute("width");
		String strHeight = mElement.getAttribute("height");
		int width, height, weight = 0;
		if (TextUtils.isEmpty(strWidth)) {
			width = LayoutParams.WRAP_CONTENT;
		} else {
			weight = getLayoutParam(strWidth);
			if (weight == 0) {
				width = Integer.valueOf(strWidth);
			} else {
				width = 0;
			}
		}

		if (TextUtils.isEmpty(strHeight)) {
			height = LayoutParams.WRAP_CONTENT;
		} else {
			weight = getLayoutParam(strWidth);
			if (weight == 0) {
				height = Integer.valueOf(strHeight);
			} else {
				height = 0;
			}
		}

		LayoutParams lp = new LayoutParams(width, height);
		if (weight > 0) {
			lp.weight = weight;
		}

		view.setLayoutParams(lp);

		// cellSapcing 作用在控件上的策略是，父控件paddingLeft，paddingTop
		// 子控件layout_marginRight, layout_marginBottom
		String strCellSapcing = mElement.getAttribute("cellspacing");
		if (!TextUtils.isEmpty(strCellSapcing)) {
			int cellSpacing = UIHelper.dip2px(mContext,
					Float.valueOf(strCellSapcing));
			view.setPadding(cellSpacing, cellSpacing, 0, 0);
		}
	}

	/**
	 * 将HTML的长宽转换为Android的LayoutParams
	 * 
	 * @param strAttributeValue
	 * @return
	 */
	private int getLayoutParam(String strAttributeValue) {
		if (strAttributeValue.contains("%")) {
			String s = strAttributeValue.replace("%", "");
			return Integer.valueOf(s);
		}
		return 0;
	}

	/**
	 * 设置View的css样式
	 * 
	 */
	protected void setCss() {
		View view = getView();
		CssApply apply = getCssApply(mContext, mElement, mCssParser, view);
		if (apply != null) {
			apply.addCss(mCssParser);
			apply.apply();
		}
	}

	/**
	 * 获取一个CssApply类，这个CssApply根据Object节点提取出来的
	 * css样式与Android的样式做一次转换，然后作用到相对应的View
	 * 
	 * @param e
	 *            含有css选择器的一段DOM节点。可以是一个Element或者一个，也 可以是<"class",
	 *            "css style string">的Map(只支持class选 择器)，也可以时一段css style string。
	 * @param cssParser
	 * @param view
	 * @return
	 */
	static CssApply getCssApply(Context context, Object e, CssParser cssParser,
			View view) {
		if (e == null || cssParser == null || view == null) {
			return null;
		}

		String strClass = "";
		if (e instanceof Element) {

		} else if (e instanceof Map) {

		} else if (e instanceof String)
			strClass = (String) e;
		if (!TextUtils.isEmpty(strClass)) {
			String classes[] = strClass.split(" ");
			int length = classes.length;
			if (classes.length > 0) {
				CssApply apply = new CssApply(view, context);
				for (int i = 0; i < length; i++) {
					String tmp = classes[i].trim();
					if (!TextUtils.isEmpty(tmp))
						apply.addTag("." + tmp.trim());
				}
				apply.addCss(cssParser);
				return apply;
			}
		}
		return null;
	}

}
