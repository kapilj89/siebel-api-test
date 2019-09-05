package se.telia.siebel.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

public class SiebelOutboundSessionInterceptor extends AbstractSoapInterceptor {
    private final SiebelSession session;

    public SiebelOutboundSessionInterceptor(SiebelSession session) {
        super("pre-protocol");
        this.session = session;
    }

    public final void handleMessage(SoapMessage message) {
        if (session != null) {
            try {
                message.getHeaders().addAll(this.createSoapHeaders(session));
            } catch (JAXBException var4) {
                throw new Fault(var4);
            }
        }
    }

    private List<Header> createSoapHeaders(SiebelSession session) throws JAXBException {
        ArrayList<Header> headers = new ArrayList<>();
        String sessionType = session.getSessionType();
        Credentials credentials = session.getCredentials();
        headers.add(createSiebelSoapHeader("http://siebel.com/webservices", "SessionType", sessionType));
        String sessionId = session.getSessionId();
        if (sessionId != null) {
            headers.add(createSiebelSoapHeader("http://siebel.com/webservices", "SessionToken", sessionId));
        } else if (credentials != null) {
            String username = credentials.getUsername();
            String password = credentials.getPassword();
            if (username != null && password != null) {
                headers.add(createSiebelSoapHeader("http://siebel.com/webservices", "UsernameToken", username));
                headers.add(createSiebelSoapHeader("http://siebel.com/webservices", "PasswordText", password));
            }
        }

        return headers;
    }

    private static Header createSiebelSoapHeader(String namespaceURI, String key, String value) throws JAXBException {
        return new Header(new QName(namespaceURI, key), value, new JAXBDataBinding(String.class));
    }
}