package se.telia.siebel.apiquerys;

import se.telia.siebel.data.DataStorage;

import java.awt.List;
import java.util.Collection;
import java.util.Map;

import com.siebel.selfservice.common.account.SelfServiceAccount;
import com.siebel.selfservice.common.account.SelfServiceAccountExecuteInput;
import com.siebel.selfservice.common.account.SelfServiceAccountExecuteOutput;
import com.siebel.selfservice.common.account.SelfServiceAccount_Service;
import com.siebel.selfservice.common.account.data.AccountBusinessAddressData;
import com.siebel.selfservice.common.account.data.AccountData;
import com.siebel.selfservice.common.account.data.ListOfAccountBusinessAddressData;
import com.siebel.selfservice.common.account.data.ListOfSSAccountData;
import com.siebel.selfservice.common.account.id.AccountId;
import com.siebel.selfservice.common.address.SelfServiceAddress;
import com.siebel.selfservice.common.address.SelfServiceAddressQueryPageInput;
import com.siebel.selfservice.common.address.SelfServiceAddressQueryPageOutput;
import com.siebel.selfservice.common.address.SelfServiceAddress_Service;
import com.siebel.selfservice.common.address.data.CutAddressData;
import com.siebel.selfservice.common.address.data.ListOfSsAddressIoData;
import com.siebel.selfservice.common.address.query.CutAddressQuery;
import com.siebel.selfservice.common.address.query.ListOfSsAddressIoQuery;
import com.siebel.selfservice.common.address.query.QueryType;
import com.siebel.selfservice.common.user.SelfServiceUser;
import com.siebel.selfservice.common.user.SelfServiceUser_Service;
import com.siebel.xml.swicustomerpartyio.Account;
import com.siebel.xml.swicustomerpartyio.ListOfAccountBusinessAddress;

public class QueryServiceAccountAddressUpdate {
	   private static final String BI_DIRECTIONAL = "BiDirectional";
	    private static final String LDC = "LDC";
	    private static final String ALL = "All";
	    private static final String SKIPNODE = "skipnode";
	    private static final String FORWARD_ONLY = "ForwardOnly";
//	    SelfServiceAccount selfServiceAccount;
	    DataStorage dataStorage;
	    SelfServiceAddress_Service selfServiceAddress_Service;
	    SelfServiceAddress selfServiceAddress;
	    SelfServiceAccount_Service selfServiceAccount_service;
	    SelfServiceAccount selfServiceAccount;
        AccountId Acc=new AccountId();  
       public QueryServiceAccountAddressUpdate(DataStorage dataStorage) {
           this.dataStorage = dataStorage;
           selfServiceAddress_Service = new SelfServiceAddress_Service();
           selfServiceAddress = selfServiceAddress_Service.getSelfServiceAddress();
           new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAddress);
           selfServiceAccount_service = new SelfServiceAccount_Service();
           selfServiceAccount = selfServiceAccount_service.getSelfServiceAccount();
           new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAccount);
       }
        
      
       
      public ListOfSSAccountData getListOfSSAccountData(Map<String, String> addressMap,String CopperMaxValue) {
           ListOfSSAccountData listOfSSAccountData = new ListOfSSAccountData();
         listOfSSAccountData.getAccount().add(getAccountDet(addressMap,CopperMaxValue));
         listOfSSAccountData.setLastpage(null);
         listOfSSAccountData.setRecordcount(null);
         
//         listOfSSAccountData.getAccount().add(getAccountadd());
         return listOfSSAccountData;
      }
       
       public AccountData getAccountDet(Map<String, String> AddressMap,String CopperMaxValue) {

         AccountData accountData = new AccountData();
         accountData.setOperation("skipnode");
         String id=dataStorage.getServiceAccountId();
         accountData.setId(id);
         ListOfAccountBusinessAddressData AddressData=new ListOfAccountBusinessAddressData();
         AddressData.getAccountBusinessAddress().add(getAccountaddressdata(AddressMap,CopperMaxValue));
         accountData.setListOfAccountBusinessAddress(AddressData);
         return accountData;
      }

      public AccountBusinessAddressData getAccountaddressdata(Map<String, String> AddressMap,String CopperMaxValue) {
    AccountBusinessAddressData AccountBusinessAddressData = new AccountBusinessAddressData();
      AccountBusinessAddressData.setIsPrimaryMVG("Y");
      AccountBusinessAddressData.setOperation("insert");
      AccountBusinessAddressData.setCountry("Sweden");
      AccountBusinessAddressData.setMainAddressFlag(true);
      AccountBusinessAddressData.setPostalCode(AddressMap.get("PostalCode"));
      AccountBusinessAddressData.setStreetAddress(AddressMap.get("StreetAddress"));
      AccountBusinessAddressData.setStreetAddress2(AddressMap.get("StreetAddress2"));
      AccountBusinessAddressData.setTSEntrance(AddressMap.get("Entrance"));
      AccountBusinessAddressData.setTSApartmentNumber(AddressMap.get("ApartmentNum"));
      AccountBusinessAddressData.setCity(AddressMap.get("City"));
      AccountBusinessAddressData.setTSPointId(AddressMap.get("PointId"));
      System.out.println("INSIDEUPDATEADD : " + AddressMap.get("StreetAddress"));
      AccountBusinessAddressData.setTSInstallationAddressFlag(true);
      AccountBusinessAddressData.setTSShippingAddressFlag(true);
      if (CopperMaxValue.equals("2")){
          AccountBusinessAddressData.setTSHDBlockFlag(true);
          AccountBusinessAddressData.setTSHDStream(2);
          AccountBusinessAddressData.setTSSDStream(2);
          AccountBusinessAddressData.setTSVDSLBlockFlag(true);
          }
      return AccountBusinessAddressData;}


