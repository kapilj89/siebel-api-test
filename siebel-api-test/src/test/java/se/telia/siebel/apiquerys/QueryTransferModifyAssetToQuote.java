/*//package se.telia.siebel.apiquerys;
//
//import java.util.List;
//
//import org.junit.Assert;
//
//import com.siebel.ordermanagement.abo.ABOWebService;
//import com.siebel.ordermanagement.abo.ModifyAssetToQuoteInput;
//import com.siebel.ordermanagement.abo.ModifyAssetToQuoteOutput;
//import com.siebel.ordermanagement.abo.ModifyAssetToQuotePort;
//import com.siebel.ordermanagement.quote.data.ListOfQuote;
//import com.siebel.ordermanagement.quote.data.Quote;
//import com.siebel.ordermanagement.quote.data.QuoteItem;
//
//import se.telia.siebel.data.DataStorage;
//
//public class QueryTransferModifyAssetToQuote {
//   
//	DataStorage dataStorage ;
//	ModifyAssetToQuotePort modifyAssetToQuotePort;
//
////	TSC2BTransferOrderProcess transferOrderProcess;
//
//    public QueryTransferModifyAssetToQuote(DataStorage dataStorage) {
//        this.dataStorage=dataStorage;
//        ABOWebService ModifyWebService_service = new ABOWebService();
//        modifyAssetToQuotePort = ModifyWebService_service.getModifyAssetToQuotePort();
//        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(modifyAssetToQuotePort);
//
//    }
//
//	public ListOfQuote modifyAssetToQuote(String quoteNumber,String Asset, String Bundle, String ActualSpeed, String ModifiedSpeed) {
//
//		TransferModifyAssetToQuoteInput modify = new TransferModifyAssetToQuoteInput();
//		modify.setQuoteNumber(quoteNumber);
//        modify.setAssetNumber(Asset);
//		modify.setTSChannelName("TELIASE");
//		modify.setOrderSubType("Modify");
//		modify.setAccountId(dataStorage.getServiceAccountId());
//		modify.setTSReasonCode("No Reason");
//		modify.setPriceOnSync("Y");
//
//		ModifyAssetToQuoteOutput ModifyAssetToQuoteOutput = modifyAssetToQuotePort.modifyAssetToQuote(modify);
//		ListOfQuote listofQuote = ModifyAssetToQuoteOutput.getListOfQuote();
//		System.out.println("Quote=" + listofQuote);
//		String errorSpcCode = ModifyAssetToQuoteOutput.getErrorSpcCode();
////		Assert.assertTrue("".equals(errorSpcCode)); // Ensure that there is no
//													// error code
//		String activeDocId = ModifyAssetToQuoteOutput.getActiveDocumentId();
//		dataStorage.setActiveQuoteId(activeDocId);
//		dataStorage.setListofQuote(listofQuote);
//		ListOfQuote Quotelist = dataStorage.getListofQuote();
//		List<Quote> quotedata = Quotelist.getQuote();
//		Quote quoteDetails = listofQuote.getQuote().get(0);
//		dataStorage.setQuote(quoteDetails);
//		for (Quote qc : quotedata) {
//			List<QuoteItem> Qi = qc.getListOfQuoteItem().getQuoteItem();
//
//			for (QuoteItem quoteitem : Qi) {
//				System.out.println("QuoteItem " + quoteitem.getName());
//				System.out.println("ActualSpeed " + ActualSpeed);
//				List<QuoteItem> ListofQuote = quoteitem.getQuoteItem();
//				if (quoteitem.getName().equalsIgnoreCase(Bundle)) {
//					System.out.println("Inside Modify asset Bundle Name: "+Bundle);
//					for (QuoteItem quote : ListofQuote) {
//						System.out.println("Inside ServiceBundle quote" + quote.getName());
//						System.out.println("ActualSpeed " + ActualSpeed);
//						if (quote.getName().equalsIgnoreCase(ActualSpeed)) {
//							String IntegrationID = quote.getAssetIntegrationId();
//							System.out.println("Modify AssetIntID:" + IntegrationID);
//							dataStorage.setIntegrationId(IntegrationID);
//							
//
//						}
//					}
//				}
//			}
//		}
//		return listofQuote;
//	}
//
//}
//
*/