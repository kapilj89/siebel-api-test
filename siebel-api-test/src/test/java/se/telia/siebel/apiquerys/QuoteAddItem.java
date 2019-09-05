package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.quote.QuoteAddItemsInput;
import com.siebel.ordermanagement.quote.QuoteAddItemsOutput;
import com.siebel.ordermanagement.quote.QuoteAddItemsPort;
import com.siebel.ordermanagement.quote.QuoteAddItemsWS;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.productdata.*;
import com.siebel.ordermanagement.quote.quoting.ExecuteQuotingOutput;

import se.telia.siebel.data.DataStorage;


public class QuoteAddItem {
      DataStorage dataStorage;
      QuoteAddItemsPort quoteAddItemsPort; 
      
      private static final String Sync_Quote = "Y"; 
      private static final String PRICING_FLAG = "Y";
      private static final String Check_Eligibility = "N";

      public QuoteAddItem(DataStorage dataStorage) {        
            
            this.dataStorage = dataStorage;
            QuoteAddItemsWS quoteAddItemsWS = new QuoteAddItemsWS();
        quoteAddItemsPort = quoteAddItemsWS.getQuoteAddItemsPort();
            new SiebelSoapCommunication(dataStorage).setupSoapCommunication(quoteAddItemsPort);
      }
      private ListOfData buildListOfDataStructure(String ProductId) {
              final ListOfData listOfData = new ListOfData();
              listOfData.setProductData(new ProductData());
              final Item bundleLevelItem = buildItemStructure(ProductId);
              listOfData.getProductData().getItem().add(bundleLevelItem);
              Item productLevelItem = buildItemStructure(ProductId);
              bundleLevelItem.getItem().add(productLevelItem);
              Item subProductLevelItem = buildItemStructure(ProductId);
              productLevelItem.getItem().add(subProductLevelItem);
              return listOfData;
          }
      private static Item buildItemStructure(String ProductId) {
              final Item item = new Item();
              
              item.setProductId(ProductId);
              item.setProductType("Promotion");
              item.setQuantity("1");
              return item;
          }
      
       public Quote quoteAddItems(Quote quote, String ProductId) {
              
                  final QuoteAddItemsInput quoteAddItemsInput = new QuoteAddItemsInput();
              quoteAddItemsInput.setGetPricing(PRICING_FLAG);
                  quoteAddItemsInput.setSyncQuoteFlag(Sync_Quote);
                  quoteAddItemsInput.setCheckEligibility(Check_Eligibility);
              quoteAddItemsInput.setListOfData(buildListOfDataStructure(ProductId));

//            Quote quote = new Quote();
//                quote.setQuoteNumber(quoteNumber);
//                quote.setTSAgreementStatus("Not Applicable");
//                quote.setTSAgreementType("Not Applicable");
//                quote.setTSConfirmSignFlag("N");
//                quote.setTSConfirmSignFlag("N");
                  

                  
                  
                  ListOfQuote listOfQuote = new ListOfQuote();
                  listOfQuote.getQuote().add(quote);
                  quoteAddItemsInput.setListOfQuote(listOfQuote);
                  
              QuoteAddItemsOutput quoteAddItemsOutput = quoteAddItemsPort.quoteAddItems(quoteAddItemsInput);
              Quote updatedQuote = quoteAddItemsOutput.getListOfQuote().getQuote().get(0);
              return updatedQuote;
//            return quoteAddItemsOutput.getListOfQuote();
          }

      
     
      
}

