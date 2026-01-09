package com.cware.api.panaver.Customer.CustomerInquiry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.cware.netshopping.common.util.ConfigUtil;

@SuppressWarnings({"unchecked", "rawtypes", "serial", "unused"})
public class CustomerInquiryCommon extends org.apache.axis2.client.Stub {
	
	protected org.apache.axis2.description.AxisOperation[] _operations;
	
	// hashmaps to keep the fault mapping
	private HashMap faultExceptionNameMap = new HashMap();
	private HashMap faultExceptionClassNameMap = new HashMap();
	private HashMap faultMessageMap = new HashMap();

	private static int counter = 0;
	private static String CustomerServiceUrl = ConfigUtil.getString("PANAVER_CUST_SERVICE_URL");
	
	public CustomerInquiryCommon() throws org.apache.axis2.AxisFault {
		this(CustomerServiceUrl);
	}

	public CustomerInquiryCommon(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {
		this(configurationContext, CustomerServiceUrl);
	}

	public CustomerInquiryCommon(String targetEndpoint) throws org.apache.axis2.AxisFault {
		this(null, targetEndpoint);
	}
	
	public CustomerInquiryCommon(org.apache.axis2.context.ConfigurationContext configurationContext, String targetEndpoint)
			throws org.apache.axis2.AxisFault {
		this(configurationContext, targetEndpoint, false);
	}
	
	public CustomerInquiryCommon(org.apache.axis2.context.ConfigurationContext configurationContext, String targetEndpoint, boolean useSeparateListener)
			throws org.apache.axis2.AxisFault {
		// To populate AxisService
		populateAxisService();
		populateFaults();

		_serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);

		_serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
		_serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

		// Set the soap version
		_serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

	}
	
	// populates the faults
	private void populateFaults() {

	}
	
	private static synchronized String getUniqueSuffix() {
		// reset the counter if it is greater than 99999
		if (counter > 99999) {
			counter = 0;
		}
		counter = counter + 1;
		return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
	}
	
