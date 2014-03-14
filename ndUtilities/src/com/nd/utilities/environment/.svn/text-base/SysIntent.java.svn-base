package com.nd.utilities.environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;
import android.text.TextUtils;

public class SysIntent {
	public final static String TYPE_IMAGE = "image/*";
	public final static String TYPE_TXT = "text/plain";
	public final static String TYPE_PDF = "application/pdf";
	public final static String TYPE_CHM = "application/x-chm";
	public final static String TYPE_WORD = "application/msword";
	public final static String TYPE_EXCEL = "application/vnd.ms-excel";
	public final static String TYPE_PPT = "application/vnd.ms-powerpoint";
	public final static String TYPE_AUDIO = "audio/*";
	public final static String TYPE_VIDEO = "video/*";
	public final static int CHOOSE_PICTURE = 500;
	public final static int TAKE_PHOTO = 501;
	public final static int CUT_PHOTO=503;
	/**
	 * 打開瀏覽器
	 * 
	 * @param activity
	 * @param url
	 */
	public static boolean browse(Activity activity, String url) {
		try {
			if (!url.startsWith("http://"))
				url = "http://" + url;
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			activity.startActivity(it);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 撥號
	 * 
	 * @param activity
	 * @param phonenum
	 */
	public static boolean dial(Activity activity, String phonenum) {
		try {
			if (!phonenum.startsWith("tel:"))
				phonenum = "tel:" + phonenum;
			Uri uri = Uri.parse(phonenum);
			Intent it = new Intent(Intent.ACTION_DIAL, uri);
			activity.startActivity(it);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 呼叫
	 * 
	 * @param activity
	 * @param phonenum
	 */
	public static boolean call(Activity activity, String phonenum) {
		try {
			if (!phonenum.startsWith("tel:"))
				phonenum = "tel:" + phonenum;
			Uri uri = Uri.parse(phonenum);
			Intent it = new Intent(Intent.ACTION_CALL, uri);
			activity.startActivity(it);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 地圖，傳入經緯度，以,隔開
	 * 
	 * @param activity
	 * @param geo
	 */
	public static boolean map(Activity activity, double lat,double lon,String address) {
		try {
			String uri = "geo:"+ lat + "," + lon + "?q="+address;
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			activity.startActivity(it);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 安裝
	 * 
	 * @param activity
	 * @param packageName
	 */
	public static boolean install(Activity activity, String packageName) {
		try {
			Uri installUri = Uri.fromParts("package", packageName, null);
			Intent returnIt = new Intent(Intent.ACTION_PACKAGE_ADDED,
					installUri);
			activity.startActivity(returnIt);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 發送短信
	 * 
	 * @param activity
	 * @param phonenum
	 * @param content
	 */
	public static boolean sendSMS(Activity activity, String phonenum,
			String content) {
		try {
			if (!phonenum.startsWith("smsto:"))
				phonenum = "smsto:" + phonenum;
			Uri uri = Uri.parse(phonenum);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			it.putExtra("sms_body", content);
			it.setType("vnd.android-dir/mms-sms");
			activity.startActivity(it);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 發送彩信
	 * 
	 * @param activity
	 * @param phonenum
	 * @param content
	 * @param filePath
	 * @param type
	 */
	public static boolean sendMMS(Activity activity, String phonenum,
			String content, String filePath, String type) {
		try {
			// if (!TextUtils.isEmpty(phonenum) &&
			// !phonenum.startsWith("mms://"))
			// phonenum = "mms://" + phonenum;
			Intent mIntent = null;
			// Uri smsToUri = Uri.parse(phonenum);// 联系人地址
			mIntent = new Intent(android.content.Intent.ACTION_SEND);// ,
																		// smsToUri);
			mIntent.putExtra("address", phonenum);
			mIntent.putExtra("sms_body", content);
			if (!TextUtils.isEmpty(filePath)) {
				Uri uri = Uri.fromFile(new File(filePath));
				mIntent.putExtra(Intent.EXTRA_STREAM, uri);
			}
			mIntent.setType(type);
			activity.startActivity(mIntent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 發送郵件
	 * 
	 * @param activity
	 * @param receiver
	 * @param subject
	 * @param content
	 * @param filePath
	 */
	public static boolean sendEmail(Activity activity, String[] receiver,
			String subject, String content, String filePath) {
		try {
			Intent email = new Intent(android.content.Intent.ACTION_SEND);
			email.setType("plain/text");
			email.putExtra(android.content.Intent.EXTRA_EMAIL, receiver);
			email.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
			email.putExtra(android.content.Intent.EXTRA_TEXT, content);
			if (!TextUtils.isEmpty(filePath)) {
				email.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(filePath)));
				email.setType("application/octet-stream");
			}
			email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(email);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 卸載
	 * 
	 * @param activity
	 * @param packageName
	 */
	public static boolean unInstall(Activity activity, String packageName) {
		try {
			Uri unInstallUri = Uri.fromParts("package", packageName, null);
			Intent returnIt = new Intent(Intent.ACTION_DELETE, unInstallUri);
			activity.startActivity(returnIt);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 導航
	 * 
	 * @param activity
	 * @param fromlan
	 * @param fromlon
	 * @param tolan
	 * @param tolon
	 */
	public static boolean navigation(Activity activity, double fromlan,
			double fromlon, double tolan, double tolon) {
		try {
			String from = fromlan + "," + fromlon;
			String to = tolan + "," + tolon;
			String url = "http://maps.google.com/maps?f=d&saddr=" + from
					+ "&daddr=" + to + "&h1=tw";
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean BNavigation(Activity activity, double lat,
			double lon, String addr) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri uri = Uri.parse("geo:" + lat + "," + lon + "," + addr);
			intent.setData(uri);
			intent.setPackage("com.baidu.BaiduMap");
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 打開文件格式 "image/*"，"text/plain"，application/pdf";，"application/x-chm";，
	 * "application/msword"
	 * ;，"application/vnd.ms-excel"，"application/vnd.ms-powerpoint";
	 * 
	 * @param activity
	 * @param filePath
	 * @param fileType
	 */
	public static boolean openFile(Activity activity, String filePath,
			String fileType) {
		try {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(new File(filePath));
			intent.setDataAndType(uri, fileType);
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 打開html文件
	 * 
	 * @param activity
	 * @param param
	 */
	public static boolean openHtmlFile(Activity activity, String param) {
		try {
			Uri uri = Uri.parse(param).buildUpon()
					.encodedAuthority("com.android.htmlfileprovider")
					.scheme("content").encodedPath(param).build();
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.setDataAndType(uri, "text/html");
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 打開音頻，視頻文件 "audio/*"，"video/*";
	 * 
	 * @param activity
	 * @param filePath
	 * @param fileType
	 */
	public static boolean openMediaFile(Activity activity, String filePath,
			String fileType) {
		try {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("oneshot", 0);
			intent.putExtra("configchange", 0);
			Uri uri = Uri.fromFile(new File(filePath));
			intent.setDataAndType(uri, fileType);
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean openMediaFileFromUrl(Activity activity, String url,
			String fileType){
		try {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("oneshot", 0);
			intent.putExtra("configchange", 0);
			Uri uri = Uri.parse(url);
			intent.setDataAndType(uri, fileType);
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 打開GTalk進行聊天
	 * 
	 * @param activity
	 * @param email
	 */
	public static boolean openGTalk(Activity activity, String email) {
		try {
			Uri imUri = new Uri.Builder().scheme("imto").authority("gtalk")
					.appendPath(email).build();
			Intent intent = new Intent(Intent.ACTION_SENDTO, imUri);
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 打開帳戶與同步設置界面
	 */
	public static boolean openSyncSettings(Activity activity) {
		try {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_SYNC_SETTINGS);
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 打開添加帳戶界面
	 */
	public static boolean openAddAccount(Activity activity) {
		try {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_ADD_ACCOUNT);
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 調用系統相冊選擇照片
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean choosePictureFromGallery(Activity activity) {
		try {
			Intent pictureSd = new Intent();
			// 开启Pictures画面Type设定为image
			pictureSd.setType(TYPE_IMAGE);
			pictureSd.setAction(Intent.ACTION_GET_CONTENT);
			activity.startActivityForResult(pictureSd, CHOOSE_PICTURE);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 調用系統相機
	 * 
	 * @param activity
	 * @return 拍照后得到的照片的Uri
	 */
	public static Uri takePhoto(Activity activity) {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			SimpleDateFormat timeStampFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String filename = timeStampFormat.format(new Date());
			ContentValues values = new ContentValues();
			values.put(Media.TITLE, filename);
			Uri photoUri = activity.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			activity.startActivityForResult(intent, TAKE_PHOTO);
			return photoUri;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 裁剪圖片
	 * @param activity
	 * @param uri  圖片的uri
	 * @param aspectX  比例
	 * @param aspectY
	 * @param outputX  輸出大小
	 * @param outputY
	 * @return
	 */
	public static boolean cutPhoto(Activity activity, Uri uri,int aspectX,int aspectY,int outputX,int outputY ) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setData(uri); // data是图库选取文件传回的参数
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			intent.putExtra("noFaceDetection", true);
			intent.putExtra("return-data", true);
			intent.setDataAndType(uri, "image/*");
			activity.startActivityForResult(intent, CUT_PHOTO);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean gotoSetting(Activity activity){
		try{
			Intent intent = new Intent();
			ComponentName cm = new ComponentName(
					"com.android.settings",
					"com.android.settings.Settings");
			intent.setComponent(cm);
			intent.setAction("android.intent.action.VIEW");
			activity.startActivity(intent);
			return true;
		}catch(Exception e){
			return false;
		}		
	}
	
	public static boolean gotoWifiSetting(Activity activity){
		try{
			Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);			
            activity.startActivity(intent);
			return true;
		}catch(Exception e){
			return false;
		}		
	}
}
