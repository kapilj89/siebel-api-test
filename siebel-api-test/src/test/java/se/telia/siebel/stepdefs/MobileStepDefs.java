/**package se.telia.siebel.stepdefs;

import com.siebel.ordermanagement.catalog.data.productdetails.Product;
import com.siebel.ordermanagement.configurator.EndConfigurationInput;
import com.siebel.ordermanagement.configurator.cfginteractdata.Item;
import com.siebel.ordermanagement.configurator.cfginteractdata.ListOfData;
import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.ordermanagement.quote.data.QuoteItemXA;
import org.junit.Assert;

import cucumber.api.java8.En;
import se.telia.siebel.apiquerys.*;
import se.telia.siebel.data.AccountDetails;
import se.telia.siebel.data.DataStorage;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static se.telia.siebel.apiquerys.GenerateQuoteNumber.getGeneratedQuoteNumber;
import static se.telia.siebel.apiquerys.SiebelDateFormat.siebelDateFormat;
import static se.telia.siebel.apiquerys.SiebelDateFormat.getTomorrowsDate;
import static se.telia.siebel.apiquerys.SiebelFlattenDataStructures.getFlattenedQuoteItems;


public class MobileStepDefs implements En {
    DataStorage dataStorage;
    public MobileStepDefs( DataStorage dataStorage) {

        System.out.println("MobileStepDefs Constructor");
        this.dataStorage = dataStorage; // dataStorage is injected and contains stuff that needs sharing between steps


        When("^call SelfServiceUser to get primary organization id which is used in quote creation$", () -> {
            System.out.println("\nSelfServiceUser\n");
            QuerySelfServiceUser selfService = new QuerySelfServiceUser(dataStorage);
            String primaryOrgID = selfService.getPrimaryOrganizationId(dataStorage.getLoginName());
            System.out.println("Primary Organization id is :"+primaryOrgID);
            dataStorage.setPrimaryOrganizationId(primaryOrgID);
        });

        And("^call QueryCustomer using SSN \"([^\"]*)\" to get account and billing details$", (String ssn) -> {
            System.out.println("\nQueryCustomer\n");
            QueryCustomer queryCustomer = new QueryCustomer(dataStorage) ;
            AccountDetails accountDetails = queryCustomer.getGetAccountDetailsString(ssn);
            Assert.assertNotNull("accountDetails is null", accountDetails);
            System.out.println("This is the accountDetails that we got back:");
            System.out.println(accountDetails.dump());
            dataStorage.setAccountDetails(accountDetails);
        });
        
        
        And("^call GetProductDetailsService using promotionCode \"([^\"]*)\" and get ProductId, PriceList$", (String productName) -> {
            System.out.println("\nGetProductDetailsService\n");
            QueryGetProductDetails queryGetProdDetails = new QueryGetProductDetails(dataStorage);
            Product product = queryGetProdDetails.getProductsDetails(productName);
            String productID = product.getID();
            String priceList = product.getPriceListId();
            System.out.println("ProductID is :"+productID +".  PriceList ID is :"+priceList);
            dataStorage.setProductId(productID);
            dataStorage.setPriceListId(priceList);
        });
        
        And("^call ApplyProductPromotionService and get quote", () -> {
            System.out.println("\nApplyProductPromotionService\n");
            String quoteNumber = getGeneratedQuoteNumber();
            System.out.println("quoteNumber="+quoteNumber);
            String dueDate = siebelDateFormat(getTomorrowsDate());
            QueryApplyProductPromotion queryApplyProductPromotion = new QueryApplyProductPromotion(dataStorage);
            Quote quote = queryApplyProductPromotion.applyProductPromotion(dataStorage.getAccountDetails(),
                    quoteNumber,
                    dueDate,
                    dataStorage.getPrimaryOrganizationId(),
                    dataStorage.getProductId(),
                    dataStorage.getPriceListId());
            Assert.assertNotNull(quote);
            dataStorage.setQuote(quote);
        });

        And("^call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote$", () -> {
            System.out.println("\nQuotingPortService\n");
            QueryQuotingWebService queryQuotingWebService = new QueryQuotingWebService(dataStorage);
            Quote updatedQuote = queryQuotingWebService.commitVirtualQuote(dataStorage.getQuote());
            dataStorage.setQuote(updatedQuote);
        });

        And("^call BeginConfigurationService using product item name \"([^\"]*)\"$", (String productItemName) -> {
            System.out.println("\nBeginConfigurationService\n");
            QueryProductConfigurator queryProductConfigurator = new QueryProductConfigurator(dataStorage);
            ListOfData listOfData = queryProductConfigurator.beginConfiguration(dataStorage.getQuote(), productItemName);
            Assert.assertNotNull("beginConfiguration returned null",listOfData);

            dataStorage.setListOfData(listOfData);


        });

        And("^call UpdateConfigurationSetAttribute to set commitment duration to \"([^\"]*)\" months for product item name \"([^\"]*)\"$", (String commitmentDuration, String productItemName) -> {
            System.out.println("Vi letar efter "+ productItemName);
            List<Item> itemList =  dataStorage.getListOfData().getProductData().getItem();
            String integrationId=null;

            for (Item item: itemList ) {
                List<Item> innerItemList = item.getItem();
                for (Item innerItem: innerItemList ) {
                    if (productItemName.equals(innerItem.getName())) {
                        integrationId = innerItem.getIntegrationId();
                    }
                }
            }

            Assert.assertNotNull("integrationId not found",integrationId);
            dataStorage.setIntegrationId(integrationId);

            QueryUpdateConfiguration queryUpdateConfiguration=new QueryUpdateConfiguration(dataStorage);
            Map<String, String> attributes = new HashMap<>() ;
            attributes.put("CommitmentDuration",commitmentDuration);
            attributes.put("CommitmentUOM","Months");
            boolean ok = queryUpdateConfiguration.setAttributes(integrationId, attributes);
            Assert.assertTrue("Update of the commitment duration failed",ok);
            dataStorage.setCommitmentDuration(commitmentDuration);
        });

        And("^call EndConfigurationService and get Quote$", () -> {
            System.out.println("\nEndConfigurationService\n");
            System.out.println("dataStorage.getIntegrationId()="+dataStorage.getIntegrationId()+"\n");
            QueryEndConfiguration queryEndConfiguration = new QueryEndConfiguration(dataStorage);
            Quote quote = queryEndConfiguration.endConfiguration();
            Assert.assertNotNull("quote from endConfiguration is null", quote);
            List<QuoteItem> quoteItemList = getFlattenedQuoteItems(quote);
            System.out.println("Length of quoteItemList="+ quoteItemList.size() + "\n");
            String commitmentDuration=null;
            outerloop: for (QuoteItem quoteItem: quoteItemList) {
                if (dataStorage.getIntegrationId().equals(quoteItem.getId())) {
                    System.out.println("found integration id in quoteitem, looking for quoteItemXA \n");
                    for(QuoteItemXA quoteItemXA : quoteItem.getListOfQuoteItemXA().getQuoteItemXA()){
                        if (quoteItemXA.getAttribute().equals("CommitmentDuration")) {
                            commitmentDuration=quoteItemXA.getValue();
                            System.out.println("in response commitmentDuration was found to be "+commitmentDuration +"\n" );
                            break outerloop;
                        }
                    }
                }
            }
            Assert.assertNotNull("commitmentDuration is null after end configuration",commitmentDuration);
            Assert.assertEquals("commitmentDuration is wrong value after end configuration",commitmentDuration, dataStorage.getCommitmentDuration());
            dataStorage.setQuote(quote);
        });


        And("^call SynchronizeQuote to populate the due date on the quote line items for a Mobile order$", () -> {
            System.out.println("\nSynchronizeQuote\n");
            Quote quote = dataStorage.getQuote();

            String dueDate = SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getTomorrowsDate());
            String dueDateHardware = SiebelDateFormat.siebelDateFormat( new Date());


            List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        quoteItem.setTSC2BDueDate(dueDate);
                        quoteItem.setRequestedDeliveryDate(dueDate);
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


        And("^call QuoteCheckOutService and get ActiveOrderID$", () -> {
            System.out.println("\nQuoteCheckOutService\n");
            QueryQuoteCheckout queryQuoteCheckout = new QueryQuoteCheckout(dataStorage);
            String activeOrderId = queryQuoteCheckout.quoteCheckout(dataStorage.getQuote().getId());
            Assert.assertNotNull("Order id is null after quoteCheckout",activeOrderId);
            dataStorage.setActiveOrderId(activeOrderId);
            System.out.println("activeOrderId="+activeOrderId);
        });
        Then("^call SubmitOrder Service and Get successful OrderID", () -> {
            System.out.println("\nSubmitOrder\n");
            QueryTSChannelSISOMBillingSubmitOrder queryTSChannelSISOMBillingSubmitOrder = new QueryTSChannelSISOMBillingSubmitOrder(dataStorage);
            String objectSpcId = queryTSChannelSISOMBillingSubmitOrder.submitOrder(dataStorage.getActiveOrderId());
            System.out.println("objectSpcId (the order id which is the same as the actibeOrderId, and that is returned from the submit order api) ="+objectSpcId);
            Assert.assertEquals("Submit order didn't work", dataStorage.getActiveOrderId() , objectSpcId);
        });


    }
}**/
