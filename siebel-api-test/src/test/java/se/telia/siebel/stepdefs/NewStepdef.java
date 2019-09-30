package se.telia.siebel.stepdefs;

import com.siebel.ordermanagement.catalog.data.productdetails.Product;
import com.siebel.ordermanagement.configurator.EndConfigurationInput;
import com.siebel.ordermanagement.configurator.cfginteractdata.DomainItem;
import com.siebel.ordermanagement.configurator.cfginteractdata.Item;
import com.siebel.ordermanagement.configurator.cfginteractdata.ListOfData;
import com.siebel.ordermanagement.configurator.cfginteractdata.Relationship;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.ordermanagement.quote.data.QuoteItemXA;
import com.siebel.ordermanagement.quote.productdata.*;

import org.junit.Assert;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import se.telia.siebel.apiquerys.*;
import se.telia.siebel.data.AccountDetails;
import se.telia.siebel.data.DataStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static se.telia.siebel.apiquerys.GenerateQuoteNumber.getGeneratedQuoteNumber;
import static se.telia.siebel.apiquerys.SiebelDateFormat.siebelDateFormat;
import static se.telia.siebel.apiquerys.SiebelDateFormat.getTomorrowsDate;
import static se.telia.siebel.apiquerys.SiebelFlattenDataStructures.getFlattenedQuoteItems;


public class NewStepdef implements En {
    DataStorage dataStorage;

