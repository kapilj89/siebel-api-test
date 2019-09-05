package se.telia.siebel.apiquerys;

import com.siebel.selfservice.common.account.SelfServiceAccount;
import com.siebel.selfservice.common.account.SelfServiceAccountExecuteInput;
import com.siebel.selfservice.common.account.SelfServiceAccountExecuteOutput;
import com.siebel.selfservice.common.account.SelfServiceAccount_Service;
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
import com.siebel.selfservice.common.account.data.AccountBusinessAddressData;


import se.telia.siebel.data.DataStorage;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class QueryCheckMoveInAddress {

    private static final String BI_DIRECTIONAL = "BiDirectional";
    private static final String LDC = "LDC";
    private static final String ALL = "All";
    private static final String SKIPNODE = "skipnode";
    private static final String FORWARD_ONLY = "ForwardOnly";

    DataStorage dataStorage;
    SelfServiceAddress_Service selfServiceAddress_Service;
    SelfServiceAddress selfServiceAddress;
    SelfServiceAccount_Service selfServiceAccount_service;
    SelfServiceAccount selfServiceAccount;

    public QueryCheckMoveInAddress(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        selfServiceAddress_Service = new SelfServiceAddress_Service();
        selfServiceAddress = selfServiceAddress_Service.getSelfServiceAddress();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAddress);
        selfServiceAccount_service = new SelfServiceAccount_Service();
        selfServiceAccount = selfServiceAccount_service.getSelfServiceAccount();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceAccount);
    }

    public void checkMoveInAddress(Map<String, String> moveInAddressMap) throws IllegalStateException {
        String pointId = moveInAddressMap.get("MoveInPointId");
        CutAddressQuery cutAddressQuery = new CutAddressQuery();
        com.siebel.selfservice.common.address.query.QueryType queryType = new com.siebel.selfservice.common.address.query.QueryType();
        cutAddressQuery.setTSPointId(string2QueryType("='" + pointId + "'"));
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
        System.out.println("dataStorage.getPrimaryAddressId()=" + dataStorage.getPrimaryAddressId());
        if (cutAddressList.size() > 0) {
            System.out.println("cutAddressList.size()=" + cutAddressList.size());
            CutAddressData cutAddressData = cutAddressList.get(0);
            String id = cutAddressData.getId();
            System.out.println("id=" + id);

            if (id == null || id.length() == 0) {
                System.out.println();
                associateMoveInAddress();
            } else {
                if (!id.equals(dataStorage.getPrimaryAddressId())) {
                    associateMoveInAddress();
                } else {
                    System.out.println("primary address id = cutAddressId");
                    System.out.println("neither associateMoveInAddress nor CreateMoveInAddress is needed");
                }
            }
        } else {
            System.out.println("Address is not retrieved, goto CreateMoveInAddress");
            createMoveInAddress(moveInAddressMap);
        }

    }

    public static QueryType string2QueryType(String s) {
        QueryType q = new QueryType();
        q.setValue(s);
        return q;
    }

    private void associateMoveInAddress() {
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
    }

    private void createMoveInAddress(Map<String, String> moveInAddressMap) throws IllegalStateException {
        System.out.println("createMoveInAddress");

        AccountBusinessAddressData a = new AccountBusinessAddressData();
        a.setCountry("Sweden");
        a.setCity(moveInAddressMap.get("MoveInCity"));
        a.setPostalCode(moveInAddressMap.get("MoveInPostalCode"));
        a.setStreetAddress(moveInAddressMap.get("MoveInStreetAddress"));
        a.setStreetAddress2(moveInAddressMap.get("MoveInStreetAddress2"));
        a.setTSEntrance(moveInAddressMap.get("MoveInEntrance"));
        a.setTSPointId(moveInAddressMap.get("MoveInPointId"));
        a.setTSApartmentNumber(moveInAddressMap.get("MoveInApartmentNum"));
        a.setTSInstallationAddressFlag(Boolean.TRUE);
        a.setTSShippingAddressFlag(Boolean.TRUE);
        a.setIsPrimaryMVG("Y"); // Multivalue group = MVG
        a.setOperation("insert");

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(moveInAddressMap.get("MoveOutDate"));
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            a.setStartDate(xmlGregCal);
        } catch (ParseException | DatatypeConfigurationException e) {
            e.printStackTrace();
            throw new IllegalStateException("Move out date not parsed correctly");
        }

        ListOfAccountBusinessAddressData l = new ListOfAccountBusinessAddressData();
        l.getAccountBusinessAddress().add(a);

        AccountData accountData = getAccountData(dataStorage.getServiceAccountId());
        accountData.setListOfAccountBusinessAddress(l);

        ListOfSSAccountData listOfSSAccountData = new ListOfSSAccountData();
        listOfSSAccountData.getAccount().add(accountData);
        listOfSSAccountData.setLastpage(Boolean.FALSE);

        SelfServiceAccountExecuteInput selfServiceAccountExecuteInput = new SelfServiceAccountExecuteInput();
        selfServiceAccountExecuteInput.setListOfSSAccount(listOfSSAccountData);
        selfServiceAccountExecuteInput.setLOVLanguageMode(LDC);
        selfServiceAccountExecuteInput.setExecutionMode(FORWARD_ONLY);
        selfServiceAccountExecuteInput.setViewMode(ALL);

        SelfServiceAccountExecuteOutput selfServiceAccountExecuteOutput = selfServiceAccount.selfServiceAccountExecute(selfServiceAccountExecuteInput);
        // Don't care about the output.
    }

    private AccountData getAccountData(String accountId) {
        AccountData accountData = new AccountData();
        accountData.setId(accountId);
        accountData.setOperation(SKIPNODE);
        return accountData;
    }
}
