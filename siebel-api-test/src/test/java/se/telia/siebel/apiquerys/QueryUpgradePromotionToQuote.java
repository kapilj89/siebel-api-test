package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.promotion.PromotionWebService;
import com.siebel.ordermanagement.promotion.UpgradePromotionToQuoteInput;
import com.siebel.ordermanagement.promotion.UpgradePromotionToQuoteOutput;
import com.siebel.ordermanagement.promotion.UpgradePromotionToQuotePort;
import com.siebel.ordermanagement.quote.data.ListOfQuote;
import com.siebel.ordermanagement.quote.data.Quote;

import se.telia.siebel.data.AccountDetails;
import se.telia.siebel.data.DataStorage;


public class QueryUpgradePromotionToQuote {
	DataStorage dataStorage;
	UpgradePromotionToQuotePort upgradePromotionToQuotePort;
	
	private static final String DEFAULT_FREIGHT = "0";
	private static final String DEFAULT_TSSCORING_RESPONSE = "ACCEPTED";
	private static final String DEFAULT_SKIP_CREDIT_CHECK_FLAG = "N";
	private static final String DELIVERYDATE_FLAG = "Y";
	private static final String RECALDUEDATE_FLAG = "Y";

    public QueryUpgradePromotionToQuote(DataStorage dataStorage){
    	PromotionWebService upgradePromotion = new PromotionWebService();
    	upgradePromotionToQuotePort=upgradePromotion.getUpgradePromotionToQuotePort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(upgradePromotionToQuotePort);
    }
    
    
	public Quote upgradePromotion(AccountDetails accountDetails,
			String quoteNumber,
			String dueDate,
			String primaryOrganisationId,
			String productId,
			String ReasonCode, String Assetnumber)
		{
			Quote quote = new Quote();
			quote.setQuoteNumber(quoteNumber);
			quote.setTSChannelName("TELIASE"); 

			quote.setDueDate(dueDate);
			quote.setPrimaryOrganizationId(primaryOrganisationId);
			quote.setAccountId(accountDetails.getCustomerAccount());
			quote.setServiceAccountId(accountDetails.getServiceAccount());
			quote.setContactId(accountDetails.getPrimaryContact());
			quote.setBillingAccountId(accountDetails.getBillingAccount());
			quote.setBillingProfileId(accountDetails.getBillingProfile());
			quote.setRevision("1");
			quote.setTSCSRId("");
			quote.setFreight(DEFAULT_FREIGHT);
			quote.setTSC2BDeliveryDate(dueDate);
			quote.setTScalDeliverDateFlag(DELIVERYDATE_FLAG);
			quote.setTSReCalDeliveryDateFlag(RECALDUEDATE_FLAG);
			quote.setTSScoringResponse(DEFAULT_TSSCORING_RESPONSE);
			quote.setTSSkipCreditCheckflag(DEFAULT_SKIP_CREDIT_CHECK_FLAG);

			ListOfQuote listOfQuote = new ListOfQuote();
			listOfQuote.getQuote().add(quote);

			UpgradePromotionToQuoteInput upgradePromotionToQuoteInput = new UpgradePromotionToQuoteInput();
			upgradePromotionToQuoteInput.setDueDate(dueDate);
			upgradePromotionToQuoteInput.setTSChannelName("TELIASE");
			upgradePromotionToQuoteInput.setAssetNumber(Assetnumber);
			upgradePromotionToQuoteInput.setTSReasonCode(ReasonCode);
			upgradePromotionToQuoteInput.setOrderSubType("Upgrade Promotion");
			upgradePromotionToQuoteInput.setQuoteNumber(quoteNumber);
			upgradePromotionToQuoteInput.setAccountId(accountDetails.getServiceAccount());			
			upgradePromotionToQuoteInput.setNewPromotionId(productId);
//			upgradePromotionToQuoteInput.setListOfQuote(listOfQuote);


			UpgradePromotionToQuoteOutput upgradePromotionToQuoteOutput = upgradePromotionToQuotePort.upgradePromotionToQuote(upgradePromotionToQuoteInput);
			Quote quoteDetails = upgradePromotionToQuoteOutput.getListOfQuote().getQuote().get(0);
			return quoteDetails;

		}

}
