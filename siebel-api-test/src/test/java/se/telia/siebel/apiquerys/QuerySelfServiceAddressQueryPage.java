package se.telia.siebel.apiquerys;

import java.util.List;
import java.util.Map;

import com.siebel.selfservice.common.account.SelfServiceAccount;
import com.siebel.selfservice.common.account.SelfServiceAccountExecuteInput;
import com.siebel.selfservice.common.account.SelfServiceAccountExecuteOutput;
import com.siebel.selfservice.common.account.SelfServiceAccount_Service;
import com.siebel.selfservice.common.account.data.AccountBusinessAddressData;
import com.siebel.selfservice.common.account.data.AccountData;
import com.siebel.selfservice.common.account.data.ListOfAccountBusinessAddressData;
import com.siebel.selfservice.common.account.data.ListOfSSAccountData;
import com.siebel.selfservice.common.address.SelfServiceAddress;
import com.siebel.selfservice.common.address.SelfServiceAddressQueryPageInput;
import com.siebel.selfservice.common.address.SelfServiceAddressQueryPageOutput;
import com.siebel.selfservice.common.address.SelfServiceAddress_Service;
import com.siebel.selfservice.common.address.data.CutAddressData;
import com.siebel.selfservice.common.address.data.ListOfSsAddressIoData;
import com.siebel.selfservice.common.address.query.CutAddressQuery;
import com.siebel.selfservice.common.address.query.ListOfSsAddressIoQuery;
import com.siebel.selfservice.common.address.query.QueryType;

import se.telia.siebel.data.DataStorage;

