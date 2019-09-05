Feature: New Broadband stand alone 30 Mbit XDSL, with hardware (router) order

  @xDSLBroadbandwithHardware
  Scenario: Create New Broadband stand alone 30 Mbit XDSL, with hardware (router) order
    This scenario used create new broadband stand alone 30 Mbit XDSL, with hardware (router) order

    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "199806303963" to get account and billing details
    And call GetProductDetailsService using promotionCode "P-IA-xDSL-Broadband-1711" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "Service Bundle-IA-1703-1"
    And call Addproduct to set RelationShipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 30-ADSL-1722"
    And call EndConfigurationService and get Quote
    And call GetProductDetailsService using promotionCode "P-HW-RGW-1743" and get ProductId, PriceList
    And call QuoteAddItems and get quote
    And call BeginConfigurationService using product item name "Service Bundle-IA-router-1703-1"
    And call Addproduct to set RelationShipName "OptGrp-IA-Router-1703-1" and Package "CPE-Wifi-router-TG789-1711"  
    And call EndConfigurationService and get Quote
    And call xDSLSynchronizeQuote to populate the AccessCode "100" and ConnRef number "FB26083414" on the quote line items for a xDSL order
    #And call QuoteCheckOutService and get ActiveOrderID
    #Then call SubmitOrder Service and Get successful OrderID

