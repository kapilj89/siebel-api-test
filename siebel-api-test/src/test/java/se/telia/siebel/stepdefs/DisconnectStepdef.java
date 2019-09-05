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


public class DisconnectStepdef implements En {
    DataStorage dataStorage;
    public DisconnectStepdef( DataStorage dataStorage) {

        System.out.println("Disconnect Constructor");
        this.dataStorage = dataStorage; // dataStorage is injected and contains stuff that needs sharing between steps


        When("^call QuoteCheckOut to convert the quote into a order$", () -> {
            QueryQuoteCheckout queryQuoteCheckout = new QueryQuoteCheckout(dataStorage);
            String activeOrderId = queryQuoteCheckout.quoteCheckout(dataStorage.getActiveQuoteId());
            Assert.assertNotNull("Order id is null after quoteCheckout",activeOrderId);
            dataStorage.setActiveOrderId(activeOrderId);
            System.out.println("activeOrderId="+activeOrderId);
        });

       


    }
}