public class QuerySelfServiceAddressQueryPage {
	DataStorage dataStorage ;
	SelfServiceAddress_Service selfServiceAddress_Service;
    SelfServiceAddress selfServiceAddress;
    SelfServiceAccount_Service selfServiceAccount_service;
    SelfServiceAccount selfServiceAccount;
    private static final String BI_DIRECTIONAL = "BiDirectional";
    private static final String LDC = "LDC";
    private static final String ALL = "All";
    private static final String SKIPNODE = "skipnode";
    private static final String FORWARD_ONLY = "ForwardOnly";
    public QuerySelfServiceAddressQueryPage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        selfServiceAddress_Service = new SelfServiceAddress_Service();
        selfServiceAddress = selfServiceAddress_Service.getSelfServiceAddress();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAddress);
        selfServiceAccount_service = new SelfServiceAccount_Service();
        selfServiceAccount = selfServiceAccount_service.getSelfServiceAccount();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAccount);
    }
    public void SiebelCheckExistingAddress(Map<String, String> AddressMap,String CopperMaxValues) throws IllegalStateException {
    	CutAddressQuery cutAddressQuery = new CutAddressQuery();
        com.siebel.selfservice.common.address.query.QueryType queryType = new com.siebel.selfservice.common.address.query.QueryType();
        System.out.println("Point ID"+AddressMap.get("PointId") );
        cutAddressQuery.setTSPointId(string2QueryType("='" +AddressMap.get("PointId")  + "'"));
        cutAddressQuery.setId(string2QueryType(""));
        ListOfSsAddressIoQuery listOfSsAddressIoQuery = new ListOfSsAddressIoQuery();
        listOfSsAddressIoQuery.setCutAddress(cutAddressQuery);
        SelfServiceAddressQueryPageInput selfServiceAddressQueryPageInput = new SelfServiceAddressQueryPageInput();
        selfServiceAddressQueryPageInput.setListOfSsAddressIo(listOfSsAddressIoQuery);
        selfServiceAddressQueryPageInput.setLOVLanguageMode("LIC");
        selfServiceAddressQueryPageInput.setExecutionMode(FORWARD_ONLY);
        selfServiceAddressQueryPageInput.setViewMode(ALL);
        selfServiceAddressQueryPageInput.setPickListName("?");
        SelfServiceAddressQueryPageOutput selfServiceAddressQueryPageOutput = selfServiceAddress.selfServiceAddressQueryPage(selfServiceAddressQueryPageInput);
        ListOfSsAddressIoData listOfSsAddressIoData = selfServiceAddressQueryPageOutput.getListOfSsAddressIo();
        if (listOfSsAddressIoData == null) {
            throw new IllegalStateException("listOfSsAddressIoData is null :" + listOfSsAddressIoData);
        }
        List<CutAddressData> cutAddressList = listOfSsAddressIoData.getCutAddress();
        if (cutAddressList == null) {
            throw new IllegalStateException("cutAddressList is null :" + cutAddressList);
        }
        for (CutAddressData cutAddressData : cutAddressList) {
            String id = cutAddressData.getId();
            System.out.println("all addresses id=" + id);
        }
        if (cutAddressList.size() > 0) {
            System.out.println("cutAddressList.size()=" + cutAddressList.size());
            CutAddressData cutAddressData = cutAddressList.get(0);
            String id = cutAddressData.getId();
            System.out.println("id=" + id);
            dataStorage.setPrimaryAddressId(id);
            if (id == null || id.length() == 0) {
                System.out.println("ID is null");
                UpdateNewAddress(AddressMap,CopperMaxValues);
//                associateMoveInAddress();
            } else if(cutAddressData.getId().equalsIgnoreCase(dataStorage.getServiceAddressId())){
//            	AddExistingAddress(AddressMap);
//              
                    System.out.println("primary address id = cutAddressId");
                    
//                    System.out.println("neither associateMoveInAddress nor CreateMoveInAddress is needed");
                }else if(!cutAddressData.getId().equals(dataStorage.getServiceAddressId())){
                	AddExistingAddress(AddressMap,CopperMaxValues);
                }
            }else {
            	System.out.println("Entered Else");
            	UpdateNewAddress(AddressMap,CopperMaxValues);
            }
        } 
   

		private void AddExistingAddress(Map<String, String> AddressMap,String CopperMaxValues) {
			System.out.println("Add Existing");
			AccountBusinessAddressData accountBusinessAddressData = new AccountBusinessAddressData();
			 accountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
			 accountBusinessAddressData.setIsPrimaryMVG("Y");
			 accountBusinessAddressData.setOperation("insert");
			System.out.println(dataStorage.getPrimaryAddressId());
//			accountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
            accountBusinessAddressData.setTSInstallationAddressFlag(Boolean.TRUE);
            accountBusinessAddressData.setTSShippingAddressFlag(Boolean.TRUE);
            if (CopperMaxValues.equals("2")){
                accountBusinessAddressData.setTSHDBlockFlag(true);
                accountBusinessAddressData.setTSHDStream(2);
                accountBusinessAddressData.setTSSDStream(2);
                accountBusinessAddressData.setTSVDSLBlockFlag(true);
               System.out.println("Added values Required for XDSL");
                }
//            accountBusinessAddressData.setPostalCode(AddressMap.get("PostalCode"));
//            accountBusinessAddressData.setStreetAddress(AddressMap.get("StreetAddress"));
//            accountBusinessAddressData.setTSStreetNumber(AddressMap.get("StreetAddress2"));
//            accountBusinessAddressData.setTSEntrance(AddressMap.get("Entrance"));
//            accountBusinessAddressData.setTSApartmentNumber(AddressMap.get("ApartmentNum"));
//            accountBusinessAddressData.setCity(AddressMap.get("City"));
//            accountBusinessAddressData.setTSPointId(AddressMap.get("PointId"));
            System.out.println("INSIDEUPDATEADD : " + AddressMap.get("StreetAddress"));
//

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


			
		}
		private void UpdateNewAddress(Map<String, String> AddressMap,String CopperMaxValues) {

            System.out.println("UpdateNewAddress");
            AccountBusinessAddressData accountBusinessAddressData = new AccountBusinessAddressData();
//            accountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
            accountBusinessAddressData.setIsPrimaryMVG("Y");
            accountBusinessAddressData.setOperation("insert");
            accountBusinessAddressData.setCountry("Sweden");
//            accountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
            accountBusinessAddressData.setMainAddressFlag(true);
            accountBusinessAddressData.setTSInstallationAddressFlag(Boolean.TRUE);
            accountBusinessAddressData.setTSShippingAddressFlag(Boolean.TRUE);
            accountBusinessAddressData.setPostalCode(AddressMap.get("PostalCode"));
            accountBusinessAddressData.setStreetAddress(AddressMap.get("StreetAddress"));
            accountBusinessAddressData.setTSStreetNumber(AddressMap.get("StreetAddress2"));
            accountBusinessAddressData.setTSEntrance(AddressMap.get("Entrance"));
            accountBusinessAddressData.setTSApartmentNumber(AddressMap.get("ApartmentNum"));
            accountBusinessAddressData.setCity(AddressMap.get("City"));
            accountBusinessAddressData.setTSPointId(AddressMap.get("PointId"));
            System.out.println("INSIDEUPDATEADD : " + AddressMap.get("StreetAddress"));
            if (CopperMaxValues.equals("2")){
                accountBusinessAddressData.setTSHDBlockFlag(true);
                accountBusinessAddressData.setTSHDStream(2);
                accountBusinessAddressData.setTSSDStream(2);
                accountBusinessAddressData.setTSVDSLBlockFlag(true);
               System.out.println("Added values Required for XDSL");
                }
            
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
        
//    		QuerySelfServiceAccountExecute querySelfServiceAccount = new QuerySelfServiceAccountExecute(dataStorage);
//            querySelfServiceAccount.SetUpdateAddress(AddressMap, "");
//    		
    	}
		// TODO Auto-generated method stub
		
	
	public static QueryType string2QueryType(String s) {
        QueryType q = new QueryType();
        q.setValue(s);
        return q;
    }
	 private AccountData getAccountData(String accountId) {
	        AccountData accountData = new AccountData();
	        accountData.setId(accountId);
	        accountData.setOperation(SKIPNODE);
	        return accountData;
	    }
}
