Feature: TeliaPlay stand alone order

  @TeliaPlay
  Scenario: Create  TeliaPlay stand alone
    This scenario used create new TeliaPlay stand alone

    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "199805133270" to get account and billing details
    And call GetProductDetailsService using promotionCode "P-TV-PlayPlus-1711" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "Service Bundle-TV-PlayPlus-1704"
    And call Addproduct to set RelationShipName "OptGrp-TV-PlayPlusPackages-1704" and Package "TV-PlayPlusHBO-1701"
    And call EndConfigurationService and get Quote
    And call SynchronizeQuote to populate the due date on the quote line items for a Mobile order
    And call QuoteCheckOutService and get ActiveOrderID
    Then call SubmitOrder Service and Get successful OrderID

