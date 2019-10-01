package se.telia.siebel.stepdefs;

import com.siebel.ordermanagement.configurator.cfginteractdata.DomainItem;
import com.siebel.ordermanagement.configurator.cfginteractdata.Item;
import com.siebel.ordermanagement.configurator.cfginteractdata.ListOfData;
import com.siebel.ordermanagement.configurator.cfginteractdata.Relationship;
import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.ordermanagement.quote.data.QuoteItemXA;
import cucumber.api.java8.En;
import org.junit.Assert;
import se.telia.siebel.apiquerys.*;
import se.telia.siebel.data.DataStorage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static se.telia.siebel.apiquerys.GenerateQuoteNumber.getGeneratedQuoteNumber;
import static se.telia.siebel.apiquerys.SiebelFlattenDataStructures.getFlattenedQuoteItems;

public class MoveStepDefs implements En {
	DataStorage dataStorage;
	public  Map<String, String> AssetHolder = new HashMap<>();
	final Map<String, ListOfData> StoreServiceBundle = new HashMap<>();
	final Map<String, Quote> StoreQuote = new HashMap<>();
	final Map<String, String> IntegrationID = new HashMap<>();

	public MoveStepDefs(DataStorage dataStorage) {
		System.out.println("SanityMoveStepDefs Constructor");
		this.dataStorage = dataStorage; // dataStorage is injected and contains
		// stuff that needs sharing between
		// steps

		And("^call QuerySelfServiceAccount to add end date$", () -> {
			System.out.println("\nAddEndDate\n");
			QuerySelfServiceAccount querySelfServiceAddress = new QuerySelfServiceAccount(dataStorage);
			querySelfServiceAddress.AddEndDateInExistingAddress();
		});

		  When("^call QueryCustomer using SSN \"([^\"]*)\" to get PrimaryAddressId$", (String ssn) -> {
	            QueryCustomer queryCustomer = new QueryCustomer(dataStorage) ;
	            String primaryAddressId = queryCustomer.getPrimaryAddressId(ssn);
	            System.out.println("primaryAddress="+primaryAddressId);
	            Assert.assertNotNull(primaryAddressId);
	            dataStorage.setPrimaryAddressId(primaryAddressId);
	        });

		When("^call MoveModifyAssetToQuote to create move quote using AssetNumber and ServiceAccountId$", () -> {
			String quoteNumber = getGeneratedQuoteNumber();
			System.out.println("quoteNumber=" + quoteNumber);
            System.out.println("AssetHolder Size :"+ dataStorage.getAssetHolder().size());
			for (int i = 0; i < dataStorage.getAssetHolder().size(); i++) {
				String Asset = dataStorage.getAssetHolder().get("AssetNumber" + i);
				if (Asset == null) {
					System.out.println("NO SUCH ASSET");
				} else {
					QueryMoveModifyAssetToQuote queryMoveModifyAssetToQuote = new QueryMoveModifyAssetToQuote(
							dataStorage);
					queryMoveModifyAssetToQuote.moveModifyAssetToQuote(quoteNumber, Asset);

				}
			}
		});

		

		

		When("^call SynchronizeQuote for a Move order$", () -> {
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
		
		And("^check \"([^\"]*)\" and Move all exsiting Asset with help of product item name \"([^\"]*)\" RelationShipName \"([^\"]*)\" and Package \"([^\"]*)\" to \"([^\"]*)\"$",
				(String flag, String ServiceBundle, String RelationshipName, String BasePackage, String NewPackage) -> {

					if (flag.equalsIgnoreCase("N")) {
						System.out.println("\nQueryQuote\n");
						QueryQuote queryQuote = new QueryQuote(dataStorage);
						List<Quote> quoteList = queryQuote.getQuoteById();
						System.out.println("quote.size()=" + quoteList.size());
						Assert.assertTrue("Wrong number of quotes in the list: " + quoteList.size(),
								quoteList.size() == 1);
						dataStorage.setQuote(quoteList.get(0));
						System.out.println("The quote is now in the dataStorage. id=" + quoteList.get(0).getId());

						System.out.println("\nSKIP PRODUCT CUSTOMIZE\n");

					} else {
						List<String> Products = new ArrayList<String>(Arrays.asList(ServiceBundle.split(";")));
						List<String> Realtions = new ArrayList<String>(Arrays.asList(RelationshipName.split(";")));
						List<String> ExsitingSpeed = new ArrayList<String>(Arrays.asList(BasePackage.split(";")));
						List<String> NewSpeed = new ArrayList<String>(Arrays.asList(NewPackage.split(";")));

						for (int i = 0; i < Products.size(); i++) {

							System.out.println("\nQueryQuote\n");
							QueryQuote queryQuote = new QueryQuote(dataStorage);
							List<Quote> quoteList = queryQuote.getQuoteById();
							System.out.println("quote.size()=" + quoteList.size());
							Assert.assertTrue("Wrong number of quotes in the list: " + quoteList.size(),
									quoteList.size() == 1);
							dataStorage.setQuote(quoteList.get(0));
							StoreQuote.put("Quote" + i, dataStorage.getQuote());
							System.out.println("The quote is now in the dataStorage. id=" + quoteList.get(0).getId());
							// end Query Config

							System.out.println("BundleName: " + Products.get(i));
							String relationshipName = Realtions.get(i);
							System.out.println("RelationshipName: " + relationshipName);
							String BaseItem = ExsitingSpeed.get(i);
							System.out.println("ExsitingSpeed: " + BaseItem);
							String NewItem = NewSpeed.get(i);
							System.out.println("NewSpeed: " + NewItem);

							System.out.println("\nBeginConfigurationService\n");
							QueryProductConfigurator queryProductConfigurator = new QueryProductConfigurator(
									dataStorage);
							ListOfData listOfData = queryProductConfigurator
									.beginConfiguration(StoreQuote.get("Quote" + i), Products.get(i));
							Assert.assertNotNull("beginConfiguration returned null", listOfData);
							StoreServiceBundle.put("ListOf " + i, listOfData);
							dataStorage.setListOfData(listOfData);
							// end begincongif

							System.out.println("\nInside Modify product\n");
							String RelationShipID = null, ProductID = null, ModifiedProductID = null,
									integrationId = null;
							ListOfData LOD = StoreServiceBundle.get("ListOf " + i);
							List<Item> itemList = LOD.getProductData().getItem();
							System.out.println("\nFETCH INTEGRATION ID\n");
							for (Item item : itemList) {
								List<Item> ToFetchItem = item.getItem();
								for (Item FetchInt : ToFetchItem) {
									if (FetchInt.getName().equalsIgnoreCase(BaseItem)) {
										String IntID = FetchInt.getIntegrationId();
										IntegrationID.put("INTEGRATIONID " + i, IntID);
										System.out.println("Fetched INTID :" + IntID);
									}
								}
								List<Relationship> RelationList = item.getRelationship();
								for (Relationship relation : RelationList) {
									if (relation.getName().equalsIgnoreCase(relationshipName)) {
										System.out.println("PRINT RELATIONSHIP: " + relation.getName());
										System.out.println("PRINTRELATIONSHIPID:" + relation.getId());
										RelationShipID = relation.getId();
										List<DomainItem> packagename = relation.getDomainItem();
										for (DomainItem speed : packagename) {
											if (speed.getName().equalsIgnoreCase(BaseItem)) {
												System.out.println("PRINT Product: " + speed.getName());
												ProductID = speed.getId();
												System.out.println("PRINTProductID:" + ProductID);
											}
											if (speed.getName().equalsIgnoreCase(NewItem)) {
												System.out.println("PRINT Product to be modified: " + speed.getName());
												ModifiedProductID = speed.getId();
												System.out.println("PRINTProductIDtobe modified:" + ModifiedProductID);
												integrationId = IntegrationID.get("INTEGRATIONID " + i);
												QueryUpdateConfiguration queryUpdateConfiguration = new QueryUpdateConfiguration(
														dataStorage);
												boolean ok = queryUpdateConfiguration.UpdateItem(integrationId,
														RelationShipID, ProductID, ModifiedProductID);
												Assert.assertTrue("Modified Successfully", ok);
											}
										}
									}
								}
							}
							// end modifyProduct

							System.out.println("\nEndConfigurationService\n");

							QueryEndConfiguration queryEndConfiguration = new QueryEndConfiguration(dataStorage);
							Quote quote = queryEndConfiguration.endConfiguration();
							Assert.assertNotNull("quote from endConfiguration is null", quote);
							List<QuoteItem> quoteItemList = getFlattenedQuoteItems(quote);
							System.out.println("Length of quoteItemList=" + quoteItemList.size() + "\n");
							String commitmentDuration = null;
							System.out.println(
									"dataStorage.getIntegrationId()=" + IntegrationID.get("INTEGRATIONID " + i) + "\n");

							outerloop: for (QuoteItem quoteItem : quoteItemList) {

								if (IntegrationID.get("INTEGRATIONID " + i).equals(quoteItem.getId())) {
									System.out.println("found integration id in quoteitem, looking for quoteItemXA \n");
									for (QuoteItemXA quoteItemXA : quoteItem.getListOfQuoteItemXA().getQuoteItemXA()) {
										if (quoteItemXA.getAttribute().equals("CommitmentDuration")) {
											commitmentDuration = quoteItemXA.getValue();
											System.out.println("in response commitmentDuration was found to be "
													+ commitmentDuration + "\n");
											break outerloop;
										}
									}
								}
							}
							dataStorage.setQuote(quote);
						}
					}

				});
	}
}