package se.telia.siebel.apiquerys;

import se.telia.siebel.data.DataStorage;

import java.awt.List;
import java.util.Collection;

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

public class QuerySelfServiceAccount {
      DataStorage dataStorage;
      SelfServiceAccount_Service service;
      SelfServiceAccount selfServiceAccountPort;
      AccountId Acc=new AccountId();  
       public QuerySelfServiceAccount(DataStorage dataStorage) {
            this.dataStorage = dataStorage;
            service = new SelfServiceAccount_Service();
            selfServiceAccountPort = service.getSelfServiceAccount();
            new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAccountPort);
         }
             
       
      public ListOfSSAccountData getListOfSSAccountData() {
           ListOfSSAccountData listOfSSAccountData = new ListOfSSAccountData();
         listOfSSAccountData.getAccount().add(getAccountDet());
//         listOfSSAccountData.getAccount().add(getAccountadd());
         return listOfSSAccountData;
      }
       
       public AccountData getAccountDet() {

         AccountData accountData = new AccountData();
         accountData.setOperation("skipnode");
         String id=dataStorage.getServiceAccountId();
         accountData.setId(id);
         ListOfAccountBusinessAddressData AddressData=new ListOfAccountBusinessAddressData();
         AddressData.getAccountBusinessAddress().add(getAccountaddressdata());
         accountData.setListOfAccountBusinessAddress(AddressData);
         return accountData;
      }

	public AccountBusinessAddressData getAccountaddressdata() {
		AccountBusinessAddressData AccountBusinessAddressData = new AccountBusinessAddressData();
		AccountBusinessAddressData.setIsPrimaryMVG("Y");
		AccountBusinessAddressData.setOperation("update");
		// AccountBusinessAddressData.
		// AccountBusinessAddressData.setTSPointId("152983411");
		// AccountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
		AccountBusinessAddressData.setAddressId(dataStorage.getPrimaryAddressId());
		// AccountBusinessAddressData.setTSPointId("152983411");
		AccountBusinessAddressData.setTSHDBlockFlag(true);
		AccountBusinessAddressData.setTSHDStream(2);
		AccountBusinessAddressData.setTSSDStream(2);
		AccountBusinessAddressData.setTSVDSLBlockFlag(true);
		// AccountBusinessAddressData.setTSPointId("129997480");
		// AccountBusinessAddressData.setTSStreetNumber("6");
		// AccountBusinessAddressData.setStreetAddress("BÖLETVÄGEN");
		// AccountBusinessAddressData.setCity("KÅLLERED");

		// AccountBusinessAddressData.setTSEntrance("A");

		return AccountBusinessAddressData;
	}


      public void SetUpdateCopperMaxforxDSL(){
            SelfServiceAccountExecuteInput SelfServiceAccountExecuteInput=new SelfServiceAccountExecuteInput();
            SelfServiceAccountExecuteInput.setExecutionMode("ForwardOnly");
            SelfServiceAccountExecuteInput.setLOVLanguageMode("LIC");
            SelfServiceAccountExecuteInput.setViewMode("All");
            SelfServiceAccountExecuteInput.setListOfSSAccount(getListOfSSAccountData()); 
            SelfServiceAccountExecuteOutput selfServiceAccountExecuteOutput = selfServiceAccountPort.selfServiceAccountExecute(SelfServiceAccountExecuteInput);

             
       }
}

