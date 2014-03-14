package com.nd.utilities.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.nd.utilities.language.MapHelper;
import com.nd.utilities.language.StringHelper;
import com.nd.utilities.language.Json2Std;
/*
import com.heimavista.hvFrame.vm.VmAction;
import com.heimavista.hvFrame.vm.VmAction.CompleteBlock;
*/

public class httpWrapper{
	private static final String TAG="httpWrapper";
	public static enum Method {
		GET, POST
	};

	//private URI m_uri;
	private HashMap<String, String> m_headers = new HashMap<String, String>();
	private HttpEntity m_entity = null;
	private HttpRequestBase m_httprequest;
	private HttpResponse m_rawresponse;
	private boolean m_errorflag;
	private int m_httpStatus=-1;
	//private List<NameValuePair> m_postValuePairs = null;
	private List<Object> m_uploadfile = null;
	private List<String> m_uploadFileKey=null;
	private String m_contentstring = null;;
	private int TIME_OUT_DELAY=60*1000;
	private HashMap<String,String> m_mimeMaps;
	private HashMap<String,String> m_bindMaps=null;
	private HashMap<String,String> m_postValues=null;
	private String m_url="";
	private boolean m_needCache;
	
	// cache
	private static final int WRAPPER_CACHE_EXPIRE = 5000;
	private static httpWrapper m_cacheInstance;
	private Map<String, Map<String, Object>> m_cacheMap;
	private Map<String, Long> m_cacheTimer;
	private Lock m_cacheLocker;
	private Map<String, Lock> m_cacheConditionLock;
	
	private httpWrapper() {
		
	}
	
	public httpWrapper(URI p_uri) {
		m_errorflag = false;
		//m_uri = p_uri;
		m_url= p_uri.getScheme()+"://"+p_uri.getHost()+p_uri.getRawPath()+"?"+p_uri.getRawQuery();
		Log.d(TAG, m_url);
	}
	public httpWrapper(String url) throws URISyntaxException{
		m_errorflag = false;
		m_url=url;
		//m_uri = new URI(url);
	}
	
	public static httpWrapper getCacheInstance() {
		if (m_cacheInstance == null) {
			m_cacheInstance = new httpWrapper();
			m_cacheInstance.makeCacheStruct();
		}
		return m_cacheInstance;
	}
	
	private void makeCacheStruct() {
		m_cacheMap = new HashMap<String, Map<String, Object>>();
		m_cacheTimer = new HashMap<String, Long>();
		m_cacheLocker = new ReentrantLock();
		m_cacheConditionLock = new HashMap<String, Lock>();
	}
	
	private Lock registerCacheWrapper(httpWrapper wrapper) {
		if (!wrapper.getNeedCache()) return null;
		Lock result;
		m_cacheLocker.lock();
		clearExpireCache();
		String key = wrapper.uniqueIdentifier();
		if (!m_cacheConditionLock.containsKey(key)) {
			m_cacheMap.remove(key);
			m_cacheConditionLock.put(key, new ReentrantLock());
			result = null;
		} else {
			result = m_cacheConditionLock.get(key);
		}
		m_cacheLocker.unlock();
		return result;
	}
	
	private void setCacheForWrapper(httpWrapper wrapper) {
		if (!wrapper.getNeedCache()) return;
		m_cacheLocker.lock();
		String key = wrapper.uniqueIdentifier();
		Lock condition =  m_cacheConditionLock.get(key);
		condition.lock();
		Map<String, Object> httpCache = new HashMap<String, Object>();
		httpCache.put("ResponseString", wrapper.getResponseString());
		httpCache.put("Error", wrapper.isError());
		httpCache.put("HttpStatus", wrapper.getHttpStatus());
		m_cacheMap.put(key, httpCache);
		m_cacheTimer.put(key, System.currentTimeMillis());
		condition.unlock();
		m_cacheLocker.unlock();
	}
	
	private Map<String, Object> getCacheForWrapper(httpWrapper wrapper) {
		return m_cacheMap.get(wrapper.uniqueIdentifier());
	}
	
	private void clearExpireCache() {
		m_cacheLocker.lock();
		Iterator<String> keys = m_cacheTimer.keySet().iterator();
		Long now = System.currentTimeMillis();
		while (keys.hasNext()) {
			String key = keys.next();
			if ((now - m_cacheTimer.get(key)) > WRAPPER_CACHE_EXPIRE) {
				m_cacheMap.remove(key);
				m_cacheTimer.remove(key);
				m_cacheConditionLock.remove(key);
			}
		}
		m_cacheLocker.unlock();
	}
	
