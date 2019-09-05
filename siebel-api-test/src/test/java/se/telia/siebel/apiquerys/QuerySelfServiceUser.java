package se.telia.siebel.apiquerys;

import com.siebel.selfservice.common.user.SelfServiceUser;
import com.siebel.selfservice.common.user.SelfServiceUserQueryPageInput;
import com.siebel.selfservice.common.user.SelfServiceUserQueryPageOutput;
import  com.siebel.selfservice.common.user.SelfServiceUser_Service;

import com.siebel.selfservice.common.user.query.ListOfSSUserQuery;
import com.siebel.selfservice.common.user.query.QueryType;
import com.siebel.selfservice.common.user.query.UserQuery;
import se.telia.siebel.data.DataStorage;


public class QuerySelfServiceUser {
   DataStorage dataStorage;
   SelfServiceUser_Service service;
   SelfServiceUser selfServiceUserPort;

   public QuerySelfServiceUser(DataStorage dataStorage) {
      this.dataStorage = dataStorage;
      service = new SelfServiceUser_Service();
      selfServiceUserPort = service.getSelfServiceUser();
      new SiebelSoapCommunication(dataStorage).setupSoapCommunication(selfServiceUserPort);
   }

   public String getPrimaryOrganizationId(String loginName) {
      SelfServiceUserQueryPageInput selfServiceUserQueryPageInput = new SelfServiceUserQueryPageInput();
      selfServiceUserQueryPageInput.setLOVLanguageMode("LDC");
      selfServiceUserQueryPageInput.setExecutionMode("ForwardOnly");
      selfServiceUserQueryPageInput.setViewMode("All");

      ListOfSSUserQuery listOfSSUserQuery = new ListOfSSUserQuery();
      UserQuery userQuery = new UserQuery();
      QueryType queryType = new QueryType();
      queryType.setValue("='"+loginName+"'");
      userQuery.setLoginName(queryType);
      queryType = new QueryType();
      userQuery.setPrimaryOrganizationId(queryType); // Needs to be set or else it is not in the output later
      listOfSSUserQuery.setUser(userQuery);
      selfServiceUserQueryPageInput.setListOfSSUser(listOfSSUserQuery);

      SelfServiceUserQueryPageOutput selfServiceUserQueryPageOutput = selfServiceUserPort.selfServiceUserQueryPage(selfServiceUserQueryPageInput);
      String primaryOrganizationId = selfServiceUserQueryPageOutput.getListOfSSUser().getUser().get(0).getPrimaryOrganizationId();
      return primaryOrganizationId;
   }
}

