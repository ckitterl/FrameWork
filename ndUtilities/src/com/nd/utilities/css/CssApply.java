package com.nd.utilities.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.utilities.language.MapHelper;
import com.nd.utilities.logger.Logger;
import com.nd.utilities.media.ImageHelper;
import com.nd.utilities.ui.view.NDTextView;
import com.nd.utilities.ui.UIHelper;

public class CssApply {
	private View mView;
	private List<String> mTags = new ArrayList<String>();
	public Map<String, Object> mProperties = new HashMap<String, Object>();

	private Context mContext;

	public CssApply(View view,Context context) {
		mView = view;
		mContext=context;
	}

	public void addTag(String tag) {
		mTags.add(tag);
	}

	public void addTag(List<String> tags) {
		mTags.addAll(tags);
	}

	public void addCss(String css) {
		CssParser parser = new CssParser();
		parser.parserCss(css);
		addCss(parser);
	}

	public void addCssJson(String css) {
		CssParser parser = new CssParser();
		parser.parserJson(css);
		addCss(parser);
	}

	public void addCss(CssParser css) {
		if (css == null)
			return;
		for (String tag : mTags) {
			for (Rule rule : css.getRule()) {
				if (rule.getSelectors().contains(tag)) {
					for (String property : rule.getPropertys()) {
						if (property.equals("border")) {
							String[] tmp = rule.getPropertyValue(property)
									.trim().split(" ");
							if (tmp.length == 2) {
								mProperties.put("border-width", tmp[0]);
								mProperties.put("border-color", tmp[1]);
							}
						} else if (property.equals("font")) {
							String[] tmp = rule.getPropertyValue(property)
									.trim().split(" ");
							if (tmp.length == 2) {
								mProperties.put("font-size", tmp[0]);
								mProperties.put("font-family", tmp[1]);
							}
						} else if (property.equals("padding")) {
							String[] tmp = rule.getPropertyValue(property)
									.trim().split(" ");
							if (tmp.length == 2) {
								mProperties.put("-mobile-vpadding", tmp[0]);
								mProperties.put("-mobile-hpadding", tmp[1]);
							}
						} else
							mProperties.put(property,
									rule.getPropertyValue(property));
					}
				}
			}
		}
	}

	public void apply() {
		// Iterator<String> keys = mProperties.keySet().iterator();
		// while (keys.hasNext()) {
		// String property = keys.next();
		// setProperty(property);
		// }
		setBasicProperty();
		setSpecialProperty();
	}

	private void setBasicProperty() {
		setWidthHeight();
		setBackground();
		setBackgroundImage();
		setAlpha();
		setVisibility();
		setPadding();
		setMargins();
		setPosition();
		setMin();
		setAlign();
	}

	protected void setSpecialProperty() {
		if (mView instanceof TextView) {
			if (mView instanceof EditText)
				setEditTextProperty();
			setTextViewProperty();
		} else if (mView instanceof Button) {
			setButtonProperty();
		}
	}

