package se.telia.siebel.stepdefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.xml.asset_management_complex_io.data.AssetMgmtAssetHeaderData;

import cucumber.api.java8.En;
import se.telia.siebel.apiquerys.QueryAsset;
import se.telia.siebel.apiquerys.QueryQuote;
import se.telia.siebel.apiquerys.QueryQuoteCheckout;
import se.telia.siebel.apiquerys.QuerySelfServiceAddressQueryPage;
import se.telia.siebel.data.DataStorage;
import se.telia.siebel.apiquerys.QueryTSChannelSISOMBillingSubmitOrder;


public class CommonStepDefs implements En {
	DataStorage dataStorage;
//	public Map<String, String> AssetHolder = new HashMap<>();

	public CommonStepDefs(DataStorage dataStorage) {
		this.dataStorage = dataStorage; // dataStorage is injected and contains
										// stuff that needs sharing between
										// steps
		Given("^call QueryAllAsset using SSN \"([^\"]*)\" to get asset details AssetNumber and ServiceAccountId for promotionName \"([^\"]*)\"$",
				(String ssn, String promotionName) -> {
					QueryAsset queryAsset = new QueryAsset(dataStorage);
					Map<String, String> ProductPromotions = new HashMap<>();
					Map<String,String> AssetHolder=new HashMap<String, String>();
					String[] Promotion = promotionName.split(";");
					for (int i = 0; i < Promotion.length; i++) {
						System.out.println("Check for Asset " + Promotion[i]);
						AssetMgmtAssetHeaderData assetMgmtAssetHeaderData = queryAsset.getAssetMgmtAssetHeaderData(ssn,
								Promotion[i]);
						System.out.println("Asset MgmtAssetHeaderData" + assetMgmtAssetHeaderData);
						if (assetMgmtAssetHeaderData == null) {
							System.out.println("Asset is not available: " + Promotion[i]);
							Assert.assertNotNull("ASSET NOT FOUND ", assetMgmtAssetHeaderData);
						} else {
							String assetNumber = assetMgmtAssetHeaderData.getAssetNumber();
							String serviceAccountId = assetMgmtAssetHeaderData.getServiceAccountId();
							String ProductId = assetMgmtAssetHeaderData.getProductId();
							System.out.println("AssetNumber" + i + ":" + assetNumber);
							AssetHolder.put("AssetNumber" + i, assetNumber);
							System.out.println("serviceAccountId=" + serviceAccountId);
							dataStorage.setServiceAccountId(serviceAccountId);
							dataStorage.setIntegrationId(assetMgmtAssetHeaderData.getIntegrationId());
							dataStorage.setAssetNumber(assetNumber);

						}
					}
					dataStorage.setAssetHolder(AssetHolder);
				});
		When("^call QueryQuote to fetch the quote from database$", () -> {

			QueryQuote queryQuote = new QueryQuote(dataStorage);
			List<Quote> quoteList = queryQuote.getQuoteById();
			System.out.println("quote.size()=" + quoteList.size());
			Assert.assertTrue("Wrong number of quotes in the list: " + quoteList.size(), quoteList.size() == 1);
			dataStorage.setQuote(quoteList.get(0));
			System.out.println("The quote is now in the dataStorage. id=" + quoteList.get(0).getId());

		});
		When("^call TSChannelSubmitOrder to submit the order$", () -> {

			System.out.println("\nSubmitOrder\n");
			QueryTSChannelSISOMBillingSubmitOrder queryTSChannelSISOMBillingSubmitOrder = new QueryTSChannelSISOMBillingSubmitOrder(
					dataStorage);
			String objectSpcId = queryTSChannelSISOMBillingSubmitOrder.submitOrder(dataStorage.getActiveOrderId());
			System.out.println(
					"objectSpcId (the order id which is the same as the actibeOrderId, and that is returned from the submit order api) ="
							+ objectSpcId);
			Assert.assertEquals("Submit order didn't work", dataStorage.getActiveOrderId(), objectSpcId);
		});
		
		And("^call QuerySelfServiceAddress Add/Update format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode \"([^\"]*)\"$", (String AddressData) -> {
            System.out.println("\nQuerySelfServiceAccount\n");
            Map<String,String> AddressMap= new HashMap<>();
            String[] AddressParam=AddressData.split(";");
            AddressMap.put("StreetAddress", AddressParam[0]);
            AddressMap.put("StreetAddress2", AddressParam[1]);
            AddressMap.put("Entrance", AddressParam[2]);
            AddressMap.put("City", AddressParam[3]);
            AddressMap.put("ApartmentNum", AddressParam[4]);
            AddressMap.put("PostalCode", AddressParam[6]);
            AddressMap.put("PointId", AddressParam[5]);
//            AddressMap.put("RowID", AddressParam[6]);
            QuerySelfServiceAddressQueryPage querySelfServiceAddress =new QuerySelfServiceAddressQueryPage(dataStorage);
            querySelfServiceAddress.SiebelCheckExistingAddress(AddressMap,"");
//            QuerySelfServiceAccount querySelfServiceAccount = new QuerySelfServiceAccount(dataStorage);
//            querySelfServiceAccount.
//            System.out.println( querySelfServiceAccount.getListOfSSAccountData());
//            System.out.println("Adding copper max HD & SD");

         
        }); 

		And("^call QuoteCheckOutService and get ActiveOrderID$", () -> {
			System.out.println("\nQuoteCheckOutService\n");
			QueryQuoteCheckout queryQuoteCheckout = new QueryQuoteCheckout(dataStorage);
			String activeOrderId = queryQuoteCheckout.quoteCheckout(dataStorage.getQuote().getId());
			Assert.assertNotNull("Order id is null after quoteCheckout", activeOrderId);
			dataStorage.setActiveOrderId(activeOrderId);
			System.out.println("activeOrderId=" + activeOrderId);
		});
	}
}
