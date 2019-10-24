Feature: Sanity Test for(Primary)SDU New BB and IPTV

  @SanitySDUBroadbandIPTV
  Scenario Outline: 
    Create New Broadband and IPTV order
    This scenario used create new broadband and IPTV order
    Note:AddressData format-

    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "<SSN>" to get account and billing details
    And call AddressAddorupdate with format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"
    And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "<ServiceBundle>"
    And call Addproduct to set RelationShipName "<RelationShipName>" and Package "<Package>"
    And call EndConfigurationService and get Quote
    And call GetProductDetailsService using promotionCode "<promotionCodeIPTV>" and get ProductId, PriceList
    And call QuoteAddItems and get quote
    And call BeginConfigurationService using product item name "Service Bundle-TV-IPTV-1704"
    And call Addproduct to set RelationShipName "<RelationShipTV>" and Package "<PackageTV>"
    And call EndConfigurationService and get Quote
    And call SDU BBIPTV SynchronizeQuote to populate the AccessCode "<AccessCode>" on the quote line items for a SDU order
    And call QuoteCheckOutService and get ActiveOrderID
    And call TSChannelSubmitOrder to submit the order

    Examples: 
      | CustomerType | SSN          | AddressData                                | promotionCode             | ServiceBundle            | RelationShipName       | Package                  | promotionCodeIPTV       | RelationShipTV               | PackageTV     | AccessCode |
      | B2C          | 199810234212 | BÅBERG;;;TROLLHÄTTAN;VILLA;130397808;46177 | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 |        200 |