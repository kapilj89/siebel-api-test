Feature: IPTV on LAN SDU access, with hardware (TV-box)

  @SDUrouter+TV-box
  Scenario: Create New IPTV on LAN SDU access, with hardware (TV-box)
    This scenario used create new IPTV on LAN SDU access, with hardware (TV-box)

    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "199805136943" to get account and billing details
    And call GetProductDetailsService using promotionCode "P-TV-IPTV-PlayPlus-1711" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "Service Bundle-TV-IPTV-1704"
    And call Addproduct to set RelationShipName "OptGrp-TV-TeliaPackages-1704" and Package "TV-Stor-1606"
		And call EndConfigurationService and get Quote
    And call GetProductDetailsService using promotionCode "P-HW-IPTV-1907" and get ProductId, PriceList
    And call QuoteAddItems and get quote
    And call BeginConfigurationService using product item name "Service Bundle-TV-Hardware-1704"
		And call Addproduct to set RelationShipName "TV-Arris2853-1606" and Package "TV-Arris2853-1606"
		And call EndConfigurationService and get Quote
    And call SynchronizeQuote to populate the AccessCode "200" on the quote line items for a SDU order
    And call QuoteCheckOutService and get ActiveOrderID
    Then call SubmitOrder Service and Get successful OrderID

