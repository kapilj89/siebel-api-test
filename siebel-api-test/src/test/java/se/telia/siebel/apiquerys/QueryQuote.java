package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.quote.quoting.QuoteCheckOutInput;
import se.telia.siebel.data.DataStorage;
import com.siebel.ordermanagement.quote.*;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.ListOfQuote;

import java.util.List;


public class QueryQuote {
    DataStorage dataStorage ;
    QuoteWebService quoteWebService;
    QuotePort quotePort;

    public QueryQuote(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        quoteWebService = new QuoteWebService();
        quotePort = quoteWebService.getQuotePort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(quotePort);
    }

    public List<Quote> getQuoteById() {
        Quote q = new Quote();
        q.setId(dataStorage.getActiveQuoteId());
        ListOfQuote listOfQuote = new ListOfQuote();
        listOfQuote.getQuote().add(q);
        GetQuoteInput getQuoteInput = new GetQuoteInput();
        getQuoteInput.setListOfQuote(listOfQuote);
        GetQuoteOutput getQuoteOutput = quotePort.getQuote(getQuoteInput);
        List<Quote> quoteList = getQuoteOutput.getListOfQuote().getQuote();
        return quoteList;

    }

    public boolean updateQuote(Quote quote){
        System.out.println("In updateQuote");
        System.out.println("list getTSMoveOutDate before synchronize  ");
        List<QuoteItem> flattenedQuoteItems = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
        for (QuoteItem qi : flattenedQuoteItems){
             System.out.println("qi.getTSMoveOutDate = "+ qi.getTSMoveOutDate());
         }
        System.out.println("list TSC2BDueDate before synchronize  ");
        for (QuoteItem qi : flattenedQuoteItems){
            System.out.println("qi.getTSC2BDueDate = "+ qi.getTSC2BDueDate());
        }
        System.out.println("done list before synchronize");
        SynchronizeQuoteInput synchronizeQuoteInput = new SynchronizeQuoteInput();
        ListOfQuote listOfQuote = new ListOfQuote();
        listOfQuote.getQuote().add(quote);
        synchronizeQuoteInput.setListOfQuote(listOfQuote);
        synchronizeQuoteInput.setStatusObject("?");
        SynchronizeQuoteOutput synchronizeQuoteOutput = quotePort.synchronizeQuote(synchronizeQuoteInput);
        String id = synchronizeQuoteOutput.getListOfQuote().getQuote().get(0).getId();
        System.out.println("qoute id="+id);
        if(id == null) {return false;}
        return true;
        // TSMoveOutDate is an in only parameter. It will never
        // be populated in any output on the Quote.
        // You can see the change only after Quote has been transformed to Order.
    }

    public void quoteCheckout() {
        QuoteCheckOutInput quoteCheckOutInput = new QuoteCheckOutInput();
        quoteCheckOutInput.setObjectSpcId(dataStorage.getActiveQuoteId());
        quoteCheckOutInput.setCreditCardAuthorizationFlag("N");
        quoteCheckOutInput.setAutoOrderFlag("Y");
        
    }
    
    public void addAccess (String AccessType){
//    	QuoteCheckOutInput quoteCheckOutInput = new QuoteCheckOutInput();
        SynchronizeQuoteInput synchronizeQuoteInput = new SynchronizeQuoteInput();
        synchronizeQuoteInput.getListOfQuote().getQuote();
    }
}
