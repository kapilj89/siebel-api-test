Feature: Modify

  @move
  Scenario: Modify single broadband on xDSL
  This scenario is a bit tricky because some steps will not be executed under all circumstances
    Given call QueryAsset using SSN "194012030690" to get asset details AssetNumber and ServiceAccountId for promotionName "P-TV-IPTV-PlayPlus-1711"
    And call ModifyAssetToQuote for the RelationshipName "OptGrp-TV-TeliaPackages-1704" and Package "TV-Stor-1606" to "TV-Lagom-1606"
    And call BeginConfigurationService using product item name "Service Bundle-TV-IPTV-1704"
   	And call ModifyProduct to set RelationShipName "OptGrp-TV-TeliaPackages-1704" and Package "TV-Stor-1606" to "TV-Lagom-1606"
    And call EndConfigurationService and get Quote
    And call SynchronizeQuote for a Modify order
    And call QuoteCheckOut to convert the quote into a move order
    And call TSChannelSubmitOrder to submit the order
    #Then call CheckOrderStatus to verify the order status to be "Open"
    #And test data is reset after move order