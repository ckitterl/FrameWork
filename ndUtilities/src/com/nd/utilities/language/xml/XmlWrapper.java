package com.nd.utilities.language.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.content.Context;

import com.nd.utilities.io.FileHelper;
import com.nd.utilities.logger.Logger;

/**
 * @author caimk
 * 
 * 二进制文件的格式如下( | 只是分隔作用，不代表实际字符,[]里的内容是可选内容) 
 * | 节点标志位 | Name长度 | Name内容 | Value长度 | Value内容 | [回朔深度] |
 * 节点标志位、Name长度、Value长度和回朔深度是一个2byte的short变量，
 * 所有的内容都是UTF-8编码的字符内容
 * 算法如下：
 * 一个XML文件解析成Document结构可以用一棵树来表示，对XML文件的序列化就可以抽象为
 * 对一棵树的一维化和重建。这里采用进入前序遍历，记录节点深度的方法来一维化这棵树
 * 例如有这样一棵树
 * 		 			 A
 *				   / | \
 *				  B  C   D
 *               / \    /|\ 
 *              E   F  G H I
 *                   \
 *                   J
 *                   
 * 序列化完后就变成了: A B E ） F J ))) C ) D G ) H ) i ))
 *  其中 ”）“的个数代表了回朔的深度，也就是说每次访问到节点末尾后往上回朔的节点个数。
 *  有了这个序列之后就不难还原这棵树了 —— 用递归算法，从A开始重建树，临时指针随着树
 *  的重建移动。当访问到回朔值的时候，指针往父节点移动n位（n取决于回朔的深度）。最后
 *  得到的临时指针即这棵树的祖先节点.
 *  
 *  这里的每个字母代表DOM树的一个Node，包括Name和Value对，以及其对应的属性值。其
 *  格式可用如下的格式说明 ( | 只是分隔作用，不代表实际字符,[]里的内容是可选内容) 
 *  
 *  ******************************************************************
 *  | 节点标志位 | Name长度 | Name内容 | Value长度 | Value内容 | [回朔深度] |
 *  ******************************************************************
 *  
 *  
 *  节点标志位、Name长度、Value长度和回朔深度是一个2byte的short变量，
 *  所有的内容都是UTF-8编码的字符内容
 */
public class XmlWrapper {
	// 默认的XML序列化文件缓存地址 
	public static final String DEFAULT_CACHE_DIR = "/serializer_xml/";
	// 默认的XML序列化文件后缀
	public static final String CACHE_EXTENSION = ".sexml";

	/**
	 * 获取一个XML文件的DOM树结构
	 * 
	 * @param file
	 *            输入的XML文件
	 * @return XML文件代表的DOM树
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document getDocument(Context context, File file)
			throws ParserConfigurationException, SAXException, IOException {
		if (!file.exists()) {
			Logger.e(XmlWrapper.class, "input xml file is not exists");
			throw new IOException("input xml file is not exists");
		}
		return getDocument(context, new FileInputStream(file),
				FileHelper.getFileNameWitouExtension(file), file.lastModified());
	}

	/**
	 * 获取一个XML文件的DOM树结构
	 * 
	 * @param context
	 * @param is
	 *            输入流，代表一个XML字符流的输入
	 * @return XMLXML字符流代表的DOM树
	 */
	public static Document getDocument(Context context, InputStream is,
			String identifier, long lastModifiedTime)
			throws ParserConfigurationException, SAXException, IOException {
		File dir = new File(context.getCacheDir()
				+ DEFAULT_CACHE_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// XML序列化文件缓存
		String serializedXmlCachePath = context.getCacheDir()
				+ DEFAULT_CACHE_DIR + identifier + CACHE_EXTENSION;
		File outFile = new File(serializedXmlCachePath);
		// TODO 判断文件夹是否存在(是在这里还是在外面，如果在这里将导致每次获取都要去判断，建议放在某个初始化的方法中)

		// 获取缓存文件，并对比文件的生成日期。如果缓存文件的日期比原XML文件新，
		// 则载入缓存文件，并序列化；否则解析原XML文件，并序列后存储到缓存文件夹
		if (!outFile.exists() || outFile.lastModified() < lastModifiedTime) { // 需要更新缓存
			// 序列化文件，并将序列化的结果存储到CacheFile里
			DomDocumentSerializer serializer = new DomDocumentSerializer(
					context, is, outFile);
			serializer.serialize(true);
			return serializer.getDocument();
		} else { // 无需更新缓存，直接解析缓存文件，获取XML树
			DomDocumentUnserializer unserializer = new DomDocumentUnserializer(
					new FileInputStream(outFile));
			return unserializer.unserialize();
		}
	}

}
