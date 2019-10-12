@SanityMobile 

Feature: API Mobile Sanity Test 

  @tag1
  Scenario Outline: Create base order with mobile voice and Add Voice Suplementory line
   
      When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call GetProductPromotionDetailsService using promotionCode "<promotionCode>" and service "<AdditionalService>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call QuoteAddBundleItem to add a serviceBundle and get quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call UpdateConfiguration to SetAttribute "ICCID" Value of SIM "<ICCIDNumber>"
      And call EndConfigurationService and get Quote 
      And call BeginConfigurationService using product item name "<AdditionalService>"
      And call UpdateConfiguration to SetAttribute "ICCID" Value of SIM "<ICCIDNumber2>"
      And call EndConfigurationService and get Quote 
      And call AddVoice SynchronizeQuote to book number "<MobileNumber>" for serivce "<ServiceBundle>" and "<AdditionalService>" in a Mobile order
      And call QuoteCheckOutService and get ActiveOrderID
      And call TSChannelSubmitOrder to submit the order
      

    Examples: 
      | CustomerType | SSN          | promotionCode            | AdditionalService                |ServiceBundle                        |RelationShip   |ICCIDNumber |ICCIDNumber2 |MobileNumber            |
      | B2C          | 199811107136 | MT-Telia Mobil 30GB-1924 | MT-Voice SIM Service Bundle-1746 |MT-Mobile Privat Service Bundle-1743 |SIM Card Voice |574698765432|524576795325 |46730990739;46730996004 |
  
  
    # ODERID - 5-2477729448

