package se.telia.siebel.apiquerys;

import se.telia.siebel.data.DataStorage;

import java.awt.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
         AccountBusinessAddressData AccountBusinessAddressData=new AccountBusinessAddressData();
         AccountBusinessAddressData.setIsPrimaryMVG("Y");
           AccountBusinessAddressData.setOperation("update");
           AccountBusinessAddressData.setId(dataStorage.getPrimaryAddressId());
           DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           Date date = null;
           try {
           	Calendar cl = Calendar.getInstance();
//   			cl.add(Calendar.MINUTE,-21);
   			String newtime =format.format(cl.getTime()); 
               XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(newtime);
               AccountBusinessAddressData.setEndDate(xmlGregCal);
           } catch (DatatypeConfigurationException e) {
               e.printStackTrace();
               throw new IllegalStateException("Move in date not parsed correctly");
           }
            return AccountBusinessAddressData;
      }


      public void AddEndDateInExistingAddress(){
            SelfServiceAccountExecuteInput SelfServiceAccountExecuteInput=new SelfServiceAccountExecuteInput();
            SelfServiceAccountExecuteInput.setExecutionMode("ForwardOnly");
            SelfServiceAccountExecuteInput.setLOVLanguageMode("LIC");
            SelfServiceAccountExecuteInput.setViewMode("All");
            SelfServiceAccountExecuteInput.setListOfSSAccount(getListOfSSAccountData()); 
            SelfServiceAccountExecuteOutput selfServiceAccountExecuteOutput = selfServiceAccountPort.selfServiceAccountExecute(SelfServiceAccountExecuteInput);
       }
}

