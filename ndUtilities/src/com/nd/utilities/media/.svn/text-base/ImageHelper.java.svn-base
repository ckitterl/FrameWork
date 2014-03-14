package com.nd.utilities.media;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ThumbnailUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.nd.utilities.logger.Logger;
import com.nd.utilities.ui.UIHelper;

/**
 * ImageHelper class
 * 
 */
public class ImageHelper {
	public static final int SCALE_ENLARGE = 100;
	public static final int SCALE_NARROW = 101;
	private float mWidthRatio=1;
	private float mHeightRatio=1;
	private static ImageHelper mInstance ;
	/**
	 * 单体
	 * @return
	 */
	public static ImageHelper instance(){
		if(mInstance == null){
			synchronized(ImageHelper.class){
				if(mInstance == null){
					mInstance = new ImageHelper();
			    }
			}
		}
		return mInstance;
	}
	/**
	 * 禁止从外部实例化
	 */
	private ImageHelper(){
	}
	/**
	 * 设置屏幕宽度与基准宽度的比率，例如，基准宽度为320
	 * @param ratio 比率
	 */
	public void setWidthRatio(float ratio){
		mWidthRatio = ratio;
	}
	/**
	 * 设置屏幕高度与基准高度的比率，例如，基准高度为320
	 * @param ratio 比率
	 */
	public void setHeightRatio(float ratio){
		mHeightRatio=ratio;
	}

