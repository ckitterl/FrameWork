package com.nd.utilities.ui.view;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ZoomButtonsController;
import android.widget.RelativeLayout.LayoutParams;

import com.nd.utilities.logger.Logger;

public class NDWebView extends WebView {
	private Context mContext;

	public NDWebView(Context context) {
		super(context);
		mContext = context;
		WebSettings();
		BasicSettings();
	}

	public NDWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		WebSettings();
		BasicSettings();
	}

	public int getScrollWidth() {
		return computeHorizontalScrollRange();
	}

	public int getScrollHeight() {
		return computeVerticalScrollRange();
	}

	public void disableUserSelect() {
		setOnLongClickListener(new WebView.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
	}

	private void WebSettings() {
		WebSettings settings = getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true);
		/*
		String databasePath = ndApp.getInstance().getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		settings.setDatabasePath(databasePath);
		settings.setGeolocationEnabled(true);
		settings.setGeolocationDatabasePath(databasePath);
		*/
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 默认使用缓存
		settings.setAppCacheMaxSize(8 * 1024 * 1024);// 缓存最多可以有8M
		settings.setAllowFileAccess(true);// 可以读取文件缓存(manifest生效)
		settings.setAppCacheEnabled(true);// 应用可以有缓存
		settings.setPluginState(PluginState.ON);
		enableZoom();
	}

	public void enableScale() {
		Display display = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay();
		setInitialScale(display.getWidth() / 10);
		WebSettings settings = getSettings();
		settings.setUseWideViewPort(true);
	}

	public void enableZoom() {
		getSettings().setSupportZoom(true);
		getSettings().setBuiltInZoomControls(true);
		if (Build.VERSION.SDK_INT > 10) {
			getSettings().setDisplayZoomControls(false);
		} else {
			setZoomControlGone(this);
		}
	}

	public void disableZoom() {
		getSettings().setSupportZoom(false);
		getSettings().setBuiltInZoomControls(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		try {
			return super.onTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setZoomControlGone(View view) {
		Class classType;
		Field field;
		try {
			classType = WebView.class;
			field = classType.getDeclaredField("mZoomButtonsController");
			field.setAccessible(true);
			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
					view);
			mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
			try {
				field.set(view, mZoomButtonsController);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private void BasicSettings() {
		setHorizontalScrollbarOverlay(true);
		setVerticalScrollbarOverlay(true);
		requestFocusFromTouch();
		setAlwaysDrawnWithCacheEnabled(true);
		setWebChromeClient(chromeClient);
		setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				// 实现下载的代码
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				mContext.startActivity(intent);
			}
		});
	}
	private static View m_webVideoView;
	private static CustomViewCallback m_customViewCallback;
	/*
	public static void initWebVideo(View view,CustomViewCallback customViewCallback){
		m_webVideoView=view;
		m_customViewCallback=customViewCallback;
		ndApp.getInstance()
		.getCurrentActivity()
		.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		view.setBackgroundColor(Color.BLACK);
		
		ndApp.getInstance().getRootAppControl().getRootView()
				.addView(view, lp);
	}
	public static void destoryWebVideo(){
		m_webVideoView.setVisibility(View.GONE);
		ndApp.getInstance().getRootAppControl().getRootView().removeView(m_webVideoView);
		if(m_customViewCallback!=null)
			m_customViewCallback.onCustomViewHidden();
		m_webVideoView=null;
		m_customViewCallback=null;		
		ndApp.getInstance()
		.getCurrentActivity()
		.setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
	}
	*/
	public static boolean inWebVideoState(){
		return m_webVideoView==null?false:true;
	}
	
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	WebChromeClient chromeClient = new WebChromeClient() {
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			Logger.e(getClass(), "view:" + view + ",callback:" + callback);
			//initWebVideo(view, callback);
		}
		
		// 上傳附件
		// 3.0 + 调用这个方法
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType) {
			openFileChooser(uploadMsg);
		}

		// 3.0 - 调用这个方法
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			((Activity) mContext).startActivityForResult(
					Intent.createChooser(i, "Browser"), FILECHOOSER_RESULTCODE);
		}

		@Override
		public void onExceededDatabaseQuota(String url,
				String databaseIdentifier, long currentQuota,
				long estimatedSize, long totalUsedQuota,
				WebStorage.QuotaUpdater quotaUpdater) {
			quotaUpdater.updateQuota(estimatedSize * 2);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			Builder builder = new Builder(mContext);
			builder.setTitle("Alert");
			builder.setMessage(message);
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						// @Override
						public void onClick(DialogInterface dialog, int which) {
							result.confirm();
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			Builder builder = new Builder(mContext);
			builder.setTitle("Confirm");
			builder.setMessage(message);
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						// @Override
						public void onClick(DialogInterface dialog, int which) {
							result.confirm();
						}
					});
			builder.setNeutralButton(android.R.string.cancel,
					new AlertDialog.OnClickListener() {
						// @Override
						public void onClick(DialogInterface dialog, int which) {
							result.cancel();
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
		}
	};

	public void fileBrowserResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
					: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	public void pause() {
		pauseTimers();
		callHiddenWebViewMethod("onPause");
	}

	public void resume() {
		resumeTimers();
		callHiddenWebViewMethod("onResume");
	}

	public void clear() {
		try {
			stopLoading();
			clearHistory();
			clearFormData();
			clearCache(true);
			pause();
			if (getParent() != null) {
				((ViewGroup) getParent()).removeView(this);
			}
			destroy();
			// mContext.deleteDatabase("webview.db");
			// mContext.deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void callHiddenWebViewMethod(String name) {
		try {
			Method method = WebView.class.getMethod(name);
			method.invoke(this);
		} catch (NoSuchMethodException e) {
			Logger.i(getClass(), "No such method: " + name);
		} catch (IllegalAccessException e) {
			Logger.i(getClass(), "Illegal Access: " + name);
		} catch (InvocationTargetException e) {
			Logger.d(getClass(), "Invocation Target Exception: " + name);
		}
	}
}
