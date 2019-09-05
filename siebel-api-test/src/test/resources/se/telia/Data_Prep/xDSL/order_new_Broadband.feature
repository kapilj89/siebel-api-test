@BaseOrders
Feature: New Broadband for XDSL

  @xDSLBroadband
  Scenario Outline: 
  Create New Broadband for multiple SSN and please provide New Address Data and below inputs has only fb number without PSTN
    Note:AddressData format-StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode

    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "<SSN>" to get account and billing details
    And call AddServiceAdress format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"   
    And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "<ServiceBundle>"
    And call Addproduct to set RelationShipName "<RelationShipName>" and Package "<Package>"
    And call EndConfigurationService and get Quote
    And call xDSLSynchronizeQuote to populate the AccessCode "<AccessCode>" and ConnRef number "<FbNumber>" on the quote line items for a xDSL order
    And call QuoteCheckOutService and get ActiveOrderID
    Then call SubmitOrder Service and Get successful OrderID

Examples: 
| CustomerType | SSN          | AddressData                                  | promotionCode            | ServiceBundle            | RelationShipName             | Package                  | AccessCode | FbNumber           | 
| B2C          | 194103065738 | TORNFALKSGATAN;6;A;MALMÖ;214;121965883;21560 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 30-ADSL-1722 | 100        |  FB26083414        | 
| B2C          | 194103065738 | TORNFALKSGATAN;6;A;MALMÖ;214;121965883;21560 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 30-ADSL-1722 | 100        |  FB26083414        |     
| B2C          | 194103065738 | TORNFALKSGATAN;6;A;MALMÖ;214;121965883;21560 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 30-ADSL-1722 | 100        |  FB26083414        |  
| B2B          | 5561600627   | TORNFALKSGATAN;6;A;MALMÖ;214;121965883;21560 | P-BIA-xDSL-BB Start-1827 | Bredbandsaccesser        | Bredbandsaccesser alternativ | IA-Bredband 100/100-1703 | 100        |  FB26083414        |  
| B2B          | 5561601021   | TORNFALKSGATAN;6;A;MALMÖ;214;121965883;21560 | P-BIA-xDSL-BB Start-1827 | Bredbandsaccesser        | Bredbandsaccesser alternativ | IA-Bredband 100/100-1703 | 100        |  FB26083414        |
