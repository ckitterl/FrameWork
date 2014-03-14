package com.nd.ndFrame.layoutTemplate.node;

import org.w3c.dom.Element;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.nd.ndFrame.layoutTemplate.IDLLayoutTemplate;
import com.nd.utilities.css.CssParser;

public class LayoutNodeLinearLayout extends LayoutNode {
	private LinearLayout mLinearLayout;

	public LayoutNodeLinearLayout(Context context, Element e,
			CssParser cssParser, IDLLayoutTemplate dataLayer, int orientation) {
		super(context, e, cssParser, dataLayer);
		mLinearLayout = new LinearLayout(context);
		mLinearLayout.setOrientation(orientation);
	}

	@Override
	public View buildView() {
		setCss();
		return mLinearLayout;
	}

	@Override
	public View getView() {
		return mLinearLayout;
	}


}
