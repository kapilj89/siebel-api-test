package se.telia.siebel.apiquerys;

import java.util.List;
import org.junit.Assert;
import com.siebel.ordermanagement.abo.ABOWebService;
import com.siebel.ordermanagement.abo.DisconnectAssetToQuoteInput;
import com.siebel.ordermanagement.abo.DisconnectAssetToQuoteOutput;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;
import se.telia.siebel.data.DataStorage;

public class QueryDisconnectAssetToQuote {
DataStorage dataStorage ;
com.siebel.ordermanagement.abo.DisconnectAssetToQuotePort DisconnectAssetToQuotePort;

public QueryDisconnectAssetToQuote(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
        ABOWebService DisconnectWebService_service = new ABOWebService();
        DisconnectAssetToQuotePort= DisconnectWebService_service.getDisconnectAssetToQuotePort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(DisconnectAssetToQuotePort);
    }

public Quote DisconnectAsset(String quoteNumber , String Promotions){
     DisconnectAssetToQuoteInput disconnect=new DisconnectAssetToQuoteInput();
     disconnect.setTSChannelName("TELIASE");
     disconnect.setTSReasonCode("Normal");
     disconnect.setAccountId(dataStorage.getServiceAccountId());
     disconnect.setQuoteNumber(quoteNumber);
     disconnect.setAssetNumber(Promotions);
     disconnect.setDueDate(SiebelDateFormat.getCETtime());
     DisconnectAssetToQuoteOutput DisconnectAssetToQuoteOutput =DisconnectAssetToQuotePort.disconnectAssetToQuote(disconnect);
     String errorSpcCode = DisconnectAssetToQuoteOutput.getErrorSpcCode();
     Assert.assertTrue("".equals(errorSpcCode)); // Ensure that there is no error code
     ListOfQuote listofQuote = DisconnectAssetToQuoteOutput.getListOfQuote();
        System.out.println("Quote="+listofQuote);
        String activeDocId = DisconnectAssetToQuoteOutput.getActiveDocumentId();
        dataStorage.setActiveQuoteId(activeDocId);
        dataStorage.setListofQuote(listofQuote);
        Quote quoteDetails = listofQuote.getQuote().get(0);
        dataStorage.setQuote(quoteDetails);
     return quoteDetails;
}
}