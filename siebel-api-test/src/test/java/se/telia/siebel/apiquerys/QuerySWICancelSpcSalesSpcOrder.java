package se.telia.siebel.apiquerys;



import com.siebel.customui.SWICancelSalesOrderInput;
import com.siebel.customui.SWICancelSpcSalesSpcOrder;
import com.siebel.customui.SWICancelSpcSalesSpcOrder_Service;
import com.siebel.ordermanagement.order.data.Order;
import se.telia.siebel.data.DataStorage;

public class QuerySWICancelSpcSalesSpcOrder {
    DataStorage dataStorage ;
    SWICancelSpcSalesSpcOrder_Service service;
    SWICancelSpcSalesSpcOrder port;
    public QuerySWICancelSpcSalesSpcOrder(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        service=new SWICancelSpcSalesSpcOrder_Service();
        port = service.getSWICancelSpcSalesSpcOrder();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(port);
    }


    public boolean cancelOrder(String activeOrderID) {

        QueryOrder queryOrder = new QueryOrder(dataStorage);
        Order order = queryOrder.getOrderById(activeOrderID);

        // Uppdate the order with the cancle reson
        order.setCancelReason("No Reason");
        queryOrder.synchronizeOrder(order);

        // Cancle the order
        SWICancelSalesOrderInput swiCancelSalesOrderInput =new SWICancelSalesOrderInput();
        swiCancelSalesOrderInput.setObjectSpcId(order.getId());
        port.swiCancelSalesOrder(swiCancelSalesOrderInput);

        // Now wait for the cancellation to get through
        int retry=0;
        String status="";
        do {
            System.out.println("Waiting for orderstatus to become Cancelled, "+retry);
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Order tmpOrder = queryOrder.getOrderById(dataStorage.getActiveOrderId());
            status = tmpOrder.getStatus();
            System.out.println("Orderstatus is now " + status);
            ++retry;
        }
        while (! "Cancelled".equals(status) && retry < 30);
        return ("Cancelled".equals(status));
    }
}
