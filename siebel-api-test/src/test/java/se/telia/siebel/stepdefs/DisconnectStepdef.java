package se.telia.siebel.stepdefs;

import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import com.siebel.xml.asset_management_complex_io.data.AssetMgmtAssetHeaderData;
import org.junit.Assert;
import cucumber.api.java8.En;
import se.telia.siebel.apiquerys.*;
import se.telia.siebel.data.DataStorage;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static se.telia.siebel.apiquerys.GenerateQuoteNumber.getGeneratedQuoteNumber;
 

public class DisconnectStepdef implements En {
    DataStorage dataStorage;
    boolean flag;
    int assetcount;
    final Map<String, String> promotions = new HashMap<>();

	public DisconnectStepdef(DataStorage dataStorage) {
		System.out.println("Disconnect Constructor");
		this.dataStorage = dataStorage; // dataStorage is injected and contains
										// stuff that needs sharing between
										// steps
		Given("^call QueryMainAsset using SSN \"([^\"]*)\" to get asset details AssetNumber and ServiceAccountId for promotionName \"([^\"]*)\"$",
				(String ssn, String promotionName) -> {
					QueryAsset queryAsset = new QueryAsset(dataStorage);
					String[] promotionNameParam = promotionName.split(";");
					System.out.println("No of Promotions:" + promotionNameParam.length);
					int j = promotionNameParam.length;
					for (int i = 0; i < promotionNameParam.length; i++) {
						System.out.println(promotionNameParam[i]);
						AssetMgmtAssetHeaderData assetMgmtAssetHeaderData = queryAsset.getAssetMgmtAssetHeaderData(ssn,
								promotionNameParam[i]);
						System.out.println("Asset MgmtAssetHeaderData" + assetMgmtAssetHeaderData);
						if (assetMgmtAssetHeaderData == null) {
							System.out.println("Promotion is not available: " + promotionNameParam[i]);
							flag = false;
							assetcount = j - 1;
							j--;
						} else {
							String assetNumber = assetMgmtAssetHeaderData.getAssetNumber();
							String serviceAccountId = assetMgmtAssetHeaderData.getServiceAccountId();
							String ProductId = assetMgmtAssetHeaderData.getProductId();
							System.out.println("AssetNumber" + i + ":" + assetNumber);
							promotions.put("AssetNumber" + i, assetNumber);
							System.out.println("serviceAccountId=" + serviceAccountId);
							dataStorage.setServiceAccountId(serviceAccountId);
						}
						System.out.println(assetcount);
					}

					if (assetcount == 0) {
						Assert.assertTrue("No Assets Found :) in SSN NO: " + ssn, flag);
					}
				});

		When("^call DisconnectAssetToQuote$", () -> {
			System.out.println("DisconnectAssetToQuote");
			String quoteNumber = getGeneratedQuoteNumber();
			System.out.println("quoteNumber=" + quoteNumber);
			System.out.println("Number of promo:" + promotions.size());
			List<String> Assetno = new ArrayList(promotions.values());
			for (int i = 0; i < promotions.size(); i++) {
				String Promo = promotions.get("AssetNumber" + i);
				System.out.println("Promo" + promotions.values());
				QueryDisconnectAssetToQuote queryDisconnectAssetToQuote = new QueryDisconnectAssetToQuote(dataStorage);
				queryDisconnectAssetToQuote.DisconnectAsset(quoteNumber, Assetno.get(i));
			}
		});

		When("^call SynchronizeQuote for a Disconnect order$", () -> {
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

	}

}