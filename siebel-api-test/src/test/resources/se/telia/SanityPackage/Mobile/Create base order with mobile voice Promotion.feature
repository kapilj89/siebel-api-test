@SanityMobileBaseMobileVoice 

Feature: API Mobile Sanity Test 

  Scenario Outline: Create base order with mobile voice 
   
      When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call UpdateConfiguration to SetAttribute "ICCID" Value of SIM "<ICCIDNumber>"
      And call EndConfigurationService and get Quote
      And call SynchronizeQuote to book number "<MobileNumber>" for serivce "<ServiceBundle>" in a Mobile order
      And call QuoteCheckOutService and get ActiveOrderID
      And call TSChannelSubmitOrder to submit the order
      

   Examples: 
      | SSN          | promotionCode            | ServiceBundle                        | ICCIDNumber  | MobileNumber | 
      | 199806166774 | MT-Telia Mobil 30GB-1924 | MT-Mobile Privat Service Bundle-1743 | 574698765432 | 46730990947  | 
      | 199806166774 | MT-Telia Mobil 30GB-1924 | MT-Mobile Privat Service Bundle-1743 | 574698765432 | 46730990947  | 
      | 199806166774 | MT-Telia Mobil 30GB-1924 | MT-Mobile Privat Service Bundle-1743 | 574698765432 | 46730990947  | 
      | 199806166774 | MT-Telia Mobil 30GB-1924 | MT-Mobile Privat Service Bundle-1743 | 574698765432 | 46730990947  | 
      | 199806166774 | MT-Telia Mobil 30GB-1924 | MT-Mobile Privat Service Bundle-1743 | 574698765432 | 46730990947  | 
      | 199806166774 | MT-Telia Mobil 30GB-1924 | MT-Mobile Privat Service Bundle-1743 | 574698765432 | 46730990947  | 
  
  
  

