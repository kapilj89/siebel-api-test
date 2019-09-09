package se.telia.siebel.apiquerys;

import java.util.List;

import org.junit.Assert;

import com.siebel.ordermanagement.abo.ABOWebService;
import com.siebel.ordermanagement.abo.DisconnectAssetToOrderPort;
import com.siebel.ordermanagement.abo.DisconnectAssetToQuoteInput;
import com.siebel.ordermanagement.abo.DisconnectAssetToQuoteOutput;
import com.siebel.ordermanagement.abo.ModifyAssetToQuoteInput;
import com.siebel.ordermanagement.abo.ModifyAssetToQuoteOutput;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;

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
	 public ListOfQuote DisconnectAsset(String quoteNumber){
	    	DisconnectAssetToQuoteInput disconnect=new DisconnectAssetToQuoteInput();
	    	disconnect.setAssetNumber("1-2602523941");
	    	disconnect.setDueDate(SiebelDateFormat.getCurentCETtime());
	    	disconnect.setTSReasonCode("Normal");
//	    	disconnect.setQuoteId(value);
//	    	ModifyAssetToQuoteInput modify = new ModifyAssetToQuoteInput();
//	    	modify.setQuoteNumber(quoteNumber);
//	      modify.setDueDate(SiebelDateFormat.siebelDateFormat(dataStorage.getMoveInAddressMap().get("MoveOutDate")));
//	        modify.setAssetNumber(dataStorage.getAssetNumber());
	    	disconnect.setTSChannelName("TELIASE");
//	        modify.setOrderSubType("Modify");
//	        modify.setAccountId(dataStorage.getServiceAccountId());
//	        modify.setTSReasonCode("No Reason");
//	    	disconnect.setPriceOnSync("Y");
//	        modify.setActiveDocumentId("");
	    	DisconnectAssetToQuoteOutput DisconnectAssetToQuoteOutput =DisconnectAssetToQuotePort.disconnectAssetToQuote(disconnect);
//	        ModifyAssetToQuoteOutput ModifyAssetToQuoteOutput = modifyAssetToQuotePort.modifyAssetToQuote(modify);
	        ListOfQuote listofQuote = DisconnectAssetToQuoteOutput.getListOfQuote();
	        System.out.println("Quote="+listofQuote);
	        String errorSpcCode = DisconnectAssetToQuoteOutput.getErrorSpcCode();
	        Assert.assertTrue("".equals(errorSpcCode)); // Ensure that there is no error code
	        String activeDocId = DisconnectAssetToQuoteOutput.getActiveDocumentId();
	        dataStorage.setActiveQuoteId(activeDocId);
	        dataStorage.setListofQuote(listofQuote);
	        ListOfQuote Quotelist=dataStorage.getListofQuote();
	        List<Quote>quotedata=Quotelist.getQuote();
	        Quote quoteDetails = listofQuote.getQuote().get(0);
	        dataStorage.setQuote(quoteDetails);
	        for(Quote qc:quotedata){/*
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
	        */}
	        return listofQuote;
	    }
}
