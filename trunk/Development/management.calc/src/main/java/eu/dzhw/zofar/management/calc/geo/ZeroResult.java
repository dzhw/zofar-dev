package eu.dzhw.zofar.management.calc.geo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class ZeroResult implements XmlObject,Serializable {

	private static final long serialVersionUID = -8728607495536391900L;

	public ZeroResult() {
		super();
	}

	@Override
	public XmlDocumentProperties documentProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dump() {
		// TODO Auto-generated method stub

	}

	@Override
	public Node getDomNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object monitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlCursor newCursor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node newDomNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node newDomNode(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream newInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream newInputStream(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader newReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader newReader(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLInputStream newXMLInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLInputStream newXMLInputStream(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader newXMLStreamReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader newXMLStreamReader(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(File arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(OutputStream arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(Writer arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ContentHandler arg0, LexicalHandler arg1) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(File arg0, XmlOptions arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(OutputStream arg0, XmlOptions arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(Writer arg0, XmlOptions arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ContentHandler arg0, LexicalHandler arg1, XmlOptions arg2) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public String xmlText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String xmlText(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject changeType(SchemaType arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int compareValue(XmlObject arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public XmlObject copy() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public XmlObject copy(XmlOptions arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public XmlObject[] execQuery(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject[] execQuery(String arg0, XmlOptions arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isImmutable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNil() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SchemaType schemaType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject selectAttribute(QName arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject selectAttribute(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject[] selectAttributes(QNameSet arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject[] selectChildren(QName arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject[] selectChildren(QNameSet arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject[] selectChildren(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject[] selectPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject[] selectPath(String arg0, XmlOptions arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlObject set(XmlObject arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNil() {
		// TODO Auto-generated method stub

	}

	@Override
	public XmlObject substitute(QName arg0, SchemaType arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean valueEquals(XmlObject arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int valueHashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public XmlObject copy(XmlOptions arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
