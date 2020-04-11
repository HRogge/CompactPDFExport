package de.hrogge.CompactPDFExport;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExtXPath implements XPathVariableResolver {
	private XPath xpath;
	private Node root;
	private String[] parameters;
	
	public ExtXPath(Node root) {
		this.root = root;
		this.parameters = new String[0];
		this.xpath = XPathFactory.newInstance().newXPath();
		this.xpath.setXPathVariableResolver(this);
	}

	public Document getDocument() {
		return this.root.getOwnerDocument();
	}
	public String evaluate(String query, String... parameters) throws XPathExpressionException {
		this.parameters = parameters;
		return this.xpath.evaluate(query, this.root);
	}
	
	public String evaluate(String query, Node node, String... parameters) throws XPathExpressionException {
		this.parameters = parameters;
		return this.xpath.evaluate(query, node);
	}
	
	public boolean evaluateBool(String query, String... parameters) throws XPathExpressionException {
		this.parameters = parameters;
		String result = this.xpath.evaluate(query, this.root);
		return result != null && result.equals("true");
	}
	
	public boolean evaluateBool(String query, Node node, String... parameters) throws XPathExpressionException {
		this.parameters = parameters;
		String result = this.xpath.evaluate(query, node);
		return result != null && result.equals("true");
	}
	
	public Integer evaluateInt(String query, String... parameters) throws XPathExpressionException {
		String result = this.evaluate(query, this.root, parameters);
		return result == null ? null : Integer.parseInt(result);
	}
	
	public Integer evaluateInt(String query, Node node, String... parameters) throws XPathExpressionException {
		String result = this.evaluate(query, node, parameters);
		return result == null ? null : Integer.parseInt(result);
	}
	
	public NodeList evaluateList(String query, String... parameters) throws XPathExpressionException {
		this.parameters = parameters;
		return (NodeList)this.xpath.evaluate(query, this.root, XPathConstants.NODESET);
	}
		
	public NodeList evaluateList(String query, Node node, String... parameters) throws XPathExpressionException {
		this.parameters = parameters;
		return (NodeList)this.xpath.evaluate(query, node, XPathConstants.NODESET);
	}
		
	@Override
	public Object resolveVariable(QName name) {
		int idx = Integer.parseInt(name.getLocalPart());
		return this.parameters[idx];
	}
}
