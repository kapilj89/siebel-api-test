package se.telia.siebel.apiquerys;

import com.siebel.customui.MoveModifyAssetToQuoteInput;
import com.siebel.customui.MoveModifyAssetToQuoteOutput;
import com.siebel.customui.TSC2BMoveOrderWebService;
import com.siebel.customui.TSC2BMoveOrderWebService_Service;
import org.junit.Assert;
import se.telia.siebel.data.DataStorage;

public class QueryMoveModifyAssetToQuote {
    DataStorage dataStorage ;

    TSC2BMoveOrderWebService_Service tsc2BMoveOrderWebService_service;
    TSC2BMoveOrderWebService tsc2BMoveOrderWebService;

    public QueryMoveModifyAssetToQuote(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
        tsc2BMoveOrderWebService_service = new TSC2BMoveOrderWebService_Service();
        tsc2BMoveOrderWebService = tsc2BMoveOrderWebService_service.getTSC2BMoveOrderWebService();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(tsc2BMoveOrderWebService);

    }
    public void moveModifyAssetToQuote(String quoteNumber){
        MoveModifyAssetToQuoteInput m = new MoveModifyAssetToQuoteInput();
        m.setQuoteNumber(quoteNumber);

        m.setDueDate(SiebelDateFormat.siebelDateFormat(dataStorage.getMoveInAddressMap().get("MoveOutDate")));
        m.setAssetNumber(dataStorage.getAssetNumber());
        m.setTSChannelName("TELIASE");
//        m.setRequestType("Modify");
//        m.setOrderSubType("Move");
        m.setAccountId(dataStorage.getServiceAccountId());
//        m.setTSReasonCode("");
//        m.setActiveDocumentId("");
        MoveModifyAssetToQuoteOutput moveModifyAssetToQuoteOutput = tsc2BMoveOrderWebService.moveModifyAssetToQuote(m);
        String activeQuoteId = moveModifyAssetToQuoteOutput.getActiveQuoteId();
        System.out.println("activeQuoteId="+activeQuoteId);
        String errorSpcCode = moveModifyAssetToQuoteOutput.getErrorSpcCode();
        Assert.assertTrue("".equals(errorSpcCode)); // Ensure that there is no error code
        dataStorage.setActiveQuoteId(activeQuoteId);
    }

}
