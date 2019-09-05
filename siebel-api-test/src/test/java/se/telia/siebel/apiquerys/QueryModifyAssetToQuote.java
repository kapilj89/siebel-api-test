package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.abo.ABOWebService;
import com.siebel.ordermanagement.abo.ModifyAssetToQuoteInput;
import com.siebel.ordermanagement.abo.ModifyAssetToQuoteOutput;
import com.siebel.ordermanagement.abo.ModifyAssetToQuotePort;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
//import com.siebel.ordermanagement.quote.item.data.ListOfQuoteItem;
//import com.siebel.ordermanagement.quote.item.data.QuoteItem;
import com.siebel.ordermanagement.quote.data.QuoteItem;

import java.util.List;

import org.junit.Assert;
import se.telia.siebel.data.DataStorage;

public class QueryModifyAssetToQuote {
   
	DataStorage dataStorage ;
	ModifyAssetToQuotePort modifyAssetToQuotePort;

    public QueryModifyAssetToQuote(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
        ABOWebService ModifyWebService_service = new ABOWebService();
        modifyAssetToQuotePort = ModifyWebService_service.getModifyAssetToQuotePort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(modifyAssetToQuotePort);

    }
    public ListOfQuote modifyAssetToQuote(String quoteNumber,String ActualSpeed,String ModifiedSpeed){
    	
    	ModifyAssetToQuoteInput modify = new ModifyAssetToQuoteInput();
    	modify.setQuoteNumber(quoteNumber);
//      modify.setDueDate(SiebelDateFormat.siebelDateFormat(dataStorage.getMoveInAddressMap().get("MoveOutDate")));
        modify.setAssetNumber(dataStorage.getAssetNumber());
        modify.setTSChannelName("TELIASE");
//        modify.setOrderSubType("Modify");
        modify.setAccountId(dataStorage.getServiceAccountId());
        modify.setTSReasonCode("No Reason");
        modify.setPriceOnSync("Y");
//        modify.setActiveDocumentId("");
        ModifyAssetToQuoteOutput ModifyAssetToQuoteOutput = modifyAssetToQuotePort.modifyAssetToQuote(modify);
        ListOfQuote listofQuote = ModifyAssetToQuoteOutput.getListOfQuote();
        System.out.println("Quote="+listofQuote);
        String errorSpcCode = ModifyAssetToQuoteOutput.getErrorSpcCode();
        Assert.assertTrue("".equals(errorSpcCode)); // Ensure that there is no error code
        String activeDocId = ModifyAssetToQuoteOutput.getActiveDocumentId();
        dataStorage.setActiveQuoteId(activeDocId);
        dataStorage.setListofQuote(listofQuote);
        ListOfQuote Quotelist=dataStorage.getListofQuote();
        List<Quote>quotedata=Quotelist.getQuote();
        Quote quoteDetails = listofQuote.getQuote().get(0);
        dataStorage.setQuote(quoteDetails);
        for(Quote qc:quotedata){
        	List<QuoteItem> Qi=qc.getListOfQuoteItem().getQuoteItem();
        	
        	for(QuoteItem quoteitem:Qi){
        		System.out.println("QuoteItem "+quoteitem.getName());
        		System.out.println("ActualSpeed "+ActualSpeed);
        		if(quoteitem.getName().equalsIgnoreCase("Service Bundle-IA-1703-1")){
        			List<QuoteItem> ListofQuote =quoteitem.getQuoteItem();
        			for(QuoteItem quote : ListofQuote){
        				System.out.println("Inside ServiceBundle quote" + quote.getName());
                		System.out.println("ActualSpeed "+ActualSpeed);
                		if(quote.getName().equalsIgnoreCase(ActualSpeed)){
                			String IntegrationID=quote.getAssetIntegrationId();
                			System.out.println("Modify AssetIntID:"+IntegrationID);
                			dataStorage.setIntegrationId(IntegrationID);
                		}
        			}
        			
        		}
        		
        	}
        }
        return listofQuote;
    }

}

