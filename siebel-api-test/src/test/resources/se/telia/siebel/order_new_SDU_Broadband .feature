Feature: New Broadband order

  @NewBroadband
    Scenario: Create New Broadband
    This scenario used create new broadband 

    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "194012178556" to get account and billing details
    #And call QuerySelfServiceAddress  
     #	| PointId        | 121948221        |
      #| City           | MALMÖ            |
      #| StreetAddress  | KRONETORPSGATAN  |
      #| StreetAddress2 | 62               |
      #| Entrance       | C                |
      #| PostalCode     | 21226            |
      #| ApartmentNum   | 21-148-0125      |
       #
    And call GetProductDetailsService using promotionCode "P-IA-Fiber-Broadband-1743" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "Service Bundle-IA-1703-1"
    And call Addproduct to set RelationShipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 100/100-1703"
    And call EndConfigurationService and get Quote
    And call SynchronizeQuote to populate the AccessCode "200" on the quote line items for a SDU order
    And call QuoteCheckOutService and get ActiveOrderID
    Then call SubmitOrder Service and Get successful OrderID
#APITEST2019-08-09_16:26:50
