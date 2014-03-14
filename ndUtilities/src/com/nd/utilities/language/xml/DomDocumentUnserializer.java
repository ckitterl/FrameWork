package com.nd.utilities.language.xml;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DomDocumentUnserializer {
	private Document document;
	private DataInputStream dis;

	private Node tempNode;

	public DomDocumentUnserializer(InputStream is) {
		try {
			init(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void init(InputStream is) throws ParserConfigurationException {
		dis = new DataInputStream(is);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		this.document = builder.newDocument();

	}

	public Document unserialize() throws IOException {

		int flag = dis.readShort();
		while (flag != ControlCode.STREAM_END) {
			flag = readNode(flag);
		}
		document.appendChild(tempNode);
		return document;
	}

	public int readNode(int flag) throws IOException {
		int depth = 0;
		if (flag > 0) {
			depth = flag;
			try {
				flag = dis.readShort();
			} catch (EOFException e) { // 读出最后一个字节后，再读标志位的时候已经没有了，这时候抛出异常是预期的
				while (depth > 0) { // // 最末一次的回朔，流结束
					tempNode = tempNode.getParentNode();
					depth--;
				}
				return ControlCode.STREAM_END;
			}
		}

		while (depth > 0) { // 回朔到节点
			tempNode = tempNode.getParentNode();
			depth--;
		}


		if (flag != ControlCode.ATTRIBUTE) { // 不是属性，创建一个节点
			Node node = createNode(flag);
			if (tempNode == null) { // node是根节点
				tempNode = node;
			} else {
				tempNode.appendChild(node);
				tempNode = node;
			}
			
			flag = dis.readShort();
		}

		while (flag == ControlCode.ATTRIBUTE) {
			flag = setNodeAttribute(tempNode);
			System.out.println(flag);
		}

		return flag;
	}

	public Node createNode(int type) throws IOException {
		String name = null, value = null;
		int nameLength = dis.readShort();
		if (nameLength > 0) {
			byte[] bName = new byte[nameLength];
			dis.read(bName);
			name = new String(bName, "UTF-8");
		}

		int valueLength = dis.readShort();
		if (valueLength > 0) {
			byte[] bValue = new byte[valueLength];
			dis.read(bValue);
			value = new String(bValue, "UTF-8");
		}

		return createNode(type, name, value);
	}

	private Node createNode(int type, String nodeName, String nodeValue) {
		if (ControlCode.ELEMENT == type) {
			return document.createElement(nodeName);
		} else if (ControlCode.TEXT == type) {
			return document.createTextNode(nodeValue);
		} else if (ControlCode.CDATA_SECTION == type) {
			return document.createCDATASection(nodeValue);
		}
		return null;
	}

	private int setNodeAttribute(Node d) throws IOException {
		if (!(d instanceof Element)) {
			return -1;
		}

		Element e = (Element) d;
		int nameLength = dis.readShort();
		byte[] bName = new byte[nameLength];
		dis.read(bName);
		String name = new String(bName, "UTF-8");

		int valueLength = dis.readShort();
		byte[] bValue = new byte[valueLength];
		dis.read(bValue);
		String value = new String(bValue, "UTF-8");
		e.setAttribute(name, value);
		return dis.readShort();
	}
}
