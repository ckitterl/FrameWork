package com.nd.ndFrame.layoutTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.larry.layouttemplate.MainActivity;
import com.nd.ndFrame.layoutTemplate.node.LayoutNode;
import com.nd.ndFrame.layoutTemplate.node.LayoutNodeFactory;
import com.nd.utilities.css.CssParser;
import com.nd.utilities.execption.FrameNoSupportMethodException;
import com.nd.utilities.execption.FrameUnsupportTagTypeException;
import com.nd.utilities.language.xml.XmlWrapper;
import com.nd.utilities.logger.Logger;

/**
 * LayoutTemplate解析生成器
 * 
 * @author wangjg
 * 
 */
public class LayoutTemplate {

	private Context mContext;
	private IDLLayoutTemplate mDataLayer;

	private LinearLayout mRootLayout; // 外层祖先节点
	private LinearLayout mMainLayout; // 中间区域容器节点

	// WebView's style
	private String mHtmlStyle;
	// The style to describe Native View
	private String mLayoutStyle;

	private CssParser mCssParser;

	private Document mRootDocument;

	public LayoutTemplate(Context context, IDLLayoutTemplate dataLayer) {
		mContext = context;
		mDataLayer = dataLayer;
		// Init wrapper ViewGroup
		mRootLayout = new LinearLayout(context);
		mRootLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// No need to compute the height to decide whether is need
		// add scrollView.
		mMainLayout = new LinearLayout(context);
		mMainLayout.setOrientation(LinearLayout.VERTICAL);
		ScrollView scrollView = new ScrollView(context);
		scrollView.addView(mMainLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mRootLayout.addView(scrollView);
	}

	public LayoutTemplate(Context context) {
		mContext = context;
	}

	/**
	 * 载入 Layout字串
	 * 
	 * @param content
	 */
	public void loadLayout(String content) {

	}

	/**
	 * 载入XML布局文件
	 * 
	 * @param file
	 */
	public void loadLayout(File file) {
		try {
			mRootDocument = XmlWrapper.getDocument(mContext, file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (mRootDocument != null) {
			// 读取css样式到指定格式
			initAttribute(mRootDocument.getDocumentElement());
			// 根据DOM树，生成View节点
			generateLayout();
		}
	}

	/**
	 * 执行布局，生成View
	 */
	@SuppressLint({ "NewApi" })
	private void generateLayout() {
		mRootLayout.setOrientation(LinearLayout.VERTICAL);
		// 外層m_scrollView要開啟軟件加速，webview才能透明顯示
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mRootLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		/**
		 * 遍历XML节点，生成View
		 */

		NodeList headSection = mRootDocument
				.getElementsByTagName("HeadSection");
		NodeList sections = mRootDocument.getElementsByTagName("Section");
		NodeList footSection = mRootDocument
				.getElementsByTagName("FootSection");

		try {
			LayoutNode headLayoutNode = parseSection((Element) headSection
					.item(0));
			LayoutNode mainLayoutNode = parseSection((Element) sections.item(0));
			LayoutNode footLayoutNode = parseSection((Element) footSection
					.item(0));

			mRootLayout.addView(headLayoutNode.getView(), 0);
			mMainLayout.addView(mainLayoutNode.getView());
			mRootLayout.addView(footLayoutNode.getView(), 2);

		} catch (FrameUnsupportTagTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FrameNoSupportMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 先序遍历节点，递归创建子View
	 * 
	 * @param e
	 *            DOM节点树
	 * @throws FrameUnsupportTagTypeException
	 * @throws FrameNoSupportMethodException
	 */
	private LayoutNode parseSection(Element e)
			throws FrameUnsupportTagTypeException,
			FrameNoSupportMethodException {
		if (e == null) {
			return null;
		}

		LayoutNode layoutNode = LayoutNodeFactory.getLayoutNode(mContext, e,
				mCssParser, mDataLayer);

		NodeList children = e.getChildNodes();
		int length = children.getLength();
		for (int i = 0; i < length; i++) {
			Node n = children.item(i);
			if (n instanceof Element) {
				Element childElement = (Element) n;
				LayoutNode childLayoutNode = parseSection(childElement);
				layoutNode.addChildLayoutNode(childLayoutNode);
			}
		}

		return layoutNode;
	}

	private void initAttribute(Element root) {
		// NodeList layoutStyle = root.getElementsByTagName("style");
		// NodeList children = layoutStyle.item(0).getChildNodes();
		// int length = layoutStyle.getLength();
		// for (int i = 0; i < length; i++) {
		// Node child = children.item(i);
		// if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
		// System.out.print(child.getNodeValue());
		// }
		// System.out.print(child.getNodeType());
		// }
		NodeList layoutNode = root.getChildNodes();
		int size = layoutNode.getLength();
		for (int i = 0; i < size; i++) {
			if (layoutNode.item(i) instanceof Element) {
				Element layout = (Element) layoutNode.item(i);
				if ("style".equalsIgnoreCase(layout.getTagName())) {
					mHtmlStyle = layout.getTextContent();
				} else if ("layoutStyle".equalsIgnoreCase(layout.getTagName())) {
					String layoutStyle = layout.getTextContent();
					Logger.d(getClass(), "layoutStyle:" + layoutStyle);
					if (!TextUtils.isEmpty(layoutStyle)) {
						mCssParser = new CssParser();
						mCssParser.parserCss(replaceString(layoutStyle));
					}
				}
			}
		}
	}

	private String replaceString(String html) {
		String targetStr = html;
		// 替换字段标题
		String regEx = "\\$\\{Title:([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		while (m.find()) {
			String srcStr = m.group();
			String category = m.group(1);
			String field = m.group(2);
			String title = nameForField(field, category);
			targetStr = targetStr.replace(srcStr, title);
		}
		// 替换字段值
		regEx = "\\$\\{Value:([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}";
		p = Pattern.compile(regEx);
		m = p.matcher(html);
		while (m.find()) {
			String srcStr = m.group();
			String category = m.group(1);
			String field = m.group(2);
			String value = valueForField(field, category);
			targetStr = targetStr.replace(srcStr, value);
		}

		// TODO add to support more dataset
		// // 替换多语字串
		// regEx = "\\$\\{LocalizeString:([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}";
		// p = Pattern.compile(regEx);
		// m = p.matcher(html);
		// while (m.find()) {
		// String srcStr = m.group();
		// String bundle = m.group(1);
		// String key = m.group(2);
		// String desc = localizeString(key, bundle);
		// targetStr = targetStr.replace(srcStr, desc);
		// }
		// // 替換addOn中文字
		// regEx = "\\$\\{WordInAddOn:([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}";
		// p = Pattern.compile(regEx);
		// m = p.matcher(html);
		// while (m.find()) {
		// String srcStr = m.group();
		// String category = m.group(1);
		// String field = m.group(2);
		// String value = wordInAddOnForField(field, category);
		// targetStr = targetStr.replace(srcStr, value);
		// }
		//
		// // 替换path路径
		// regEx = "\\$\\{Path:([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}";
		// p = Pattern.compile(regEx);
		// m = p.matcher(html);
		// while (m.find()) {
		// String srcStr = m.group();
		// String category = m.group(1);
		// String field = m.group(2);
		// List<Object> path = pathForField(field, category);
		// if (path.size() > 0) {
		// targetStr = targetStr.replace(srcStr, path.get(0).toString());
		// }
		// }
		//
		// // 替换App信息
		// regEx = "\\$\\{App:([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}";
		// p = Pattern.compile(regEx);
		// m = p.matcher(html);
		// while (m.find()) {
		// String srcStr = m.group();
		// String category = m.group(1);
		// String field = m.group(2);
		// String value = appForField(field, category);
		// targetStr = targetStr.replace(srcStr, value);
		// }

		return targetStr;
	}

	public String valueForField(String field, String category) {
		Logger.d(getClass(), "ds:" + mDataLayer);
		if (mDataLayer == null)
			return "";
		return mDataLayer.valueForField(field, category);
	}

	public String nameForField(String field, String category) {
		if (mDataLayer == null)
			return "";
		return mDataLayer.nameForField(field, category);
	}

	// public String wordInAddOnForField(String field, String category) {
	// Logger.d(getClass(), "ds:" + mDataLayer);
	// if (mDataLayer == null)
	// return "";
	// return mDataLayer.wordInAddOnForField(field, category);
	// }
	//
	// public List<Object> pathForField(String field, String category) {
	// Logger.d(getClass(), "ds:" + mDataLayer);
	// if (mDataLayer == null)
	// return null;
	// return mDataLayer.pathForField(field, category);
	// }

	/**
	 * TODO delete after merge
	 * 
	 * @return
	 */
	public View getRootView() {
		return mRootLayout;
	}

}