	/**
	 * A utility method that copies the namepaces from the SOAPEnvelope
	 */
	private Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
		Map returnMap = new HashMap();
		Iterator namespaceIterator = env.getAllDeclaredNamespaces();
		while (namespaceIterator.hasNext()) {
			org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
			returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
		}
		return returnMap;
	}
	
	private javax.xml.namespace.QName[] opNameArray = null;
	
	private boolean optimizeContent(javax.xml.namespace.QName opName) {
		if (opNameArray == null) {
			return false;
		}
		for (int i = 0; i < opNameArray.length; i++) {
			if (opName.equals(opNameArray[i])) {
				return true;
			}
		}
		return false;
	}
	
	private void populateAxisService() throws org.apache.axis2.AxisFault {

		// creating the Service with a unique name
		_service = new org.apache.axis2.description.AxisService("CustomerInquiryService" + getUniqueSuffix());
		addAnonymousOperations();

		// creating the operations
		org.apache.axis2.description.AxisOperation __operation;

		_operations = new org.apache.axis2.description.AxisOperation[1];

		__operation = new org.apache.axis2.description.OutInAxisOperation();

		__operation.setName(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "getCustomerInquiryList"));
		_service.addOperation(__operation);

		_operations[0] = __operation;

	}
	
	public static class AccessCredentialsType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * AccessCredentialsType Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * field for AccessLicense
		 */

		protected String localAccessLicense;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getAccessLicense() {
			return localAccessLicense;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            AccessLicense
		 */
		public void setAccessLicense(String param) {

			this.localAccessLicense = param;

		}

		/**
		 * field for Timestamp
		 */

		protected String localTimestamp;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getTimestamp() {
			return localTimestamp;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Timestamp
		 */
		public void setTimestamp(String param) {

			this.localTimestamp = param;

		}

		/**
		 * field for Signature
		 */

		protected String localSignature;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getSignature() {
			return localSignature;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Signature
		 */
		public void setSignature(String param) {

			this.localSignature = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":AccessCredentialsType", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "AccessCredentialsType", xmlWriter);
				}

			}

			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "AccessLicense", xmlWriter);

			if (localAccessLicense == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("AccessLicense cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localAccessLicense);

			}

			xmlWriter.writeEndElement();

			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "Timestamp", xmlWriter);

			if (localTimestamp == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localTimestamp);

			}

			xmlWriter.writeEndElement();

			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "Signature", xmlWriter);

			if (localSignature == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("Signature cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localSignature);

			}

			xmlWriter.writeEndElement();

			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			ArrayList elementList = new ArrayList();
			ArrayList attribList = new ArrayList();

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessLicense"));

			if (localAccessLicense != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAccessLicense));
			} else {
				throw new org.apache.axis2.databinding.ADBException("AccessLicense cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp"));

			if (localTimestamp != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Signature"));

			if (localSignature != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSignature));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Signature cannot be null!!");
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static AccessCredentialsType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				AccessCredentialsType object = new AccessCredentialsType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"AccessCredentialsType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (AccessCredentialsType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessLicense").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setAccessLicense(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setTimestamp(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Signature").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setSignature(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class BaseRequestType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * BaseRequestType Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * field for AccessCredentials
		 */

		protected AccessCredentialsType localAccessCredentials;

		/**
		 * Auto generated getter method
		 * 
		 * @return AccessCredentialsType
		 */
		public AccessCredentialsType getAccessCredentials() {
			return localAccessCredentials;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            AccessCredentials
		 */
		public void setAccessCredentials(AccessCredentialsType param) {

			this.localAccessCredentials = param;

		}

		/**
		 * field for RequestID
		 */

		protected String localRequestID;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localRequestIDTracker = false;

		public boolean isRequestIDSpecified() {
			return localRequestIDTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getRequestID() {
			return localRequestID;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            RequestID
		 */
		public void setRequestID(String param) {
			localRequestIDTracker = param != null;

			this.localRequestID = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":BaseRequestType", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BaseRequestType", xmlWriter);
				}

			}

			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			localAccessCredentials.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"), xmlWriter);
			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"));

			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			elementList.add(localAccessCredentials);
			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static BaseRequestType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				BaseRequestType object = new BaseRequestType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"BaseRequestType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (BaseRequestType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials").equals(reader.getName())) {

						object.setAccessCredentials(AccessCredentialsType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class BaseCheckoutRequestType extends BaseRequestType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * BaseCheckoutRequestType Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * field for DetailLevel
		 */

		protected String localDetailLevel;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localDetailLevelTracker = false;

		public boolean isDetailLevelSpecified() {
			return localDetailLevelTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getDetailLevel() {
			return localDetailLevel;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            DetailLevel
		 */
		public void setDetailLevel(String param) {
			localDetailLevelTracker = param != null;

			this.localDetailLevel = param;

		}

		/**
		 * field for Version
		 */

		protected String localVersion;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getVersion() {
			return localVersion;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Version
		 */
		public void setVersion(String param) {

			this.localVersion = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":BaseCheckoutRequestType", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BaseCheckoutRequestType", xmlWriter);
			}

			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			localAccessCredentials.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"), xmlWriter);
			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			if (localDetailLevelTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "DetailLevel", xmlWriter);

				if (localDetailLevel == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetailLevel);

				}

				xmlWriter.writeEndElement();
			}
			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "Version", xmlWriter);

			if (localVersion == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localVersion);

			}

			xmlWriter.writeEndElement();

			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
			attribList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "BaseCheckoutRequestType"));

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"));

			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			elementList.add(localAccessCredentials);
			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}
			if (localDetailLevelTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel"));

				if (localDetailLevel != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetailLevel));
				} else {
					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");
				}
			}
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version"));

			if (localVersion != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static BaseCheckoutRequestType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				BaseCheckoutRequestType object = new BaseCheckoutRequestType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"BaseCheckoutRequestType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (BaseCheckoutRequestType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials").equals(reader.getName())) {

						object.setAccessCredentials(AccessCredentialsType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetailLevel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setVersion(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class BaseResponseType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * BaseResponseType Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * field for RequestID
		 */

		protected String localRequestID;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localRequestIDTracker = false;

		public boolean isRequestIDSpecified() {
			return localRequestIDTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getRequestID() {
			return localRequestID;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            RequestID
		 */
		public void setRequestID(String param) {
			localRequestIDTracker = param != null;

			this.localRequestID = param;

		}

		/**
		 * field for ResponseType
		 */

		protected String localResponseType;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getResponseType() {
			return localResponseType;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            ResponseType
		 */
		public void setResponseType(String param) {

			this.localResponseType = param;

		}

		/**
		 * field for ResponseTime
		 */

		protected long localResponseTime;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localResponseTimeTracker = false;

		public boolean isResponseTimeSpecified() {
			return localResponseTimeTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return long
		 */
		public long getResponseTime() {
			return localResponseTime;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            ResponseTime
		 */
		public void setResponseTime(long param) {

			// setting primitive attribute tracker to true
			localResponseTimeTracker = param != java.lang.Long.MIN_VALUE;

			this.localResponseTime = param;

		}

		/**
		 * field for Error
		 */

		protected ErrorType localError;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localErrorTracker = false;

		public boolean isErrorSpecified() {
			return localErrorTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return ErrorType
		 */
		public ErrorType getError() {
			return localError;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Error
		 */
		public void setError(ErrorType param) {
			localErrorTracker = param != null;

			this.localError = param;

		}

		/**
		 * field for WarningList
		 */

		protected WarningList_type0 localWarningList;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localWarningListTracker = false;

		public boolean isWarningListSpecified() {
			return localWarningListTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return WarningList_type0
		 */
		public WarningList_type0 getWarningList() {
			return localWarningList;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            WarningList
		 */
		public void setWarningList(WarningList_type0 param) {
			localWarningListTracker = param != null;

			this.localWarningList = param;

		}

		/**
		 * field for QuotaStatus
		 */

		protected QuotaStatusType localQuotaStatus;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localQuotaStatusTracker = false;

		public boolean isQuotaStatusSpecified() {
			return localQuotaStatusTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return QuotaStatusType
		 */
		public QuotaStatusType getQuotaStatus() {
			return localQuotaStatus;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            QuotaStatus
		 */
		public void setQuotaStatus(QuotaStatusType param) {
			localQuotaStatusTracker = param != null;

			this.localQuotaStatus = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":BaseResponseType", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BaseResponseType", xmlWriter);
				}

			}
			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "ResponseType", xmlWriter);

			if (localResponseType == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localResponseType);

			}

			xmlWriter.writeEndElement();
			if (localResponseTimeTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "ResponseTime", xmlWriter);

				if (localResponseTime == java.lang.Long.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException("ResponseTime cannot be null!!");

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
				}

				xmlWriter.writeEndElement();
			}
			if (localErrorTracker) {
				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				localError.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"), xmlWriter);
			}
			if (localWarningListTracker) {
				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				localWarningList.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"), xmlWriter);
			}
			if (localQuotaStatusTracker) {
				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				localQuotaStatus.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"), xmlWriter);
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType"));

			if (localResponseType != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseType));
			} else {
				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");
			}
			if (localResponseTimeTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime"));

				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
			}
			if (localErrorTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"));

				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				elementList.add(localError);
			}
			if (localWarningListTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"));

				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				elementList.add(localWarningList);
			}
			if (localQuotaStatusTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"));

				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				elementList.add(localQuotaStatus);
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static BaseResponseType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				BaseResponseType object = new BaseResponseType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"BaseResponseType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (BaseResponseType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setResponseTime(java.lang.Long.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement() && new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error").equals(reader.getName())) {

						object.setError(ErrorType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList").equals(reader.getName())) {

						object.setWarningList(WarningList_type0.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus").equals(reader.getName())) {

						object.setQuotaStatus(QuotaStatusType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}

	public static class BaseCheckoutResponseType extends BaseResponseType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * BaseCheckoutResponseType Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * field for DetailLevel
		 */

		protected String localDetailLevel;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localDetailLevelTracker = false;

		public boolean isDetailLevelSpecified() {
			return localDetailLevelTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getDetailLevel() {
			return localDetailLevel;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            DetailLevel
		 */
		public void setDetailLevel(String param) {
			localDetailLevelTracker = param != null;

			this.localDetailLevel = param;

		}

		/**
		 * field for Version
		 */

		protected String localVersion;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localVersionTracker = false;

		public boolean isVersionSpecified() {
			return localVersionTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getVersion() {
			return localVersion;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Version
		 */
		public void setVersion(String param) {
			localVersionTracker = param != null;

			this.localVersion = param;

		}

		/**
		 * field for Release
		 */

		protected String localRelease;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localReleaseTracker = false;

		public boolean isReleaseSpecified() {
			return localReleaseTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getRelease() {
			return localRelease;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Release
		 */
		public void setRelease(String param) {
			localReleaseTracker = param != null;

			this.localRelease = param;

		}

		/**
		 * field for Timestamp
		 */

		protected java.util.Calendar localTimestamp;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localTimestampTracker = false;

		public boolean isTimestampSpecified() {
			return localTimestampTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.util.Calendar
		 */
		public java.util.Calendar getTimestamp() {
			return localTimestamp;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Timestamp
		 */
		public void setTimestamp(java.util.Calendar param) {
			localTimestampTracker = param != null;

			this.localTimestamp = param;

		}

		/**
		 * field for MessageID
		 */

		protected String localMessageID;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localMessageIDTracker = false;

		public boolean isMessageIDSpecified() {
			return localMessageIDTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getMessageID() {
			return localMessageID;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            MessageID
		 */
		public void setMessageID(String param) {
			localMessageIDTracker = param != null;

			this.localMessageID = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":BaseCheckoutResponseType", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BaseCheckoutResponseType", xmlWriter);
			}

			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "ResponseType", xmlWriter);

			if (localResponseType == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localResponseType);

			}

			xmlWriter.writeEndElement();
			if (localResponseTimeTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "ResponseTime", xmlWriter);

				if (localResponseTime == java.lang.Long.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException("ResponseTime cannot be null!!");

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
				}

				xmlWriter.writeEndElement();
			}
			if (localErrorTracker) {
				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				localError.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"), xmlWriter);
			}
			if (localWarningListTracker) {
				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				localWarningList.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"), xmlWriter);
			}
			if (localQuotaStatusTracker) {
				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				localQuotaStatus.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"), xmlWriter);
			}
			if (localDetailLevelTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "DetailLevel", xmlWriter);

				if (localDetailLevel == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetailLevel);

				}

				xmlWriter.writeEndElement();
			}
			if (localVersionTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Version", xmlWriter);

				if (localVersion == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localVersion);

				}

				xmlWriter.writeEndElement();
			}
			if (localReleaseTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Release", xmlWriter);

				if (localRelease == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Release cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRelease);

				}

				xmlWriter.writeEndElement();
			}
			if (localTimestampTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Timestamp", xmlWriter);

				if (localTimestamp == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");

				} else {

					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));

				}

				xmlWriter.writeEndElement();
			}
			if (localMessageIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "MessageID", xmlWriter);

				if (localMessageID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("MessageID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localMessageID);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
			attribList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "BaseCheckoutResponseType"));
			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType"));

			if (localResponseType != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseType));
			} else {
				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");
			}
			if (localResponseTimeTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime"));

				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
			}
			if (localErrorTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"));

				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				elementList.add(localError);
			}
			if (localWarningListTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"));

				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				elementList.add(localWarningList);
			}
			if (localQuotaStatusTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"));

				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				elementList.add(localQuotaStatus);
			}
			if (localDetailLevelTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel"));

				if (localDetailLevel != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetailLevel));
				} else {
					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");
				}
			}
			if (localVersionTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version"));

				if (localVersion != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
				}
			}
			if (localReleaseTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Release"));

				if (localRelease != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRelease));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Release cannot be null!!");
				}
			}
			if (localTimestampTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp"));

				if (localTimestamp != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");
				}
			}
			if (localMessageIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MessageID"));

				if (localMessageID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMessageID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("MessageID cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static BaseCheckoutResponseType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				BaseCheckoutResponseType object = new BaseCheckoutResponseType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"BaseCheckoutResponseType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (BaseCheckoutResponseType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setResponseTime(java.lang.Long.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement() && new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error").equals(reader.getName())) {

						object.setError(ErrorType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList").equals(reader.getName())) {

						object.setWarningList(WarningList_type0.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus").equals(reader.getName())) {

						object.setQuotaStatus(QuotaStatusType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetailLevel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setVersion(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Release").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRelease(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setTimestamp(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MessageID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setMessageID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class ErrorType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * ErrorType Namespace URI = http://customerinquiry.shopn.platform.nhncorp.com/
		 * Namespace Prefix = ns1
		 */

		/**
		 * field for Code
		 */

		protected String localCode;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getCode() {
			return localCode;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Code
		 */
		public void setCode(String param) {

			this.localCode = param;

		}

		/**
		 * field for Message
		 */

		protected String localMessage;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getMessage() {
			return localMessage;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Message
		 */
		public void setMessage(String param) {

			this.localMessage = param;

		}

		/**
		 * field for Detail
		 */

		protected String localDetail;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localDetailTracker = false;

		public boolean isDetailSpecified() {
			return localDetailTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getDetail() {
			return localDetail;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Detail
		 */
		public void setDetail(String param) {
			localDetailTracker = param != null;

			this.localDetail = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":ErrorType", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "ErrorType", xmlWriter);
				}

			}

			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "Code", xmlWriter);

			if (localCode == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("Code cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localCode);

			}

			xmlWriter.writeEndElement();

			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "Message", xmlWriter);

			if (localMessage == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("Message cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localMessage);

			}

			xmlWriter.writeEndElement();
			if (localDetailTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Detail", xmlWriter);

				if (localDetail == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Detail cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetail);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Code"));

			if (localCode != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCode));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Code cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Message"));

			if (localMessage != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMessage));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Message cannot be null!!");
			}
			if (localDetailTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Detail"));

				if (localDetail != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetail));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Detail cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static ErrorType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				ErrorType object = new ErrorType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"ErrorType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (ErrorType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement() && new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Code").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Message").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setMessage(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Detail").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetail(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class ExtensionMapper {

		public static java.lang.Object getTypeObject(String namespaceURI, String typeName, javax.xml.stream.XMLStreamReader reader)
				throws java.lang.Exception {


			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "AccessCredentialsType".equals(typeName)) {
				return AccessCredentialsType.Factory.parse(reader);
			}
	
			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "BaseCheckoutRequestType".equals(typeName)) {
				return BaseCheckoutRequestType.Factory.parse(reader);
			}

			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "WarningList_type0".equals(typeName)) {
				return WarningList_type0.Factory.parse(reader);
			}

			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "BaseRequestType".equals(typeName)) {
				return BaseRequestType.Factory.parse(reader);
			}

			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "requestReturnRequest".equals(typeName)) {
				//return RequestReturnRequest.Factory.parse(reader);
			}

			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "WarningType".equals(typeName)) {
				return WarningType.Factory.parse(reader);
			}
			
			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "BaseCheckoutResponseType".equals(typeName)) {
				return BaseCheckoutResponseType.Factory.parse(reader);
			}

			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "ErrorType".equals(typeName)) {
				return ErrorType.Factory.parse(reader);
			}


			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "userType".equals(typeName)) {
				//return UserType.Factory.parse(reader);
			}

			if ("http://customerinquiry.shopn.platform.nhncorp.com/".equals(namespaceURI) && "BaseResponseType".equals(typeName)) {
				return BaseResponseType.Factory.parse(reader);
			}

			throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
		}

	}
	
	public static class QuotaStatusType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * QuotaStatusType Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * field for RemainingQuota
		 */

		protected long localRemainingQuota;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localRemainingQuotaTracker = false;

		public boolean isRemainingQuotaSpecified() {
			return localRemainingQuotaTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return long
		 */
		public long getRemainingQuota() {
			return localRemainingQuota;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            RemainingQuota
		 */
		public void setRemainingQuota(long param) {

			// setting primitive attribute tracker to true
			localRemainingQuotaTracker = param != java.lang.Long.MIN_VALUE;

			this.localRemainingQuota = param;

		}

		/**
		 * field for ExpirationTime
		 */

		protected java.util.Calendar localExpirationTime;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localExpirationTimeTracker = false;

		public boolean isExpirationTimeSpecified() {
			return localExpirationTimeTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return java.util.Calendar
		 */
		public java.util.Calendar getExpirationTime() {
			return localExpirationTime;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            ExpirationTime
		 */
		public void setExpirationTime(java.util.Calendar param) {
			localExpirationTimeTracker = param != null;

			this.localExpirationTime = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":QuotaStatusType", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "QuotaStatusType", xmlWriter);
				}

			}
			if (localRemainingQuotaTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RemainingQuota", xmlWriter);

				if (localRemainingQuota == java.lang.Long.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException("RemainingQuota cannot be null!!");

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRemainingQuota));
				}

				xmlWriter.writeEndElement();
			}
			if (localExpirationTimeTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "ExpirationTime", xmlWriter);

				if (localExpirationTime == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("ExpirationTime cannot be null!!");

				} else {

					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExpirationTime));

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (localRemainingQuotaTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RemainingQuota"));

				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRemainingQuota));
			}
			if (localExpirationTimeTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ExpirationTime"));

				if (localExpirationTime != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localExpirationTime));
				} else {
					throw new org.apache.axis2.databinding.ADBException("ExpirationTime cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static QuotaStatusType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				QuotaStatusType object = new QuotaStatusType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"QuotaStatusType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (QuotaStatusType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RemainingQuota").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRemainingQuota(org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setRemainingQuota(java.lang.Long.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ExpirationTime").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setExpirationTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class ServiceTypeCode implements org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/",
				"serviceTypeCode", "cus");

		/**
		 * field for ServiceTypeCode
		 */

		protected String localServiceTypeCode;

		private static HashMap _table_ = new HashMap();

		// Constructor

		protected ServiceTypeCode(String value, boolean isRegisterValue) {
			localServiceTypeCode = value;
			if (isRegisterValue) {

				_table_.put(localServiceTypeCode, this);

			}

		}

		public static final String _CHECKOUT = org.apache.axis2.databinding.utils.ConverterUtil.convertToString("CHECKOUT");

		public static final String _SHOPN = org.apache.axis2.databinding.utils.ConverterUtil.convertToString("SHOPN");

		public static final ServiceTypeCode CHECKOUT = new ServiceTypeCode(_CHECKOUT, true);

		public static final ServiceTypeCode SHOPN = new ServiceTypeCode(_SHOPN, true);

		public String getValue() {
			return localServiceTypeCode;
		}

		public boolean equals(java.lang.Object obj) {
			return (obj == this);
		}

		public int hashCode() {
			return toString().hashCode();
		}

		public String toString() {

			return localServiceTypeCode.toString();

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			String namespace = parentQName.getNamespaceURI();
			String _localName = parentQName.getLocalPart();

			writeStartElement(null, namespace, _localName, xmlWriter);

			// add the type details if this is used in a simple type
			if (serializeType) {
				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":serviceTypeCode", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "serviceTypeCode", xmlWriter);
				}
			}

			if (localServiceTypeCode == null) {

				throw new org.apache.axis2.databinding.ADBException("serviceTypeCode cannot be null !!");

			} else {

				xmlWriter.writeCharacters(localServiceTypeCode);

			}

			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it
			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(MY_QNAME, new java.lang.Object[] {
					org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
					org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localServiceTypeCode) }, null);

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			public static ServiceTypeCode fromValue(String value) throws java.lang.IllegalArgumentException {
				ServiceTypeCode enumeration = (ServiceTypeCode)

				_table_.get(value);

				if ((enumeration == null) && !((value == null) || (value.equals("")))) {
					throw new java.lang.IllegalArgumentException();
				}
				return enumeration;
			}

			public static ServiceTypeCode fromString(String value, String namespaceURI) throws java.lang.IllegalArgumentException {
				try {

					return fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));

				} catch (java.lang.Exception e) {
					throw new java.lang.IllegalArgumentException();
				}
			}

			public static ServiceTypeCode fromString(javax.xml.stream.XMLStreamReader xmlStreamReader, String content) {
				if (content.indexOf(":") > -1) {
					String prefix = content.substring(0, content.indexOf(":"));
					String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
					return ServiceTypeCode.Factory.fromString(content, namespaceUri);
				} else {
					return ServiceTypeCode.Factory.fromString(content, "");
				}
			}

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static ServiceTypeCode parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				ServiceTypeCode object = null;
				// initialize a hash map to keep values
				java.util.Map attributeMap = new HashMap();
				java.util.List extraAttributeList = new java.util.ArrayList<org.apache.axiom.om.OMAttribute>();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement() || reader.hasText()) {

							String content = reader.getElementText();

							if (content.indexOf(":") > 0) {
								// this seems to be a Qname so find the
								// namespace and send
								prefix = content.substring(0, content.indexOf(":"));
								namespaceuri = reader.getNamespaceURI(prefix);
								object = ServiceTypeCode.Factory.fromString(content, namespaceuri);
							} else {
								// this seems to be not a qname send and empty
								// namespace incase of it is
								// check is done in fromString method
								object = ServiceTypeCode.Factory.fromString(content, "");
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class CustomerInquiryList_type0 implements org.apache.axis2.databinding.ADBBean {
		
		protected InquiryType[] localCustomerInquiry;

		protected boolean localCustomerInquiryTracker = false;

		public boolean isCustomerInquirySpecified() {
			return localCustomerInquiryTracker;
		}

		public InquiryType[] getCustomerInquiry() {
			return localCustomerInquiry;
		}

		protected void validateCustomerInquiry(InquiryType[] param) {

		}

		public void setCustomerInquiry(InquiryType[] param) {
			validateCustomerInquiry(param);
			localCustomerInquiryTracker = param != null;
			this.localCustomerInquiry = param;
		}

		public void addCustomerInquiry(InquiryType param) {
			if (localCustomerInquiry == null) {
				localCustomerInquiry = new InquiryType[]{};
			}
			localCustomerInquiryTracker = true;
			java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localCustomerInquiry);
			list.add(param);
			this.localCustomerInquiry = (InquiryType[]) list.toArray(new InquiryType[list.size()]);
		}

		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":CustomerInquiryList_type0", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "CustomerInquiryList_type0", xmlWriter);
				}

			}
			if (localCustomerInquiryTracker) {
				if (localCustomerInquiry != null) {
					for (int i = 0; i < localCustomerInquiry.length; i++) {
						if (localCustomerInquiry[i] != null) {
							localCustomerInquiry[i].serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "CustomerInquiry"), xmlWriter);
						} else {
						}
					}
				} else {
					throw new org.apache.axis2.databinding.ADBException("CustomerInquiry cannot be null!!");
				}
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "n"; // 재확인 대상!!!
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}
		
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (localCustomerInquiryTracker) {
				if (localCustomerInquiry != null) {
					for (int i = 0; i < localCustomerInquiry.length; i++) {

						if (localCustomerInquiry[i] != null) {
							elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "CustomerInquiry"));
							elementList.add(localCustomerInquiry[i]);
						} else {
						}
					}
				} else {
					throw new org.apache.axis2.databinding.ADBException("CustomerInquiry cannot be null!!");
				}
			}
			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
		}

		public static class Factory {

			public static CustomerInquiryList_type0 parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				CustomerInquiryList_type0 object = new CustomerInquiryList_type0();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"CustomerInquiryList_type0".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (CustomerInquiryList_type0) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}
						}
					}
					
					Vector handledAttributes = new Vector();

					reader.next();

					java.util.ArrayList list1 = new java.util.ArrayList();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "CustomerInquiry").equals(reader.getName())) {

						list1.add(InquiryType.Factory.parse(reader));

						boolean loopDone1 = false;
						while (!loopDone1) {
							// We should be at the end element, but make sure
							while (!reader.isEndElement())
								reader.next();
							// Step out of this element
							reader.next();
							// Step to next element event.
							while (!reader.isStartElement() && !reader.isEndElement())
								reader.next();
							if (reader.isEndElement()) {
								// two continuous end elements means we are
								// exiting the xml structure
								loopDone1 = true;
							} else {
								if (new javax.xml.namespace.QName("", "CustomerInquiry").equals(reader.getName())) {
									list1.add(InquiryType.Factory.parse(reader));
								} else {
									loopDone1 = true;
								}
							}
						}
						// call the converter utility to convert and set the
						// array

						object.setCustomerInquiry((InquiryType[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(InquiryType.class, list1));

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class
		
	}
	
	public static class InquiryType implements org.apache.axis2.databinding.ADBBean {
		
		/*
			 * This type was generated from the piece of schema that had name =
			 * inquiryType Namespace URI =
			 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
			 */
			
			/**
			 * field for InquiryID
			 */
			protected String localInquiryID;
	
			/*
			 * This tracker boolean wil be used to detect whether the user called
			 * the set method for this attribute. It will be used to determine
			 * whether to include this field in the serialized XML
			 */
			protected boolean localInquiryIDTracker = false;
	
			public boolean isInquiryIDSpecified() {
				return localInquiryIDTracker;
			}
	
			/**
			 * Auto generated getter method
			 * 
			 * @return String
			 */
			public String getInquiryID() {
				return localInquiryID;
			}
	
			/**
			 * Auto generated setter method
			 * 
			 * @param param
			 *            InquiryID
			 */
			public void setInquiryID(String param) {
				localInquiryIDTracker = param != null;
	
				this.localInquiryID = param;
	
			}
		
			/**
			 * field for OrderID
			 */

			protected String localOrderID;

			/*
			 * This tracker boolean wil be used to detect whether the user called
			 * the set method for this attribute. It will be used to determine
			 * whether to include this field in the serialized XML
			 */
			protected boolean localOrderIDTracker = false;

			public boolean isOrderIDSpecified() {
				return localOrderIDTracker;
			}

			/**
			 * Auto generated getter method
			 * 
			 * @return String
			 */
			public String getOrderID() {
				return localOrderID;
			}

			/**
			 * Auto generated setter method
			 * 
			 * @param param
			 *            OrderID
			 */
			public void setOrderID(String param) {
				localOrderIDTracker = param != null;

				this.localOrderID = param;

			}
			
			/**
			 * field for ProductOrderID
			 */

			protected String localProductOrderID;

			/*
			 * This tracker boolean wil be used to detect whether the user called
			 * the set method for this attribute. It will be used to determine
			 * whether to include this field in the serialized XML
			 */
			protected boolean localProductOrderIDTracker = false;

			public boolean isProductOrderIDSpecified() {
				return localProductOrderIDTracker;
			}

			/**
			 * Auto generated getter method
			 * 
			 * @return String
			 */
			public String getProductOrderID() {
				return localProductOrderID;
			}

			/**
			 * Auto generated setter method
			 * 
			 * @param param
			 *            ProductOrderID
			 */
			public void setProductOrderID(String param) {
				localProductOrderIDTracker = param != null;

				this.localProductOrderID = param;

			}

			protected String localProductName;

			protected boolean localProductNameTracker = false;

			public boolean isProductNameSpecified() {
				return localProductNameTracker;
			}

			public String getProductName() {
				return localProductName;
			}

			public void setProductName(String param) {
				localProductNameTracker = param != null;

				this.localProductName = param;
			}
			
			protected String localProductID;

			protected boolean localProductIDTracker = false;

			public boolean isProductIDSpecified() {
				return localProductIDTracker;
			}

			public String getProductID() {
				return localProductID;
			}

			public void setProductID(String param) {
				localProductIDTracker = param != null;

				this.localProductID = param;
			}
			
			protected String localProductOrderOption;

			protected boolean localProductOrderOptionTracker = false;

			public boolean isProductOrderOptionSpecified() {
				return localProductOrderOptionTracker;
			}

			public String getProductOrderOption() {
				return localProductOrderOption;
			}

			public void setProductOrderOption(String param) {
				localProductOrderOptionTracker = param != null;

				this.localProductOrderOption = param;
			}
			
			protected String localCustomerID;

			protected boolean localCustomerIDTracker = false;

			public boolean isCustomerIDSpecified() {
				return localCustomerIDTracker;
			}

			public String getCustomerID() {
				return localCustomerID;
			}

			public void setCustomerID(String param) {
				localCustomerIDTracker = param != null;

				this.localCustomerID = param;
			}
			
			protected String localTitle;

			protected boolean localTitleTracker = false;

			public boolean isTitleSpecified() {
				return localTitleTracker;
			}

			public String getTitle() {
				return localTitle;
			}

			public void setTitle(String param) {
				localTitleTracker = param != null;

				this.localTitle = param;
			}
			
			protected String localCategory;

			protected boolean localCategoryTracker = false;

			public boolean isCategorySpecified() {
				return localCategoryTracker;
			}

			public String getCategory() {
				return localCategory;
			}

			public void setCategory(String param) {
				localCategoryTracker = param != null;

				this.localCategory = param;
			}
			
			
			/**
			 * field for InquiryDateTime
			 */

			protected java.util.Calendar localInquiryDateTime;

			/*
			 * This tracker boolean wil be used to detect whether the user called
			 * the set method for this attribute. It will be used to determine
			 * whether to include this field in the serialized XML
			 */
			protected boolean localInquiryDateTimeTracker = false;

			public boolean isInquiryDateTimeSpecified() {
				return localInquiryDateTimeTracker;
			}

			/**
			 * Auto generated getter method
			 * 
			 * @return java.util.Calendar
			 */
			public java.util.Calendar getInquiryDateTime() {
				return localInquiryDateTime;
			}

			/**
			 * Auto generated setter method
			 * 
			 * @param param
			 *            InquiryDateTime
			 */
			public void setInquiryDateTime(java.util.Calendar param) {
				localInquiryDateTimeTracker = param != null;

				this.localInquiryDateTime = param;

			}
			
			protected String localInquiryContent;

			protected boolean localInquiryContentTracker = false;

			public boolean isInquiryContentSpecified() {
				return localInquiryContentTracker;
			}

			public String getInquiryContent() {
				return localInquiryContent;
			}

			public void setInquiryContent(String param) {
				localInquiryContentTracker = param != null;

				this.localInquiryContent = param;
			}
			
			protected String localAnswerContentID;

			protected boolean localAnswerContentIDTracker = false;

			public boolean isAnswerContentIDSpecified() {
				return localAnswerContentIDTracker;
			}

			public String getAnswerContentID() {
				return localAnswerContentID;
			}

			public void setAnswerContentID(String param) {
				localAnswerContentIDTracker = param != null;

				this.localAnswerContentID = param;
			}
			
			protected String localAnswerContent;

			protected boolean localAnswerContentTracker = false;

			public boolean isAnswerContentSpecified() {
				return localAnswerContentTracker;
			}

			public String getAnswerContent() {
				return localAnswerContent;
			}

			public void setAnswerContent(String param) {
				localAnswerContentTracker = param != null;

				this.localAnswerContent = param;
			}
			
			protected String localAnswerTempleteID;

			protected boolean localAnswerTempleteIDTracker = false;

			public boolean isAnswerTempleteIDSpecified() {
				return localAnswerTempleteIDTracker;
			}

			public String getAnswerTempleteID() {
				return localAnswerTempleteID;
			}

			public void setAnswerTempleteID(String param) {
				localAnswerTempleteIDTracker = param != null;

				this.localAnswerTempleteID = param;
			}
			
			protected boolean localIsAnswered;
			
			protected boolean localIsAnsweredTracker;
			
			public boolean isIsAnsweredSpecified() {
				return localIsAnsweredTracker;
			}
			
			public boolean getIsAnswered() {
				return localIsAnswered;
			}
			
			public void setIsAnswered(boolean param) {
				localIsAnsweredTracker = true;
				
				this.localIsAnswered = param;
			}
			
			protected String localCustomerName;

			protected boolean localCustomerNameTracker = false;

			public boolean isCustomerNameSpecified() {
				return localCustomerNameTracker;
			}

			public String getCustomerName() {
				return localCustomerName;
			}

			public void setCustomerName(String param) {
				localCustomerNameTracker = param != null;

				this.localCustomerName = param;
			}
			/**
			 * 
			 * @param parentQName
			 * @param factory
			 * @return org.apache.axiom.om.OMElement
			 */
			public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
					throws org.apache.axis2.databinding.ADBException {

				org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
				return factory.createOMElement(dataSource, parentQName);

			}

			public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
					throws XMLStreamException, org.apache.axis2.databinding.ADBException {
				serialize(parentQName, xmlWriter, false);
			}

			public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
					throws XMLStreamException, org.apache.axis2.databinding.ADBException {

				String prefix = null;
				String namespace = null;

				prefix = parentQName.getPrefix();
				namespace = parentQName.getNamespaceURI();
				writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

				if (serializeType) {

					String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
					if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
						writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":inquiryType", xmlWriter);
					} else {
						writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "inquiryType", xmlWriter);
					}

				}
				
				if (localInquiryIDTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "InquiryID", xmlWriter);

					if (localInquiryID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("InquiryID cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localInquiryID);

					}

					xmlWriter.writeEndElement();
				}
				
				if (localOrderIDTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "OrderID", xmlWriter);

					if (localOrderID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("OrderID cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localOrderID);

					}

					xmlWriter.writeEndElement();
				}
				
				if (localProductOrderIDTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "ProductOrderID", xmlWriter);

					if (localProductOrderID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("ProductOrderID cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localProductOrderID);

					}

					xmlWriter.writeEndElement();
				}
				
				if (localProductNameTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "ProductName", xmlWriter);

					if (localProductName == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("ProductName cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localProductName);

					}

					xmlWriter.writeEndElement();
				}
				if (localProductIDTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "ProductID", xmlWriter);

					if (localProductID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("ProductID cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localProductID);

					}

					xmlWriter.writeEndElement();
				}
				if (localProductOrderOptionTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "ProductOrderOption", xmlWriter);

					if (localProductID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("ProductOrderOption cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localProductOrderOption);

					}

					xmlWriter.writeEndElement();
				}
				if (localCustomerIDTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "CustomerID", xmlWriter);

					if (localCustomerID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("CustomerID cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localCustomerID);

					}

					xmlWriter.writeEndElement();
				}
				if (localTitleTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "Title", xmlWriter);

					if (localTitle == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("Title cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localTitle);

					}

					xmlWriter.writeEndElement();
				}
				if (localCategoryTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "Category", xmlWriter);

					if (localCategory == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("Category cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localCategory);

					}

					xmlWriter.writeEndElement();
				}
				
				if (localInquiryDateTimeTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "InquiryDateTime", xmlWriter);

					if (localInquiryDateTime == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("InquiryDateTime cannot be null!!");

					} else {

						xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryDateTime));

					}

					xmlWriter.writeEndElement();
				}
				
				if (localInquiryContentTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "InquiryContent", xmlWriter);

					if (localInquiryContent == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("InquiryContent cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localInquiryContent);

					}

					xmlWriter.writeEndElement();
				}
				if (localAnswerContentIDTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "AnswerContentID", xmlWriter);

					if (localAnswerContentID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("AnswerContentID cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localAnswerContentID);

					}

					xmlWriter.writeEndElement();
				}
				if (localAnswerContentTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "AnswerContent", xmlWriter);

					if (localAnswerContent == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("AnswerContent cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localAnswerContent);

					}

					xmlWriter.writeEndElement();
				}
				if (localAnswerTempleteIDTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "AnswerTempleteID", xmlWriter);

					if (localAnswerTempleteID == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("AnswerTempleteID cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localAnswerTempleteID);

					}

					xmlWriter.writeEndElement();
				}
				
				if (localIsAnsweredTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "IsAnswered", xmlWriter);

					if (false) {

						throw new org.apache.axis2.databinding.ADBException("IsAnswered cannot be null!!");

					} else {
						xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsAnswered));
					}

					xmlWriter.writeEndElement();
				}
				
				if (localCustomerNameTracker) {
					namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
					writeStartElement(null, namespace, "CustomerName", xmlWriter);

					if (localCustomerName == null) {
						// write the nil attribute

						throw new org.apache.axis2.databinding.ADBException("CustomerName cannot be null!!");

					} else {

						xmlWriter.writeCharacters(localCustomerName);

					}

					xmlWriter.writeEndElement();
				}
				
			}

			private static String generatePrefix(String namespace) {
				if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
					return "cus";
				}
				return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
			}

			/**
			 * Utility method to write an element start tag.
			 */
			private void writeStartElement(String prefix, String namespace, String localPart,
					XMLStreamWriter xmlWriter) throws XMLStreamException {
				String writerPrefix = xmlWriter.getPrefix(namespace);
				if (writerPrefix != null) {
					xmlWriter.writeStartElement(namespace, localPart);
				} else {
					if (namespace.length() == 0) {
						prefix = "";
					} else if (prefix == null) {
						prefix = generatePrefix(namespace);
					}

					xmlWriter.writeStartElement(prefix, localPart, namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				}
			}

			/**
			 * Util method to write an attribute with the ns prefix
			 */
			private void writeAttribute(String prefix, String namespace, String attName, String attValue,
					XMLStreamWriter xmlWriter) throws XMLStreamException {
				if (xmlWriter.getPrefix(namespace) == null) {
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				}
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}

			/**
			 * Util method to write an attribute without the ns prefix
			 */
			private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
					throws XMLStreamException {
				if (namespace.equals("")) {
					xmlWriter.writeAttribute(attName, attValue);
				} else {
					registerPrefix(xmlWriter, namespace);
					xmlWriter.writeAttribute(namespace, attName, attValue);
				}
			}

			/**
			 * Util method to write an attribute without the ns prefix
			 */
			private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
					XMLStreamWriter xmlWriter) throws XMLStreamException {

				String attributeNamespace = qname.getNamespaceURI();
				String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
				if (attributePrefix == null) {
					attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
				}
				String attributeValue;
				if (attributePrefix.trim().length() > 0) {
					attributeValue = attributePrefix + ":" + qname.getLocalPart();
				} else {
					attributeValue = qname.getLocalPart();
				}

				if (namespace.equals("")) {
					xmlWriter.writeAttribute(attName, attributeValue);
				} else {
					registerPrefix(xmlWriter, namespace);
					xmlWriter.writeAttribute(namespace, attName, attributeValue);
				}
			}

			/**
			 * method to handle Qnames
			 */

			private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
				String namespaceURI = qname.getNamespaceURI();
				if (namespaceURI != null) {
					String prefix = xmlWriter.getPrefix(namespaceURI);
					if (prefix == null) {
						prefix = generatePrefix(namespaceURI);
						xmlWriter.writeNamespace(prefix, namespaceURI);
						xmlWriter.setPrefix(prefix, namespaceURI);
					}

					if (prefix.trim().length() > 0) {
						xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
					} else {
						// i.e this is the default namespace
						xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
					}

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}
			}

			private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

				if (qnames != null) {
					// we have to store this data until last moment since it is not
					// possible to write any
					// namespace data after writing the charactor data
					StringBuffer stringToWrite = new StringBuffer();
					String namespaceURI = null;
					String prefix = null;

					for (int i = 0; i < qnames.length; i++) {
						if (i > 0) {
							stringToWrite.append(" ");
						}
						namespaceURI = qnames[i].getNamespaceURI();
						if (namespaceURI != null) {
							prefix = xmlWriter.getPrefix(namespaceURI);
							if ((prefix == null) || (prefix.length() == 0)) {
								prefix = generatePrefix(namespaceURI);
								xmlWriter.writeNamespace(prefix, namespaceURI);
								xmlWriter.setPrefix(prefix, namespaceURI);
							}

							if (prefix.trim().length() > 0) {
								stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
							} else {
								stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
							}
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					}
					xmlWriter.writeCharacters(stringToWrite.toString());
				}

			}

			/**
			 * Register a namespace prefix
			 */
			private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
					throws XMLStreamException {
				String prefix = xmlWriter.getPrefix(namespace);
				if (prefix == null) {
					prefix = generatePrefix(namespace);
					javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
					while (true) {
						String uri = nsContext.getNamespaceURI(prefix);
						if (uri == null || uri.length() == 0) {
							break;
						}
						prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
					}
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				}
				return prefix;
			}

			/**
			 * databinding method to get an XML representation of this object
			 * 
			 */
			public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

				java.util.ArrayList elementList = new java.util.ArrayList();
				java.util.ArrayList attribList = new java.util.ArrayList();

				if (localInquiryIDTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryID"));

					if (localInquiryID != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryID));
					} else {
						throw new org.apache.axis2.databinding.ADBException("InquiryID cannot be null!!");
					}
				}
				if (localOrderIDTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "OrderID"));

					if (localOrderID != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOrderID));
					} else {
						throw new org.apache.axis2.databinding.ADBException("OrderID cannot be null!!");
					}
				}
				if (localProductOrderIDTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ProductOrderID"));

					if (localProductOrderID != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProductOrderID));
					} else {
						throw new org.apache.axis2.databinding.ADBException("ProductOrderID cannot be null!!");
					}
				}
				if (localProductNameTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ProductName"));

					if (localProductName != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProductName));
					} else {
						throw new org.apache.axis2.databinding.ADBException("ProductName cannot be null!!");
					}
				}
				if (localProductIDTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ProductID"));

					if (localProductID != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProductID));
					} else {
						throw new org.apache.axis2.databinding.ADBException("ProductID cannot be null!!");
					}
				}
				if (localProductOrderOptionTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ProductOrderOption"));

					if (localProductOrderOption != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localProductOrderOption));
					} else {
						throw new org.apache.axis2.databinding.ADBException("ProductOrderOption cannot be null!!");
					}
				}
				if (localCustomerIDTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "CustomerID"));

					if (localCustomerID != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustomerID));
					} else {
						throw new org.apache.axis2.databinding.ADBException("CustomerID cannot be null!!");
					}
				}
				if (localTitleTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Title"));

					if (localTitle != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTitle));
					} else {
						throw new org.apache.axis2.databinding.ADBException("Title cannot be null!!");
					}
				}
				if (localCategoryTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Category"));

					if (localCategory != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCategory));
					} else {
						throw new org.apache.axis2.databinding.ADBException("Category cannot be null!!");
					}
				}
				if (localInquiryDateTimeTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryDateTime"));

					if (localInquiryDateTime != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryDateTime));
					} else {
						throw new org.apache.axis2.databinding.ADBException("InquiryDateTime cannot be null!!");
					}
				}
				if (localInquiryContentTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryContent"));

					if (localInquiryContent != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryContent));
					} else {
						throw new org.apache.axis2.databinding.ADBException("InquiryContent cannot be null!!");
					}
				}
				if (localAnswerContentIDTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerContentID"));

					if (localAnswerContentID != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnswerContentID));
					} else {
						throw new org.apache.axis2.databinding.ADBException("AnswerContentID cannot be null!!");
					}
				}
				if (localAnswerContentTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerContent"));

					if (localAnswerContent != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnswerContent));
					} else {
						throw new org.apache.axis2.databinding.ADBException("AnswerContent cannot be null!!");
					}
				}
				if (localAnswerTempleteIDTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerTempleteID"));

					if (localAnswerTempleteID != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnswerTempleteID));
					} else {
						throw new org.apache.axis2.databinding.ADBException("AnswerTempleteID cannot be null!!");
					}
				}
				if (localIsAnsweredTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "IsAnswered"));

					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsAnswered));
				}
				if (localCustomerNameTracker) {
					elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "CustomerName"));

					if (localCustomerName != null) {
						elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCustomerName));
					} else {
						throw new org.apache.axis2.databinding.ADBException("CustomerName cannot be null!!");
					}
				}

				return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

			}

			/**
			 * Factory class that keeps the parse method
			 */
			public static class Factory {

				/**
				 * static method to create the object Precondition: If this object
				 * is an element, the current or next start element starts this
				 * object and any intervening reader events are ignorable If this
				 * object is not an element, it is a complex type and the reader is
				 * at the event just after the outer start element Postcondition: If
				 * this object is an element, the reader is positioned at its end
				 * element If this object is a complex type, the reader is
				 * positioned at the end element of its outer element
				 */
				public static InquiryType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
					InquiryType object = new InquiryType();

					int event;
					String nillableValue = null;
					String prefix = "";
					String namespaceuri = "";
					try {

						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();

						if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
							String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
							if (fullTypeName != null) {
								String nsPrefix = null;
								if (fullTypeName.indexOf(":") > -1) {
									nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
								}
								nsPrefix = nsPrefix == null ? "" : nsPrefix;

								String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

								if (!"inquiryType".equals(type)) {
									// find namespace for the prefix
									String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
									return (InquiryType) ExtensionMapper.getTypeObject(nsUri, type, reader);
								}

							}

						}

						// Note all attributes that were handled. Used to differ
						// normal attributes
						// from anyAttributes.
						Vector handledAttributes = new Vector();

						reader.next();
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();
							
						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "InquiryID").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setInquiryID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}

						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "OrderID").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setOrderID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();

						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "ProductOrderID").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setProductOrderID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "ProductName").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setProductName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "ProductID").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setProductID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "ProductOrderOption").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setProductOrderOption(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "CustomerID").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setCustomerID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "Title").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setTitle(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "Category").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setCategory(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();

						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "InquiryDateTime").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setInquiryDateTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "InquiryContent").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setInquiryContent(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "AnswerContentID").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setAnswerContentID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "AnswerContent").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setAnswerContent(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "AnswerTempleteID").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setAnswerTempleteID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();

						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "IsAnswered").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setIsAnswered(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}
						
						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();


						if (reader.isStartElement()
								&& new javax.xml.namespace.QName("", "CustomerName").equals(reader.getName())) {

							String content = reader.getElementText();

							object.setCustomerName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

							reader.next();

						} // End of if for expected property start element

						else {

						}

						while (!reader.isStartElement() && !reader.isEndElement())
							reader.next();

						if (reader.isStartElement())
							// A start element we are not expecting indicates a
							// trailing invalid property
							throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

					} catch (XMLStreamException e) {
						throw new java.lang.Exception(e);
					}

					return object;
				}

			}// end of factory class

		}
	
	public static class WarningType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * WarningType Namespace URI = http://customerinquiry.shopn.platform.nhncorp.com/
		 * Namespace Prefix = ns1
		 */

		/**
		 * field for Code
		 */

		protected String localCode;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getCode() {
			return localCode;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Code
		 */
		public void setCode(String param) {

			this.localCode = param;

		}

		/**
		 * field for Message
		 */

		protected String localMessage;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getMessage() {
			return localMessage;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Message
		 */
		public void setMessage(String param) {

			this.localMessage = param;

		}

		/**
		 * field for Detail
		 */

		protected String localDetail;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localDetailTracker = false;

		public boolean isDetailSpecified() {
			return localDetailTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getDetail() {
			return localDetail;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Detail
		 */
		public void setDetail(String param) {
			localDetailTracker = param != null;

			this.localDetail = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":WarningType", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "WarningType", xmlWriter);
				}

			}

			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "Code", xmlWriter);

			if (localCode == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("Code cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localCode);

			}

			xmlWriter.writeEndElement();

			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "Message", xmlWriter);

			if (localMessage == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("Message cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localMessage);

			}

			xmlWriter.writeEndElement();
			if (localDetailTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Detail", xmlWriter);

				if (localDetail == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Detail cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetail);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Code"));

			if (localCode != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localCode));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Code cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Message"));

			if (localMessage != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMessage));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Message cannot be null!!");
			}
			if (localDetailTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Detail"));

				if (localDetail != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetail));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Detail cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static WarningType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				WarningType object = new WarningType();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"WarningType".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (WarningType) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement() && new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Code").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setCode(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Message").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setMessage(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Detail").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetail(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class WarningList_type0 implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * WarningList_type0 Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * field for Warning This was an Array!
		 */

		protected WarningType[] localWarning;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localWarningTracker = false;

		public boolean isWarningSpecified() {
			return localWarningTracker;
		}

		/**
		 * Auto generated getter method
		 * 
		 * @return WarningType[]
		 */
		public WarningType[] getWarning() {
			return localWarning;
		}

		/**
		 * validate the array for Warning
		 */
		protected void validateWarning(WarningType[] param) {

		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Warning
		 */
		public void setWarning(WarningType[] param) {

			validateWarning(param);

			localWarningTracker = param != null;

			this.localWarning = param;
		}

		/**
		 * Auto generated add method for the array for convenience
		 * 
		 * @param param
		 *            WarningType
		 */
		public void addWarning(WarningType param) {
			if (localWarning == null) {
				localWarning = new WarningType[] {};
			}

			// update the setting tracker
			localWarningTracker = true;

			java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(localWarning);
			list.add(param);
			this.localWarning = (WarningType[]) list.toArray(new WarningType[list.size()]);

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			if (serializeType) {

				String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
				if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":WarningList_type0", xmlWriter);
				} else {
					writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "WarningList_type0", xmlWriter);
				}

			}
			if (localWarningTracker) {
				if (localWarning != null) {
					for (int i = 0; i < localWarning.length; i++) {
						if (localWarning[i] != null) {
							localWarning[i].serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Warning"), xmlWriter);
						} else {

							// we don't have to do any thing since minOccures is
							// zero

						}

					}
				} else {

					throw new org.apache.axis2.databinding.ADBException("Warning cannot be null!!");

				}
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			if (localWarningTracker) {
				if (localWarning != null) {
					for (int i = 0; i < localWarning.length; i++) {

						if (localWarning[i] != null) {
							elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Warning"));
							elementList.add(localWarning[i]);
						} else {

							// nothing to do

						}

					}
				} else {

					throw new org.apache.axis2.databinding.ADBException("Warning cannot be null!!");

				}

			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static WarningList_type0 parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				WarningList_type0 object = new WarningList_type0();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"WarningList_type0".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (WarningList_type0) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					java.util.ArrayList list1 = new java.util.ArrayList();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Warning").equals(reader.getName())) {

						// Process the array and step past its final element's
						// end.
						list1.add(WarningType.Factory.parse(reader));

						// loop until we find a start element that is not part
						// of this array
						boolean loopDone1 = false;
						while (!loopDone1) {
							// We should be at the end element, but make sure
							while (!reader.isEndElement())
								reader.next();
							// Step out of this element
							reader.next();
							// Step to next element event.
							while (!reader.isStartElement() && !reader.isEndElement())
								reader.next();
							if (reader.isEndElement()) {
								// two continuous end elements means we are
								// exiting the xml structure
								loopDone1 = true;
							} else {
								if (new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Warning").equals(reader.getName())) {
									list1.add(WarningType.Factory.parse(reader));

								} else {
									loopDone1 = true;
								}
							}
						}
						// call the converter utility to convert and set the
						// array

						object.setWarning((WarningType[]) org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(WarningType.class, list1));

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	/**
	 * field for WarningList
	 */

	protected WarningList_type0 localWarningList;

	/*
	 * This tracker boolean wil be used to detect whether the user called
	 * the set method for this attribute. It will be used to determine
	 * whether to include this field in the serialized XML
	 */
	protected boolean localWarningListTracker = false;

	public boolean isWarningListSpecified() {
		return localWarningListTracker;
	}

	/**
	 * Auto generated getter method
	 * 
	 * @return WarningList_type0
	 */
	public WarningList_type0 getWarningList() {
		return localWarningList;
	}

	/**
	 * Auto generated setter method
	 * 
	 * @param param
	 *            WarningList
	 */
	public void setWarningList(WarningList_type0 param) {
		localWarningListTracker = param != null;

		this.localWarningList = param;

	}
	
	public static class GetCustomerInquiryListResponseE implements org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/",
				"GetCustomerInquiryListResponse", "n");

		/**
		 * field for GetCustomerInquiryListResponse
		 */

		protected GetCustomerInquiryListResponse localGetCustomerInquiryListResponse;

		/**
		 * Auto generated getter method
		 * 
		 * @return GetCustomerInquiryListResponse
		 */
		public GetCustomerInquiryListResponse getGetCustomerInquiryListResponse() {
			return localGetCustomerInquiryListResponse;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            GetCustomerInquiryListResponse
		 */
		public void setGetCustomerInquiryListResponse(GetCustomerInquiryListResponse param) {

			this.localGetCustomerInquiryListResponse = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localGetCustomerInquiryListResponse == null) {
				String namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "GetCustomerInquiryListResponse", xmlWriter);

				// write the nil attribute
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "n", "1", xmlWriter);
				xmlWriter.writeEndElement();
			} else {
				localGetCustomerInquiryListResponse.serialize(MY_QNAME, xmlWriter);
			}

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localGetCustomerInquiryListResponse == null) {
				return new org.apache.axis2.databinding.utils.reader.NullXMLStreamReader(MY_QNAME);
			} else {
				return localGetCustomerInquiryListResponse.getPullParser(MY_QNAME);
			}

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static GetCustomerInquiryListResponseE parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				GetCustomerInquiryListResponseE object = new GetCustomerInquiryListResponseE();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
					if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
						// Skip the element and report the null value. It cannot
						// have subelements.
						while (!reader.isEndElement())
							reader.next();

						return object;

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "GetCustomerInquiryListResponse")
											.equals(reader.getName())) {

								nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
								if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
									object.setGetCustomerInquiryListResponse(null);
									reader.next();

								} else {

									object.setGetCustomerInquiryListResponse(GetCustomerInquiryListResponse.Factory.parse(reader));
								}
							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class GetCustomerInquiryListRequestE implements org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/",
				"GetCustomerInquiryListRequest", "cus");

		/**
		 * field for GetCustomerInquiryListRequest
		 */

		protected GetCustomerInquiryListRequest localGetCustomerInquiryListRequest;

		/**
		 * Auto generated getter method
		 * 
		 * @return GetCustomerInquiryListRequest
		 */
		public GetCustomerInquiryListRequest getGetCustomerInquiryListRequest() {
			return localGetCustomerInquiryListRequest;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            GetCustomerInquiryListRequest
		 */
		public void setGetCustomerInquiryListRequest(GetCustomerInquiryListRequest param) {

			this.localGetCustomerInquiryListRequest = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

 			if (localGetCustomerInquiryListRequest == null) {
				String namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "GetCustomerInquiryListRequest", xmlWriter);

				// write the nil attribute
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "n", "1", xmlWriter);
				xmlWriter.writeEndElement();
			} else {
				localGetCustomerInquiryListRequest.serialize(MY_QNAME, xmlWriter);
			}

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localGetCustomerInquiryListRequest == null) {
				return new org.apache.axis2.databinding.utils.reader.NullXMLStreamReader(MY_QNAME);
			} else {
				return localGetCustomerInquiryListRequest.getPullParser(MY_QNAME);
			}

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static GetCustomerInquiryListRequestE parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				GetCustomerInquiryListRequestE object = new GetCustomerInquiryListRequestE();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
					if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
						// Skip the element and report the null value. It cannot
						// have subelements.
						while (!reader.isEndElement())
							reader.next();

						return object;

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "GetCustomerInquiryListRequest").equals(reader
											.getName())) {

								nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
								if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
									object.setGetCustomerInquiryListRequest(null);
									reader.next();

								} else {

									object.setGetCustomerInquiryListRequest(GetCustomerInquiryListRequest.Factory.parse(reader));
								}
							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public GetCustomerInquiryListResponseE getCustomerInquiryList(GetCustomerInquiryListRequestE getCustomerInquiryListRequest) throws java.rmi.RemoteException {
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
			_operationClient.getOptions().setAction("http://customerinquiry.shopn.platform.nhncorp.com/CustomerInquiryServicePortType/GetCustomerInquiryList");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

			addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;
			// _operationClient.getOptions().getSoapVersionURI()
			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), getCustomerInquiryListRequest,
					optimizeContent(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "getCustomerInquiryList")),
					new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "getCustomerInquiryList"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
					.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

			java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),GetCustomerInquiryListResponseE.class, getEnvelopeNamespaces(_returnEnv));

			return (GetCustomerInquiryListResponseE) object;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "GetCustomerInquiryList"))) {
					// make the fault by reflection
					try {
						String exceptionClassName = (String) faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(
								faultElt.getQName(), "GetCustomerInquiryList"));
						Class exceptionClass = Class.forName(exceptionClassName);
						java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
						// message class
						String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
								"GetCustomerInquiryList"));
						Class messageClass = Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender().cleanup(_messageContext);
			}
		}
	}
	
	public static class GetCustomerInquiryListRequest extends BaseCheckoutRequestType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * GetCustomerInquiryListRequest Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */
		
		/*
		protected ServiceTypeCode localServiceType;

		public ServiceTypeCode getServiceType() {
			return localServiceType;
		}
		
		public void setServiceType(ServiceTypeCode param) {
			this.localServiceType = param;
		}
		*/
		
		protected String localServiceType;
		
		public String getServiceType() {
			return localServiceType;
		}
		
		public void setServiceType(String param) {
			this.localServiceType = param;
		}
		
		/**
		 * field for MallID
		 */

		protected String localMallID;

		/**
		 * Auto generated getter method
		 * 
		 * @return String
		 */
		public String getMallID() {
			return localMallID;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            MallID
		 */
		public void setMallID(String param) {

			this.localMallID = param;

		}

		/**
		 * field for InquiryTimeFrom
		 */

		protected java.util.Calendar localInquiryTimeFrom;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.util.Calendar
		 */
		public java.util.Calendar getInquiryTimeFrom() {
			return localInquiryTimeFrom;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            InquiryTimeFrom
		 */
		public void setInquiryTimeFrom(java.util.Calendar param) {

			this.localInquiryTimeFrom = param;

		}
		
		/**
		 * field for InquiryTimeTo
		 */

		protected java.util.Calendar localInquiryTimeTo;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.util.Calendar
		 */
		public java.util.Calendar getInquiryTimeTo() {
			return localInquiryTimeTo;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            InquiryTimeTo
		 */
		public void setInquiryTimeTo(java.util.Calendar param) {

			this.localInquiryTimeTo = param;

		}
		
		
		/**
		 * field for IsAnswered
		 */

		protected boolean localIsAnswered = false;
		
		public boolean getIsAnswered() {
			return localIsAnswered;
		}
		
		public void setIsAnswered(boolean param) {
			this.localIsAnswered = param;
		}


		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":getCustomerInquiryListRequest", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "getCustomerInquiryListRequest", xmlWriter);
			}

			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			
			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			localAccessCredentials.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"), xmlWriter);
			
			if (localDetailLevelTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "DetailLevel", xmlWriter);

				if (localDetailLevel == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetailLevel);

				}

				xmlWriter.writeEndElement();
			}
			
			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			
			writeStartElement(null, namespace, "Version", xmlWriter);
			if (localVersion == null) {
				throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localVersion);
			}
			xmlWriter.writeEndElement();


			/*if (localServiceType == null) {
				throw new org.apache.axis2.databinding.ADBException("ServiceType cannot be null!!");
			}
			localServiceType.serialize(new javax.xml.namespace.QName("", "ServiceType"), xmlWriter);
			*/
			
			writeStartElement(null, "", "ServiceType", xmlWriter);
			if (localServiceType == null) {
				throw new org.apache.axis2.databinding.ADBException("ServiceType cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localServiceType);
			}
			xmlWriter.writeEndElement();
			
			
			writeStartElement(null, "", "MallID", xmlWriter);
			if (localMallID == null) {
				throw new org.apache.axis2.databinding.ADBException("MallID cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localMallID);
			}
			xmlWriter.writeEndElement();

			writeStartElement(null, "", "InquiryTimeFrom", xmlWriter);
			if (localInquiryTimeFrom == null) {
				throw new org.apache.axis2.databinding.ADBException("InquiryTimeFrom cannot be null!!");
			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryTimeFrom));
			}

			xmlWriter.writeEndElement();
			
			writeStartElement(null, "", "InquiryTimeTo", xmlWriter);
			if (localInquiryTimeTo == null) {
				throw new org.apache.axis2.databinding.ADBException("InquiryTimeTo cannot be null!!");
			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryTimeTo));
			}
			xmlWriter.writeEndElement();
			
			if (localIsAnswered == true) {
				writeStartElement(null, "", "IsAnswered", xmlWriter);
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localIsAnswered));
				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();
		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
			attribList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "getCustomerInquiryListRequest"));

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"));

			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			elementList.add(localAccessCredentials);
			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}
			if (localDetailLevelTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel"));

				if (localDetailLevel != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetailLevel));
				} else {
					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");
				}
			}
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version"));

			if (localVersion != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MallID"));

			if (localMallID != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMallID));
			} else {
				throw new org.apache.axis2.databinding.ADBException("MallID cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryTimeFrom"));

			if (localInquiryTimeFrom != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryTimeFrom));
			} else {
				throw new org.apache.axis2.databinding.ADBException("InquiryTimeFrom cannot be null!!");
			}
			
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryTimeFrom"));

			if (localInquiryTimeTo != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryTimeTo));
			} else {
				throw new org.apache.axis2.databinding.ADBException("InquiryTimeTo cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ServiceType"));

			if (localServiceType == null) {
				throw new org.apache.axis2.databinding.ADBException("ServiceType cannot be null!!");
			}
			
			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static GetCustomerInquiryListRequest parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				GetCustomerInquiryListRequest object = new GetCustomerInquiryListRequest();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"getCustomerInquiryListRequest".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (GetCustomerInquiryListRequest) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials").equals(reader.getName())) {

						object.setAccessCredentials(AccessCredentialsType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetailLevel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setVersion(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MallID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setMallID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryTimeFrom").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setInquiryTimeFrom(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
					
					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryTimeTo").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setInquiryTimeTo(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ServiceType").equals(reader.getName())) {

						//object.setServiceType(ServiceTypeCode.Factory.parse(reader));
						String content = reader.getElementText();
						object.setServiceType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	public static class GetCustomerInquiryListResponse extends BaseCheckoutResponseType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * getCustomerInquiryListResponse Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		
		protected int localReturnedDataCount;
		protected boolean localReturnedDataCountTracker = false;
		public boolean isReturnedDataCountSpecified() {
			return localReturnedDataCountTracker;
		}

		public int getReturnedDataCount() {
			return localReturnedDataCount;
		}

		public void setReturnedDataCount(int param) {

			// setting primitive attribute tracker to true
			localReturnedDataCountTracker = param != java.lang.Integer.MIN_VALUE;

			this.localReturnedDataCount = param;

		}

		protected boolean localHasMoreData;
		protected boolean localHasMoreDataTracker = false;
		public boolean isHasMoreDataSpecified() {
			return localHasMoreDataTracker;
		}

		public boolean getHasMoreData() {
			return localHasMoreData;
		}

		public void setHasMoreData(boolean param) {

			// setting primitive attribute tracker to true
			localHasMoreDataTracker = true;

			this.localHasMoreData = param;

		}


		protected CustomerInquiryList_type0 localCustomerInquiryList;

		protected boolean localCustomerInquiryListTracker = false;

		public boolean isCustomerInquiryListSpecified() {
			return localCustomerInquiryListTracker;
		}
	
		public CustomerInquiryList_type0 getCustomerInquiryList() {
			return localCustomerInquiryList;
		}
	
		public void setCustomerInquiryList(CustomerInquiryList_type0 param) {
			localCustomerInquiryListTracker = param != null;
			this.localCustomerInquiryList = param;
		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":getCustomerInquiryListResponse", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "getCustomerInquiryListResponse", xmlWriter);
			}

			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "ResponseType", xmlWriter);

			if (localResponseType == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localResponseType);

			}

			xmlWriter.writeEndElement();
			if (localResponseTimeTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "ResponseTime", xmlWriter);

				if (localResponseTime == java.lang.Long.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException("ResponseTime cannot be null!!");

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
				}

				xmlWriter.writeEndElement();
			}
			if (localErrorTracker) {
				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				localError.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"), xmlWriter);
			}
			if (localWarningListTracker) {
				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				localWarningList.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"), xmlWriter);
			}
			if (localQuotaStatusTracker) {
				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				localQuotaStatus.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"), xmlWriter);
			}
			if (localDetailLevelTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "DetailLevel", xmlWriter);

				if (localDetailLevel == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetailLevel);

				}

				xmlWriter.writeEndElement();
			}
			if (localVersionTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Version", xmlWriter);

				if (localVersion == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localVersion);

				}

				xmlWriter.writeEndElement();
			}
			if (localReleaseTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Release", xmlWriter);

				if (localRelease == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Release cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRelease);

				}

				xmlWriter.writeEndElement();
			}
			if (localTimestampTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Timestamp", xmlWriter);

				if (localTimestamp == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");

				} else {

					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));

				}

				xmlWriter.writeEndElement();
			}
			if (localMessageIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "MessageID", xmlWriter);

				if (localMessageID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("MessageID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localMessageID);

				}

				xmlWriter.writeEndElement();
			}
			if (localReturnedDataCountTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "ReturnedDataCount", xmlWriter);

				if (localReturnedDataCount == java.lang.Integer.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException("ReturnedDataCount cannot be null!!");

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReturnedDataCount));
				}

				xmlWriter.writeEndElement();
			}
			if (localHasMoreDataTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "HasMoreData", xmlWriter);

				if (false) {

					throw new org.apache.axis2.databinding.ADBException("HasMoreData cannot be null!!");

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHasMoreData));
				}

				xmlWriter.writeEndElement();
			}
			
			if (localCustomerInquiryListTracker) {
				if (localCustomerInquiryList == null) {
					throw new org.apache.axis2.databinding.ADBException("CustomerInquiryList cannot be null!!");
				}
				localCustomerInquiryList.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "CustomerInquiryList"), xmlWriter);
			}
			xmlWriter.writeEndElement();

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "n";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
			attribList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "getCustomerInquiryListResponse"));
			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType"));

			if (localResponseType != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseType));
			} else {
				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");
			}
			if (localResponseTimeTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime"));

				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
			}
			if (localErrorTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"));

				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				elementList.add(localError);
			}
			if (localWarningListTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"));

				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				elementList.add(localWarningList);
			}
			if (localQuotaStatusTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"));

				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				elementList.add(localQuotaStatus);
			}
			if (localDetailLevelTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel"));

				if (localDetailLevel != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetailLevel));
				} else {
					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");
				}
			}
			if (localVersionTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version"));

				if (localVersion != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
				}
			}
			if (localReleaseTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Release"));

				if (localRelease != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRelease));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Release cannot be null!!");
				}
			}
			if (localTimestampTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp"));

				if (localTimestamp != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");
				}
			}
			if (localMessageIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MessageID"));

				if (localMessageID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMessageID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("MessageID cannot be null!!");
				}
			}
			if (localReturnedDataCountTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ReturnedDataCount"));

				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localReturnedDataCount));
			}
			if (localHasMoreDataTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "HasMoreData"));

				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localHasMoreData));
			}
			
			if (localCustomerInquiryListTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "CustomerInquiryList"));

				if (localCustomerInquiryList == null) {
					throw new org.apache.axis2.databinding.ADBException("CustomerInquiryList cannot be null!!");
				}
				elementList.add(localCustomerInquiryList);
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static GetCustomerInquiryListResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				GetCustomerInquiryListResponse object = new GetCustomerInquiryListResponse();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"getCustomerInquiryListResponse".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (GetCustomerInquiryListResponse) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					java.util.ArrayList list16 = new java.util.ArrayList();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setResponseTime(java.lang.Long.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement() && new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error").equals(reader.getName())) {

						object.setError(ErrorType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList").equals(reader.getName())) {

						object.setWarningList(WarningList_type0.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus").equals(reader.getName())) {

						object.setQuotaStatus(QuotaStatusType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetailLevel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setVersion(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Release").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRelease(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setTimestamp(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MessageID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setMessageID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}
					
					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "CustomerInquiryList").equals(reader.getName())) {

						object.setCustomerInquiryList(CustomerInquiryList_type0.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "ReturnedDataCount").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setReturnedDataCount(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setReturnedDataCount(java.lang.Integer.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("", "HasMoreData").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setHasMoreData(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}
	
	/* methods to provide back word compatibility */

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			GetCustomerInquiryListRequestE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
					param.getOMElement(GetCustomerInquiryListRequestE.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}
	
	private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, Class type, java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {
		try {
			if (GetCustomerInquiryListRequestE.class.equals(type)) {
				return GetCustomerInquiryListRequestE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			
			if (GetCustomerInquiryListResponseE.class.equals(type)) {
				return GetCustomerInquiryListResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			
			if (AnswerCustomerInquiryRequestE.class.equals(type)) {
				return AnswerCustomerInquiryRequestE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
			
			if (AnswerCustomerInquiryResponseE.class.equals(type)) {
				return AnswerCustomerInquiryResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
			}
		} catch (java.lang.Exception e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
		return null;
	}
	
	public static class AnswerCustomerInquiryRequest extends BaseCheckoutRequestType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * AnswerCustomerInquiryRequest Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */
		/**
		 * field for ServiceType
		 */
		protected String localServiceType;
		
		public String getServiceType() {
			return localServiceType;
		}
		
		public void setServiceType(String param) {
			this.localServiceType = param;
		}
		
		/**
		 * field for MallID
		 */

		protected String localMallID;

		public String getMallID() {
			return localMallID;
		}

		public void setMallID(String param) {
			this.localMallID = param;
		}

		/**
		 * field for InquiryID
		 */

		protected String localInquiryID;

		public String getInquiryID() {
			return localInquiryID;
		}

		public void setInquiryID(String param) {
			this.localInquiryID = param;
		}
		
		/**
		 * field for AnswerContent
		 */

		protected String localAnswerContent;

		public String getAnswerContent() {
			return localAnswerContent;
		}

		public void setAnswerContent(String param) {
			this.localAnswerContent = param;
		}
		
		/**
		 * field for AnswerContentID
		 */

		protected String localAnswerContentID;

		public String getAnswerContentID() {
			return localAnswerContentID;
		}

		public void setAnswerContentID(String param) {
			this.localAnswerContentID = param;
		}

		/**
		 * field for ActionType
		 */
		protected String localActionType;
		
		public String getActionType() {
			return localActionType;
		}
		
		public void setActionType(String param) {
			this.localActionType = param;
		}

		/**
		 * field for AnswerTempleteID
		 */

		protected String localAnswerTempleteID;

		public String getAnswerTempleteID() {
			return localAnswerTempleteID;
		}

		public void setAnswerTempleteID(String param) {
			this.localAnswerTempleteID = param;
		}
		
		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":answerCustomerInquiryRequest", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "answerCustomerInquiryRequest", xmlWriter);
			}

			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			
			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			localAccessCredentials.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"), xmlWriter);
			
			if (localDetailLevelTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "DetailLevel", xmlWriter);

				if (localDetailLevel == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetailLevel);

				}

				xmlWriter.writeEndElement();
			}
			
			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			
			writeStartElement(null, namespace, "Version", xmlWriter);
			if (localVersion == null) {
				throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localVersion);
			}
			xmlWriter.writeEndElement();
		
			writeStartElement(null, "", "MallID", xmlWriter);
			if (localMallID == null) {
				throw new org.apache.axis2.databinding.ADBException("MallID cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localMallID);
			}
			xmlWriter.writeEndElement();
			
			writeStartElement(null, "", "ServiceType", xmlWriter);
			if (localServiceType == null) {
				throw new org.apache.axis2.databinding.ADBException("ServiceType cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localServiceType);
			}
			xmlWriter.writeEndElement();

			writeStartElement(null, "", "InquiryID", xmlWriter);
			if (localInquiryID == null) {
				throw new org.apache.axis2.databinding.ADBException("InquiryID cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localInquiryID);
			}
			xmlWriter.writeEndElement();
			
			writeStartElement(null, "", "AnswerContent", xmlWriter);
			if (localAnswerContent == null) {
				throw new org.apache.axis2.databinding.ADBException("AnswerContent cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localAnswerContent);
			}
			xmlWriter.writeEndElement();
			
			writeStartElement(null, "", "AnswerContentID", xmlWriter);
			if (localAnswerContentID == null) {
				throw new org.apache.axis2.databinding.ADBException("AnswerContentID cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localAnswerContentID);
			}
			xmlWriter.writeEndElement();
			
			writeStartElement(null, "", "ActionType", xmlWriter);
			if (localActionType == null) {
				throw new org.apache.axis2.databinding.ADBException("ActionType cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localActionType);
			}
			xmlWriter.writeEndElement();
			
			writeStartElement(null, "", "AnswerTempleteID", xmlWriter);
			if (localAnswerTempleteID == null) {
				throw new org.apache.axis2.databinding.ADBException("AnswerTempleteID cannot be null!!");
			} else {
				xmlWriter.writeCharacters(localAnswerTempleteID);
			}
			xmlWriter.writeEndElement();
		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
			attribList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "answerCustomerInquiryRequest"));

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials"));

			if (localAccessCredentials == null) {
				throw new org.apache.axis2.databinding.ADBException("AccessCredentials cannot be null!!");
			}
			elementList.add(localAccessCredentials);
			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}
			if (localDetailLevelTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel"));

				if (localDetailLevel != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetailLevel));
				} else {
					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");
				}
			}
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version"));

			if (localVersion != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
			} else {
				throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
			}

			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ServiceType"));

			if (localServiceType == null) {
				throw new org.apache.axis2.databinding.ADBException("ServiceType cannot be null!!");
			}
			
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MallID"));

			if (localMallID != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMallID));
			} else {
				throw new org.apache.axis2.databinding.ADBException("MallID cannot be null!!");
			}
			
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryID"));

			if (localInquiryID != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localInquiryID));
			} else {
				throw new org.apache.axis2.databinding.ADBException("InquiryID cannot be null!!");
			}
			
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerContent"));

			if (localAnswerContent != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnswerContent));
			} else {
				throw new org.apache.axis2.databinding.ADBException("AnswerContent cannot be null!!");
			}
			
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerContentID"));

			if (localAnswerContentID != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnswerContentID));
			} else {
				throw new org.apache.axis2.databinding.ADBException("AnswerContentID cannot be null!!");
			}
			
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ActionType"));

			if (localActionType == null) {
				throw new org.apache.axis2.databinding.ADBException("ActionType cannot be null!!");
			}
			
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerContentID"));

			if (localAnswerTempleteID != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAnswerTempleteID));
			} else {
				throw new org.apache.axis2.databinding.ADBException("AnswerTempleteID cannot be null!!");
			}
			
			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static AnswerCustomerInquiryRequest parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				AnswerCustomerInquiryRequest object = new AnswerCustomerInquiryRequest();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"answerCustomerInquiryRequest".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (AnswerCustomerInquiryRequest) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AccessCredentials").equals(reader.getName())) {

						object.setAccessCredentials(AccessCredentialsType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetailLevel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setVersion(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ServiceType").equals(reader.getName())) {

						//object.setServiceType(ServiceTypeCode.Factory.parse(reader));
						String content = reader.getElementText();
						object.setServiceType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
					
					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MallID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setMallID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "InquiryID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setInquiryID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
					
					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerContent").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setAnswerContent(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
					
					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerContentID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setAnswerContentID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
					
					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ActionType").equals(reader.getName())) {

						//object.setServiceType(ServiceTypeCode.Factory.parse(reader));
						String content = reader.getElementText();
						object.setActionType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
					
					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerTempleteID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setAnswerTempleteID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}
					
				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}//Request
	
	public static class AnswerCustomerInquiryRequestE implements org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/",
				"AnswerCustomerInquiryRequest", "cus");

		/**
		 * field for AnswerCustomerInquiryRequest
		 */

		protected AnswerCustomerInquiryRequest localAnswerCustomerInquiryRequest;

		/**
		 * Auto generated getter method
		 * 
		 * @return AnswerCustomerInquiryRequest
		 */
		public AnswerCustomerInquiryRequest getAnswerCustomerInquiryRequest() {
			return localAnswerCustomerInquiryRequest;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            AnswerCustomerInquiryRequest
		 */
		public void setAnswerCustomerInquiryRequest(AnswerCustomerInquiryRequest param) {

			this.localAnswerCustomerInquiryRequest = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

 			if (localAnswerCustomerInquiryRequest == null) {
				String namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "AnswerCustomerInquiryRequest", xmlWriter);

				// write the nil attribute
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "n", "1", xmlWriter);
				xmlWriter.writeEndElement();
			} else {
				localAnswerCustomerInquiryRequest.serialize(MY_QNAME, xmlWriter);
			}

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localAnswerCustomerInquiryRequest == null) {
				return new org.apache.axis2.databinding.utils.reader.NullXMLStreamReader(MY_QNAME);
			} else {
				return localAnswerCustomerInquiryRequest.getPullParser(MY_QNAME);
			}

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static AnswerCustomerInquiryRequestE parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				AnswerCustomerInquiryRequestE object = new AnswerCustomerInquiryRequestE();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
					if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
						// Skip the element and report the null value. It cannot
						// have subelements.
						while (!reader.isEndElement())
							reader.next();

						return object;

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerCustomerInquiryRequest").equals(reader
											.getName())) {

								nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
								if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
									object.setAnswerCustomerInquiryRequest(null);
									reader.next();

								} else {

									object.setAnswerCustomerInquiryRequest(AnswerCustomerInquiryRequest.Factory.parse(reader));
								}
							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}//RequestE
	
	public static class AnswerCustomerInquiryResponseE implements org.apache.axis2.databinding.ADBBean {

		public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/",
				"AnswerCustomerInquiryResponse", "n");

		/**
		 * field for AnswerCustomerInquiryResponse
		 */

		protected AnswerCustomerInquiryResponse localAnswerCustomerInquiryResponse;

		/**
		 * Auto generated getter method
		 * 
		 * @return AnswerCustomerInquiryResponse
		 */
		public AnswerCustomerInquiryResponse getAnswerCustomerInquiryResponse() {
			return localAnswerCustomerInquiryResponse;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            AnswerCustomerInquiryResponse
		 */
		public void setAnswerCustomerInquiryResponse(AnswerCustomerInquiryResponse param) {

			this.localAnswerCustomerInquiryResponse = param;

		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME);
			return factory.createOMElement(dataSource, MY_QNAME);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localAnswerCustomerInquiryResponse == null) {
				String namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "AnswerCustomerInquiryResponse", xmlWriter);

				// write the nil attribute
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "n", "1", xmlWriter);
				xmlWriter.writeEndElement();
			} else {
				localAnswerCustomerInquiryResponse.serialize(MY_QNAME, xmlWriter);
			}

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "cus";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			// We can safely assume an element has only one type associated with
			// it

			if (localAnswerCustomerInquiryResponse == null) {
				return new org.apache.axis2.databinding.utils.reader.NullXMLStreamReader(MY_QNAME);
			} else {
				return localAnswerCustomerInquiryResponse.getPullParser(MY_QNAME);
			}

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static AnswerCustomerInquiryResponseE parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				AnswerCustomerInquiryResponseE object = new AnswerCustomerInquiryResponseE();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
					if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
						// Skip the element and report the null value. It cannot
						// have subelements.
						while (!reader.isEndElement())
							reader.next();

						return object;

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					while (!reader.isEndElement()) {
						if (reader.isStartElement()) {

							if (reader.isStartElement()
									&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "AnswerCustomerInquiryResponse")
											.equals(reader.getName())) {

								nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "n");
								if ("true".equals(nillableValue) || "1".equals(nillableValue)) {
									object.setAnswerCustomerInquiryResponse(null);
									reader.next();

								} else {

									object.setAnswerCustomerInquiryResponse(AnswerCustomerInquiryResponse.Factory.parse(reader));
								}
							} // End of if for expected property start element

							else {
								// A start element we are not expecting
								// indicates an invalid parameter was passed
								throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
							}

						} else {
							reader.next();
						}
					} // end of while loop

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}//ResponseE
	
	public static class AnswerCustomerInquiryResponse extends BaseCheckoutResponseType implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * answerCustomerInquiryResponse Namespace URI =
		 * http://customerinquiry.shopn.platform.nhncorp.com/ Namespace Prefix = ns1
		 */

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this, parentQName);
			return factory.createOMElement(dataSource, parentQName);

		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {
			serialize(parentQName, xmlWriter, false);
		}

		public void serialize(final javax.xml.namespace.QName parentQName, XMLStreamWriter xmlWriter, boolean serializeType)
				throws XMLStreamException, org.apache.axis2.databinding.ADBException {

			String prefix = null;
			String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();
			writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

			String namespacePrefix = registerPrefix(xmlWriter, "http://customerinquiry.shopn.platform.nhncorp.com/");
			if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix + ":answerCustomerInquiryResponse", xmlWriter);
			} else {
				writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "answerCustomerInquiryResponse", xmlWriter);
			}

			if (localRequestIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "RequestID", xmlWriter);

				if (localRequestID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRequestID);

				}

				xmlWriter.writeEndElement();
			}
			namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
			writeStartElement(null, namespace, "ResponseType", xmlWriter);

			if (localResponseType == null) {
				// write the nil attribute

				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");

			} else {

				xmlWriter.writeCharacters(localResponseType);

			}

			xmlWriter.writeEndElement();
			if (localResponseTimeTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "ResponseTime", xmlWriter);

				if (localResponseTime == java.lang.Long.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException("ResponseTime cannot be null!!");

				} else {
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
				}

				xmlWriter.writeEndElement();
			}
			if (localErrorTracker) {
				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				localError.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"), xmlWriter);
			}
			if (localWarningListTracker) {
				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				localWarningList.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"), xmlWriter);
			}
			if (localQuotaStatusTracker) {
				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				localQuotaStatus.serialize(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"), xmlWriter);
			}
			if (localDetailLevelTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "DetailLevel", xmlWriter);

				if (localDetailLevel == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localDetailLevel);

				}

				xmlWriter.writeEndElement();
			}
			if (localVersionTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Version", xmlWriter);

				if (localVersion == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localVersion);

				}

				xmlWriter.writeEndElement();
			}
			if (localReleaseTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Release", xmlWriter);

				if (localRelease == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Release cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localRelease);

				}

				xmlWriter.writeEndElement();
			}
			if (localTimestampTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "Timestamp", xmlWriter);

				if (localTimestamp == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");

				} else {

					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));

				}

				xmlWriter.writeEndElement();
			}
			if (localMessageIDTracker) {
				namespace = "http://customerinquiry.shopn.platform.nhncorp.com/";
				writeStartElement(null, namespace, "MessageID", xmlWriter);

				if (localMessageID == null) {
					// write the nil attribute

					throw new org.apache.axis2.databinding.ADBException("MessageID cannot be null!!");

				} else {

					xmlWriter.writeCharacters(localMessageID);

				}

				xmlWriter.writeEndElement();
			}

		}

		private static String generatePrefix(String namespace) {
			if (namespace.equals("http://customerinquiry.shopn.platform.nhncorp.com/")) {
				return "n";
			}
			return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
		}

		/**
		 * Utility method to write an element start tag.
		 */
		private void writeStartElement(String prefix, String namespace, String localPart,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			String writerPrefix = xmlWriter.getPrefix(namespace);
			if (writerPrefix != null) {
				xmlWriter.writeStartElement(namespace, localPart);
			} else {
				if (namespace.length() == 0) {
					prefix = "";
				} else if (prefix == null) {
					prefix = generatePrefix(namespace);
				}

				xmlWriter.writeStartElement(prefix, localPart, namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(String prefix, String namespace, String attName, String attValue,
				XMLStreamWriter xmlWriter) throws XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			xmlWriter.writeAttribute(namespace, attName, attValue);
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(String namespace, String attName, String attValue, XMLStreamWriter xmlWriter)
				throws XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
				XMLStreamWriter xmlWriter) throws XMLStreamException {

			String attributeNamespace = qname.getNamespaceURI();
			String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname, XMLStreamWriter xmlWriter) throws XMLStreamException {
			String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
				}

			} else {
				xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames, XMLStreamWriter xmlWriter) throws XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				StringBuffer stringToWrite = new StringBuffer();
				String namespaceURI = null;
				String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						} else {
							stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
						}
					} else {
						stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private String registerPrefix(XMLStreamWriter xmlWriter, String namespace)
				throws XMLStreamException {
			String prefix = xmlWriter.getPrefix(namespace);
			if (prefix == null) {
				prefix = generatePrefix(namespace);
				javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
				while (true) {
					String uri = nsContext.getNamespaceURI(prefix);
					if (uri == null || uri.length() == 0) {
						break;
					}
					prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
				}
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}
			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
			attribList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "answerCustomerInquiryResponse"));
			if (localRequestIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID"));

				if (localRequestID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRequestID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("RequestID cannot be null!!");
				}
			}
			elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType"));

			if (localResponseType != null) {
				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseType));
			} else {
				throw new org.apache.axis2.databinding.ADBException("ResponseType cannot be null!!");
			}
			if (localResponseTimeTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime"));

				elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localResponseTime));
			}
			if (localErrorTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error"));

				if (localError == null) {
					throw new org.apache.axis2.databinding.ADBException("Error cannot be null!!");
				}
				elementList.add(localError);
			}
			if (localWarningListTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList"));

				if (localWarningList == null) {
					throw new org.apache.axis2.databinding.ADBException("WarningList cannot be null!!");
				}
				elementList.add(localWarningList);
			}
			if (localQuotaStatusTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus"));

				if (localQuotaStatus == null) {
					throw new org.apache.axis2.databinding.ADBException("QuotaStatus cannot be null!!");
				}
				elementList.add(localQuotaStatus);
			}
			if (localDetailLevelTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel"));

				if (localDetailLevel != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDetailLevel));
				} else {
					throw new org.apache.axis2.databinding.ADBException("DetailLevel cannot be null!!");
				}
			}
			if (localVersionTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version"));

				if (localVersion != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Version cannot be null!!");
				}
			}
			if (localReleaseTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Release"));

				if (localRelease != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localRelease));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Release cannot be null!!");
				}
			}
			if (localTimestampTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp"));

				if (localTimestamp != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTimestamp));
				} else {
					throw new org.apache.axis2.databinding.ADBException("Timestamp cannot be null!!");
				}
			}
			if (localMessageIDTracker) {
				elementList.add(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MessageID"));

				if (localMessageID != null) {
					elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMessageID));
				} else {
					throw new org.apache.axis2.databinding.ADBException("MessageID cannot be null!!");
				}
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static AnswerCustomerInquiryResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
				AnswerCustomerInquiryResponse object = new AnswerCustomerInquiryResponse();

				int event;
				String nillableValue = null;
				String prefix = "";
				String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
						String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
						if (fullTypeName != null) {
							String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

							if (!"answerCustomerInquiryResponse".equals(type)) {
								// find namespace for the prefix
								String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
								return (AnswerCustomerInquiryResponse) ExtensionMapper.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					Vector handledAttributes = new Vector();

					reader.next();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "RequestID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRequestID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseType").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {
						// A start element we are not expecting indicates an
						// invalid parameter was passed
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "ResponseTime").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setResponseTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToLong(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setResponseTime(java.lang.Long.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement() && new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Error").equals(reader.getName())) {

						object.setError(ErrorType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "WarningList").equals(reader.getName())) {

						object.setWarningList(WarningList_type0.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "QuotaStatus").equals(reader.getName())) {

						object.setQuotaStatus(QuotaStatusType.Factory.parse(reader));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "DetailLevel").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setDetailLevel(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Version").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setVersion(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Release").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setRelease(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "Timestamp").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setTimestamp(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "MessageID").equals(reader.getName())) {

						String content = reader.getElementText();

						object.setMessageID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());

				} catch (XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}//Response
	
	public AnswerCustomerInquiryResponseE AnswerCustomerInquiry(AnswerCustomerInquiryRequestE answerCustomerInquiryRequest) throws java.rmi.RemoteException {
		org.apache.axis2.context.MessageContext _messageContext = null;
		try {
			org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
			_operationClient.getOptions().setAction("http://customerinquiry.shopn.platform.nhncorp.com/CustomerInquiryServicePortType/AnswerCustomerInquiry");
			_operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

			addPropertyToOperationClient(_operationClient, org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

			// create a message context
			_messageContext = new org.apache.axis2.context.MessageContext();

			// create SOAP envelope with that payload
			org.apache.axiom.soap.SOAPEnvelope env = null;
			// _operationClient.getOptions().getSoapVersionURI()
			env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), answerCustomerInquiryRequest,
					optimizeContent(new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "answerCustomerInquiry")),
					new javax.xml.namespace.QName("http://customerinquiry.shopn.platform.nhncorp.com/", "answerCustomerInquiry"));

			// adding SOAP soap_headers
			_serviceClient.addHeadersToEnvelope(env);
			// set the message context with that soap envelope
			_messageContext.setEnvelope(env);

			// add the message contxt to the operation client
			_operationClient.addMessageContext(_messageContext);

			// execute the operation client
			_operationClient.execute(true);

			org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
					.getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
			org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

			java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),AnswerCustomerInquiryResponseE.class, getEnvelopeNamespaces(_returnEnv));

			return (AnswerCustomerInquiryResponseE) object;

		} catch (org.apache.axis2.AxisFault f) {

			org.apache.axiom.om.OMElement faultElt = f.getDetail();
			if (faultElt != null) {
				if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "AnswerCustomerInquiry"))) {
					// make the fault by reflection
					try {
						String exceptionClassName = (String) faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(
								faultElt.getQName(), "AnswerCustomerInquiry"));
						Class exceptionClass = Class.forName(exceptionClassName);
						java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
						// message class
						String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
								"AnswerCustomerInquiry"));
						Class messageClass = Class.forName(messageClassName);
						java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
						java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
						m.invoke(ex, new java.lang.Object[] { messageObject });

						throw new java.rmi.RemoteException(ex.getMessage(), ex);
					} catch (ClassCastException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (ClassNotFoundException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.NoSuchMethodException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.reflect.InvocationTargetException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.IllegalAccessException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					} catch (java.lang.InstantiationException e) {
						// we cannot intantiate the class - throw the original
						// Axis fault
						throw f;
					}
				} else {
					throw f;
				}
			} else {
				throw f;
			}
		} finally {
			if (_messageContext.getTransportOut() != null) {
				_messageContext.getTransportOut().getSender().cleanup(_messageContext);
			}
		}
	}//stub.answerCustomerInquiry
	
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
			AnswerCustomerInquiryRequestE param, boolean optimizeContent,
			javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {

		try {

			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
			emptyEnvelope.getBody().addChild(
					param.getOMElement(AnswerCustomerInquiryRequestE.MY_QNAME, factory));
			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}
	
}
