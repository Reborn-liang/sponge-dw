package cn.nearf.ggz.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XmlUtils {
	private static final Log logger = LogFactory.getLog(XmlUtils.class);

	public static org.dom4j.Document parseText(String srcTxt) throws Exception {
		org.dom4j.Document result = null;
		
		String text = srcTxt.replaceAll("\\<xsd:", "<").replaceAll("</xsd:", "</").replaceAll("xmlns:xsd=\"\"", "");

		SAXReader saxReader = new SAXReader();
		saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		saxReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
		saxReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		
		String encoding = getEncoding(text);

		InputSource source = new InputSource(new StringReader(text));
		source.setEncoding(encoding);

		result = saxReader.read(source);
		if (result.getXMLEncoding() == null) {
			result.setXMLEncoding(encoding);
		}
		return result;
	}
	
	private static String getEncoding(String text) {
		String result = null;

		String xml = text.trim();
		if (xml.startsWith("<?xml")) {
			int end = xml.indexOf("?>");
			String sub = xml.substring(0, end);
			StringTokenizer tokens = new StringTokenizer(sub, " =\"'");
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				if ("encoding".equals(token)) {
					if (!tokens.hasMoreTokens()) {
						break;
					}
					result = tokens.nextToken();
					break;
				}
			}
		}
		return result;
	}
	
	private static Map<String, Object> xml2map(String xmlString) throws Exception {
		org.dom4j.Document doc = parseText(xmlString);
		org.dom4j.Element rootElement = doc.getRootElement();
		Map<String, Object> map = new HashMap<String, Object>();
		ele2map(map, rootElement);
		return map;
	}

	private static void ele2map(Map<String, Object> map, org.dom4j.Element ele) {
		// 获得当前节点的子节点
		List<org.dom4j.Element> elements = ele.elements();
		if (elements.size() == 0) {
			// 没有子节点说明当前节点是叶子节点，直接取值即可
			map.put(ele.getName(), ele.getText());
		} else if (elements.size() == 1) {
			// 只有一个子节点说明不用考虑list的情况，直接继续递归即可
			Map<String, Object> tempMap = new HashMap<String, Object>();
			ele2map(tempMap, elements.get(0));
			map.put(ele.getName(), tempMap);
		} else {
			// 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
			// 构造一个map用来去重
			Map<String, Object> tempMap = new HashMap<String, Object>();
			for (org.dom4j.Element element : elements) {
				tempMap.put(element.getName(), null);
			}
			Set<String> keySet = tempMap.keySet();
			for (String string : keySet) {
				Namespace namespace = elements.get(0).getNamespace();
				List<org.dom4j.Element> elements2 = ele.elements(new QName(string, namespace));
				// 如果同名的数目大于1则表示要构建list
				if (elements2.size() > 1) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (org.dom4j.Element element : elements2) {
						Map<String, Object> tempMap1 = new HashMap<String, Object>();
						ele2map(tempMap1, element);
						list.add(tempMap1);
					}
					map.put(string, list);
				} else {
					// 同名的数量不大于1则直接递归去
					Map<String, Object> tempMap1 = new HashMap<String, Object>();
					ele2map(tempMap1, elements2.get(0));
//					map.put(string, tempMap1);
				}
			}
		}
	}
	
	private static Map<String, Object> xmlToMap(String xmlString) throws Exception {
		org.dom4j.Document doc = parseText(xmlString);
		Map<String, Object> map = (Map<String, Object>) xmlToMap(doc.getRootElement());
		return map;
	}
	
	private static Object xmlToMap(org.dom4j.Element element) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<org.dom4j.Element> elements = element.elements();
		if (elements.size() == 0) {
			map.put(element.getName(), element.getText());
			if (!element.isRootElement()) {
				return element.getText();
			}
		} else if (elements.size() == 1) {
			map.put(elements.get(0).getName(), xmlToMap(elements.get(0)));
		} else if (elements.size() > 1) {
			// 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
			// 构造一个map用来去重
			Map<String, org.dom4j.Element> tempMap = new HashMap<String, org.dom4j.Element>();
			for (org.dom4j.Element ele : elements) {
				tempMap.put(ele.getName(), ele);
			}
			Set<String> keySet = tempMap.keySet();
			for (String string : keySet) {
				Namespace namespace = tempMap.get(string).getNamespace();
				List<org.dom4j.Element> elements2 = element.elements(new QName(string, namespace));
				// 如果同名的数目大于1则表示要构建list
				if (elements2.size() > 1) {
					List<Object> list = new ArrayList<Object>();
					for (org.dom4j.Element ele : elements2) {
						list.add(xmlToMap(ele));
					}
					map.put(string, list);
				} else {
					// 同名的数量不大于1则直接递归去
					map.put(string, xmlToMap(elements2.get(0)));
				}
			}
		}

		return map;
	}
	
	
	
	
	public static Map<String, Object> simpleXmlToMap(String xmlString) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String FEATURE = null;
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(FEATURE, true);
			
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities 
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(FEATURE, false);
			
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities 
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(FEATURE, false);
			
			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(FEATURE, false);
			
			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
			
			// And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then 
			// ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
			// (http://cwe.mitre.org/data/definitions/918.html) and denial 
			// of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."
			
			// remaining parser logic
			DocumentBuilder safebuilder = dbf.newDocumentBuilder();
			
			Document doc = safebuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
            
			Element root = doc.getDocumentElement();
			root.normalize();
			
            NodeList nodes = root.getChildNodes();
		
            for (int i = 0; i < nodes.getLength(); i++) {
            		Node node = nodes.item(i);
            		if (node.getNodeType() != 1) {
            			continue;
            		}
            		map.put(node.getNodeName(), node.getTextContent());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("XmlUtils::(" + xmlString + ")", e);
		}
		return map;
	}
	
	public static Map<String, Object> simpleXmlToMap(InputStream inputStream) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String FEATURE = null;
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(FEATURE, true);
			
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities 
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(FEATURE, false);
			
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities 
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(FEATURE, false);
			
			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(FEATURE, false);
			
			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
			
			// And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then 
			// ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
			// (http://cwe.mitre.org/data/definitions/918.html) and denial 
			// of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."
			
			// remaining parser logic
			DocumentBuilder safebuilder = dbf.newDocumentBuilder();
			
			Document doc = safebuilder.parse(inputStream);
            
			Element root = doc.getDocumentElement();
			root.normalize();
			
            NodeList nodes = root.getChildNodes();
		
            for (int i = 0; i < nodes.getLength(); i++) {
            		Node node = nodes.item(i);
            		if (node.getNodeType() != 1) {
            			continue;
            		}
            		map.put(node.getNodeName(), node.getTextContent());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("XmlUtils::" + e);
		}
		return map;
	}

	public static void main(String[] args) throws Exception {
		String xml = "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid>\n<attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type>\n<fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>\r\n<out_trade_no><![CDATA[1409811653]]></out_trade_no>\r\n<result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id><time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
//		System.err.println(xml2map(xml));
		System.out.println(simpleXmlToMap(xml).size() + "::" + simpleXmlToMap(xml));
		for (Map.Entry<String, Object> entry : simpleXmlToMap(xml).entrySet()) {
			System.out.println(entry.getValue().getClass());
		}
		System.out.println(simpleXmlToMap(xml).get("out_trade_no"));
//		xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><classs><id>001</id><name>st001</name><student><id>0001</id><name><first>xue</first><last>bo</last></name></student></classs><classs><id>001</id><name>st001</name><student><id>0001</id><name><first>xue</first><last>bo</last></name></student></classs></root>";
//		System.err.println(xml2map(xml));
//		System.out.println(xmlToMap(xml));
	}
}
