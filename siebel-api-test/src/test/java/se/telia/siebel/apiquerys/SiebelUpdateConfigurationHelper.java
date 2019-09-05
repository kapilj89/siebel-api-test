package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.configurator.cfginteractdata.Attribute;
import com.siebel.ordermanagement.configurator.cfginteractdata.AttributeValue;
import com.siebel.ordermanagement.configurator.cfginteractdata.DomainItem;
import com.siebel.ordermanagement.configurator.cfginteractdata.Explanation;
import com.siebel.ordermanagement.configurator.cfginteractdata.Item;
import com.siebel.ordermanagement.configurator.cfginteractdata.ListOfData;
import com.siebel.ordermanagement.configurator.cfginteractdata.ProductData;
import com.siebel.ordermanagement.configurator.cfginteractdata.Relationship;


public class SiebelUpdateConfigurationHelper {
    public static ListOfData buildListOfDataStructure() {
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
