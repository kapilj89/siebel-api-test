package se.telia.siebel.stepdefs;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
import com.siebel.xml.asset_management_complex_io.data.AssetMgmtAssetHeaderData;

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


public class DisconnectStepdef implements En {
    DataStorage dataStorage;
    public DisconnectStepdef( DataStorage dataStorage) {

        System.out.println("Disconnect Constructor");
        this.dataStorage = dataStorage; // dataStorage is injected and contains stuff that needs sharing between steps
        Given("^call QueryMainAsset using SSN \"([^\"]*)\" to get asset details AssetNumber and ServiceAccountId for promotionName \"([^\"]*)\"$", (String ssn, String promotionName) -> {
        	QueryAsset queryAsset = new QueryAsset(dataStorage);
        	final Multimap<String, String> promotions = ArrayListMultimap.create();
        	String[] promotionNameParam=promotionName.split(";");
        	for(int i=0;i<promotionNameParam.length;i++){
        	System.out.println(promotionNameParam[i]);
            AssetMgmtAssetHeaderData assetMgmtAssetHeaderData = queryAsset.getAssetMgmtAssetHeaderData(ssn, promotionNameParam[i]);
            System.out.println("Asset MgmtAssetHeaderData"+assetMgmtAssetHeaderData);
            if(assetMgmtAssetHeaderData==null){
//            	 Assert.assertNotNull("assetMgmtAssetHeaderData is null after getAssetMgmtAssetHeaderData", assetMgmtAssetHeaderData);
            	System.out.println("Promotion is not available: "+promotionNameParam[i]);
            }else{
            String assetNumber = assetMgmtAssetHeaderData.getAssetNumber();
            String serviceAccountId = assetMgmtAssetHeaderData.getServiceAccountId();
            String ProductId=  assetMgmtAssetHeaderData.getProductId();
//            System.out.println("assetNumber=" + assetNumber);
            System.out.println("AssetNumber"+i+":" +assetNumber);
            promotions.put("AssetNumber"+i, assetNumber);
            System.out.println("serviceAccountId=" + serviceAccountId);	
            }
//            Assert.assertNotNull("AssetNumber is null", assetNumber);
//            Assert.assertNotNull("ServiceAccountId is null", assetNumber);
//            dataStorage.setAssetNumber(assetNumber);
//            dataStorage.setServiceAccountId(serviceAccountId);
//            dataStorage.setProductId(ProductId);
            
        	}
        });

        When("^call QuoteCheckOut to convert the quote into a order$", () -> {
            QueryQuoteCheckout queryQuoteCheckout = new QueryQuoteCheckout(dataStorage);
            String activeOrderId = queryQuoteCheckout.quoteCheckout(dataStorage.getActiveQuoteId());
            Assert.assertNotNull("Order id is null after quoteCheckout",activeOrderId);
            dataStorage.setActiveOrderId(activeOrderId);
            System.out.println("activeOrderId="+activeOrderId);
        });

        When("^call DisconnectAssetToQuote for the RelationshipName \"([^\"]*)\" and Package \"([^\"]*)\" to \"([^\"]*)\"", (String Productname,String ExistingSpeed,String ModifiedSpeed) -> {
        	System.out.println("DisconnectAssetToQuote");
        	String quoteNumber = getGeneratedQuoteNumber();
            System.out.println("quoteNumber="+quoteNumber);
            QueryDisconnectAssetToQuote queryDisconnectAssetToQuote =new QueryDisconnectAssetToQuote(dataStorage);
            queryDisconnectAssetToQuote.DisconnectAsset(quoteNumber);
//;            QueryModifyAssetToQuote queryModifyAssetToQuote = new QueryModifyAssetToQuote(dataStorage);
//            queryModifyAssetToQuote.modifyAssetToQuote(quoteNumber,ExistingSpeed,ModifiedSpeed);
           
          
        
        });


    }
}
