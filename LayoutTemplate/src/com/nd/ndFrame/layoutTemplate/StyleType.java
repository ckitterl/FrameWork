package com.nd.ndFrame.layoutTemplate;

/**
 * 定义一些节点的结构样式。目前只定义了一些实现需要的样式，所以不是很完整
 * 
 * @author caimk
 * 
 */
public enum StyleType {
	PADDING, PADDING_LEFT, PADDING_RIGHT, PADDING_TOP, PADDING_BOTTOM,

	LAYOUT_MARGIN, LAYOUT_MARGIN_LEFT, LAYOUT_MARGIN_RIGHT, LAYOUT_MARGIN_TOP, LAYOUT_MARGIN_BOTTOM,

	CHILD_LAYOUT_MARGIN, CHILD_LAYOUT_MARGIN_LEFT, CHILD_LAYOUT_MARGIN_RIGHT, CHILD_LAYOUT_MARGIN_TOP, CHILD_LAYOUT_MARGIN_BOTTOM,

	LAYOUT_WIDTH, LAYOUT_HEIGHT,

	LAYOUT_WEIGHT;
}
