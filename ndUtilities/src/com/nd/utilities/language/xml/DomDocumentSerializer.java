package com.nd.utilities.language.xml;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.text.TextUtils;

public class DomDocumentSerializer {
	private DataOutputStream dos;
	private int backDepth = 0;
	private Document mDocument;

	/**
	 * 序列化一个XML文件。从inFile（一个XML文件）中解析出一棵DOM树，并解析成二进制文件，存入outFile中 二进制文件的格式如下( |
	 * 只是分隔作用，不代表实际字符,[]里的内容是可选内容) | 节点标志位 | Name长度 | Name内容 | Value长度 | Value内容
	 * | [回朔深度] |
	 * 
	 * 节点标志位、Name长度、Value长度和回朔深度是一个2byte的short变量， 所有的内容都是UTF-8编码的字符内容
	 * 
	 * @param context
	 * @param InputStream
	 *            输入的XML流
	 * @param outFile
	 *            输出的序列化后的二进制文件
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public DomDocumentSerializer(Context context, InputStream in, File outFile)
			throws ParserConfigurationException, SAXException, IOException {
		dos = new DataOutputStream(new FileOutputStream(outFile));

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		mDocument = builder.parse(in);
	}

	public Document getDocument() {
		return mDocument;
	}

	public void serialize(boolean trim) throws IOException {
		if (trim) {
			// TODO trim xml
		}
		Node d = mDocument.getDocumentElement();
		serializeInterval(d);
		dos.writeShort(backDepth);
	}

	public void serializeInterval(Node d) throws IOException {
		// 序列化这个节点
		serializeNode(d);

		// 递归回朔,重置回朔值
		if (!d.hasChildNodes()) {
			backDepth = 0; // 重置回朔值
			return;
		} else { // 记录回朔的节点树
			NodeList childNotes = d.getChildNodes();
			for (int i = 0; i < childNotes.getLength(); i++) {
				Node childNode = childNotes.item(i);
				if (childNode.getNodeType() != Node.ELEMENT_NODE
						&& childNode.getNodeType() != Node.ATTRIBUTE_NODE
						&& childNode.getNodeType() != Node.TEXT_NODE
						&& childNode.getNodeType() != Node.CDATA_SECTION_NODE) {
					continue;
				}
				serializeInterval(childNode);
				backDepth++;
			}
		}
	}

	/**
	 * 序列化一个节点（节点名称、属性、值）
	 * 
	 * @param n
	 *            节点
	 * @param backDepth
	 *            回朔的深度，用来保持树的结构
	 * @throws IOException
	 */
	private void serializeNode(Node n) throws IOException {
		if (backDepth > 0) { // 节点控制字符，表示回朔了几个节点
			dos.writeShort(backDepth);
			backDepth = 0;
		}

		switch (n.getNodeType()) {
		case Node.ELEMENT_NODE:
			dos.writeShort(ControlCode.ELEMENT);
			break;
		case Node.CDATA_SECTION_NODE:
			dos.writeShort(ControlCode.CDATA_SECTION);
			break;
		case Node.TEXT_NODE:
			dos.writeShort(ControlCode.TEXT);
			break;
		}

		String nodeName = n.getNodeName();
		addString(nodeName);
		String value = n.getNodeValue();
		if (!TextUtils.isEmpty(value)) {
			addString(value);
		} else {
			addString(null);
		}

		serializeAttribute(n);
		// dos.writeShort(ControlCode.END_NODE);
	}

	/**
	 * 序列化属性节点 ———————————————————————————————————————
	 * |A|5|xxxxx|6|xxxxxx|A|4|xxxx|6|xxxxxx|......
	 * ————————————————————————————————————————
	 * 
	 * @param n
	 * @throws IOException
	 */
	private void serializeAttribute(Node n) throws IOException {
		NamedNodeMap attributeMap = n.getAttributes();
		if (attributeMap == null || attributeMap.getLength() == 0) {
			return;
		}

		String name, value;
		for (int i = 0; i < attributeMap.getLength(); i++) {
			dos.writeShort(ControlCode.ATTRIBUTE);
			Node temp = attributeMap.item(i);
			name = temp.getNodeName();
			value = temp.getNodeValue();
			addString(name);
			addString(value);
		}
	}

	/**
	 * 写入节点的Name或者Value，包括长度
	 * 
	 * @param data
	 * @throws IOException
	 */
	private void addString(String data) throws IOException {
		if (TextUtils.isEmpty(data)) {
			dos.writeShort(0);
		} else {
			byte[] bData = data.getBytes("UTF-8");
			dos.writeShort(bData.length);
			dos.write(bData);
		}
		dos.flush();
	}

	/**
	 * 打印xml字符串
	 * 
	 * @param d
	 * @throws TransformerException
	 */
	private void dumpXml(Document d) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(d.getDocumentElement()),
				new StreamResult(writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		System.out.println(output);
	}
}