	public String getUrl(){
		return m_url;
	}
	public void setNeedCache(boolean needCache) {
		m_needCache = needCache;
	}
	public boolean getNeedCache() {
		return m_needCache;
	}
	/**
	 * 設置超時時間 
	 * @param timeout 單位 :s
	 */
	public void setTimeOut(int timeout){
		TIME_OUT_DELAY=timeout*1000;
	}
	public void addHeader(String p_name, String p_value) {
		m_headers.put(p_name, p_value);
	}
	public void bindArgument(String p_arg,String p_value){
		if(m_bindMaps==null){
			m_bindMaps=new HashMap<String,String>();
		}
		m_bindMaps.put("${"+p_arg+"}", p_value);
	}
	private String replaceStringWithArg(String src)
	{
		if(m_bindMaps==null || m_bindMaps.size()==0) return src;
		if(src.indexOf("${")==-1 ) return src;
			
		Iterator<String> iter= m_bindMaps.keySet().iterator();
		String arg,value;
		String result=src;
		while(iter.hasNext()){
			arg=iter.next();
			value=m_bindMaps.get(arg);
			result=result.replace(arg, value);
		}
		return result;
	}
	public void setPostEntity(HttpEntity p_entity) {
		m_entity = p_entity;
	}

	public void addPostValue(String p_name, String p_value) {
		/*
		if (m_postValuePairs == null) {
			m_postValuePairs = new ArrayList<NameValuePair>();
		}
		m_postValuePairs.add(new BasicNameValuePair(p_name, p_value));
		*/
		if(m_postValues==null){
			m_postValues=new HashMap<String,String>();
		}
		m_postValues.put(p_name, p_value);
	}