//      public void SetUpdateAddress(Map<String, String> AddressMap, String copperMaxValue){
//           SelfServiceAccountExecuteInput SelfServiceAccountExecuteInput=new SelfServiceAccountExecuteInput();
//            SelfServiceAccountExecuteInput.setExecutionMode("ForwardOnly");
//            SelfServiceAccountExecuteInput.setLOVLanguageMode("LIC");
//            SelfServiceAccountExecuteInput.setViewMode("All");
//            SelfServiceAccountExecuteInput.setListOfSSAccount(getListOfSSAccountData(AddressMap,copperMaxValue)); 
//            SelfServiceAccountExecuteOutput selfServiceAccountExecuteOutput = selfServiceAccountPort.selfServiceAccountExecute(SelfServiceAccountExecuteInput);
//
//             
//       }
      public void SetAddUpdateAddress(Map<String, String> AddressMap, String copperMaxValue){
    	  System.out.println(AddressMap.get("PointId"));
          CutAddressQuery cutAddressQuery = new CutAddressQuery();
          com.siebel.selfservice.common.address.query.QueryType queryType = new com.siebel.selfservice.common.address.query.QueryType();
          cutAddressQuery.setTSPointId(string2QueryType("='" + AddressMap.get("PointId") + "'"));
          cutAddressQuery.setId(string2QueryType(""));
          ListOfSsAddressIoQuery listOfSsAddressIoQuery = new ListOfSsAddressIoQuery();
          listOfSsAddressIoQuery.setCutAddress(cutAddressQuery);
          SelfServiceAddressQueryPageInput selfServiceAddressQueryPageInput = new SelfServiceAddressQueryPageInput();
          selfServiceAddressQueryPageInput.setListOfSsAddressIo(listOfSsAddressIoQuery);
          selfServiceAddressQueryPageInput.setLOVLanguageMode("LIC");
          selfServiceAddressQueryPageInput.setExecutionMode("ForwardOnly");
          selfServiceAddressQueryPageInput.setViewMode("All");
          selfServiceAddressQueryPageInput.setPickListName("?");
          SelfServiceAddressQueryPageOutput selfServiceAddressQueryPageOutput = selfServiceAddress.selfServiceAddressQueryPage(selfServiceAddressQueryPageInput);

          ListOfSsAddressIoData listOfSsAddressIoData = selfServiceAddressQueryPageOutput.getListOfSsAddressIo();
          if (listOfSsAddressIoData == null) {
              throw new IllegalStateException("listOfSsAddressIoData is null :" + listOfSsAddressIoData);
          }
          java.util.List<CutAddressData>cutAddressList = listOfSsAddressIoData.getCutAddress();
          if (cutAddressList == null) {
              throw new IllegalStateException("cutAddressList is null :" + cutAddressList);
          }
          for (CutAddressData cutAddressData : cutAddressList) {
              String id = cutAddressData.getId();
              System.out.println("all addresses id=" + id);
          }
          System.out.println("dataStorage.getPrimaryAddressId()=" + dataStorage.getPrimaryAddressId());
          if (cutAddressList.size() > 0) {
              System.out.println("cutAddressList.size()=" + cutAddressList.size());
              CutAddressData cutAddressData = cutAddressList.get(0);
              String id = cutAddressData.getId();
              System.out.println("id=" + id);

              if (id == null || id.length() == 0) {
                  System.out.println();
                  AddAvailableAddress();
              } else {
                  if (!id.equals(dataStorage.getPrimaryAddressId())) {
                      AddNewAddress();
                  } else {
                      System.out.println("primary address id = cutAddressId");
                      System.out.println("neither associateMoveInAddress nor CreateMoveInAddress is needed");
                  }
              }
          } else {
              System.out.println("Address is not retrieved, goto CreateMoveInAddress");
//              createMoveInAddress(moveInAddressMap);
          }
          

            
      }

      private void AddNewAddress() {

          System.out.println("associateMoveInAddress");
          AccountBusinessAddressData accountBusinessAddressData = new AccountBusinessAddressData();
          accountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
          accountBusinessAddressData.setTSInstallationAddressFlag(Boolean.TRUE);
          accountBusinessAddressData.setTSShippingAddressFlag(Boolean.TRUE);
          accountBusinessAddressData.setIsPrimaryMVG("Y");

          ListOfAccountBusinessAddressData l = new ListOfAccountBusinessAddressData();
          l.getAccountBusinessAddress().add(accountBusinessAddressData);

          AccountData accountData = getAccountData(dataStorage.getServiceAccountId());
          accountData.setListOfAccountBusinessAddress(l);

          ListOfSSAccountData listOfSSAccountData = new ListOfSSAccountData();
          listOfSSAccountData.getAccount().add(accountData);
          listOfSSAccountData.setLastpage(Boolean.FALSE);

          SelfServiceAccountExecuteInput selfServiceAccountExecuteInput = new SelfServiceAccountExecuteInput();
          selfServiceAccountExecuteInput.setListOfSSAccount(listOfSSAccountData);
          selfServiceAccountExecuteInput.setLOVLanguageMode(LDC);
          selfServiceAccountExecuteInput.setExecutionMode(BI_DIRECTIONAL);
          selfServiceAccountExecuteInput.setViewMode(ALL);

          SelfServiceAccountExecuteOutput selfServiceAccountExecuteOutput = selfServiceAccount.selfServiceAccountExecute(selfServiceAccountExecuteInput);
          // Don't care about the output
      
    	  
		// TODO Auto-generated method stub
		
	}


	private void AddAvailableAddress() {
          System.out.println("Add Available Address");
          AccountBusinessAddressData accountBusinessAddressData = new AccountBusinessAddressData();
          accountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
          accountBusinessAddressData.setTSInstallationAddressFlag(Boolean.TRUE);
          accountBusinessAddressData.setTSShippingAddressFlag(Boolean.TRUE);
          accountBusinessAddressData.setIsPrimaryMVG("Y");

          ListOfAccountBusinessAddressData l = new ListOfAccountBusinessAddressData();
          l.getAccountBusinessAddress().add(accountBusinessAddressData);

          AccountData accountData = getAccountData(dataStorage.getServiceAccountId());
          accountData.setListOfAccountBusinessAddress(l);

          ListOfSSAccountData listOfSSAccountData = new ListOfSSAccountData();
          listOfSSAccountData.getAccount().add(accountData);
          listOfSSAccountData.setLastpage(Boolean.FALSE);

          SelfServiceAccountExecuteInput selfServiceAccountExecuteInput = new SelfServiceAccountExecuteInput();
          selfServiceAccountExecuteInput.setListOfSSAccount(listOfSSAccountData);
          selfServiceAccountExecuteInput.setLOVLanguageMode(LDC);
          selfServiceAccountExecuteInput.setExecutionMode(BI_DIRECTIONAL);
          selfServiceAccountExecuteInput.setViewMode(ALL);

          SelfServiceAccountExecuteOutput selfServiceAccountExecuteOutput = selfServiceAccount.selfServiceAccountExecute(selfServiceAccountExecuteInput);
          // Don't care about the output
      }
      private AccountData getAccountData(String accountId) {
          AccountData accountData = new AccountData();
          accountData.setId(accountId);
          accountData.setOperation("skipnode");
          return accountData;
      }


	public static QueryType string2QueryType(String s) {
        QueryType q = new QueryType();
        q.setValue(s);
        return q;
    }
}
