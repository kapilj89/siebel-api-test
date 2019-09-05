Feature: New Broadband stand alone 100/100 Mbit LAN SDU access, with hardware (router) order

  @NewBroadbandwithHardware
  Scenario: Create New Broadband Stand alone with 100/100 Mbit LAN SDU access ,with hardware (router)
    This scenario used create new broadband standalone with 100/100 Mbit LAN SDU Access with hardware

    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "199805133270" to get account and billing details
    And call GetProductDetailsService using promotionCode "P-IA-Fiber-Broadband-1743" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "Service Bundle-IA-1703-1"
    And call Addproduct to set RelationShipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 100/100-1703"
    #And call Addproduct to set RelationShipID "1-2ISKBNG" and ProductID "1-IFWD"
    And call EndConfigurationService and get Quote
    And call GetProductDetailsService using promotionCode "P-HW-RGW-1743" and get ProductId, PriceList
    And call QuoteAddItems and get quote
    And call BeginConfigurationService using product item name "Service Bundle-IA-router-1703-1"
    #And call Addproduct to set RelationShipID "1-2ISKBRO" and ProductID "1-1DWF3B"
    And call Addproduct to set RelationShipName "OptGrp-IA-Router-1703-1" and Package "CPE-Wifi-router-TG789-1711"  
    And call EndConfigurationService and get Quote
    And call SynchronizeQuote to populate the AccessCode "200" on the quote line items for a SDU order
    And call QuoteCheckOutService and get ActiveOrderID
    Then call SubmitOrder Service and Get successful OrderID

