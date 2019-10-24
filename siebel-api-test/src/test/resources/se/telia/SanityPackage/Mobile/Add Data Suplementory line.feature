@SanityMobileSupplementoryData 

Feature: Add supplementory data on MBB Promotion 

  Scenario Outline: Create base order MBB and Add Data Suplementory line
   
      When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call GetProductPromotionDetailsService using promotionCode "<promotionCode>" and service "<AdditionalService>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call QuoteAddBundleItem to add a serviceBundle and get quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call UpdateConfiguration to SetAttribute "ICCID" Value of SIM "<ICCIDNumber>"
      And call UpdateConfiguration to SetAttribute "Email" Value of SIM "<MailID>"
      And call EndConfigurationService and get Quote 
      And call BeginConfigurationService using product item name "<AdditionalService>"
      And call UpdateConfiguration to SetAttribute "ICCID" Value of SIM "<ICCIDNumber2>"
      And call EndConfigurationService and get Quote 
      And call AddVoice SynchronizeQuote to book number "<MobileNumber>" for serivce "<ServiceBundle>" and "<AdditionalService>" in a Mobile order
      And call QuoteCheckOutService and get ActiveOrderID
      And call TSChannelSubmitOrder to submit the order
      

    Examples: 
       | SSN          | promotionCode                      | AdditionalService                      |ServiceBundle                     |MailID     |ICCIDNumber |ICCIDNumber2 |MobileNumber                    |
       | 199811107136 | MT-MBB Privat 20 GB no commit-1741 | MB-Data SIM Privat Service Bundle-1741 |MT-MBB Privat Service Bundle-1741 |kzx@cg.com |574698765432|524576795325 |467100999000369;467100999000374 |
  
  

