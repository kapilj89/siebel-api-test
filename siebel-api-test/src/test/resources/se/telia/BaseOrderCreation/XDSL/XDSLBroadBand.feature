@BaseOrders
Feature: New Broadband stand alone 30 Mbit XDSL, with hardware (router) order

@xDSLBroadband
  Scenario Outline: 
  Create New Broadband stand alone 30 Mbit XDSL, with hardware (router) order
  This scenario used create new broadband stand alone 30 Mbit XDSL, with hardware (router) order
  Note:AddressData format-
  
     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      #And call AddServiceAdress format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"    
      And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call Addproduct to set RelationShipName "<RelationShipName>" and Package "<Package>"
      And call EndConfigurationService and get Quote
      And call xDSL DataPrep SynchronizeQuote to populate the AccessCode "<AccessCode>" PSTN "<PSTN>" and ConnRef number "<FbNumber>" on the quote line items for a xDSL order
      And call QuoteCheckOutService and get ActiveOrderID
      Then call SubmitOrder Service and Get successful OrderID
  
    Examples: 
      | CustomerType | SSN          | AddressData                                    | promotionCode            | ServiceBundle            | RelationShipName             | Package                             | AccessCode | FbNumber   | PSTN       |
      | B2B          | 5565235271   | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-BIA-xDSL-BB Start-1827 | Bredbandsaccesser        | Bredbandsaccesser alternativ | BIA-xDSL BredbandStart 30-ADSL-1747 | 100        | FB26083414 | 4615721953 |
      #| B2B          | 5565235271   | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-BIA-xDSL-BB Start-1827 | Bredbandsaccesser        | Bredbandsaccesser alternativ | BIA-xDSL BredbandStart 30-ADSL-1747 | 100        | FB26083414 | 					 |
      #| B2C          | 194103063329 | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 30-ADSL-1722            | 100        | FB26083414 |            |
      #| B2C          | 194103063329 | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 30-ADSL-1722            | 100        | FB26083414 | 					 |
      #| B2C          | 194103063329 | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 30-VDSL-1722            | 100        | FB26083414 |            |
      #| B2B          | 5565235271   | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-BIA-xDSL-BB Start-1827 | Bredbandsaccesser        | Bredbandsaccesser alternativ | BIA-xDSL BredbandStart 30-ADSL-1747 | 100        | FB26083414 | 4615721953 |
      #| B2B          | 5565235271   | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-BIA-xDSL-BB Start-1827 | Bredbandsaccesser        | Bredbandsaccesser alternativ | BIA-xDSL BredbandStart 30-ADSL-1747 | 100        | FB26083414 | 4615721953 |
      #| B2C          | 194103063329 | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 30-ADSL-1722            | 100        | FB26083414 | 4615721953 |
  
  
