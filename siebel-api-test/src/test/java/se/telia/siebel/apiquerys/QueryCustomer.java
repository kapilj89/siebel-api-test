package se.telia.siebel.apiquerys;

import com.siebel.customui.accountmanagement.query.QueryCustomerInput;
import com.siebel.customui.accountmanagement.query.QueryCustomerOutput;
import com.siebel.customui.accountmanagement.query.TSC2BQueryCustomerService;
import com.siebel.xml.swicustomerpartyio.Account;
import com.siebel.xml.swicustomerpartyio.ComInvoiceProfile;
import com.siebel.xml.swicustomerpartyio.ListOfSwicustomerpartyio;
import com.siebel.xml.tsswicustomerpartyio_lw.*;
import se.telia.siebel.data.AccountDetails;
import se.telia.siebel.data.DataStorage;


import java.util.List;

public class QueryCustomer {
    DataStorage dataStorage ;
    TSC2BQueryCustomerService service;
    com.siebel.customui.accountmanagement.query.QueryCustomer queryCustomerPort;

    public QueryCustomer(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
        TSC2BQueryCustomerService service = new TSC2BQueryCustomerService();
        queryCustomerPort = service.getQueryCustomer();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(queryCustomerPort);

    }

    public String getPrimaryAddressId(String ssn) {

        QueryCustomerInput queryCustomerInput = new QueryCustomerInput();
        queryCustomerInput.setSSN(ssn);
        queryCustomerInput.setFullStructure("1");
        QueryCustomerOutput queryCustomerOutput = queryCustomerPort.queryCustomer(queryCustomerInput);
        ListOfSwicustomerpartyioLw listOfSwicustomerpartyioLw = queryCustomerOutput.getListOfSwicustomerpartyioLw();
        List<Accounts> accountList = listOfSwicustomerpartyioLw.getAccounts();
        boolean serviceAccountFound = false;
        for (Accounts acc : accountList) {
            if (acc.getRowId() != null && acc.getRowId().equals(dataStorage.getServiceAccountId())) {
                serviceAccountFound = true;
                // Use java lambda expression to filer the list and find the primary address
                AccountBusinessAddresses accountBusinessAddresses =
                        acc.getListOfAccountBusinessAddresses().getAccountBusinessAddresses().stream()
                                .filter(e -> "Y".equals(e.getIsPrimaryMVG()))
                                .findFirst()
                                .get();
                if (accountBusinessAddresses==null) {
                    return null;
                }
                String primaryAddressId = accountBusinessAddresses.getId();
                dataStorage.setPrimaryAddressId(primaryAddressId);
                return primaryAddressId;
            }
        }
        return null;
    }

    public AccountDetails getGetAccountDetailsString(String ssn){

        AccountDetails accountDetails=new AccountDetails();
        QueryCustomerInput queryCustomerInput = new QueryCustomerInput();
        queryCustomerInput.setSSN(ssn);
        queryCustomerInput.setFullStructure("Y");
        queryCustomerInput.setTSCustomerId("");
        QueryCustomerOutput queryCustomerOutput = queryCustomerPort.queryCustomer(queryCustomerInput);
        ListOfSwicustomerpartyio listOfSwicustomerpartyio = queryCustomerOutput.getListOfSwicustomerpartyio();
        List<Account> account = listOfSwicustomerpartyio.getAccount();
        if (account.size()==0) {
            System.out.println("No accounts found.");
            return null;
        }
        accountDetails.setCustomerAccount( account.get(0).getMasterAccountId());
//        if (account.get(0).getPrimaryAddressId()) {
//            System.out.println("No accounts found.");
//            return null;
//        }
        for (Account acc : account) {
            if ("Billing".equals(acc.getAccountTypeCode())){
                // /Account/ListOfComInvoiceProfile/ComInvoiceProfile/AccountId
                accountDetails.setBillingAccount( acc.getListOfComInvoiceProfile().getComInvoiceProfile().get(0).getAccountId());
                accountDetails.setBillingProfile( acc.getPrimaryBillingProfileId());
            }
        }
        for (Account acc : account) {
            if ("Service".equals(acc.getAccountTypeCode())){
                accountDetails.setServiceAccount(acc.getListOfPosition().getPosition().get(0).getAccountId());
                accountDetails.setPrimaryContact(acc.getPrimaryContactId());
                dataStorage.setServiceAddressId( acc.getPrimaryAddressId());
                dataStorage.setServiceAccountId(acc.getAccountNumber());
            }
        }

        // /Account/ListOfPosition/Position/AccountId




        return accountDetails;
    }

}
