package com.nd.ndFrame.layoutTemplate.node;

import org.w3c.dom.Element;

import android.content.Context;
import android.widget.LinearLayout;

import com.nd.ndFrame.layoutTemplate.IDLLayoutTemplate;
import com.nd.utilities.css.CssParser;
import com.nd.utilities.execption.FrameUnsupportTagTypeException;

public enum LayoutNodeFactory {
	INSTANCE;

	LayoutNodeFactory() {

	}

	public static LayoutNode getLayoutNode(Context context, Element e,
			CssParser cssParser, IDLLayoutTemplate mDataLayer) throws FrameUnsupportTagTypeException {
		if (context == null || e == null || cssParser == null) {
			throw new NullPointerException("param null");
		}

		LayoutNode layoutNode;

		String tagName = e.getTagName();
		if (tagName.equalsIgnoreCase("table")) {
			layoutNode = new LayoutNodeLinearLayout(context, e, cssParser, mDataLayer,
					LinearLayout.VERTICAL);
		} else if (tagName.equalsIgnoreCase("HeadSection") 
				|| tagName.equalsIgnoreCase("Section")
				|| tagName.equalsIgnoreCase("FootSection")) {
			layoutNode = new LayoutNodeLinearLayout(context, e, cssParser, mDataLayer,
					LinearLayout.VERTICAL);
		} else if (tagName.equalsIgnoreCase("tr")) {
			layoutNode = new LayoutNodeLinearLayout(context, e, cssParser, mDataLayer,
					LinearLayout.HORIZONTAL);
		} else if (tagName.equalsIgnoreCase("td")) {
			layoutNode = new LayoutNodeLinearLayout(context, e, cssParser, mDataLayer,
					LinearLayout.VERTICAL);
		} else if (tagName.equalsIgnoreCase("label")) {
			layoutNode = new LayoutNodeLabel(context, e, cssParser, mDataLayer);
//		} else if (tagName.equalsIgnoreCase("image")) {
//			layoutNode = new LayoutNodeImage(context, e, cssParser, mDataLayer);
//		} else if (tagName.equalsIgnoreCase("html")) {
//			layoutNode = new LayoutNodeHtml(context, e, cssParser, mDataLayer);
		} else {
			throw new FrameUnsupportTagTypeException(tagName);
		}

		return layoutNode;
	}
}
