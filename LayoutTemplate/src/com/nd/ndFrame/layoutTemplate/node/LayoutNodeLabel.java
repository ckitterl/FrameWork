package com.nd.ndFrame.layoutTemplate.node;

import org.w3c.dom.Element;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.nd.ndFrame.layoutTemplate.IDLLayoutTemplate;
import com.nd.utilities.css.CssParser;

/**
 * Label 节点
 * 
 * @author wangjg
 * 
 */
public class LayoutNodeLabel extends LayoutNode {
	private TextView mTextView;
	private Element mElement;

	public LayoutNodeLabel(Context context, Element e, CssParser cssParser, IDLLayoutTemplate dataLayer) {
		super(context, e, cssParser, dataLayer);
		mContext = context;
		mElement = e;
		buildView();
	}

	@Override
	public View buildView() {
		mTextView = new TextView(mContext);
		String rawContent = mElement.getTextContent();
		String content = (String)translateContent(rawContent);
		mTextView.setText(content);
		setStyle();
		setCss();
		return mTextView;
	}
	
	public View getView() {
		return mTextView;
	}

}
