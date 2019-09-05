package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;
import com.siebel.ordermanagement.quote.quoting.ExecuteQuotingInput;
import com.siebel.ordermanagement.quote.quoting.ExecuteQuotingOutput;
import com.siebel.ordermanagement.quote.quoting.QuotingPort;
import com.siebel.ordermanagement.quote.quoting.QuotingWebService;
import se.telia.siebel.data.DataStorage;

public class QueryQuotingWebService {
    DataStorage dataStorage ;
    // Execute Quoting parameters (as stolen from C2B)
    private static final String PRICING_FLAG = "N";
    private static final String QUERY_QUOTE_FLAG = "Y";
    private static final String CHECK_ELIGIBILITY_FLAG = "N";
    private static final String SYNC_QUOTE_FLAG = "Y";
    private static final String VERIFY_PROMOTION_FLAG = "N";
    private static final String CALCULATE_SHIPPING_COST_FLAG = "N";
    private static final String REPRICING_FLAG = "Y";
    private static final String CALCULATE_TAX_FLAG = "N";
    private static final String DELTA_ACTION_FLAG = "N";
    private static final String DELIVERYDATE_FLAG = "Y";
    private static final String RECALDUEDATE_FLAG = "Y";

    QuotingPort quotingPort;

    public QueryQuotingWebService(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        QuotingWebService quotingWebService = new QuotingWebService();
        quotingPort=quotingWebService.getQuotingPort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(quotingPort);

    }

    public Quote commitVirtualQuote(Quote quote) {
        // This code is also stolen from C2B
        ExecuteQuotingInput executeQuotingInput = new ExecuteQuotingInput();
        executeQuotingInput.setCheckEligibilityFlag(CHECK_ELIGIBILITY_FLAG);
        executeQuotingInput.setSyncQuoteFlag(SYNC_QUOTE_FLAG);
        executeQuotingInput.setVerifyPromotionFlag(VERIFY_PROMOTION_FLAG);
        executeQuotingInput.setCalculateShippingCostFlag(CALCULATE_SHIPPING_COST_FLAG);
        executeQuotingInput.setCalculateTaxFlag(CALCULATE_TAX_FLAG);
        executeQuotingInput.setRepricingFlag(REPRICING_FLAG);
        executeQuotingInput.setQueryQuoteFlag(QUERY_QUOTE_FLAG);
        executeQuotingInput.setPricingFlag(PRICING_FLAG);
        executeQuotingInput.setDeltaSpcActionSpcCodeSpcFlag(DELTA_ACTION_FLAG);

        ListOfQuote listOfQuote = new ListOfQuote();
        listOfQuote.getQuote().add(quote);
        executeQuotingInput.setListOfQuote(listOfQuote);
        ExecuteQuotingOutput executeQuotingOutput = quotingPort.executeQuoting(executeQuotingInput);
        Quote updatedQuote = executeQuotingOutput.getListOfQuote().getQuote().get(0);
        return updatedQuote;
    }


}
