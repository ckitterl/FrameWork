
package com.nd.ndFrame.layoutTemplate.node;

import org.w3c.dom.Element;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nd.ndFrame.layoutTemplate.IDLLayoutTemplate;
import com.nd.ndFrame.layoutTemplate.node.LayoutNode;
import com.nd.utilities.css.CssParser;

public class LayoutNodeImage extends LayoutNode {
	private ImageView mImageView;

	public LayoutNodeImage(Context context, Element e, IDLLayoutTemplate dataLayer, CssParser cssParser) {
		super(context, e, cssParser, dataLayer);
	}

	@Override
	public View buildView() {
		mImageView = new ImageView(mContext);
		String rawContent = mElement.getTextContent();
		String content = (String)translateContent(rawContent);
		// TODO load image from data
		return mImageView;
	}

	@Override
	public View getView() {
		return mImageView;
	}

}
