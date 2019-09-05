Feature: Modify

  @move
  Scenario: Modify single broadband on xDSL
  This scenario is a bit tricky because some steps will not be executed under all circumstances
    Given call QueryAsset using SSN "194103063238" to get asset details AssetNumber and ServiceAccountId for promotionName "P-IA-Fiber-Broadband-1743"
    #When call QueryCustomer using SSN "194612039539" to get PrimaryAddressId
    And call ModifyAssetToQuote for the RelationshipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 100/100-1703" to "IA-Bredband 250-1703"
    #And call QueryQuote to fetch the quote from database
    And call BeginConfigurationService using product item name "Service Bundle-IA-1703-1"
   	And call ModifyProduct to set RelationShipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 1000-1703" to "IA-Bredband 500-1703"
     #And call UpdateConfigurationSetAttribute to set commitment duration to "24" months for product item name "MT-Komplett Privat 40GB-1746"
     And call EndConfigurationService and get Quote
    And call SynchronizeQuote for a Modify order
    And call QuoteCheckOut to convert the quote into a move order
    #And call GetOrder to get the order and assert orderDueDate and moveOutDate
    And call TSChannelSubmitOrder to submit the order
    #Then call CheckOrderStatus to verify the order status to be "Open"
    #And test data is reset after move order