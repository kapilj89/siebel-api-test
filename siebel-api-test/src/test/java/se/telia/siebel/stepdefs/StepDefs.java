package se.telia.siebel.stepdefs;

import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;

import cucumber.api.java8.En;
import se.telia.siebel.apiquerys.QueryQuote;
import se.telia.siebel.apiquerys.SiebelDateFormat;
import se.telia.siebel.apiquerys.SiebelFlattenDataStructures;
import se.telia.siebel.data.DataStorage;


public class StepDefs implements En {
    DataStorage dataStorage;
    public StepDefs( DataStorage dataStorage) {
        this.dataStorage = dataStorage; // dataStorage is injected and contains stuff that needs sharing between steps
       
        And("^call SDU BB SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$", (String accessType) -> {
            System.out.println("\nSynchronizeQuote for BroadBand+IPTV\n");
            Quote quote = dataStorage.getQuote();
            String dueDate = SiebelDateFormat.getCurentCETtime();

            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);
                       if(quoteItem.getLineNumber().equals("2")|| quoteItem.getLineNumber().equals("3")){
                    	   quoteItem.setTSAccessTypeId(accessType);
                       }
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });
        
        And("^call SDU IPTV SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$", (String accessType) -> {
            System.out.println("\nSynchronizeQuote for IPTV\n");
            Quote quote = dataStorage.getQuote();
            String dueDate = SiebelDateFormat.getCurentCETtime();

            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);            
                       if(quoteItem.getLineNumber().equals("4")|| quoteItem.getLineNumber().equals("5")|| quoteItem.getLineNumber().equals("6")){
                    	   quoteItem.setTSAccessTypeId(accessType);
                       }
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });


        And("^call SDU BBIPTV SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$", (String accessType) -> {
            System.out.println("\nSynchronizeQuote for BroadBand+IPTV\n");
            Quote quote = dataStorage.getQuote();
            String dueDate = SiebelDateFormat.getCurentCETtime();

            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);            
                       if(quoteItem.getLineNumber().equals("2")|| quoteItem.getLineNumber().equals("3")|| quoteItem.getLineNumber().equals("7")){
                    	   quoteItem.setTSAccessTypeId(accessType);
                       }
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });

        And("^call SDU BBVOIP SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$", (String accessType) -> {
            System.out.println("\nSynchronizeQuote for BroadBand+IPTV\n");
            Quote quote = dataStorage.getQuote();
            String dueDate = SiebelDateFormat.getCETtime();

            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);            
                       if(quoteItem.getLineNumber().equals("2")|| quoteItem.getLineNumber().equals("3")|| quoteItem.getLineNumber().equals("5")|| quoteItem.getLineNumber().equals("7")){
                    	   quoteItem.setTSAccessTypeId(accessType);
                    	   
                       }
                       if(quoteItem.getName().equals("VoIP SE Service bundle_1606") || quoteItem.getName().equals("VoIP SE_B2B Service bundle_1649")){
                    	   quoteItem.setTSVoIPDeviceType("RGW");
                       }
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });
        
        And("^call BB xDSLSynchronizeQuote to populate the AccessCode \"([^\"]*)\" and ConnRef number \"([^\"]*)\" on the quote line items for a xDSL order$", (String accessType, String fbNumber) -> {
            System.out.println("\nSynchronizeQuote for BroadBand\n");
            Quote quote = dataStorage.getQuote();            
            String dueDate = SiebelDateFormat.getCETtime();
            String BBDueDate = SiebelDateFormat.BBDueDate();
           
            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);  
                       
                       if(quoteItem.getLineNumber().equals("2")||quoteItem.getLineNumber().equals("5")){
                    	   quoteItem.setTSAccessTypeId(accessType);
                    	   quoteItem.setConnectivityReferenceNumber(fbNumber);
                       }if(quoteItem.getLineNumber().equals("5")){
                    	   quoteItem.setRequestedDeliveryDate(BBDueDate);
                  
                       }   
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });

        And("^call BBVOIPIPTV xDSLSynchronizeQuote to populate the AccessCode \"([^\"]*)\" and ConnRef number \"([^\"]*)\" on the quote line items for a xDSL order$", (String accessType, String fbNumber) -> {
            System.out.println("\nSynchronizeQuote for BroadBand\n");
            Quote quote = dataStorage.getQuote();            
            String dueDate = SiebelDateFormat.getCETtime();
            String BBDueDate = SiebelDateFormat.BBDueDate();
           
            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);  
                       
                        if(quoteItem.getLineNumber().equals("2")|| quoteItem.getLineNumber().equals("3")|| quoteItem.getLineNumber().equals("5")|| quoteItem.getLineNumber().equals("7")|| quoteItem.getLineNumber().equals("8")|| quoteItem.getLineNumber().equals("9")|| quoteItem.getLineNumber().equals("11")){
                    	   quoteItem.setTSAccessTypeId(accessType);
                    	   quoteItem.setConnectivityReferenceNumber(fbNumber);
                       }if(quoteItem.getLineNumber().equals("3")){
                    	   quoteItem.setRequestedDeliveryDate(BBDueDate);
                       }  if(quoteItem.getName().equals("VoIP SE Service bundle_1606") || quoteItem.getName().equals("VoIP SE_B2B Service bundle_1649")){
                    	   quoteItem.setTSVoIPDeviceType("RGW");
                       } 
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });
        
        And("^call BBVOIPIPTV SynchronizeQuote to populate the deliveryContract \"([^\"]*)\" and AccessCode \"([^\"]*)\" on the quote line items for a MDU order$", (String Agreement, String AccessCode) -> {
            System.out.println("\nSynchronizeQuote for BroadBand\n");
            Quote quote = dataStorage.getQuote();
//            String dueDate = SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getTomorrowsDate());
            String dueDate = SiebelDateFormat.getCETtime();
            String dueDateHardware = SiebelDateFormat.siebelDateFormat( new Date());

            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);            
//                       if(quoteItem.getLineNumber().equals("2")){
                        if(quoteItem.getLineNumber().equals("2")|| quoteItem.getLineNumber().equals("3")|| quoteItem.getLineNumber().equals("7")|| quoteItem.getLineNumber().equals("8")|| quoteItem.getLineNumber().equals("9")){
//                    	   quoteItem.setTSDeliveryContractId(Agreement);
                    	   quoteItem.setTSMDUDeliveryContractNum(Agreement);
                    	   quoteItem.setTSAccessTypeId(AccessCode);
                       }
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
                        System.out.println("Setting TSColtDeliveryFromDate to "+ dueDateHardware);
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });
        /*And("^call BBIPTV SynchronizeQuote to populate the deliveryContract \"([^\"]*)\" Revision \"([^\"]*)\" RowID \"([^\"]*)\" and AccessCode \"([^\"]*)\" on the quote line items for a MDU order$", (String Agreement,String RevisionNumber,String RowID,String AccessCode) -> {
            System.out.println("\nSynchronizeQuote for BroadBandIPTVMDU\n");
            Quote quote = dataStorage.getQuote();
            String dueDate = SiebelDateFormat.getCETtime();
            String dueDateHardware = SiebelDateFormat.siebelDateFormat( new Date());

            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);            
                        if(quoteItem.getLineNumber().equals("2")|| quoteItem.getLineNumber().equals("3")|| quoteItem.getLineNumber().equals("7")|| quoteItem.getLineNumber().equals("8")|| quoteItem.getLineNumber().equals("9")){
//                    	   quoteItem.setTSDeliveryContractId("1-1658X79");1-16891A1
                     	   quoteItem.setTSDeliveryContractId(RowID);

                    	   quoteItem.setTSMDUDeliveryContractNum(Agreement);
                    	   quoteItem.setTSMDUDeliveryContractRevNum(RevisionNumber);

                    	   quoteItem.setTSAccessTypeId(AccessCode);

                       }
                       
                    });
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
                        System.out.println("Setting TSColtDeliveryFromDate to "+ dueDateHardware);
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            boolean result = queryQuote.updateQuote(quote);
            Assert.assertTrue("No id from synchronizeQuoteOutput",result);
          System.out.println("synchronizeQuoteOutput OK");
        });
*/
        
    }
}
