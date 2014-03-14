package com.nd.utilities.media;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class BitMapCache {
	private static BitMapCache mInstance;
	private Map<String, SoftReference<Bitmap>> m_bitmapCache = new HashMap<String, SoftReference<Bitmap>>();
	/**
	 * 单体
	 * @return
	 */
	public static BitMapCache instance(){
		if(mInstance == null){
			synchronized(BitMapCache.class){
				if(mInstance == null){
					mInstance = new BitMapCache();
			    }
			}
		}
		return mInstance;
	}
	/**
	 * 禁止从外部实例化
	 */
	private BitMapCache(){
		
	}
	/**
	 * 加入缓存
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapCache(String key, Bitmap bitmap) {
		m_bitmapCache.put(key, new SoftReference<Bitmap>(bitmap));
	}
	/**
	 * 取得缓存
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapCacheByKey(String key) {
		if (m_bitmapCache.containsKey(key)) {
			SoftReference<Bitmap> bmp = m_bitmapCache.get(key);
			if (bmp != null)
				return bmp.get();
		}
		return null;
	}
}
