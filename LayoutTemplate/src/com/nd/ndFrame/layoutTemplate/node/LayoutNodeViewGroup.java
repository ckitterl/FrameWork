package com.nd.ndFrame.layoutTemplate.node;

import org.w3c.dom.Element;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nd.ndFrame.datalayer.interfaces.IDataLayer;
import com.nd.ndFrame.layoutTemplate.IDLLayoutTemplate;
import com.nd.utilities.css.CssParser;

public class LayoutNodeViewGroup extends LayoutNode {
	private ViewGroup mViewGroup;

	public LayoutNodeViewGroup(Context context, Element e, CssParser cssParser, IDLLayoutTemplate dataLayer) {
		super(context, e, cssParser, dataLayer);
		mViewGroup = new LinearLayout(context);
	}

	@Override
	public View buildView() {
		setCss();
		return mViewGroup;
	}

	@Override
	public View getView() {
		return mViewGroup;
	}

}
