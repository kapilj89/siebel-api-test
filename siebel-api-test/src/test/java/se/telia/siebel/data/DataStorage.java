package se.telia.siebel.data;


// This class hold variables that needs to be shared between steps.
// Getters and setters are taken care of by
// Lombok. Don't forget to download the Intellij plugin 
// or else get-methods will give method not found error!

import com.siebel.ordermanagement.configurator.cfginteractdata.ListOfData;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.order.data.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import se.telia.siebel.interceptor.SiebelSession;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class DataStorage {
    private String loginName; // This holds the username of the account that is login into the seibel API. It is needed to get the primaryOrganisationId which is used in quote creation.
    private String primaryOrganizationId;
    private String siebelEndpointURL;
    private SiebelSession siebelSession;  // This holds the session between the Siebel API requests
    private String assetNumber;
    private String serviceAccountId;
    private String serviceAddressId;
    private String primaryAddressId;
    private Map<String, String> moveInAddressMap;
    private String quoteNumber;
    private String activeQuoteId;
    private String activeOrderId;
    private Quote quote;
    private ListOfQuote listofQuote;
    private Order order;
    private String productId;
    private String priceListId;
    private AccountDetails accountDetails;
    private String integrationId;
    private String commitmentDuration;
    private ListOfData listOfData;
    private String rootId; 
    private Map<String, String> AssetHolder ;
    
}
