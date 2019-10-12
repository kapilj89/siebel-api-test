@SanityMobile 

Feature: API Mobile Sanity Test 

  @tag1
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
      | CustomerType | SSN          | promotionCode            |ServiceBundle                        |RelationShip   | Package       | ICCIDNumber | MobileNumber |
      | B2C          | 199811107136 | MT-Telia Mobil 30GB-1924 |MT-Mobile Privat Service Bundle-1743 |SIM Card Voice | TV-Lagom-1606 | 574698765432| 46730990947  |
  
  
    

