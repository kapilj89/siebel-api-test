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
import com.siebel.selfservice.common.user.SelfServiceUser;
import com.siebel.selfservice.common.user.SelfServiceUser_Service;
import com.siebel.xml.swicustomerpartyio.Account;
import com.siebel.xml.swicustomerpartyio.ListOfAccountBusinessAddress;

public class QuerySelfServiceAccountExecute {
      DataStorage dataStorage;
      SelfServiceAccount_Service service;
      SelfServiceAccount selfServiceAccountPort;
      AccountId Acc=new AccountId();  
       public QuerySelfServiceAccountExecute(DataStorage dataStorage) {
            this.dataStorage = dataStorage;
            service = new SelfServiceAccount_Service();
            selfServiceAccountPort = service.getSelfServiceAccount();
            new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAccountPort);
         }
            
       
      public ListOfSSAccountData getListOfSSAccountData(Map<String, String> addressMap, String CopperMaxValue) {
           ListOfSSAccountData listOfSSAccountData = new ListOfSSAccountData();
         listOfSSAccountData.getAccount().add(getAccountDet(addressMap,CopperMaxValue));
         listOfSSAccountData.setLastpage(false);
         listOfSSAccountData.setRecordcount(null);
         
//         listOfSSAccountData.getAccount().add(getAccountadd());
         return listOfSSAccountData;
      }
       
       public AccountData getAccountDet(Map<String, String> AddressMap,String CopperMaxValue ) {

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
            String id=dataStorage.getServiceAccountId();

           // AccountBusinessAddressData.setId(AddressMap.get("RowID"));
            AccountBusinessAddressData.setIsPrimaryMVG("Y");
            AccountBusinessAddressData.setOperation("insert");
            AccountBusinessAddressData.setCountry("Sweden");
            AccountBusinessAddressData.setMainAddressFlag(true);
            AccountBusinessAddressData.setPostalCode(AddressMap.get("PostalCode"));
            AccountBusinessAddressData.setStreetAddress(AddressMap.get("StreetAddress"));
            AccountBusinessAddressData.setTSStreetNumber(AddressMap.get("StreetAddress2"));
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
            return AccountBusinessAddressData;
      }


      public void SetUpdateAddress(Map<String, String> AddressMap , String copperMaxValue){
           SelfServiceAccountExecuteInput SelfServiceAccountExecuteInput=new SelfServiceAccountExecuteInput();
            SelfServiceAccountExecuteInput.setExecutionMode("BiDirectional");
            SelfServiceAccountExecuteInput.setLOVLanguageMode("LDC");
            SelfServiceAccountExecuteInput.setViewMode("All");
            SelfServiceAccountExecuteInput.setListOfSSAccount(getListOfSSAccountData(AddressMap, copperMaxValue)); 
            SelfServiceAccountExecuteOutput selfServiceAccountExecuteOutput = selfServiceAccountPort.selfServiceAccountExecute(SelfServiceAccountExecuteInput);

             
       }
}
