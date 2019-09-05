Feature: Modify

  @modifyBB
  Scenario: Modify single broadband on xDSL
    This scenario is a bit tricky because some steps will not be executed under all circumstances

    Given call QueryAsset using SSN "194103063238" to get asset details AssetNumber and ServiceAccountId for promotionName "P-IA-Fiber-Broadband-1743"
    And call ModifyAssetToQuote for the RelationshipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 100/100-1703" to "IA-Bredband 250-1703"
    And call BeginConfigurationService using product item name "Service Bundle-IA-1703-1"
    And call ModifyProduct to set RelationShipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 100/100-1703" to "IA-Bredband 10/10-1725"
    And call EndConfigurationService and get Quote
    And call SynchronizeQuote for a Modify order
    And call QuoteCheckOut to convert the quote into a move order
    And call TSChannelSubmitOrder to submit the order
