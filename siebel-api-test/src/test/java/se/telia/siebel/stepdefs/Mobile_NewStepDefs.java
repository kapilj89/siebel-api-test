package se.telia.siebel.stepdefs;

import static se.telia.siebel.apiquerys.GenerateQuoteNumber.getGeneratedQuoteNumber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.siebel.ordermanagement.catalog.data.productdetails.Product;
import com.siebel.ordermanagement.configurator.cfginteractdata.Attribute;
import com.siebel.ordermanagement.configurator.cfginteractdata.Item;
import com.siebel.ordermanagement.configurator.cfginteractdata.Relationship;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.xml.asset_management_complex_io.data.AssetMgmtAssetHeaderData;

import cucumber.api.java8.En;
import se.telia.siebel.apiquerys.QueryApplyPromotionOnExistingQuote;
import se.telia.siebel.apiquerys.QueryAsset;
import se.telia.siebel.apiquerys.QueryGetProductPromotionDetails;
import se.telia.siebel.apiquerys.QueryModifyAssetToQuote;
import se.telia.siebel.apiquerys.QueryQuote;
import se.telia.siebel.apiquerys.QueryUpdateConfiguration;
import se.telia.siebel.apiquerys.QueryUpgradePromotionToQuote;
import se.telia.siebel.apiquerys.QuoteAddBundleItem;
import se.telia.siebel.apiquerys.SiebelDateFormat;
import se.telia.siebel.apiquerys.SiebelFlattenDataStructures;
import se.telia.siebel.data.AccountDetails;
import se.telia.siebel.data.DataStorage;


public class Mobile_NewStepDefs implements En {
	DataStorage dataStorage;
	String BundleId;

