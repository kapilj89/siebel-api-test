Feature: Simple Move

  @move
  Scenario Outline: Move single broadband on xDSL
    
    This scenario is a bit tricky because some steps will not be executed under all circumstances

    Given call QueryAllAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<Promotion>"
    When call QueryCustomer using SSN "5565567384" to get PrimaryAddressId
    And call QuerySelfServiceAccount to add end date
    And call QuerySelfServiceAddress Add/Update format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<Address>”
    And call MoveModifyAssetToQuote to create move quote using AssetNumber and ServiceAccountId
    And call QueryQuote to fetch the quote from database
    And check "<Flag>" and call BeginConfigurationService using product item name "<ServiceBundle>"
    And check "<Flag>" and call ModifyProduct to set RelationShipName "<RelationShipName>" and Package "<ProductChangeFrom>" to "<ProductchangeTo>"
    And call SynchronizeQuote to populate the due date on the quote line items for a Move order
    And call QuoteCheckOut to convert the quote into a move order
    And call TSChannelSubmitOrder to submit the order

    Examples: 
      | Stream | custType | SSN          | Address                                   | Promotion                                                                 | Flag | ServiceBundle                                        | RelationShipName                                    | ProductChangeFrom                      | ProductchangeTo                    |
      | xDSL   | B2C      | 199805130326 | SKANSBACKEN;25;;NÅS;VILLA;130316202;78693  | P-IA-xDSL-Broadband-1711;P-TV-IPTV-PlayPlus-1711;VoIP SE_Promo_Offer_1743 | N    |                                                      |                                                     |                                        |                                     |
      | SDU    | B2B      | 5562773712   | DAMMKÄRR;3;;UPPHÄRAD;VILLA;149374236;46199 | P-BIA-OF-BB Start-1827;;VoIP SE_B2B_0Months_Promo_Offer_1649              | Y    | Bredbandsaccesser                                    | Bredbandsaccesser alternativ                        | BIA-OF BredbandStart 100/100-1747      | BIA-OF BredbandStart 250/250-1747   |
      | SDU    | B2C      | 199803165134 | SKANSBACKEN;25;;NÅS;VILLA;130316202;78693 | P-IA-Fiber-Broadband-1743;P-TV-IPTV-PlayPlus-1711;P-TV-IPTV-PlayPlus-1711 | Y    | Service Bundle-IA-1703-1;Service Bundle-TV-IPTV-1704 | OptGrp-IA-speed-1703-1;OptGrp-TV-TeliaPackages-1704 | IA-Bredband 100/100-1703;TV-Lagom-1606 | IA-Bredband 1000-1703;TV-Stor-1741 |
