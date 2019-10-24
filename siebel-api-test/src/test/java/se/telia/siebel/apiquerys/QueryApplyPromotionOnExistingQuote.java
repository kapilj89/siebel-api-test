package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.promotion.ApplyProductPromotionInput;
import com.siebel.ordermanagement.promotion.ApplyProductPromotionOutput;
import com.siebel.ordermanagement.promotion.ApplyPromotionPort;
import com.siebel.ordermanagement.promotion.PromotionWebService;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;
import se.telia.siebel.data.AccountDetails;
import se.telia.siebel.data.DataStorage;

import java.util.List;

public class QueryApplyPromotionOnExistingQuote {
	DataStorage dataStorage;
	ApplyPromotionPort applyPromotionPort;

	// Apply Promotion parameters
	private static final String DEFAULT_FREIGHT = "0";
	private static final String DEFAULT_REVISION = "1";
	private static final String DEFAULT_TSSCORING_RESPONSE = "ACCEPTED";
	private static final String DEFAULT_QUANTITY = "1";
	private static final String DEFAULT_SKIP_CREDIT_CHECK_FLAG = "N";
	private static final String DEFAULT_PRICING_MODE = "N";


	// Execute Quoting parameters
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

	public QueryApplyPromotionOnExistingQuote(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
		PromotionWebService promotionWebService = new PromotionWebService();
		applyPromotionPort = promotionWebService.getApplyPromotionPort();
		new SiebelSoapCommunication(dataStorage).setupSoapCommunication(applyPromotionPort);
	}

	public Quote applyProductPromotion(AccountDetails accountDetails,
		String quoteNumber,
		String dueDate,
		String primaryOrganisationId,
		String productId,
		String priceListId)
	{
		Quote quote = dataStorage.getQuote();
		quote.setQuoteNumber(quoteNumber);
		quote.setDueDate(dueDate);
		quote.setPrimaryOrganizationId(primaryOrganisationId);
		quote.setAccountId(accountDetails.getCustomerAccount());
		quote.setServiceAccountId(accountDetails.getServiceAccount());
		quote.setContactId(accountDetails.getPrimaryContact());
		quote.setBillingAccountId(accountDetails.getBillingAccount());
		quote.setBillingProfileId(accountDetails.getBillingProfile());
		quote.setPriceListId(priceListId);
		quote.setRevision("1");


		// C2B

		quote.setTSChannelName("TELIASE"); // refactor and get this value from EntryPoint
		quote.setTSCSRId("");
		quote.setFreight(DEFAULT_FREIGHT);
		quote.setTSC2BDeliveryDate(dueDate);
		quote.setTScalDeliverDateFlag(DELIVERYDATE_FLAG);
		quote.setTSReCalDeliveryDateFlag(RECALDUEDATE_FLAG);
		quote.setTSScoringResponse(DEFAULT_TSSCORING_RESPONSE);
		quote.setTSSkipCreditCheckflag(DEFAULT_SKIP_CREDIT_CHECK_FLAG);



		ListOfQuote listOfQuote = new ListOfQuote();
		listOfQuote.getQuote().add(quote);

		ApplyProductPromotionInput applyProdPromoInput = new ApplyProductPromotionInput();
		applyProdPromoInput.setEligibilityMode("2");
		applyProdPromoInput.setQuantity("1");
		applyProdPromoInput.setProdPromId(productId);
		System.out.println("productId="+productId);
		applyProdPromoInput.setListOfQuote(listOfQuote);


		ApplyProductPromotionOutput applyProdPromoOutput = applyPromotionPort.applyProductPromotion(applyProdPromoInput);
		Quote quoteDetails = applyProdPromoOutput.getListOfQuote().getQuote().get(0);
		return quoteDetails;

	}
}