	@SuppressWarnings("deprecation")
	private void setBackground() {
		int width = Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
				mProperties, "border-width", "0")));
		int radius = Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
				mProperties, "border-radius", "0")));
		if (width > 0 || radius > 0) {
			width = UIHelper.dip2px(mContext,
					width);
			radius = UIHelper.dip2px(
					mContext, radius);
			String color = CssColor.chkColor(MapHelper.getStringValueByKey(
					mProperties, "border-color", "#000000"));
			String bgColor = CssColor.chkColor(MapHelper.getStringValueByKey(
					mProperties, "background-color", "#00000000"));
			Drawable border = ImageHelper.createGradientDrawable(bgColor, width,
					color, radius);
			mView.setBackgroundDrawable(border);
		} else {
			if (mProperties.containsKey("background-color")) {
				String bgColor = MapHelper.getStringValueByKey(mProperties,
						"background-color", "#00000000");
				mView.setBackgroundColor(CssColor.getColor(bgColor));
			}
		}
	}

	private void setBackgroundImage() {
		if (mProperties.containsKey("background-image")) {
			String name = MapHelper.getStringValueByKey(mProperties,
					"background-image", "");
			if (!TextUtils.isEmpty(name)) {
				LayoutParams lp = mView.getLayoutParams();
				int width = lp != null ? lp.width : 0;
				int height = lp != null ? lp.height : 0;
				if (width < 0)
					width = 0;
				if (height < 0)
					height = 0;
				if (name.startsWith("url("))
					name = name.replace("url(", "");
				if (name.endsWith(")"))
					name = name.substring(0, name.length() - 1);
				/* todo :设置背景图
				 * 依当前资源位置找图
				 */
			}
			return;
		}
	}

	private void setAlpha() {
		if (mProperties.containsKey("opacity")) {
			float alpha = Float.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "opacity", "0.0")));
			mView.setAlpha(alpha);
		}
	}

	private void setVisibility() {
		if (mProperties.containsKey("visibility")) {
			String visibility = MapHelper.getStringValueByKey(mProperties,
					"visibility", "visible");
			if (visibility.equals("hidden"))
				mView.setVisibility(View.GONE);
			else
				mView.setVisibility(View.VISIBLE);
		}
	}

	private void setWidthHeight() {
		LayoutParams lp = mView.getLayoutParams();
		if (lp == null)
			return;
		int width = lp.width;
		if (width <= 0) {
			width = Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
					mProperties, "width", String.valueOf(width))));
		}
		if (width > 0) {
			width += Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
					mProperties, "padding-left", "0")));
			width += Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
					mProperties, "padding-right", "0")));
			width += 2 * Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "border-width", "0")));
			width = UIHelper.dip2px(mContext,
					width);
		}
		lp.width = width;

		int height = lp.height;
		if (height <= 0) {
			height = Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
					mProperties, "height", String.valueOf(height))));
		}
		if (height > 0) {
			height += Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "padding-top", "0")));
			height += Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "padding-bottom", "0")));
			height += 2 * Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "border-width", "0")));
			height = UIHelper.dip2px(
					mContext, height);
		}
		lp.height = height;
		mView.setLayoutParams(lp);
	}

	private void setPadding() {
		int padding_left = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "padding-left", "0")))
				+ Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
						mProperties, "border-width", "0")));
		padding_left = UIHelper.dip2px(
				mContext, padding_left);

		int padding_right = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "padding-right", "0")))
				+ Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
						mProperties, "border-width", "0")));
		padding_right = UIHelper.dip2px(mContext, padding_right);
				

		int padding_top = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "padding-top", "0")))
				+ Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
						mProperties, "border-width", "0")));
		padding_top = UIHelper.dip2px(mContext, padding_top);
				

		int padding_bottom = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "padding-bottom", "0")))
				+ Integer.valueOf(removeUnit(MapHelper.getStringValueByKey(
						mProperties, "border-width", "0")));
		padding_bottom = UIHelper.dip2px(mContext, padding_bottom);
		if (padding_left != 0 || padding_top != 0 || padding_right != 0
				|| padding_bottom != 0)
			mView.setPadding(padding_left, padding_top, padding_right,
					padding_bottom);
	}

	private void setMin() {
		if (mProperties.containsKey("min-width")) {
			String tmp = mProperties.get("min-width").toString();
			if (!TextUtils.isEmpty(tmp)) {
				try {
					int minWidth = Integer.valueOf(removeUnit(tmp));
					minWidth = UIHelper.dip2px(mContext, minWidth);
					mView.setMinimumWidth(minWidth);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

		if (mProperties.containsKey("min-height")) {
			String tmp = mProperties.get("min-height").toString();
			if (!TextUtils.isEmpty(tmp)) {
				try {
					int minHeight = Integer.valueOf(removeUnit(tmp));
					minHeight = UIHelper.dip2px(mContext, minHeight);
					mView.setMinimumHeight(minHeight);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	private void setPosition() {
		int left, top, right, bottom;
		String leftTmp = MapHelper.getStringValueByKey(mProperties, "left",
				String.valueOf(mView.getLeft()));
		try {
			left = Integer.valueOf(removeUnit(leftTmp));
			left = UIHelper.dip2px(mContext,
					left);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			left = mView.getLeft();
		}

		String rightTmp = MapHelper.getStringValueByKey(mProperties, "right",
				String.valueOf(mView.getRight()));
		try {
			right = Integer.valueOf(removeUnit(rightTmp));
			right = UIHelper.dip2px(mContext,
					right);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			right = mView.getRight();
		}

		String topTmp = MapHelper.getStringValueByKey(mProperties, "top",
				String.valueOf(mView.getTop()));
		try {
			top = Integer.valueOf(removeUnit(topTmp));
			top = UIHelper.dip2px(mContext,
					top);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			top = mView.getTop();
		}

		String bottomTmp = MapHelper.getStringValueByKey(mProperties,
				"bottom", String.valueOf(mView.getBottom()));
		try {
			bottom = Integer.valueOf(removeUnit(bottomTmp));
			bottom = UIHelper.dip2px(
					mContext, bottom);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			bottom = mView.getBottom();
		}

		mView.layout(left, top, right, bottom);
	}

	private void setMargins() {
		LayoutParams lp = mView.getLayoutParams();
		if (lp == null)
			return;

		int margin_top = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "margin-top", "0")));
		if (margin_top == 0)
			margin_top = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "top", "0")));
		margin_top = UIHelper.dip2px(
				mContext, margin_top);

		int margin_bottom = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "margin-bottom", "0")));
		if (margin_bottom == 0)
			margin_bottom = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "bottom", "0")));
		margin_bottom = UIHelper.dip2px(mContext, margin_bottom);

		int margin_left = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "margin-left", "0")));
		if (margin_left == 0)
			margin_left = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "left", "0")));
		margin_left = UIHelper.dip2px(mContext, margin_left);

		int margin_right = Integer.valueOf(removeUnit(MapHelper
				.getStringValueByKey(mProperties, "margin-right", "0")));
		if (margin_right == 0)
			margin_right = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "right", "0")));
		margin_right = UIHelper.dip2px(mContext, margin_right);

		Logger.d(getClass(), "margin top:" + margin_top + ",left:"
				+ margin_left + ",right:" + margin_right + ",bottom:"
				+ margin_bottom);
		if (margin_bottom == 0 && margin_left == 0 && margin_right == 0
				&& margin_top == 0)
			return;

		if (lp instanceof RelativeLayout.LayoutParams) {
			((RelativeLayout.LayoutParams) lp).setMargins(margin_left,
					margin_top, margin_right, margin_bottom);
		} else if (lp instanceof LinearLayout.LayoutParams) {
			((LinearLayout.LayoutParams) lp).setMargins(margin_left,
					margin_top, margin_right, margin_bottom);
		}
		mView.setLayoutParams(lp);

	}

	private void setAlign() {
		if (mProperties.containsKey("-mobile-halign")) {
			String align = MapHelper.getStringValueByKey(mProperties,
					"-mobile-halign", "");
			LayoutParams lp = mView.getLayoutParams();
			if (lp == null)
				return;
			if (lp instanceof RelativeLayout.LayoutParams) {
				if (align.equalsIgnoreCase("left")) {
					((RelativeLayout.LayoutParams) lp)
							.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				} else if (align.equalsIgnoreCase("right")) {
					((RelativeLayout.LayoutParams) lp)
							.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				} else if (align.equalsIgnoreCase("center")) {
					((RelativeLayout.LayoutParams) lp)
							.addRule(RelativeLayout.CENTER_HORIZONTAL);
				}
			} else if (lp instanceof LinearLayout.LayoutParams) {
				if (align.equalsIgnoreCase("left")) {
					((LinearLayout.LayoutParams) lp).gravity = Gravity.LEFT;
				} else if (align.equalsIgnoreCase("right")) {
					((LinearLayout.LayoutParams) lp).gravity = Gravity.RIGHT;
				} else if (align.equalsIgnoreCase("center")) {
					((LinearLayout.LayoutParams) lp).gravity = Gravity.CENTER_HORIZONTAL;
				}
			}
			mView.setLayoutParams(lp);
		}

		if (mProperties.containsKey("-mobile-valign")) {
			String align = MapHelper.getStringValueByKey(mProperties,
					"-mobile-valign", "");
			LayoutParams lp = mView.getLayoutParams();
			if (lp == null)
				return;
			if (lp instanceof RelativeLayout.LayoutParams) {
				if (align.equalsIgnoreCase("top")) {
					((RelativeLayout.LayoutParams) lp)
							.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				} else if (align.equalsIgnoreCase("bottom")) {
					((RelativeLayout.LayoutParams) lp)
							.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				} else if (align.equalsIgnoreCase("middle")) {
					((RelativeLayout.LayoutParams) lp)
							.addRule(RelativeLayout.CENTER_VERTICAL);
				}
			} else if (lp instanceof LinearLayout.LayoutParams) {
				if (align.equalsIgnoreCase("top")) {
					((LinearLayout.LayoutParams) lp).gravity = Gravity.TOP;
				} else if (align.equalsIgnoreCase("bottom")) {
					((LinearLayout.LayoutParams) lp).gravity = Gravity.BOTTOM;
				} else if (align.equalsIgnoreCase("middle")) {
					((LinearLayout.LayoutParams) lp).gravity = Gravity.CENTER_VERTICAL;
				}
			}
			mView.setLayoutParams(lp);
		}
	}

	private void setEditTextProperty() {
		EditText view = (EditText) mView;
		if (mProperties.containsKey("compound-padding")) {
			int compoundPadding = Integer
					.valueOf(removeUnit(MapHelper.getStringValueByKey(
							mProperties, "compound-padding", "0")));
			compoundPadding = UIHelper.dip2px(mContext, compoundPadding);
			view.setCompoundDrawablePadding(compoundPadding);
		}
	}

	private void setTextViewProperty() {
		TextView view = (TextView) mView;
		if (mProperties.containsKey("max-width")) {
			int maxWidth = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "max-width", "0")));
			maxWidth = UIHelper.dip2px(mContext, maxWidth);
			view.setMaxWidth(maxWidth);
		}

		if (mProperties.containsKey("max-height")) {
			int maxHeight = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "max-height", "0")));
			maxHeight = UIHelper.dip2px(mContext, maxHeight);
			view.setMaxHeight(maxHeight);
		}

		view.setTextColor(Color.BLACK);
		if (mProperties.containsKey("color")) {
			String color = MapHelper.getStringValueByKey(mProperties, "color",
					"#000000");
			view.setTextColor(CssColor.getColor(color));
		}

		if (mProperties.containsKey("font-size")) {
			float size = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "font-size", "16")));
			view.setTextSize(size);
		}

		if (mProperties.containsKey("font-style")) {
			String style = MapHelper.getStringValueByKey(mProperties,
					"font-style", "");
			if (style.equalsIgnoreCase("italic")) {
				view.setTypeface(null, Typeface.ITALIC);
			}
		}

		if (mProperties.containsKey("font-weight")) {
			String weight = MapHelper.getStringValueByKey(mProperties,
					"font-weight", "");
			if (weight.equalsIgnoreCase("bold")) {
				view.setTypeface(null, Typeface.BOLD);
			}
		}

		if (mProperties.containsKey("text-align")) {
			String align = MapHelper.getStringValueByKey(mProperties,
					"text-align", "");
			if (align.equalsIgnoreCase("left")) {
				view.setGravity(UIHelper.getGravity(view.getGravity(), Gravity.LEFT));
			} else if (align.equalsIgnoreCase("right")) {
				view.setGravity(UIHelper.getGravity(view.getGravity(), Gravity.RIGHT));
			} else if (align.equalsIgnoreCase("center")) {
				view.setGravity(UIHelper.getGravity(view.getGravity(), Gravity.CENTER));
			}
		}

		if (mProperties.containsKey("-mobile-text-key")) {
			String name = MapHelper.getStringValueByKey(mProperties,
					"-mobile-text-key", "");
			if (view instanceof NDTextView)
				((NDTextView)view).setText(name);
			else
				view.setText(name);
		}

		if (mProperties.containsKey("text-shadow")) {
			String shadow = MapHelper.getStringValueByKey(mProperties,
					"text-shadow", "");
			String tmp[] = shadow.trim().split(" ");
			if (tmp.length == 3) {
				view.setShadowLayer(5.0f, Float.valueOf(removeUnit(tmp[1])),
						Float.valueOf(removeUnit(tmp[2])),
						CssColor.getColor(tmp[0]));
			}
		}
	}

	private void setButtonProperty() {
		Button view = (Button) mView;
		if (mProperties.containsKey("max-width")) {
			int maxWidth = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "max-width", "0")));
			maxWidth = UIHelper.dip2px(mContext, maxWidth);
			view.setMaxWidth(maxWidth);
		}

		if (mProperties.containsKey("max-height")) {
			int maxHeight = Integer.valueOf(removeUnit(MapHelper
					.getStringValueByKey(mProperties, "max-height", "0")));
			maxHeight = UIHelper.dip2px(mContext, maxHeight);
			view.setMaxHeight(maxHeight);
		}

		if (mProperties.containsKey("color")) {
			String color = MapHelper.getStringValueByKey(mProperties, "color",
					"#000000");
			view.setTextColor(CssColor.getColor(color));
		}

		if (mProperties.containsKey("text-shadow")) {
			String shadow = MapHelper.getStringValueByKey(mProperties,
					"text-shadow", "");
			String tmp[] = shadow.trim().split(" ");
			if (tmp.length == 3) {
				view.setShadowLayer(5.0f, Float.valueOf(removeUnit(tmp[1])),
						Float.valueOf(removeUnit(tmp[2])),
						CssColor.getColor(tmp[0]));
			}
		}

		if (mProperties.containsKey("-mobile-text-key")) {
			String name = MapHelper.getStringValueByKey(mProperties,
					"-mobile-text-key", "");
			view.setText(name);
		}

		/*
		if (mProperties.containsKey("-mobile-image")) {
			String name = MapHelper.getStringValueByKey(mProperties,
					"-mobile-image", "");
			if (!TextUtils.isEmpty(name)) {
				Bitmap bmp = VmPlugin.imageBitmapWithAbsolutePath(name, 0, 0);
				if (bmp != null)
					view.setCompoundDrawables(null, new BitmapDrawable(bmp),
							null, null);
			}
		}
		*/
	}

	public String getProperty(String property, String def) {
		return MapHelper.getStringValueByKey(mProperties, property, def);
	}

	public String removeUnit(String value) {
		if (value.endsWith("px"))
			return value.substring(0, value.length() - 2);
		return value;
	}
}
