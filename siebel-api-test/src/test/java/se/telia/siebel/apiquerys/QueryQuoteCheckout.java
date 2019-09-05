package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.quote.quoting.QuoteCheckOutInput;
import com.siebel.ordermanagement.quote.quoting.QuoteCheckOutOutput;
import com.siebel.ordermanagement.quote.quoting.QuoteCheckOutPort;
import com.siebel.ordermanagement.quote.quoting.QuoteCheckOutWebService;
import se.telia.siebel.data.DataStorage;

public class QueryQuoteCheckout {
    DataStorage dataStorage ;
    QuoteCheckOutWebService quoteCheckOutWebService;
    QuoteCheckOutPort quoteCheckOutPort;
    public QueryQuoteCheckout(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        quoteCheckOutWebService=new QuoteCheckOutWebService();
        quoteCheckOutPort = quoteCheckOutWebService.getQuoteCheckOutPort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(quoteCheckOutPort);
    }


    public String quoteCheckout(String activeQuoteId) {
        QuoteCheckOutInput quoteCheckOutInput = new QuoteCheckOutInput();
        quoteCheckOutInput.setObjectSpcId(activeQuoteId );
        quoteCheckOutInput.setCreditCardAuthorizationFlag("N");
        quoteCheckOutInput.setAutoOrderFlag("Y");
        QuoteCheckOutOutput quoteCheckOutOutput = quoteCheckOutPort.quoteCheckOut(quoteCheckOutInput);
        String activeOrderId = quoteCheckOutOutput.getActiveOrderId();
        return activeOrderId;
    }
}
