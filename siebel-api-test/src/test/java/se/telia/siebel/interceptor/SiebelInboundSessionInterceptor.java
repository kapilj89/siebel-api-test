package se.telia.siebel.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;

public class SiebelInboundSessionInterceptor extends AbstractSoapInterceptor {
    private static final QName QNAME = new QName("http://siebel.com/webservices", "SessionToken");
    private final SiebelSession session;

    public SiebelInboundSessionInterceptor(SiebelSession session) {
        super("post-unmarshal");
        this.session = session;
    }

    public final void handleMessage(SoapMessage message) {
        if (session != null) {
            Header header = message.getHeader(QNAME);
            if (header != null) {
                session.setSessionId(((Node) header.getObject()).getTextContent());
            }
        }
    }
}