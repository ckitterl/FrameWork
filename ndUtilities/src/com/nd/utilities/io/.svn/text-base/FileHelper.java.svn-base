package com.nd.utilities.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.ZipFile;

import android.text.TextUtils;

import com.nd.utilities.language.StringHelper;
import com.nd.utilities.logger.Logger;

public class FileHelper {

	/*
	 * public static String getFileNameFromUrl(String p_url) { String path =
	 * p_url.substring(p_url.lastIndexOf("/") + 1, p_url.length()); int start,
	 * end; do { start = path.indexOf("{"); end = path.indexOf("}"); if (start
	 * != -1 && end != -1) { String tmp = path.substring(start, end + 1); path =
	 * path.replace(tmp, ""); } } while (start != -1 && end != -1); if
	 * (path.contains("&")) path = StringHelper.getMD5(path.getBytes()); return
	 * URLEncoder.encode(path); }
	 */
	public static boolean saveFile(String p_url, String p_filepath) {
		if (chkFileDir(p_filepath)) {
			try {
				Logger.d(FileHelper.class, "start save file:" + p_url);
				URL url = new URL(p_url);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setConnectTimeout(30 * 1000);
				con.setReadTimeout(30 * 1000);
				URLConnection con2 = url.openConnection();
				File file = new File(p_filepath + ".det");
				long pos = 0;
				if (file.exists()) {
					pos = file.length();
				}
				byte[] buf = new byte[1024];
				con.setRequestProperty("Range",
						"bytes=" + pos + "-" + con2.getContentLength());
				con.setAllowUserInteraction(true);
				BufferedInputStream bis = new BufferedInputStream(
						con.getInputStream());
				int len;
				RandomAccessFile raFile = new RandomAccessFile(p_filepath
						+ ".det", "rw");
				try {
					while ((len = bis.read(buf)) > 0) {
						synchronized (raFile) {
							raFile.seek(pos);
							raFile.write(buf, 0, len);
						}
						pos += len;
					}
				} catch (Exception e) {

				}

				raFile.close();
				file.renameTo(new File(p_filepath));
				con.disconnect();
				Logger.d(FileHelper.class, "end save file");
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logger.e(FileHelper.class, "save file error");
				File file = new File(p_filepath);
				if (file.exists())
					file.delete();
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 检查文件路径是否存在，不存在则创建目录
	 * 
	 * @param p_filepath
	 * @return
	 */
	public static boolean chkFileDir(String p_filepath) {
		boolean ret = false;
		String parentPath = getDirFromFilePath(p_filepath);
		if (!TextUtils.isEmpty(parentPath)) {
			File file = new File(parentPath);
			if (!file.exists())
				ret = file.mkdirs();
			else
				ret = true;
		}
		return ret;
	}

	/**
	 * @param p_filepath
	 * @return 返回文件的上级目录
	 */
	public static String getDirFromFilePath(String p_filepath) {
		return TextUtils.isEmpty(p_filepath) ? null : p_filepath.substring(0,
				p_filepath.lastIndexOf("/"));
	}

	public static boolean saveFile(InputStream is, String p_filepath) {
		if (chkFileDir(p_filepath)) {
			try {
				long pos = 0;
				File file = new File(p_filepath + ".det");
				byte[] buf = new byte[1024];
				BufferedInputStream bis = new BufferedInputStream(is);
				int len;
				RandomAccessFile raFile = new RandomAccessFile(p_filepath
						+ ".det", "rw");
				while ((len = bis.read(buf)) > 0) {
					synchronized (raFile) {
						raFile.seek(pos);
						raFile.write(buf, 0, len);
					}
					pos += len;
				}
				raFile.close();
				file.renameTo(new File(p_filepath));
				Logger.d(FileHelper.class, "end save file");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else
			return false;
	}

	/**
	 * 寫入文件
	 * 
	 * @param filePath
	 * @param content
	 * @param append
	 *            是否追加
	 */
	public static void writeToFile(String filePath, String content,
			boolean append) {
		try {
			FileWriter fw = new FileWriter(filePath, append);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String filePath) {
		return readFile(filePath, false);
	}

	/**
	 * 讀取文件
	 * 
	 * @param filePath
	 * @param escapeLine
	 *            是 ---不加換行符，否---加上\n
	 * @return
	 */
	public static String readFile(String filePath, boolean escapeLine) {
		StringBuffer content = new StringBuffer();
		File file = new File(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if (escapeLine)
					content.append(tempString);
				else
					content.append(tempString + "\n");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return content.toString();
	}

	public static void deleteDir(File dir) {
		deleteDir(dir, true);
	}

	/**
	 * 刪除整個文件夾
	 * 
	 * @param dir
	 */
	public static void deleteDir(File dir, boolean deleteDir) {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return; // 检查参数
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(file, true); // 递规的方式删除文件夹
		}
		if (deleteDir)
			dir.delete();// 删除目录本身
	}

	public static String getLocalFileContent(String filePath) {
		try {
			InputStream is = new FileInputStream(new File(filePath));
			int i = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			// Logger.i(AssetsManager.class, baos.toString());
			return baos.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			Logger.d(FileHelper.class, "copy folder error");
			e.printStackTrace();

		}

	}

	/**
	 * 获取文件的名字，不带后缀名
	 * @param file
	 * @return
	 */
	public static String getFileNameWitouExtension(File file) {
		String name = file.getName();
		if (!TextUtils.isEmpty(name)) {
			int pos = name.lastIndexOf(".");
			if (pos >= 0) {
				return name.substring(0, pos);
			}
		}
		
		return null;
	}
}
