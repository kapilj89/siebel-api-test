package se.telia.siebel.stepdefs;




import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.siebel.ordermanagement.configurator.cfginteractdata.DomainItem;
import com.siebel.ordermanagement.configurator.cfginteractdata.Item;
import com.siebel.ordermanagement.configurator.cfginteractdata.Relationship;
import com.siebel.ordermanagement.order.data.Order;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.xml.asset_management_complex_io.data.AssetMgmtAssetHeaderData;
import cucumber.api.DataTable;

import cucumber.api.java8.En;

import org.junit.Assert;

import se.telia.siebel.apiquerys.*;
import se.telia.siebel.data.DataStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static se.telia.siebel.apiquerys.GenerateQuoteNumber.getGeneratedQuoteNumber;


public class MoveStepDefs implements En {
    DataStorage dataStorage;
    String  ASSETINTID;

    public MoveStepDefs(DataStorage dataStorage) {
        System.out.println("MoveStepDefs Constructor");
        this.dataStorage = dataStorage; // dataStorage is injected and contains stuff that needs sharing between steps


        Given("^call QueryAsset using SSN \"([^\"]*)\" to get asset details AssetNumber and ServiceAccountId for promotionName \"([^\"]*)\"$", (String ssn, String promotionName) -> {
        	QueryAsset queryAsset = new QueryAsset(dataStorage);
            AssetMgmtAssetHeaderData assetMgmtAssetHeaderData = queryAsset.getAssetMgmtAssetHeaderData(ssn, promotionName);
            Assert.assertNotNull("assetMgmtAssetHeaderData is null after getAssetMgmtAssetHeaderData", assetMgmtAssetHeaderData);
            String assetNumber = assetMgmtAssetHeaderData.getAssetNumber();
            String serviceAccountId = assetMgmtAssetHeaderData.getServiceAccountId();
            String ProductId=  assetMgmtAssetHeaderData.getProductId();
            
            Assert.assertNotNull("AssetNumber is null", assetNumber);
            Assert.assertNotNull("ServiceAccountId is null", serviceAccountId);
            dataStorage.setAssetNumber(assetNumber);
            dataStorage.setServiceAccountId(serviceAccountId);
            dataStorage.setProductId(ProductId);
            System.out.println("assetNumber=" + assetNumber);
            System.out.println("serviceAccountId=" + serviceAccountId);	
        });

        When("^call QueryCustomer using SSN \"([^\"]*)\" to get PrimaryAddressId$", (String ssn) -> {
            QueryCustomer queryCustomer = new QueryCustomer(dataStorage) ;
            String primaryAddressId = queryCustomer.getPrimaryAddressId(ssn);
            System.out.println("primaryAddress="+primaryAddressId);
            Assert.assertNotNull(primaryAddressId);
            dataStorage.setPrimaryAddressId(primaryAddressId);
        });

        Then("^call CheckMoveInAddress using MoveInPointId to validate the existence of move-in address and call AssociateMoveInAddress or CreateMoveInAddress if necessary$", (DataTable dataTable) -> {
            // This step doesn't actually belong in the test case.
            // For move to work the move in address needs to
            // 1) Exists in Siebel
            // 2) Be associated with the service account.
            // This step check and fix that if necessary
            Map<String,String> moveInAddressMap=dataTable.asMap(String.class,String.class);
            System.out.println("moveInAddressMap:");
            for(String k : moveInAddressMap.keySet()){
                System.out.println(k +" : "+moveInAddressMap.get(k));
            }
            dataStorage.setMoveInAddressMap(moveInAddressMap); // This is needed later in the test, so save it in dataStorage
            QueryCheckMoveInAddress queryCheckMoveInAddress = new QueryCheckMoveInAddress(dataStorage);

            try {
                queryCheckMoveInAddress.checkMoveInAddress(moveInAddressMap);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Exception in checkMoveInAddress "+ e.getMessage());
            }
        });


        When("^call MoveModifyAssetToQuote to create move quote using AssetNumber and ServiceAccountId$", () -> {
            String quoteNumber = getGeneratedQuoteNumber();
            System.out.println("quoteNumber="+quoteNumber);
            QueryMoveModifyAssetToQuote queryMoveModifyAssetToQuote = new QueryMoveModifyAssetToQuote(dataStorage);
            queryMoveModifyAssetToQuote.moveModifyAssetToQuote(quoteNumber);

        });
        When("^call ModifyAssetToQuote for the RelationshipName \"([^\"]*)\" and Package \"([^\"]*)\" to \"([^\"]*)\"", (String Productname,String ExistingSpeed,String ModifiedSpeed) -> {
        	System.out.println("ModifyAssetToQuote");
        	String quoteNumber = getGeneratedQuoteNumber();
            System.out.println("quoteNumber="+quoteNumber);
            QueryModifyAssetToQuote queryModifyAssetToQuote = new QueryModifyAssetToQuote(dataStorage);
            queryModifyAssetToQuote.modifyAssetToQuote(quoteNumber,ExistingSpeed,ModifiedSpeed);
           
          
        
        });
        
        When("^call QueryQuote to fetch the quote from database$", () -> {
 	          	
            QueryQuote queryQuote = new QueryQuote(dataStorage);
            List<Quote> quoteList = queryQuote.getQuoteById();
            System.out.println("quote.size()="+quoteList.size());
            Assert.assertTrue("Wrong number of quotes in the list: "+quoteList.size(),quoteList.size()==1);          
            dataStorage.setQuote(quoteList.get(0));
            System.out.println("The quote is now in the dataStorage. id="+quoteList.get(0).getId());
            
            
        });
        
        And("^call ModifyProduct to set RelationShipName \"([^\"]*)\" and Package \"([^\"]*)\" to \"([^\"]*)\"",
				(String RelationshipName, String PackageName,String PackageName2) -> {
					System.out.println("Inside Modify product");
					
					String RelationShipID = null,ProductID = null,ModifiedProductID=null;
//					DataStorage integrationId = null;

					List<Item> itemList = dataStorage.getListOfData().getProductData().getItem();
					ListOfQuote listofQuote=dataStorage.getListofQuote();
		        	 
					for (Item item : itemList) {
						List<Relationship> RelationList = item.getRelationship();
						for (Relationship relation : RelationList) {
							if (relation.getName().equalsIgnoreCase(RelationshipName)) {
								System.out.println("PRINTRELATIONSHIPID:" + relation.getId());
								RelationShipID = relation.getId();
								List<DomainItem> packagename = relation.getDomainItem();
								for(DomainItem speed:packagename){
									if(speed.getName().equalsIgnoreCase(PackageName)){
										ProductID=speed.getId();
										
										System.out.println("PRINTProductID:" + ProductID);
//										break;
									}else if(speed.getName().equalsIgnoreCase(PackageName2)){
										ModifiedProductID=speed.getId();
										System.out.println("PRINTProductIDtobe modified:" + ModifiedProductID);
										
									}
								}
								
							}
						}
					}
//					String integrationId = "1-WJ67CX";
//					String integrationId = ASSETINTID;
					String integrationId = dataStorage.getIntegrationId();
					System.out.println("Add item int" + integrationId);
					QueryUpdateConfiguration queryUpdateConfiguration = new QueryUpdateConfiguration(dataStorage);
					Map<String, String> attributes = new HashMap<>();
					boolean ok = queryUpdateConfiguration.UpdateItem(integrationId, RelationShipID, ProductID,ModifiedProductID);
					Assert.assertTrue("Modified Successfully", ok);
				});



       

        When("^call SynchronizeQuote to populate the due date on the quote line items for a Move order$", () -> {
            Quote quote = dataStorage.getQuote();

            String moveInDate = SiebelDateFormat.siebelDateFormat(dataStorage.getMoveInAddressMap().get("MoveInDate"));
            String moveOutDate = SiebelDateFormat.siebelDateFormat(dataStorage.getMoveInAddressMap().get("MoveOutDate"));

            ListOfQuoteItem listOfQuoteItem = quote.getListOfQuoteItem();
            List<QuoteItem> quoteItemList = listOfQuoteItem.getQuoteItem();

            System.out.println("Before setting dates on the quoteItems");
            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                        System.out.println("before: " + quoteItem.getTSMoveOutDate());
                        quoteItem.setTSMoveOutDate(moveOutDate);
                        quoteItem.setTSC2BDueDate(moveInDate);
                        quoteItem.setRequestedDeliveryDate(moveInDate);
                        System.out.println("after: " + quoteItem.getTSMoveOutDate());
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            queryQuote.updateQuote(quote);
        });
        When("^call SynchronizeQuote for a Modify order$", () -> {
            Quote quote = dataStorage.getQuote();


            ListOfQuoteItem listOfQuoteItem = quote.getListOfQuoteItem();
            List<QuoteItem> quoteItemList = listOfQuoteItem.getQuoteItem();

            quoteItemList.stream()
                    .filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
                    .forEach(quoteItem -> {
                    });

            QueryQuote queryQuote = new QueryQuote(dataStorage);
            queryQuote.updateQuote(quote);
        });

        
        When("^call QuoteCheckOut to convert the quote into a move order$", () -> {
            QueryQuoteCheckout queryQuoteCheckout = new QueryQuoteCheckout(dataStorage);
            String activeOrderId = queryQuoteCheckout.quoteCheckout(dataStorage.getActiveQuoteId());
            Assert.assertNotNull("Order id is null after quoteCheckout",activeOrderId);
            dataStorage.setActiveOrderId(activeOrderId);
            System.out.println("activeOrderId="+activeOrderId);
        });

        And("^call GetOrder to get the order and assert orderDueDate and moveOutDate$", () -> {
            QueryOrder queryOrder = new QueryOrder(dataStorage);
            Order order = queryOrder.getOrderById(dataStorage.getActiveOrderId());
            String expectedDate=dataStorage.getMoveInAddressMap().get("MoveOutDate");
            expectedDate=SiebelDateFormat.siebelDateFormat(expectedDate);
            Assert.assertEquals( "RequestedShipDate is not equal to MoveOutDate", expectedDate, order.getRequestedShipDate());
            dataStorage.setOrder(order);
        });

        When("^call TSChannelSubmitOrder to submit the order$", () -> {
        	
        	System.out.println("\nSubmitOrder\n");
            QueryTSChannelSISOMBillingSubmitOrder queryTSChannelSISOMBillingSubmitOrder = new QueryTSChannelSISOMBillingSubmitOrder(dataStorage);
            String objectSpcId = queryTSChannelSISOMBillingSubmitOrder.submitOrder(dataStorage.getActiveOrderId());
            System.out.println("objectSpcId (the order id which is the same as the actibeOrderId, and that is returned from the submit order api) ="+objectSpcId);
            Assert.assertEquals("Submit order didn't work", dataStorage.getActiveOrderId() , objectSpcId);
        });

        Then("^call CheckOrderStatus to verify the order status to be \"([^\"]*)\"$", (String expectedStatus) -> {
            boolean correctStatus = isOrderInStatus(expectedStatus);
            System.out.println("is order in correct status? "+ correctStatus);
            Assert.assertTrue("Order is not in status: '"+expectedStatus+"'", correctStatus);
        });

        Then("^test data is reset after move order$", () -> {/*
            QuerySWICancelSpcSalesSpcOrder querySWICancelSpcSalesSpcOrder = new QuerySWICancelSpcSalesSpcOrder(dataStorage);
            boolean cancelOk=querySWICancelSpcSalesSpcOrder.cancelOrder(dataStorage.getActiveOrderId());
            Assert.assertTrue("The cancelation of the order went wrong", cancelOk);
            System.out.println("Order was cancelled OK.");
        */});

    }

    private boolean isOrderInStatus(String expectedStatus) {
        QueryOrder queryOrder = new QueryOrder(dataStorage);
        // Now wait for the cancellation to get through
        int retry=0;
        String status;
        do {
            System.out.println("Waiting for orderStatus to become '"+expectedStatus+"', retry: "+retry);
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Order order = queryOrder.getOrderById(dataStorage.getActiveOrderId());
            status = order.getStatus();
            System.out.println("OrderStatus is now " + status);
            ++retry;
        }
        while (! expectedStatus.equals(status) && retry < 12);
        return expectedStatus.equalsIgnoreCase(status);
    }

}
