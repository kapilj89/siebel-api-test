package se.telia.siebel.stepdefs;

import com.siebel.ordermanagement.configurator.cfginteractdata.DomainItem;
import com.siebel.ordermanagement.configurator.cfginteractdata.Item;
import com.siebel.ordermanagement.configurator.cfginteractdata.ListOfData;
import com.siebel.ordermanagement.configurator.cfginteractdata.Relationship;
import com.siebel.ordermanagement.order.data.Order;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.ordermanagement.quote.data.QuoteItemXA;
import com.siebel.xml.asset_management_complex_io.data.AssetMgmtAssetHeaderData;
import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.junit.Assert;
import se.telia.siebel.apiquerys.*;
import se.telia.siebel.data.DataStorage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static se.telia.siebel.apiquerys.GenerateQuoteNumber.getGeneratedQuoteNumber;
import static se.telia.siebel.apiquerys.SiebelFlattenDataStructures.getFlattenedQuoteItems;

public class Sanity_MoveStepDefs implements En {
	DataStorage dataStorage;
	final Map<String, String> AssetHolder = new HashMap<>();
	final Map<String, ListOfData> StoreServiceBundle = new HashMap<>();
	public Sanity_MoveStepDefs(DataStorage dataStorage) {
		System.out.println("SanityMoveStepDefs Constructor");
		this.dataStorage = dataStorage; // dataStorage is injected and contains
		// stuff that needs sharing between
		// steps

		And("^call QuerySelfServiceAccount to add end date$", () -> {
			System.out.println("\nAddEndDate\n");
			QuerySelfServiceAccount querySelfServiceAddress = new QuerySelfServiceAccount(dataStorage);
			querySelfServiceAddress.AddEndDateInExistingAddress();
		});

		Given("^call QueryAllAsset using SSN \"([^\"]*)\" to get asset details AssetNumber and ServiceAccountId for promotionName \"([^\"]*)\"$",
				(String ssn, String promotionName) -> {
					QueryAsset queryAsset = new QueryAsset(dataStorage);
					Map<String, String> ProductPromotions = new HashMap<>();
					String[] Promotion = promotionName.split(";");
					for (int i = 0; i < Promotion.length; i++) {
						System.out.println("Check for Asset " + Promotion[i]);
						AssetMgmtAssetHeaderData assetMgmtAssetHeaderData = queryAsset.getAssetMgmtAssetHeaderData(ssn,
								Promotion[i]);
						System.out.println("Asset MgmtAssetHeaderData" + assetMgmtAssetHeaderData);
						if (assetMgmtAssetHeaderData == null) {
							System.out.println("Asset is not available: " + Promotion[i]);
						} else {
							String assetNumber = assetMgmtAssetHeaderData.getAssetNumber();
							String serviceAccountId = assetMgmtAssetHeaderData.getServiceAccountId();
							String ProductId = assetMgmtAssetHeaderData.getProductId();
							System.out.println("AssetNumber" + i + ":" + assetNumber);
							AssetHolder.put("AssetNumber" + i, assetNumber);
							System.out.println("serviceAccountId=" + serviceAccountId);
							dataStorage.setServiceAccountId(serviceAccountId);
						}
					}
				});

		When("^call MoveModifyAssetToQuote to create move quote using AssetNumber and ServiceAccountId$", () -> {
			String quoteNumber = getGeneratedQuoteNumber();
			System.out.println("quoteNumber=" + quoteNumber);
			for (int i = 0; i < AssetHolder.size(); i++) {
				String Asset = AssetHolder.get("AssetNumber" + i);
				if (Asset == null) {
					System.out.println("NO SUCH ASSET");
				} else {
					QueryMoveModifyAssetToQuote queryMoveModifyAssetToQuote = new QueryMoveModifyAssetToQuote(
							dataStorage);
					queryMoveModifyAssetToQuote.moveModifyAssetToQuote(quoteNumber, Asset);
				}
			}
		});

		And("^check \"([^\"]*)\" and call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote$",
				(String flag) -> {
					if (flag.equals("N")) {
						System.out.println("NO Customization Hence skip this");
					} else {
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
					}
				});

		And("^check \"([^\"]*)\" and call BeginConfigurationService using product item name \"([^\"]*)\"$",
				(String flag, String productItemName) -> {
					if (flag.equals("N")) {
						System.out.println("NO Customization Hence skip this");
					} else {
						List<String> Products = new ArrayList<String>(Arrays.asList(productItemName.split(";")));
						if (Products.size() >= 1) {
							System.out.println("\nBeginConfigurationService\n");
							int i = 0;
							for (String ServiceBundle : Products) {
								System.out.println("BundleName: " + ServiceBundle);
								QueryProductConfigurator queryProductConfigurator = new QueryProductConfigurator(
										dataStorage);
								ListOfData listOfData = queryProductConfigurator
										.beginConfiguration(dataStorage.getQuote(), ServiceBundle);
								Assert.assertNotNull("beginConfiguration returned null", listOfData);
								dataStorage.setListOfData(listOfData);
								ListOfData LOD = dataStorage.getListOfData();
								StoreServiceBundle.put("ListOf " + i, LOD);
								String quoteId = dataStorage.getActiveQuoteId();
								i++;
							}
						}
					}
				});

		And("^check \"([^\"]*)\" and call ModifyProduct to set RelationShipName \"([^\"]*)\" and Package \"([^\"]*)\" to \"([^\"]*)\"",
				(String flag, String RelationshipName, String PackageName, String PackageName2) -> {
					if (flag.equals("N")) {
						System.out.println("NO Customization Hence skip this");
					} else {
						List<String> Realtions = new ArrayList<String>(Arrays.asList(RelationshipName.split(";")));
						List<String> BasePackage = new ArrayList<String>(Arrays.asList(PackageName.split(";")));
						List<String> MovePackage = new ArrayList<String>(Arrays.asList(PackageName2.split(";")));
						System.out.println("Inside Modify product");
						for (int i = 0; i < Realtions.size(); i++) {
							String RelationShipID = null, ProductID = null, ModifiedProductID = null;
							String relationshipName = Realtions.get(i);
							for (int j = 0; j < BasePackage.size(); j++) {
								String BaseItem = BasePackage.get(j);
								for (int k = 0; k < MovePackage.size(); k++) {
									String NewItem = MovePackage.get(k);
									System.out.println("INSIDE LOOP");
									ListOfData LOD = StoreServiceBundle.get("ListOf " + i);
									List<Item> itemList = LOD.getProductData().getItem();
									ListOfQuote listofQuote = dataStorage.getListofQuote();
									for (Item item : itemList) {
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
														System.out.println(
																"PRINT Product to be modified: " + speed.getName());
														ModifiedProductID = speed.getId();
														System.out.println(
																"PRINTProductIDtobe modified:" + ModifiedProductID);
													}
												}
											}
										}
									}
								}
							}
							String integrationId = dataStorage.getIntegrationId();
							System.out.println("Add item int" + integrationId);
							QueryUpdateConfiguration queryUpdateConfiguration = new QueryUpdateConfiguration(
									dataStorage);
							Map<String, String> attributes = new HashMap<>();
							boolean ok = queryUpdateConfiguration.UpdateItem(integrationId, RelationShipID,
									ProductID, ModifiedProductID);
							Assert.assertTrue("Modified Successfully", ok);
						}
					}
				});

		And("^check \"([^\"]*)\" and call EndConfigurationService and get Quote$", (String flag) -> {
			if (flag.equals("N")) {
				System.out.println("NO Customization Hence skip this");
			} else {
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
				dataStorage.setQuote(quote);
			}
		});
	}
}