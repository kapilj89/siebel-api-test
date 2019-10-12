package se.telia.siebel.apiquerys;



import com.siebel.customui.TSChannelSISOMBillingSubmitOrderWebService;
import com.siebel.customui.TSChannelSISOMBillingSubmitOrderWebService_Service;
import com.siebel.customui.TSChannelSubmitOrderInput;
import com.siebel.customui.TSChannelSubmitOrderOutput;
import se.telia.siebel.data.DataStorage;

public class QueryTSChannelSISOMBillingSubmitOrder {
    DataStorage dataStorage ;
    TSChannelSISOMBillingSubmitOrderWebService_Service service;
    TSChannelSISOMBillingSubmitOrderWebService port;
    public QueryTSChannelSISOMBillingSubmitOrder(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        service=new TSChannelSISOMBillingSubmitOrderWebService_Service();
        port = service.getTSChannelSISOMBillingSubmitOrderWebService();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(port);
    }


    public String submitOrder(String activeOrderId) {
        TSChannelSubmitOrderInput tsChannelSubmitOrderInput = new TSChannelSubmitOrderInput();
        tsChannelSubmitOrderInput.setFulfillmentMode("Deliver");
        tsChannelSubmitOrderInput.setObjectSpcId(activeOrderId);
        TSChannelSubmitOrderOutput tsChannelSubmitOrderOutput = port.tsChannelSubmitOrder(tsChannelSubmitOrderInput);

        String objectSpcId = tsChannelSubmitOrderOutput.getObjectSpcId();
        System.out.println("objectSpcId (the order id which is the same as the activeOrderId, and that is returned from the submit order api) ="+objectSpcId);

        if (tsChannelSubmitOrderOutput.getErrorSpcCode() != null && tsChannelSubmitOrderOutput.getErrorSpcCode().length() > 0 && ! "Success".equals( tsChannelSubmitOrderOutput.getErrorSpcCode() )) {
            System.out.println("Error in submit order \n"+
                    tsChannelSubmitOrderOutput.getErrorSpcCode() + "\n"+
                    tsChannelSubmitOrderOutput.getErrorSpcMessage());
            return null;
        }
		System.out.println("\n***OREDER PLACED SUCCESSFULLY*** \nORDER ID : "+tsChannelSubmitOrderOutput.getOrderNumber());  

        return objectSpcId; // This is the order id, the same as the input variable activeOrderId.
    }
}
