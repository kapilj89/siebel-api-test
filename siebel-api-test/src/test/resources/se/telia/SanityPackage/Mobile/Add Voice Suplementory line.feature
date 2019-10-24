@SanityMobileSupplementoryVoice 

Feature: Add supplementory voice on mobile voice 

  Scenario Outline: Create base order with mobile voice and Add Voice Suplementory line
   
      When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call GetProductPromotionDetailsService using promotionCode "<promotionCode>" and service "<AdditionalService>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
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
       | SSN          | promotionCode            | AdditionalService                |ServiceBundle                        |ICCIDNumber |ICCIDNumber2 |MobileNumber            |
       | 199806166774 | MT-Telia Mobil 30GB-1924 | MT-Voice SIM Service Bundle-1746 |MT-Mobile Privat Service Bundle-1743 |574698765432|524576795325 |46730990739;46730996004 |
  
  