	public Mobile_NewStepDefs(DataStorage dataStorage) {

		System.out.println("Mobile_NewStepDefs Constructor");
		this.dataStorage = dataStorage;

		And("^call GetProductPromotionDetailsService using promotionCode \"([^\"]*)\" and service \"([^\"]*)\" and get ProductId, PriceList$",
				(String productName, String AdditionalService) -> {
					System.out.println("\nGetProductDetailsService\n");
					QueryGetProductPromotionDetails getProdDetails = new QueryGetProductPromotionDetails(dataStorage);
					List<Product> productList = getProdDetails.getProductsDetails(productName, AdditionalService);

					for (Product product : productList) {
						if (AdditionalService.equalsIgnoreCase(product.getName())) {
							BundleId = product.getID();
							System.out.println("BundleId is :" + BundleId);
						} else if (productName.equalsIgnoreCase(product.getName())) {
							String productID = product.getID();
							String priceList = product.getPriceListId();
							System.out.println("ProductID is :" + productID + ".  PriceList ID is :" + priceList);
							dataStorage.setProductId(productID);
							dataStorage.setPriceListId(priceList);
						}
					}
				});

		And("^call QuoteAddBundleItem to add a serviceBundle and get quote", () -> {
			System.out.println("\nQuoteAdditems\n");

			QuoteAddBundleItem quoteAddItem = new QuoteAddBundleItem(dataStorage);
			Quote quote = quoteAddItem.quoteAddItems(dataStorage.getQuote(), BundleId);
			dataStorage.setQuote(quote);

		});

		And("^call UpdateConfiguration to SetAttribute \"([^\"]*)\" Value of SIM \"([^\"]*)\"$",
				(String Attribute, String Value) -> {
					System.out.println("\nUPDATE ATTRIBUTE\n");

					String[] List_of_Attributes = Attribute.split(";");
					String[] List_of_Values = Value.split(";");
					Map<String, String> attributes = new HashMap<>();

					for (int i = 0; i < List_of_Attributes.length; i++) {
						attributes.put(List_of_Attributes[i], List_of_Values[i]);

					}
					List<Item> itemList = dataStorage.getListOfData().getProductData().getItem();
					String integrationId = null;

					for (Item item : itemList) {
						List<Item> innerItemList = item.getItem();
						for (Item innerItem : innerItemList) {
							List<Relationship> relationship = innerItem.getRelationship();
							for (Relationship relation : relationship) {
								relation.setHasGenericsFlag("false");
							}
							List<Attribute> AttributeList = innerItem.getAttribute();
							for (Attribute PresentAttribute : AttributeList) {
								if (Attribute.equalsIgnoreCase(PresentAttribute.getName())) {
									integrationId = innerItem.getIntegrationId();
								}
							}
						}
					}
					Assert.assertNotNull("integrationId not found", integrationId);
					// System.out.println("UPDATE FOR " +List_of_Attributes[i]);
					QueryUpdateConfiguration queryUpdateConfiguration = new QueryUpdateConfiguration(dataStorage);
					boolean ok = queryUpdateConfiguration.setAttributes(integrationId, attributes);
					Assert.assertTrue("Update of the commitment duration failed", ok);
					dataStorage.setIntegrationId(integrationId);
				});

		And("^call SynchronizeQuote to book number \"([^\"]*)\" for serivce \"([^\"]*)\" in a Mobile order$",
				(String Number, String ServiceBundle) -> {
					System.out.println("\nSynchronizeQuote\n");
					Quote quote = dataStorage.getQuote();

					quote.setTSRetailerId("000000");

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								if (quoteItem.getName().equalsIgnoreCase(ServiceBundle)) {
									System.out.println("SERVICE ID : " + Number);
									quoteItem.setTSNHFReservationStatus("Booked");
									quoteItem.setServiceId(Number);

								}
								if (quoteItem.getTSProductType().equalsIgnoreCase("simcard")) {
									quoteItem.setProductShipFlag("N");
								}
							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
								if (quoteItem.getName().equals("MT-SIM Card 559-1620")) {
									quoteItem.setProductShipFlag("N");
								}
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

		And("^call AddVoice SynchronizeQuote to book number \"([^\"]*)\" for serivce \"([^\"]*)\" and \"([^\"]*)\" in a Mobile order$",
				(String Number, String ServiceBundle, String addtionalBundle) -> {
					System.out.println("\nSynchronizeQuote\n");
					Quote quote = dataStorage.getQuote();
					String[] bookingId = Number.split(";");
					quote.setTSRetailerId("000000");

					List<QuoteItem> quoteItemList = SiebelFlattenDataStructures.getFlattenedQuoteItems(quote);
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.forEach(quoteItem -> {
								if (quoteItem.getName().equalsIgnoreCase(ServiceBundle)) {
									System.out.println("SERVICE ID : " + bookingId[0]);
									quoteItem.setTSNHFReservationStatus("Booked");
									quoteItem.setServiceId(bookingId[0]);

								} else if (quoteItem.getName().equalsIgnoreCase(addtionalBundle)) {
									System.out.println("SERVICE ID : " + bookingId[1]);
									quoteItem.setTSNHFReservationStatus("Booked");
									quoteItem.setServiceId(bookingId[1]);

								}
								if (quoteItem.getTSProductType().equalsIgnoreCase("simcard")) {
									quoteItem.setProductShipFlag("N");
								}
							});
					quoteItemList.stream()
							.filter(quoteItem -> !"Penalty PS".equalsIgnoreCase(quoteItem.getFulfillmentItemCode()))
							.filter(quoteItem -> "Y".equals(quoteItem.getProductShipFlag())).forEach(quoteItem -> {
								if (quoteItem.getName().equals("MT-SIM Card 559-1620")) {
									quoteItem.setProductShipFlag("N");
								}
							});

					QueryQuote queryQuote = new QueryQuote(dataStorage);
					boolean result = queryQuote.updateQuote(quote);
					Assert.assertTrue("No id from synchronizeQuoteOutput", result);
					System.out.println("synchronizeQuoteOutput OK");
				});

	}
}
