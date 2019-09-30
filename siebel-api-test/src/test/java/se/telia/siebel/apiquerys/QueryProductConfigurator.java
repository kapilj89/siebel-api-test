package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.configurator.BeginConfigurationInput;
import com.siebel.ordermanagement.configurator.BeginConfigurationOutput;
import com.siebel.ordermanagement.configurator.ProductConfigurator;
import com.siebel.ordermanagement.configurator.BeginConfigurationPort;

import com.siebel.ordermanagement.configurator.cfginteractdata.*;
import com.siebel.ordermanagement.configurator.cfglinkeditems.ListOfLinkedItems;
import com.siebel.ordermanagement.configurator.cfgproperties.ListOfProperties;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.ListOfQuoteItem;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.data.QuoteItem;
import se.telia.siebel.data.DataStorage;


import static se.telia.siebel.apiquerys.SiebelFlattenDataStructures.getFlattenedQuoteItems;


public class QueryProductConfigurator {

    // Begin Configuration parameters
    public static final String INIT_INSTANCE_OPERATION = "LOAD";
    public static final String ORDERABLE_CHECK_FLAG = "N";
    public static final String SKIP_LOADING_DEFAULT_INSTANCE = "N";
    public static final String RETURN_FULL_INSTANCE = "Y";
    public static final String SKIP_CFG_ELIGIBILITY_CHECK = "N";

    DataStorage dataStorage ;
    BeginConfigurationPort beginConfigurationPort;
    public QueryProductConfigurator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        ProductConfigurator productConfigurator = new ProductConfigurator();
        beginConfigurationPort = productConfigurator.getBeginConfigurationPort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(beginConfigurationPort);
    }

    public ListOfData beginConfiguration(Quote quote, String productItemName) {
        String rootId=null;
        for (QuoteItem quoteItem: getFlattenedQuoteItems(quote) ) {
            if (productItemName.equals(quoteItem.getName())){
                rootId=quoteItem.getId();
                dataStorage.setRootId(rootId);
                break;
            }
        }
        if (rootId==null) {
            System.out.println("Can not find rootID ");
            return null;
        }
        // as stolen from C2B
        final BeginConfigurationInput beginConfigurationInput = new BeginConfigurationInput();
        beginConfigurationInput.setInitInstanceOperation(INIT_INSTANCE_OPERATION);
        beginConfigurationInput.setOrderableCheckFlag(ORDERABLE_CHECK_FLAG);
        beginConfigurationInput.setSkipLoadingDefaultInstance(SKIP_LOADING_DEFAULT_INSTANCE);
        beginConfigurationInput.setReturnFullInstance(RETURN_FULL_INSTANCE);
        beginConfigurationInput.setSkipCfgEligibilityCheck(SKIP_CFG_ELIGIBILITY_CHECK);
        beginConfigurationInput.setRootId(rootId); 
//        beginConfigurationInput.setHeaderId(quote.getId());
        beginConfigurationInput.setHeaderId(dataStorage.getActiveQuoteId()); 
        beginConfigurationInput.setListOfLinkedItems(new ListOfLinkedItems());
        beginConfigurationInput.setListOfProperties(new ListOfProperties());
        beginConfigurationInput.setListOfQuote(new ListOfQuote());
        beginConfigurationInput.setListOfData(buildListOfDataStructure());
        BeginConfigurationOutput beginConfigurationOutput = beginConfigurationPort.beginConfiguration(beginConfigurationInput);
        return beginConfigurationOutput.getListOfData();
    }

    // As stolen from C2B
    private ListOfData buildListOfDataStructure() {
        final ListOfData listOfData = new ListOfData();
        listOfData.setProductData(new ProductData());
        final Item bundleLevelItem = buildItemStructure();
        listOfData.getProductData().getItem().add(bundleLevelItem);
        Item productLevelItem = buildItemStructure();
        bundleLevelItem.getItem().add(productLevelItem);
        Item subProductLevelItem = buildItemStructure();
        productLevelItem.getItem().add(subProductLevelItem);
        return listOfData;
    }
    // As stolen from C2B
    private static Item buildItemStructure() {
        final Item item = new Item();
        item.setIntegrationId("");
        item.setName("");
        item.setProductId("");
        item.getRelationship().add(new Relationship());
        item.getRelationship().get(0).getDomainItem().add(new DomainItem());
        item.getAttribute().add(new Attribute());
        item.getAttribute().get(0).getAttributeValue().add(new AttributeValue());
        item.getExplanation().add(new Explanation());
        item.setQuantity("");
        return item;
    }


}