	public NewStepdef(DataStorage dataStorage) {

		System.out.println("MobileStepDefs Constructor");
		this.dataStorage = dataStorage; // dataStorage is injected and contains
										// stuff that needs sharing between
										// steps
		When("^call SelfServiceUser to get primary organization id which is used in quote creation$", () -> {
			System.out.println("\nSelfServiceUser\n");
			QuerySelfServiceUser selfService = new QuerySelfServiceUser(dataStorage);
			String primaryOrgID = selfService.getPrimaryOrganizationId(dataStorage.getLoginName());
			System.out.println("Primary Organization id is :" + primaryOrgID);
			dataStorage.setPrimaryOrganizationId(primaryOrgID);
		});

		And("^call QueryCustomer using SSN \"([^\"]*)\" to get account and billing details$", (String ssn) -> {
			System.out.println("\nQueryCustomer\n");
			QueryCustomer queryCustomer = new QueryCustomer(dataStorage);
			AccountDetails accountDetails = queryCustomer.getGetAccountDetailsString(ssn);
			String AddressID = queryCustomer.getPrimaryAddressId(ssn);
			dataStorage.setPrimaryAddressId(AddressID);
			Assert.assertNotNull("accountDetails is null", accountDetails);
			System.out.println("This is the accountDetails that we got back:");
			System.out.println(accountDetails.dump());
			dataStorage.setAccountDetails(accountDetails);
		});

		And("^call SDU BB SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$",
				(String accessType) -> {
					System.out.println("\nSynchronizeQuote for BroadBand+IPTV\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.getCurentCETtime();
					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")) {
									quoteItem.setTSAccessTypeId(accessType);
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call SDU IPTV SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$",
				(String accessType) -> {
					System.out.println("\nSynchronizeQuote for IPTV\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.getCurentCETtime();

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("4") || quoteItem.getLineNumber().equals("5")
										|| quoteItem.getLineNumber().equals("6")) {
									quoteItem.setTSAccessTypeId(accessType);
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call SDU BBIPTV SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$",
				(String accessType) -> {
					System.out.println("\nSynchronizeQuote for BroadBand+IPTV\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.getCurentCETtime();

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("7")) {
									quoteItem.setTSAccessTypeId(accessType);
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call SDU BBVOIP SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$",
				(String accessType) -> {
					System.out.println("\nSynchronizeQuote for BroadBand+IPTV\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.getCETtime();

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("5")
										|| quoteItem.getLineNumber().equals("7")) {
									quoteItem.setTSAccessTypeId(accessType);

								}
								if (quoteItem.getName().equals("VoIP SE Service bundle_1606")
										|| quoteItem.getName().equals("VoIP SE_B2B Service bundle_1649")) {
									quoteItem.setTSVoIPDeviceType("RGW");
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call BB xDSLSynchronizeQuote to populate the AccessCode \"([^\"]*)\" and ConnRef number \"([^\"]*)\" on the quote line items for a xDSL order$",
				(String accessType, String fbNumber) -> {
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

								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("5")) {
									quoteItem.setTSAccessTypeId(accessType);
									quoteItem.setConnectivityReferenceNumber(fbNumber);
								}
								if (quoteItem.getLineNumber().equals("5")) {
									quoteItem.setRequestedDeliveryDate(BBDueDate);

								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call BBVOIPIPTV xDSLSynchronizeQuote to populate the AccessCode \"([^\"]*)\" and ConnRef number \"([^\"]*)\" on the quote line items for a xDSL order$",
				(String accessType, String fbNumber) -> {
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

								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("5")
										|| quoteItem.getLineNumber().equals("7")
										|| quoteItem.getLineNumber().equals("8")
										|| quoteItem.getLineNumber().equals("9")
										|| quoteItem.getLineNumber().equals("11")) {
									quoteItem.setTSAccessTypeId(accessType);
									quoteItem.setConnectivityReferenceNumber(fbNumber);
								}
								if (quoteItem.getLineNumber().equals("3")) {
									quoteItem.setRequestedDeliveryDate(BBDueDate);
								}
								if (quoteItem.getName().equals("VoIP SE Service bundle_1606")
										|| quoteItem.getName().equals("VoIP SE_B2B Service bundle_1649")) {
									quoteItem.setTSVoIPDeviceType("RGW");
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call BBVOIPIPTV SynchronizeQuote to populate the deliveryContract \"([^\"]*)\" and AccessCode \"([^\"]*)\" on the quote line items for a MDU order$",
				(String Agreement, String AccessCode) -> {
					System.out.println("\nSynchronizeQuote for BroadBand\n");
					Quote quote = dataStorage.getQuote();
					// String dueDate =
					// SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getTomorrowsDate());
					String dueDate = SiebelDateFormat.getCETtime();
					String dueDateHardware = SiebelDateFormat.siebelDateFormat(new Date());

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								// if(quoteItem.getLineNumber().equals("2")){
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("7")
										|| quoteItem.getLineNumber().equals("8")
										|| quoteItem.getLineNumber().equals("9")) {
									// quoteItem.setTSDeliveryContractId(Agreement);
									quoteItem.setTSMDUDeliveryContractNum(Agreement);
									quoteItem.setTSAccessTypeId(AccessCode);
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
								quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
								System.out.println("Setting TSColtDeliveryFromDate to " + dueDateHardware);
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});
		
		 And("^call BBIPTV SynchronizeQuote to populate the deliveryContract \"([^\"]*)\" Revision \"([^\"]*)\" RowID \"([^\"]*)\" and AccessCode \"([^\"]*)\" on the quote line items for a MDU order$", (String Agreement,String RevisionNumber,String RowID,String AccessCode) -> {
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
//	                    	   quoteItem.setTSDeliveryContractId("1-1658X79");1-16891A1
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

		And("^call BBVOIPIPTV SynchronizeQuote to populate the deliveryContract \"([^\"]*)\" Revision \"([^\"]*)\" RowID \"([^\"]*)\" and AccessCode \"([^\"]*)\" on the quote line items for a MDU order$",
				(String Agreement, String RevisionNumber, String RowID, String AccessCode) -> {
					System.out.println("\nSynchronizeQuote for BroadBandIPTVMDU\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getTomorrowsDate());
					String dueDateHardware = SiebelDateFormat.siebelDateFormat(new Date());

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("7")
										|| quoteItem.getLineNumber().equals("8")
										|| quoteItem.getLineNumber().equals("9")
										|| quoteItem.getLineNumber().equals("11")) {
									// quoteItem.setTSDeliveryContractId("1-1658X79");1-16891A1
									quoteItem.setTSDeliveryContractId(RowID);

									quoteItem.setTSMDUDeliveryContractNum(Agreement);
									quoteItem.setTSMDUDeliveryContractRevNum(RevisionNumber);

									quoteItem.setTSAccessTypeId(AccessCode);

								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
								quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
								System.out.println("Setting TSColtDeliveryFromDate to " + dueDateHardware);
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call SDU BBIPTV_VOIP SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$",
				(String accessType) -> {
					System.out.println("\nSynchronizeQuote for BroadBand+IPTV\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.getCETtime();

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("7")
										|| quoteItem.getLineNumber().equals("9")
										|| quoteItem.getLineNumber().equals("8")
										|| quoteItem.getLineNumber().equals("11")) {
									quoteItem.setTSAccessTypeId(accessType);
								}
								if (quoteItem.getName().equals("VoIP SE Service bundle_1606")
										|| quoteItem.getName().equals("VoIP SE_B2B Service bundle_1649")) {
									quoteItem.setTSVoIPDeviceType("RGW");
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});
		
	
		And("^call QuerySelfServiceAddress Add/Update for XDSL format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode \"([^\"]*)\"$",
				(String AddressData) -> {
					System.out.println("\nQuerySelfServiceAccount\n");
					Map<String, String> AddressMap = new HashMap<>();
					String[] AddressParam = AddressData.split(";");
					AddressMap.put("StreetAddress", AddressParam[0]);
					AddressMap.put("StreetAddress2", AddressParam[1]);
					AddressMap.put("Entrance", AddressParam[2]);
					AddressMap.put("City", AddressParam[3]);
					AddressMap.put("ApartmentNum", AddressParam[4]);
					AddressMap.put("PostalCode", AddressParam[6]);
					AddressMap.put("PointId", AddressParam[5]);
					QuerySelfServiceAddressQueryPage querySelfServiceAddress = new QuerySelfServiceAddressQueryPage(
							dataStorage);
					querySelfServiceAddress.SiebelCheckExistingAddress(AddressMap, "2");
				});

		And("^call GetProductDetailsService using promotionCode \"([^\"]*)\" and get ProductId, PriceList$",
				(String productName) -> {
					System.out.println("\nGetProductDetailsService\n");
					QueryGetProductDetails queryGetProdDetails = new QueryGetProductDetails(dataStorage);
					Product product = queryGetProdDetails.getProductsDetails(productName);
					String productID = product.getID();
					String priceList = product.getPriceListId();
					// String IntergrationID=product.getIntegrationId();
					System.out.println("ProductID is :" + productID + ".  PriceList ID is :" + priceList);
					// System.out.println("ProductID is :"+productID +".
					// Integration ID is :"+IntergrationID);
					dataStorage.setProductId(productID);
					dataStorage.setPriceListId(priceList);
				});

		And("^call ApplyProductPromotionService and get quote", () -> {
			System.out.println("\nApplyProductPromotionService\n");

			String quoteNumber = getGeneratedQuoteNumber();
			System.out.println("quoteNumber=" + quoteNumber);
			String dueDate = SiebelDateFormat.getCETtime();
			QueryApplyProductPromotion queryApplyProductPromotion = new QueryApplyProductPromotion(dataStorage);
			Quote quote = queryApplyProductPromotion.applyProductPromotion(dataStorage.getAccountDetails(), quoteNumber,
					dueDate, dataStorage.getPrimaryOrganizationId(), dataStorage.getProductId(),
					dataStorage.getPriceListId());
			Assert.assertNotNull(quote);
			dataStorage.setQuote(quote);
		});

		And("^call QuoteAddItems and get quote", () -> {
			System.out.println("\nQuoteAdditems\n");

			String ProductId = dataStorage.getProductId();
			QuoteAddItem quoteAddItem = new QuoteAddItem(dataStorage);
			Quote quote = quoteAddItem.quoteAddItems(dataStorage.getQuote(), ProductId);

			String quoteId = dataStorage.getActiveQuoteId();
			dataStorage.setQuote(quote);

		});

		And("^call Addproduct to set RelationShipName \"([^\"]*)\" and Package \"([^\"]*)\"",
				(String RelationshipName, String PackageName) -> {
					System.out.println("Inside Add product");
					List<Item> itemList = dataStorage.getListOfData().getProductData().getItem();

					String RelationShipID = null, ProductID = null;
					for (Item item : itemList) {
						List<Relationship> RelationList = item.getRelationship();
						for (Relationship relation : RelationList) {
							if (relation.getName().equalsIgnoreCase(RelationshipName)) {
								System.out.println("PRINTRELATIONSHIPID:" + relation.getId());
								RelationShipID = relation.getId();
								List<DomainItem> packagename = relation.getDomainItem();
								for (DomainItem speed : packagename) {
									if (speed.getName().equalsIgnoreCase(PackageName)) {
										ProductID = speed.getId();
										System.out.println("PRINTProductID:" + ProductID);
										break;
									}
								}

							}
							// else
							// if(relation.getName().equalsIgnoreCase(PackageName)){
							// System.out.println("Package:" +
							// relation.getName());
							// ProductID = relation.getName();
							// }
						}

						String innerItemList = item.getIntegrationId();
						System.out.println("Addproduct IntID:" + innerItemList);
						if (innerItemList.length() > 1) {
							dataStorage.setIntegrationId(innerItemList);
							break;
						}
					}
					String integrationId = dataStorage.getIntegrationId();
					System.out.println("Add item int" + integrationId);
					QueryUpdateConfiguration queryUpdateConfiguration = new QueryUpdateConfiguration(dataStorage);
					Map<String, String> attributes = new HashMap<>();
					boolean ok = queryUpdateConfiguration.AddItem(integrationId, RelationShipID, ProductID);
					// Assert.assertTrue("Update of the commitment duration
					// failed", ok);
				});

		And("^call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote$", () -> {
			System.out.println("\nQuotingPortService\n");

			QueryQuotingWebService queryQuotingWebService = new QueryQuotingWebService(dataStorage);
			Quote updatedQuote = queryQuotingWebService.commitVirtualQuote(dataStorage.getQuote());
			dataStorage.setQuote(updatedQuote);
			List<QuoteItem> quoteitems = updatedQuote.getListOfQuoteItem().getQuoteItem();
			System.out.println("The number of quoteitems" + quoteitems.size());
			for (QuoteItem quoteitem : quoteitems) {
				String innerItemList = quoteitem.getRootAssetIntegrationId();
				System.out.println("ExecuteQuote" + innerItemList);
				if (innerItemList.length() > 1) {
					dataStorage.setIntegrationId(innerItemList);
					break;
				}
			}

		});

		And("^call BeginConfigurationService using product item name \"([^\"]*)\"$", (String productItemName) -> {
			System.out.println("\nBeginConfigurationService\n");
			String ActiveQuoteID=dataStorage.getQuote().getId();
			dataStorage.setActiveQuoteId(ActiveQuoteID);
			QueryProductConfigurator queryProductConfigurator = new QueryProductConfigurator(dataStorage);
			ListOfData listOfData = queryProductConfigurator.beginConfiguration(dataStorage.getQuote(),
					productItemName);
			// Assert.assertNotNull("beginConfiguration returned
			// null",listOfData);
			dataStorage.setListOfData(listOfData);

			String quoteId = dataStorage.getActiveQuoteId();
			// dataStorage.setIntegrationId(dataStorage.getIntegrationId());

		});

		And("^call UpdateConfigurationSetAttribute to set commitment duration to \"([^\"]*)\" months for product item name \"([^\"]*)\"$",
				(String commitmentDuration, String productItemName) -> {
					System.out.println("Vi letar efter " + productItemName);
					List<Item> itemList = dataStorage.getListOfData().getProductData().getItem();
					String integrationId = null;

					for (Item item : itemList) {
						List<Item> innerItemList = item.getItem();
						for (Item innerItem : innerItemList) {
							if (productItemName.equals(innerItem.getName())) {
								integrationId = innerItem.getIntegrationId();
							}
						}
					}
					Assert.assertNotNull("integrationId not found", integrationId);

					QueryUpdateConfiguration queryUpdateConfiguration = new QueryUpdateConfiguration(dataStorage);
					Map<String, String> attributes = new HashMap<>();
					attributes.put("CommitmentDuration", commitmentDuration);
					attributes.put("CommitmentUOM", "Months");
					boolean ok = queryUpdateConfiguration.setAttributes(integrationId, attributes);
					Assert.assertTrue("Update of the commitment duration failed", ok);
					// dataStorage.setQty(qty);
				});

		And("^call EndConfigurationService and get Quote$", () -> {
			System.out.println("\nEndConfigurationService\n");
			System.out.println("dataStorage.getIntegrationId()=" + dataStorage.getIntegrationId() + "\n");
			QueryEndConfiguration queryEndConfiguration = new QueryEndConfiguration(dataStorage);
			Quote quote = queryEndConfiguration.endConfiguration();
			Assert.assertNotNull("quote from endConfiguration is null", quote);
			List<QuoteItem> quoteItemList = getFlattenedQuoteItems(quote);
			System.out.println("Length of quoteItemList=" + quoteItemList.size() + "\n");
			String commitmentDuration = null;
			outerloop: for (QuoteItem quoteItem : quoteItemList) {
				if (dataStorage.getIntegrationId().equals(quoteItem.getId())) {
					System.out.println("found integration id in quoteitem, looking for quoteItemXA \n");
					for (QuoteItemXA quoteItemXA : quoteItem.getListOfQuoteItemXA().getQuoteItemXA()) {
						if (quoteItemXA.getAttribute().equals("CommitmentDuration")) {
							commitmentDuration = quoteItemXA.getValue();
							System.out.println(
									"in response commitmentDuration was found to be " + commitmentDuration + "\n");
							break outerloop;
						}
					}
				}
			}
			// Assert.assertNotNull("commitmentDuration is null after end
			// configuration",commitmentDuration);
			// Assert.assertEquals("commitmentDuration is wrong value after end
			// configuration",commitmentDuration,
			// dataStorage.getCommitmentDuration());
			dataStorage.setQuote(quote);
		});

		

		And("^call SynchronizeQuote to populate the AccessCode \"([^\"]*)\" on the quote line items for a SDU order$",
				(String accessType) -> {
					System.out.println("\nSynchronizeQuote for BroadBand\n");
					Quote quote = dataStorage.getQuote();
					// String dueDate =
					// SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getCurentCETtime()).trim();
					String dueDate;
					try {
						dueDate = SiebelDateFormat.siebelDateFormat(SiebelDateFormat.converteddate()).trim();

						String dueDateHardware = SiebelDateFormat.siebelDateFormat(new Date());

						List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
						quoteItemList.stream()
								.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
								.forEach(quoteItem -> {
									quoteItem.setTSC2BDueDate(dueDate);
									quoteItem.setRequestedDeliveryDate(dueDate);
									if (quoteItem.getLineNumber().equals("2")
											|| quoteItem.getLineNumber().equals("3")) {
										quoteItem.setTSAccessTypeId(accessType);
									}

								});
						quoteItemList.stream()
								.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
								.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
									quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
									System.out.println("Setting TSColtDeliveryFromDate to " + dueDateHardware);
								});

						QueryQuote queryQuote = new QueryQuote(dataStorage);
						boolean result = queryQuote.updateQuote(quote);
						Assert.assertTrue("No id from synchronizeQuoteOutput", result);
						System.out.println("synchronizeQuoteOutput OK");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

		And("^call xDSLSynchronizeQuote to populate the AccessCode \"([^\"]*)\" and ConnRef number \"([^\"]*)\" on the quote line items for a xDSL order$",
				(String accessType, String fbNumber) -> {
					System.out.println("\nSynchronizeQuote for BroadBand\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getTomorrowsDate());
					String dueDateHardware = SiebelDateFormat.siebelDateFormat(new Date());
					String BBDueDate = SiebelDateFormat.BBDueDate();

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("10")) {
									quoteItem.setTSAccessTypeId(accessType);
									quoteItem.setConnectivityReferenceNumber(fbNumber);
								}
								if (quoteItem.getLineNumber().equals("5") || quoteItem.getLineNumber().equals("3")) {
									quoteItem.setRequestedDeliveryDate(BBDueDate);
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
								quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
								System.out.println("Setting TSColtDeliveryFromDate to " + dueDateHardware);
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call xDSL DataPrep SynchronizeQuote to populate the AccessCode \"([^\"]*)\" PSTN \"([^\"]*)\" and ConnRef number \"([^\"]*)\" on the quote line items for a xDSL order$",
				(String AccessCode, String PSTN, String FbNumber) -> {
					System.out.println("\nSynchronizeQuote for BroadBand\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getTomorrowsDate());
					String dueDateHardware = SiebelDateFormat.siebelDateFormat(new Date());
					String BBDueDate = SiebelDateFormat.BBDueDate();

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2") || quoteItem.getLineNumber().equals("3")
										|| quoteItem.getLineNumber().equals("7")
										|| quoteItem.getLineNumber().equals("9")) {
									quoteItem.setTSAccessTypeId(AccessCode);
									quoteItem.setConnectivityReferenceNumber(FbNumber);
									quoteItem.setTSPSTNNumber(PSTN);

								}
								if (quoteItem.getLineNumber().equals("3") || quoteItem.getLineNumber().equals("5")) {
									quoteItem.setRequestedDeliveryDate(BBDueDate);

								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
								quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
								System.out.println("Setting TSColtDeliveryFromDate to " + dueDateHardware);
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call SynchronizeQuote to populate the deliveryContract \"([^\"]*)\" on the quote line items for a MDU order$",
				(String Agreement) -> {
					System.out.println("\nSynchronizeQuote for BroadBand\n");
					Quote quote = dataStorage.getQuote();
					String dueDate = SiebelDateFormat.siebelDateFormat(SiebelDateFormat.getTomorrowsDate());
					String dueDateHardware = SiebelDateFormat.siebelDateFormat(new Date());

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								quoteItem.setTSC2BDueDate(dueDate);
								quoteItem.setRequestedDeliveryDate(dueDate);
								if (quoteItem.getLineNumber().equals("2")) {
									// quoteItem.setTSDeliveryContractId(Agreement);
									quoteItem.setTSMDUDeliveryContractNum(Agreement);
									quoteItem.setTSAccessTypeId("300");
								}

							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
								quoteItem.setTSColtDeliveryFromDate(dueDateHardware);
								System.out.println("Setting TSColtDeliveryFromDate to " + dueDateHardware);
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call Addproduct to set RelationShipID \"([^\"]*)\" and ProductID \"([^\"]*)\"",
				(String RelationShipID, String ProductID) -> {/*
					System.out.println("Inside Add product");
					List<Item> itemList = dataStorage.getListOfData().getProductData().getItem();
					for (Item item : itemList) {
						String innerItemList = item.getIntegrationId();
						System.out.println("Addproduct IntID:" + innerItemList);
						if (innerItemList.length() > 1) {
							dataStorage.setIntegrationId(innerItemList);
							break;
						}
					}
					// List<Item> itemList =
					// dataStorage.getListOfData().getProductData().getItem();
					// System.out.println(itemList.get(0));
					// String productItemName="Service Bundle-IA-1703-1";
					// String integrationId=null;
					// for (Item item: itemList ) {
					// List<Item> innerItemList = item.getItem();
					// for (Item innerItem: innerItemList ) {
					// System.out.println("Itemlist"+innerItem.getName());
					// if (productItemName.equals(innerItem.getName())) {
					// integrationId = innerItem.getIntegrationId();
					// }
					// }
					// }
					// Assert.assertNotNull("integrationId not
					// found",integrationId);
					String integrationId = dataStorage.getIntegrationId();
					System.out.println("Add item int" + integrationId);
					// System.out.println("Int ID:"+integrationId);
					QueryUpdateConfiguration queryUpdateConfiguration = new QueryUpdateConfiguration(dataStorage);
					Map<String, String> attributes = new HashMap<>();
					// attributes.put("RelationshipId",RelationShipID);
					// attributes.put("AddProductId",ProductID);
					boolean ok = queryUpdateConfiguration.AddItem(integrationId, RelationShipID, ProductID);
					Assert.assertTrue("Update of the commitment duration failed", ok);
					// dataStorage.setQty(qty);
				*/});

	}
}
