package se.telia.siebel.apiquerys;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import se.telia.siebel.data.DataStorage;
import se.telia.siebel.interceptor.SiebelInboundSessionInterceptor;
import se.telia.siebel.interceptor.SiebelOutboundSessionInterceptor;

public class SiebelSoapCommunication {

    DataStorage dataStorage;

    public SiebelSoapCommunication(DataStorage dataStorage){
        this.dataStorage=dataStorage;
    }

    public void setupSoapCommunication(Object port) {


        // Add the interceptors for session handeling
        Client client = ClientProxy.getClient(port);
        if (client.getOutInterceptors().size()>0) {
            System.out.println("Interceptors alreade setup for this port.");
            return;
        }

        SiebelOutboundSessionInterceptor interceptorOut = new SiebelOutboundSessionInterceptor(dataStorage.getSiebelSession());
        client.getOutInterceptors().add(interceptorOut);

        // Add an incoming interceptor (also for login/session-management)
        SiebelInboundSessionInterceptor interceptorIn = new SiebelInboundSessionInterceptor(dataStorage.getSiebelSession());
        client.getInInterceptors().add(interceptorIn);

        // Add logging of request/responses (I know it is depricated, let's find out later what to use instead.)

        LoggingOutInterceptor outLog = new LoggingOutInterceptor();
        outLog.setLimit(-1);
        // log.setPrettyLogging(true); // uncomment if you want pretty print
        client.getOutInterceptors().add(outLog);

        LoggingInInterceptor inLog = new LoggingInInterceptor();
        inLog.setLimit(-1);
        // log.setPrettyLogging(true); // uncomment if you want pretty print
        client.getInInterceptors().add(inLog);
        // Set the endpoint URL
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, dataStorage.getSiebelEndpointURL()) ;

    }
}
