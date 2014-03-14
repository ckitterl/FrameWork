package com.nd.utilities.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NDScrollView extends ScrollView {
	private float xDistance, yDistance, xLast, yLast;

	public NDScrollView(Context context) {
		super(context);
	}

	public NDScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean canScroll = true;

	public void setCanScroll(boolean flag) {
		canScroll = flag;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!canScroll)
			return false;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	private int lastY = 0;
	private int touchEventId = -9983761;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == touchEventId) {
				if (lastY == getScrollY()) {
					if (scrollEndListener != null)
						scrollEndListener.scrollEnd(NDScrollView.this,
								(MotionEvent) msg.obj);
				} else {
					handler.sendMessageDelayed(
							handler.obtainMessage(touchEventId, msg.obj), 1);
					lastY = getScrollY();
				}
			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			handler.sendMessageDelayed(handler.obtainMessage(touchEventId, ev),
					1);
		}
		return super.onTouchEvent(ev);
	}

	private OnScrollEndListener scrollEndListener;

	public void setOnScrollEndListener(OnScrollEndListener p_onScrollEndListener) {
		scrollEndListener = p_onScrollEndListener;
	}
}
