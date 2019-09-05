package se.telia.siebel.apiquerys;


import com.siebel.ordermanagement.order.*;
import com.siebel.ordermanagement.order.data.Order;
import com.siebel.ordermanagement.order.data.ListOfOrder;
import com.siebel.ordermanagement.order.data.ListOfOrderHeader;
import org.junit.Assert;
import se.telia.siebel.data.DataStorage;

public class QueryOrder {
    DataStorage dataStorage ;
    OrderWebService orderWebService;
    OrderPort orderPort;
    public QueryOrder(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        orderWebService=new OrderWebService();
        orderPort = orderWebService.getOrderPort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(orderPort);
    }


    public Order getOrderById(String activeOrderId) {
        GetOrderByIdInput getOrderByIdInput=new GetOrderByIdInput();
        getOrderByIdInput.setPrimaryRowId(activeOrderId);
        GetOrderByIdOutput orderByIdOutput = orderPort.getOrderById(getOrderByIdInput);
        Order order = orderByIdOutput.getListOfOrder().getListOfOrderHeader().getOrder();
        Assert.assertNotNull("Order is null after getOrderById",order);
        return order;
    }
    public void synchronizeOrder(Order order){
        SynchronizeOrderInput synchronizeOrderInput = new SynchronizeOrderInput();
        ListOfOrderHeader listOfOrderHeader = new ListOfOrderHeader();
        listOfOrderHeader.setOrder(order);
        ListOfOrder listOfOrder = new ListOfOrder();
        listOfOrder.setListOfOrderHeader(listOfOrderHeader);
        synchronizeOrderInput.setListOfOrder(listOfOrder);
        SynchronizeOrderOutput synchronizeOrderOutput = orderPort.synchronizeOrder(synchronizeOrderInput);
        // Just read something to verify that the request was OK.
        String id =  synchronizeOrderInput.getListOfOrder().getListOfOrderHeader().getOrder().getId();
        System.out.println("After synchronizeOrder, order id is "+id);
    }

}