	/**
	 * 圓角圖片，pixels為弧度
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		if (bitmap == null)
			return null;
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 給已知圖片外圍加上圓圈
	 * 
	 * @param bitmap
	 * @param color
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap, int color) {
		int x = bitmap.getWidth();
		Bitmap output = Bitmap.createBitmap(x, x, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		// 根据原来图片大小画一个矩形
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		paint.setColor(0xff424242);
		// 画出一个圆
		canvas.drawCircle(x / 2, x / 2, x / 2, paint);
		// 取两层绘制交集,显示上层
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// 将图片画上去
		canvas.drawBitmap(bitmap, rect, rect, paint);
		paint.setColor(color);
		/* 设置paint的 style 为STROKE：空心 */
		paint.setStyle(Paint.Style.STROKE);
		/* 设置paint的外框宽度 */
		paint.setStrokeWidth(8);
		/* 画一个空心圆形 */
		canvas.drawCircle(x / 2, x / 2, x / 2, paint);
		// 返回Bitmap对象
		return output;
	}

	/**
	 * 从url中获取图片时保存缓存的的key
	 * 
	 * @param url
	 * @param scaleType
	 * @return
	 */
	public static String getBitmapKeyFromUrl(String url, int scaleType) {
		return url + "_" + scaleType;
	}

	/**
	 * 從url中獲取圖片，不保存在本地
	 * 
	 * @param url
	 * @return
	 */
	public  Bitmap getBitmapFromUrl(String url, int scaleType) {
		Logger.i(ImageHelper.class, "start image download");
		Bitmap result = BitMapCache.instance().getBitmapCacheByKey(
				getBitmapKeyFromUrl(url, scaleType));
		if (result == null) {
			InputStream is = null;
			try {
				URL aURL = new URL(url);
				HttpURLConnection httpconn = (HttpURLConnection) aURL
						.openConnection();
				if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					is = httpconn.getInputStream();
					byte[] bt = getBytes(is);
					result = getBitmapFromByte(bt, 0, 0, scaleType);
					bt = null;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Logger.i(ImageHelper.class, "end image download");
			BitMapCache.instance().addBitmapCache(url, result);
		}
		return result;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		Logger.i(ImageHelper.class, "outHeight:" + height);
		Logger.i(ImageHelper.class, "outWidth:" + width);
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// if (width > height) {
			int heightSize = Math.round((float) height / (float) reqHeight);
			// } else {
			int widthSize = Math.round((float) width / (float) reqWidth);
			// }
			if (heightSize > widthSize)
				inSampleSize = heightSize;
			else
				inSampleSize = widthSize;
		}
		return inSampleSize;
	}

	/**
	 * 根據返回的options，可得到圖片的真實的寬高
	 * 
	 * @param res
	 * @return
	 */
	public static BitmapFactory.Options getBitmapSizeFromRes(Application app,int res) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(app.getResources(), res,options);
		return options;
	}

	public static BitmapFactory.Options getBitmapSizeFromAssets(Application app,String assetsFile) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(app.getAssets().open(assetsFile),null, options);
		return options;
	}

	/**
	 * 根據返回的options，可得到圖片的真實的寬高
	 * 
	 * @param res
	 * @return
	 */
	public static BitmapFactory.Options getBitmapSizeFromFlie(File file)
			throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new FileInputStream(file), null, options);
		return options;
	}

	/**
	 * 根據返回的options，可得到圖片的真實的寬高
	 * 
	 * @param res
	 * @return
	 */
	public BitmapFactory.Options getBitmapSizeFromRes(Application app,int res,int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(app.getResources(), res,options);
		BitmapFactory.Options tmp = getSquare(options, reqWidth, reqHeight);
		reqWidth = tmp.outWidth;
		reqHeight = tmp.outHeight;
		return tmp;
	}

	/**
	 * 根據返回的options，可得到圖片的真實的寬高
	 * 
	 * @param res
	 * @return
	 */
	public BitmapFactory.Options getBitmapSizeFromFile(File file,
			int reqWidth, int reqHeight) throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new FileInputStream(file), null, options);
		BitmapFactory.Options tmp = getSquare(options, reqWidth, reqHeight);
		reqWidth = tmp.outWidth;
		reqHeight = tmp.outHeight;
		return tmp;
	}

	public static Bitmap bmpScale(Bitmap result, int reqWidth,
			int reqHeight, int scaleType) {
		Logger.d(ImageHelper.class, "reqwidth:" + reqWidth + ",reqheight:"
				+ reqHeight);
		if (result == null)
			return result;
		Logger.d(ImageHelper.class, "before width:" + result.getWidth()
				+ ",height:" + result.getHeight());
		if (scaleType == ImageHelper.SCALE_ENLARGE) {
			if (result.getWidth() < reqWidth
					|| result.getHeight() < reqHeight) {
				float scaleW = (float) reqWidth / result.getWidth();
				float scaleH = (float) reqHeight / result.getHeight();
				Logger.d(ImageHelper.class,
						"before density:" + result.getDensity());
				int density = result.getDensity();
				if (density == 0) {
					density = DisplayMetrics.DENSITY_DEFAULT;
				}
				if (scaleW >= scaleH) {
					result.setDensity((int) (density / scaleW));
				} else {
					result.setDensity((int) (density / scaleH));
				}
				Logger.d(ImageHelper.class,
						"after density:" + result.getDensity());
			}
		} else if (scaleType == ImageHelper.SCALE_NARROW) {
			float scaleW = (float) reqWidth / result.getWidth();
			float scaleH = (float) reqHeight / result.getHeight();
			if (result.getWidth() > reqWidth
					|| result.getHeight() > reqHeight) {
				if (scaleW <= scaleH) {
					result = Bitmap
							.createScaledBitmap(
									result,
									reqWidth,
									result.getHeight() * reqWidth
											/ result.getWidth(), true);
				} else {
					result = Bitmap.createScaledBitmap(
							result,
							result.getWidth() * reqHeight
									/ result.getHeight(), reqHeight, true);
				}
			} else {
				Logger.d(ImageHelper.class,
						"before density:" + result.getDensity());
				int density = result.getDensity();
				if (density == 0) {
					density = DisplayMetrics.DENSITY_DEFAULT;
				}
				if (scaleW >= scaleH) {
					result.setDensity((int) (density / scaleH));
				} else {
					result.setDensity((int) (density / scaleW));
				}
				Logger.d(ImageHelper.class,
						"after density:" + result.getDensity());
			}
		}
		Logger.d(ImageHelper.class, "after width:" + result.getWidth()
				+ ",height:" + result.getHeight());
		return result;
	}

	/**
	 * 根据资源id取图片，使用缓存时的key
	 * 
	 * @param res
	 * @param reqWidth
	 * @param reqHeight
	 * @param scaleType
	 * @return
	 */
	public static String getBitmapKeyFromRes(int res, int reqWidth,
			int reqHeight, int scaleType) {
		return res + "_" + reqWidth + "_" + reqHeight + "_" + scaleType;
	}

	/**
	 * 根據指定的寬高按比例壓縮圖片
	 * 
	 * @param res
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap getBitmapFromRes(Application app,int res, int reqWidth,
			int reqHeight, int scaleType) {
		String key = getBitmapKeyFromRes(res, reqWidth, reqHeight,scaleType);
		Bitmap result = BitMapCache.instance().getBitmapCacheByKey(key);
		if (result == null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(app.getResources(),res, options);
			BitmapFactory.Options tmp = getSquare(options, reqWidth,reqHeight);
			reqWidth = tmp.outWidth;
			reqHeight = tmp.outHeight;
			options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
			Logger.i(ImageHelper.class, "inSampleSize:" + options.inSampleSize);
			options.inJustDecodeBounds = false;
			result = getBitmapFromRes(app,res, reqWidth, reqHeight,scaleType, options);
			BitMapCache.instance().addBitmapCache(key, result);
		}
		return result;
	}

	private static Bitmap getBitmapFromRes(Application app,int res, int reqWidth,int reqHeight, int scaleType, BitmapFactory.Options options) {
		Bitmap result = null;
		try {
			options.inScaled = false;
			result = BitmapFactory.decodeStream(app.getResources().openRawResource(res), null, options);
			result = bmpScale(result, reqWidth, reqHeight, scaleType);
		} catch (OutOfMemoryError e) {
			Logger.e(ImageHelper.class, "OutOfMemoryError");
			if (options.inSampleSize == 4)
				return null;
			System.gc();
			System.runFinalization();
			options.inSampleSize = options.inSampleSize + 1;
			result = getBitmapFromRes(app,res, reqWidth, reqHeight,scaleType, options);
		}
		return result;
	}

	/**
	 * 根據指定的寬高按比例壓縮圖片
	 * 
	 * @param byte
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap getBitmapFromByte(byte[] byteArray, int reqWidth,
			int reqHeight, int scaleType) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new ByteArrayInputStream(byteArray), null,
				options);
		BitmapFactory.Options tmp = getSquare(options, reqWidth, reqHeight);
		reqWidth = tmp.outWidth;
		reqHeight = tmp.outHeight;
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		Logger.i(ImageHelper.class, "inSampleSize:" + options.inSampleSize);
		options.inJustDecodeBounds = false;
		Bitmap result = getBitmapFromByte(byteArray, reqWidth, reqHeight,
				scaleType, options);
		return result;
	}

	private static Bitmap getBitmapFromByte(byte[] byteArray, int reqWidth,
			int reqHeight, int scaleType, BitmapFactory.Options options) {
		Bitmap result = null;
		try {
			options.inScaled = false;
			result = BitmapFactory.decodeStream(
					new ByteArrayInputStream(byteArray), null, options);
			result = bmpScale(result, reqWidth, reqHeight, scaleType);
		} catch (OutOfMemoryError e) {
			Logger.e(ImageHelper.class, "OutOfMemoryError");
			if (options.inSampleSize == 4)
				return null;
			System.gc();
			System.runFinalization();
			options.inSampleSize = options.inSampleSize + 1;
			result = getBitmapFromByte(byteArray, reqWidth, reqHeight,
					scaleType, options);
		}
		return result;
	}

	public static Bitmap getCacheBitmap(String name, int reqWidth,
			int reqHeight) {
		String key = name + String.valueOf(reqWidth)
				+ String.valueOf(reqHeight);
		Bitmap result = BitMapCache.instance().getBitmapCacheByKey(key);
		return result;
	}

	/**
	 * 根据文件取图片时使用的缓存的key
	 * 
	 * @param file
	 * @param reqWidth
	 * @param reqHeight
	 * @param scaleType
	 * @return
	 */
	public static String getBitmapKeyFromFile(File file, int reqWidth,
			int reqHeight, int scaleType) {
		return file.getAbsolutePath() + "_" + reqWidth + "_" + reqHeight
				+ "_" + scaleType;
	}

	/**
	 * 根據指定的寬高按比例縮小圖片
	 * 
	 * @param file
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap getBitmapFromFile(File file, int reqWidth,
			int reqHeight, int scaleType) throws FileNotFoundException {
		String key = getBitmapKeyFromFile(file, reqWidth, reqHeight,
				scaleType);
		Bitmap result = BitMapCache.instance().getBitmapCacheByKey(key);
		if (result == null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null,
					options);
			BitmapFactory.Options tmp = getSquare(options, reqWidth,
					reqHeight);
			reqWidth = tmp.outWidth;
			reqHeight = tmp.outHeight;
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			Logger.i(ImageHelper.class, "inSampleSize:" + options.inSampleSize);
			options.inJustDecodeBounds = false;
			result = getBitmapFromFile(file, reqWidth, reqHeight,
					scaleType, options);
			BitMapCache.instance().addBitmapCache(key, result);
		}
		return result;
	}

	private static Bitmap getBitmapFromFile(File file, int reqWidth,
			int reqHeight, int scaleType, BitmapFactory.Options options)
			throws FileNotFoundException {
		Bitmap result = null;
		try {
			options.inScaled = false;
			result = BitmapFactory.decodeStream(new FileInputStream(file),
					null, options);
			result = bmpScale(result, reqWidth, reqHeight, scaleType);
		} catch (OutOfMemoryError e) {
			Logger.e(ImageHelper.class, "OutOfMemoryError");
			if (options.inSampleSize == 4)
				return null;
			System.gc();
			System.runFinalization();
			options.inSampleSize = options.inSampleSize + 1;
			result = getBitmapFromFile(file, reqWidth, reqHeight,
					scaleType, options);
		}
		return result;
	}

	/**
	 * 从assets中取图片时使用缓存的key
	 * 
	 * @param assetsFile
	 * @param reqWidth
	 * @param reqHeight
	 * @param scaleType
	 * @return
	 */
	public static String getBitmapKeyFromAssets(String assetsFile,
			int reqWidth, int reqHeight, int scaleType) {
		return assetsFile + "_" + reqWidth + "_" + reqHeight + "_"
				+ scaleType;
	}

	/**
	 * 從assets中根據指定的寬高按比例縮小圖片
	 * 
	 * @param assetsFile
	 *            path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap getBitmapFromAssets(Application app,String assetsFile, int reqWidth,int reqHeight, int scaleType) {
		String key = getBitmapKeyFromAssets(assetsFile, reqWidth,reqHeight, scaleType);
		Bitmap result = BitMapCache.instance().getBitmapCacheByKey(key);
		if (result == null) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(app.getAssets().open(assetsFile), null, options);
				BitmapFactory.Options tmp = getSquare(options, reqWidth,reqHeight);
				reqWidth = tmp.outWidth;
				reqHeight = tmp.outHeight;
				options.inSampleSize = calculateInSampleSize(options,reqWidth, reqHeight);
				Logger.i(ImageHelper.class, "inSampleSize:"
						+ options.inSampleSize);
				options.inJustDecodeBounds = false;
				result = getBitmapFromAssets(app,assetsFile, reqWidth,reqHeight, scaleType, options);
				BitMapCache.instance().addBitmapCache(key, result);
			} catch (Exception e) {
				// Logger.e(ImageHelper.class, "no file:"+assetsFile);
			}
		}
		return result;
	}

	private static Bitmap getBitmapFromAssets(Application app,String assetsFile,
			int reqWidth, int reqHeight, int scaleType,BitmapFactory.Options options) throws IOException {
		Bitmap result = null;
		try {
			options.inScaled = false;
			result = BitmapFactory.decodeStream(app.getAssets().open(assetsFile), null, options);
			result = bmpScale(result, reqWidth, reqHeight, scaleType);
		} catch (OutOfMemoryError e) {
			Logger.e(ImageHelper.class, "OutOfMemoryError");
			if (options.inSampleSize == 4)
				return null;
			System.gc();
			System.runFinalization();
			options.inSampleSize = options.inSampleSize + 1;
			result = getBitmapFromAssets(app,assetsFile, reqWidth, reqHeight,scaleType, options);
		}
		return result;
	}

	private BitmapFactory.Options getSquare(	BitmapFactory.Options options, int reqWidth, int reqHeight) {
		BitmapFactory.Options result = new BitmapFactory.Options();
		if (reqWidth <= 0 && reqHeight > 0) {
			reqWidth = options.outWidth * reqHeight / options.outHeight;
		} else if (reqWidth > 0 && reqHeight <= 0) {
			reqHeight = options.outHeight * reqWidth / options.outWidth;
		} else if (reqWidth <= 0 && reqHeight <= 0) {
			reqWidth = (int) (mWidthRatio* options.outWidth);
			reqHeight = (int) (mHeightRatio * options.outHeight);
		}
		result.outHeight = reqHeight;
		result.outWidth = reqWidth;
		return result;
	}

	public static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = is.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		byte[] bytes = baos.toByteArray();
		baos.close();
		baos = null;
		return bytes;
	}

	/**
	 * 圖片保存到sdcard上
	 * 
	 * @param src
	 * @param filepath
	 * @throws IOException
	 */
	public static void saveBitmapToFile(Bitmap src, String filepath)
			throws IOException {
		if(src==null)
			return;
		File f = new File(filepath);
		f.createNewFile();
		FileOutputStream fOut = new FileOutputStream(f);
		if (filepath.endsWith(".png"))
			src.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		else
			src.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		if (fOut != null) {
			fOut.flush();
			fOut.close();
			fOut = null;
		}
	}

	/**
	 * 根据资源名称取图片时使用缓存的key
	 * 
	 * @param fileName
	 * @param reqWidth
	 * @param reqHeight
	 * @param scaleType
	 * @return
	 */
	public static String getBitmapKeyFromRes(String fileName, int reqWidth,
			int reqHeight, int scaleType) {
		return fileName + "_" + reqWidth + "_" + reqHeight + "_"
				+ scaleType;
	}

	private static void setBitmap(ImageView view, Bitmap bmp, int reqWidth,
			int reqHeight) {
		view.setMaxWidth(bmp.getWidth() < reqWidth ? reqWidth : bmp.getWidth());
		view.setMaxHeight(bmp.getHeight() < reqHeight ? reqHeight : bmp
				.getHeight());
		view.setAdjustViewBounds(true);
		view.setImageBitmap(bmp);
	}

	public static void changeLight(ImageView imageView, int brightness) {
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
	}

	/**
	 * 焦點在view上面時
	 * 
	 * @param view
	 */
	public static void focusOn(ImageView view) {
		// changeLight((ImageView) view, -50);
		view.setColorFilter(Color.GRAY, Mode.MULTIPLY);
	}

	/**
	 * 焦點離開view時
	 * 
	 * @param view
	 */
	public static void focusMove(ImageView view) {
		// changeLight((ImageView) view, 0);
		view.setColorFilter(Color.WHITE, Mode.MULTIPLY);
	}

	/**
	 * 
	 * @param outerView
	 *            imageView的外層view
	 * @param image
	 *            要改變亮度的imageView
	 */
	public static void changeImageViewLight(View outerView,
			final ImageView image) {
		outerView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					focusOn(image);
				}
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					focusMove(image);
				}
				return false;
			}
		});
	}

	/**
	 * 取得相冊中最後一張插入的圖片
	 * 
	 * @return 圖片的Uri
	 */
	/*
	public static Uri getLastInsertPhotoUri() {
		Cursor cursor = ndApp
				.getInstance()
				.getContentResolver()
				.query(Uri.parse("content://media/external/images/media/"),
						new String[] { "max(_id)" }, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			Uri uri = Uri.parse("content://media/external/images/media/"
					+ cursor.getString(0));
			cursor.close();
			return uri;
		}
		return null;
	}
	*/

	/**
	 * 取得虛線
	 * 
	 * @return
	 */
	public static Bitmap getDottedBitmap() {
		Bitmap bmp = Bitmap.createBitmap(400, 5, Bitmap.Config.ALPHA_8);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(2);
		Path path = new Path();
		path.moveTo(0, 3);
		path.lineTo(400, 3);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
		canvas.save();
		canvas.restore();
		return bmp;
	}

	/**
	 * 取得截屏的Bitmap 不能截取狀態欄
	 * 
	 * @return
	 */
	/*
	public static Bitmap getCaptureBitmap() {
		return getCaptureBitmap(ndApp.getInstance().getCurrentActivity());
	}

	public static Bitmap getCaptureBitmap(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(
				view.getDrawingCache(),
				0,
				environment.getScreenHeight()
						- environment.getValidScreenHeight(),
				environment.getScreenWidth(),
				environment.getValidScreenHeight());
		view.setDrawingCacheEnabled(false);
		return bmp;
	}

	public static boolean screenShot(Activity activity, String filePath) {
		Bitmap bmp = getCaptureBitmap(activity);
		try {
			ImageHelper.saveBitmapToFile(bmp, filePath);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	*/

	/**
	 * 繪製圓角的背景圖
	 * 
	 * @param bgColor
	 * @param strokeWidth
	 * @param strokeColor
	 * @param cornerRadius
	 * @return
	 */
	public static GradientDrawable createGradientDrawable(String bgColor,
			int strokeWidth, String strokeColor, int cornerRadius) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setColor(UIHelper.getColor(bgColor));
		if (strokeWidth > 0)
			drawable.setStroke(strokeWidth, UIHelper.getColor(strokeColor));
		if (cornerRadius > 0)
			drawable.setCornerRadius(cornerRadius);
		return drawable;
	}

	public static Bitmap createBitmap(int width, int height) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		} catch (OutOfMemoryError e) {
			while (bitmap == null) {
				System.gc();
				System.runFinalization();
				bitmap = createBitmap(width, height);
			}
		}
		return bitmap;
	}


	public static Bitmap viewShot(View v) {
		v.clearFocus(); // 清除视图焦点
		v.setPressed(false);// 将视图设为不可点击

		boolean willNotCache = v.willNotCacheDrawing(); // 返回视图是否可以保存他的画图缓存
		v.setWillNotCacheDrawing(false);

		// Reset the drawing cache background color to fully transparent
		// for the duration of this operation //将视图在此操作时置为透明
		int color = v.getDrawingCacheBackgroundColor(); // 获得绘制缓存位图的背景颜色
		v.setDrawingCacheBackgroundColor(0); // 设置绘图背景颜色
		if (color != 0) { // 如果获得的背景不是黑色的则释放以前的绘图缓存
			v.destroyDrawingCache(); // 释放绘图资源所使用的缓存
		}
		v.buildDrawingCache(); // 重新创建绘图缓存，此时的背景色是黑色
		Bitmap cacheBitmap = v.getDrawingCache(); // 将绘图缓存得到的,注意这里得到的只是一个图像的引用
		if (cacheBitmap == null) {
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap); // 将位图实例化
		// Restore the view //恢复视图
		v.destroyDrawingCache();// 释放位图内存
		v.setWillNotCacheDrawing(willNotCache);// 返回以前缓存设置
		v.setDrawingCacheBackgroundColor(color);// 返回以前的缓存颜色设置
		return bitmap;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	public static StateListDrawable createSelector(Context context,
			Drawable normalDrawable, Drawable pressedDrawable){
		StateListDrawable sd = new StateListDrawable();
		int enable = android.R.attr.state_enabled;
		int focus = android.R.attr.state_focused;
		int press = android.R.attr.state_pressed;
		sd.addState(new int[] { enable, focus }, pressedDrawable);
		sd.addState(new int[] { press, enable }, pressedDrawable);
		sd.addState(new int[] { focus }, pressedDrawable);
		sd.addState(new int[] { press }, pressedDrawable);
		sd.addState(new int[] { enable }, normalDrawable);
		sd.addState(new int[] {}, normalDrawable);
		return sd;
	}
	
	public static StateListDrawable createSelectorForList(Context context,
			Drawable selectedDrawable, Drawable pressedDrawable){
		return createSelector(context, selectedDrawable, pressedDrawable);
	}
}