	protected HttpRequestBase getHttpRequest(Method p_method) {
		String url = replaceStringWithArg(m_url);
		Log.d(TAG, "url:"+url);
		try{
			URI uri=new URI(url);
			if (p_method == Method.POST) {
				return new HttpPost(uri);
			} else {
				return new HttpGet(uri);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;		
	}

	protected void prepareHeader() {
		for (String name : m_headers.keySet()) {
			m_httprequest.addHeader(name, m_headers.get(name));
		}
	}

	public void uploadFile(String p_file,String key) {
		if (m_uploadfile == null) {
			m_uploadfile = new ArrayList<Object>();
		}
		m_uploadfile.add(p_file);
		if(m_uploadFileKey==null){
			m_uploadFileKey=new ArrayList<String>();
		}
		m_uploadFileKey.add(key);
	}
	public void uploadFile(Bitmap p_file,String key) {
		if (m_uploadfile == null) {
			m_uploadfile = new ArrayList<Object>();
		}
		m_uploadfile.add(p_file);
		if(m_uploadFileKey==null){
			m_uploadFileKey=new ArrayList<String>();
		}
		m_uploadFileKey.add(key);
	}
	public boolean get() {
		return exec(Method.GET);
	}

	public boolean post() {
		return exec(Method.POST);
	}

	protected boolean exec(Method p_method) {
		m_contentstring = null;
		m_errorflag = false;
		m_httprequest = getHttpRequest(p_method);
		if(m_httprequest==null)
			return false;
		prepareHeader();
		m_httpStatus = 0;
		HttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter(
                 HttpConnectionParams.SO_TIMEOUT, TIME_OUT_DELAY); // 超时设置
        client.getParams().setIntParameter(
                 HttpConnectionParams.CONNECTION_TIMEOUT, TIME_OUT_DELAY);
        client.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE,
				false);
		// Object responseData = null;
		if (p_method == Method.POST && m_uploadfile != null) {
			makeUploadRequest();
		} else if (p_method == Method.POST) {
			HttpPost post = (HttpPost) m_httprequest;
			if (m_entity != null) {
				post.setEntity(m_entity);
			} else if (m_postValues != null) {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					Iterator<String> it = m_postValues.keySet().iterator();
					String name, value;
					while(it.hasNext()){
						name=it.next();
						value=replaceStringWithArg(m_postValues.get(name));
						params.add(new BasicNameValuePair(name, value));
					}
					post.setEntity(new UrlEncodedFormEntity(params,
							"utf-8"));
					m_httprequest.addHeader(HTTP.CONTENT_TYPE,
							"application/x-www-form-urlencoded");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			m_rawresponse = client.execute(m_httprequest);
			m_httpStatus = m_rawresponse.getStatusLine().getStatusCode();
			if (m_httpStatus == HttpStatus.SC_OK) {
				m_errorflag = false;
			} else { // http error
			//	Logger.e(getClass(), "http error");
				m_errorflag = true;
			}
		} catch (Exception e) {
		//	Logger.e(getClass(), "http error exception");
			m_errorflag = true;
			e.printStackTrace();
		}
		return !m_errorflag;
	}
	protected String mimeTypeForFileName(String fileName){
		if(m_mimeMaps==null){
			m_mimeMaps=new HashMap<String,String>();
			m_mimeMaps.put("png", "image/png");
			m_mimeMaps.put("jpg", "image/jpeg");
			m_mimeMaps.put("jpeg", "image/jpeg");
			m_mimeMaps.put("bmp", "image/bmp");
			m_mimeMaps.put("gif", "image/gif");
			m_mimeMaps.put("ico", "image/ico");
			m_mimeMaps.put("tif", "image/tiff");
			m_mimeMaps.put("tiff","image/tiff");
			m_mimeMaps.put("cur", "image/ico");
			m_mimeMaps.put("txt", "text/plain");
			m_mimeMaps.put("html", "text/html");
			m_mimeMaps.put("htm", "text/html");
			m_mimeMaps.put("doc", "application/msword");
			m_mimeMaps.put("docx", "application/msword");
			m_mimeMaps.put("pdf", "application/pdf");
		}
		int idx=fileName.lastIndexOf(".");
		if(idx==-1) return "application/octet-stream";// no ext
		String mime = m_mimeMaps.get(fileName.substring(idx+1));
		if(mime==null) return "application/octet-stream";
		return mime;
	}

	protected void makeUploadRequest() {
		String boundary = "--www.heimavista.com--"
				+ java.util.UUID.randomUUID().toString();
		String newLine = "\r\n";
		ByteArrayOutputStream aos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(aos);
		m_httprequest.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
		m_httprequest.addHeader(HTTP.CONTENT_TYPE,
				"multipart/form-data;boundary=" + boundary);
		try {
			if (m_postValues != null) {
				/*
				int size = m_postValuePairs.size();
				NameValuePair val;
				for (int i = 0; i < size; i++) {
					val = m_postValuePairs.get(i);
					dos.writeBytes("--" + boundary + newLine);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ val.getName() + "\"" + newLine + newLine
							+ val.getValue() + newLine);
					dos.writeBytes("--" + boundary + newLine);
				}
				*/
				Iterator<String> it = m_postValues.keySet().iterator();
				String name, value;
				while(it.hasNext()){
					name=it.next();
					value=replaceStringWithArg( m_postValues.get(name));
					dos.writeBytes("--" + boundary + newLine);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ name + "\"" + newLine + newLine
							+ value + newLine);
					dos.writeBytes("--" + boundary + newLine);
				}
			}
			if (m_uploadfile != null) {
				int bufferSize, bytesAvailable, bytesRead;
				int maxBufferSize = 4096;
				int size = m_uploadfile.size();				
				for (int i = 0; i < size; i++) {
					Object uploadfile=m_uploadfile.get(i);
					InputStream fis=null;
					String mimeType="image/png";
					String fileName="";
					if(uploadfile instanceof Bitmap){
						fileName=String.valueOf(System.currentTimeMillis())+".png";
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						((Bitmap)uploadfile).compress(Bitmap.CompressFormat.PNG, 100, baos);
					    fis= new ByteArrayInputStream(baos .toByteArray());					 
					}
					else{
						File file = new File(uploadfile.toString());
						mimeType = mimeTypeForFileName(file.getName());
						fis = new FileInputStream(file);
						fileName=file.getName();
					}					
					dos.writeBytes("--" + boundary + newLine);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ m_uploadFileKey.get(i)
							+ "\";filename=\""
							+ fileName
							+ "\""+newLine
							+ "Content-Type: "+mimeType
							+ newLine + newLine);
					bytesAvailable = fis.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					byte[] buffer = new byte[bufferSize];
					bytesRead = fis.read(buffer, 0, bufferSize);
					while (bytesRead > 0) {
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fis.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fis.read(buffer, 0, bufferSize);
					}
					dos.writeBytes(newLine);
					dos.writeBytes("--" + boundary + "--" + newLine);
					fis.close();
				}
			}
			HttpPost post = (HttpPost) m_httprequest;
			post.setEntity(new ByteArrayEntity(aos.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "Encounter i/o error when upload file");
		}
	}

	public boolean isError() {
		return m_errorflag;
	}

	public int getHttpStatus() {
		return m_httpStatus;
	}

	public HttpResponse getHttpResponse() {
		return m_rawresponse;
	}

	public String getResponseString() {
		if (m_contentstring != null) {
	//		Logger.i(getClass(), "old str=" + m_contentstring);
			return m_contentstring;
		}
		try {
			m_contentstring = EntityUtils.toString(m_rawresponse.getEntity(),
					"ISO-8859-1");
			Log.i(TAG, m_contentstring);
		} catch (Exception e) {
			e.printStackTrace();
			m_contentstring = "";
		}
		return m_contentstring;
	}
	
	public InputStream getResponseInputStream(){
		InputStream is=null;
		try {
			is = m_rawresponse.getEntity().getContent();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	public byte[] getResponseByte() {
		byte[] result = null;
		try {
			InputStream is = m_rawresponse.getEntity().getContent();

			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			result = bytestream.toByteArray();
			bytestream.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String uniqueIdentifier(){
		return uniqueIdentifier(null);
	}
	public String uniqueIdentifier(List<String> except){
		String result=replaceStringWithArg(m_url);
		if(m_postValues!=null){
			String[] names=new String[m_postValues.size()];
			Iterator<String> it = m_postValues.keySet().iterator();
			String name;
			int i=0;
			while(it.hasNext()){
				name=it.next();
				names[i++]=name;
			}
			Arrays.sort(names,String.CASE_INSENSITIVE_ORDER);
			for(i=0;i<names.length;i++){
				if(except!=null && except.contains(names[i])) continue;
				result+="&"+names[i]+"="+replaceStringWithArg(m_postValues.get(names[i]));
			}
		}
		Log.d(TAG,"uniqueIdentifier:"+ result);
		result=StringHelper.getMD5(result.getBytes());
		return result;
	}
	public Map<String, Object> getRetrievedJson(){
		Map<String, Object> result=new HashMap<String, Object>();
		Json2Std data=new Json2Std(getResponseString());
		result=data.getDataMap();
		return result;
	}
	public void sendRequest(){
		Log.d(TAG, "sendRequest");
		m_contentstring="";
		
		Lock condition = httpWrapper.getCacheInstance().registerCacheWrapper(this);
		if (condition != null) {
			condition.lock();
			Map<String, Object> httpCache = httpWrapper.getCacheInstance().getCacheForWrapper(this);
			m_contentstring = MapHelper.getStringValueByKey(httpCache, "ResponseString", "");
			m_errorflag = (Boolean) httpCache.get("Error");
			m_httpStatus = MapHelper.getIntValueByKey(httpCache, "HttpStatus", -1);
			condition.unlock();
		} else {
			condition = httpWrapper.getCacheInstance().registerCacheWrapper(this);
			if (condition != null)
				condition.lock();
			post();
			getResponseString();
			httpWrapper.getCacheInstance().setCacheForWrapper(this);
			if (condition != null)
				condition.unlock();
		}
	}
	
	private WrapperCallback m_completeBlock;
	
	public WrapperCallback getCompleteBlock() {
		return m_completeBlock;
	}
	public void setCompleteBlock(WrapperCallback completeBlock) {
		this.m_completeBlock = completeBlock;
	}
	/**
	 * 串行隊列http請求
	 * @param completion
	 * @return
	 */
	/*
	public static workerThread createSyncQueueWithCompletion(VmAction completion,String workerThreadName){
		workerThread thread=workerThread.workerWithName(workerThreadName);
		thread.setCompleteAction(completion);
		thread.setQuitOnFinished(true);
		thread.setNeedNetwork(true);
		thread.setJobEntry(new VmAction(new CompleteBlock() {			
			@Override
			public Object callBack(Map<String, Object> callBackParam) {
				Object param=callBackParam.get("param");
				if(param instanceof httpWrapper){
					httpWrapper client= (httpWrapper) param;
					client.post();
					if(client.getCompleteBlock()!=null){
						client.getCompleteBlock().callBack(client);
					}
				}
				else{
					Logger.e(getClass(), "job should be a http wrapper");
				}
				return null;
			}
		}));
		return thread;
	}
	*/
	public interface WrapperCallback{
		public void callBack(httpWrapper client);
	}
	
	private String m_postField;
	public void addPostDictionary(String field){
		m_postField=field;
		if(TextUtils.isEmpty(m_postField))
			return;
		if(m_postValues==null){
			m_postValues=new HashMap<String,String>();
		}
		Json2Std param=new Json2Std(field);
		Map<String, Object> map=param.getDataMap();
		Set<String> set=map.keySet();
		Iterator<String> ite=set.iterator();
		while(ite.hasNext()){
			String key=ite.next();
			m_postValues.put(key,MapHelper.getStringValueByKey(map, key, ""));
		}		
	}
	public void addPostDictionary(Map<String, Object> map) {
		if (map == null) return;
		if(m_postValues==null){
			m_postValues=new HashMap<String,String>();
		}
		JSONObject obj = new JSONObject();
		Iterator<String> ite = map.keySet().iterator();
		while(ite.hasNext()) {
			String key = ite.next();
			m_postValues.put(key, MapHelper.getStringValueByKey(map, key, ""));
			try {
				obj.put(key, MapHelper.getStringValueByKey(map, key, ""));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		m_postField = obj.toString();
	}
	public String getPostFields(){
		if(m_postField==null)
			return "";
		return m_postField;
	}
}
