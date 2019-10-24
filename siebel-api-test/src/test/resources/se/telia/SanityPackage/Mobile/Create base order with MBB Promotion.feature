@SanityMobileBaseMBB 

Feature: API Mobile Sanity Test 

  Scenario Outline: Create base order with MBB Promotion
   
      When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call UpdateConfiguration to SetAttribute "ICCID" Value of SIM "<ICCIDNumber>"
      And call UpdateConfiguration to SetAttribute "Email" Value of SIM "<E-mailID>"
      And call EndConfigurationService and get Quote
      And call SynchronizeQuote to book number "<MobileNumber>" for serivce "<ServiceBundle>" in a Mobile order
      And call QuoteCheckOutService and get ActiveOrderID
      And call TSChannelSubmitOrder to submit the order
      

 Examples: 
      | SSN          | promotionCode                      | ServiceBundle                     | E-mailID   | ICCIDNumber  | MobileNumber    | 
      | 199806166774 | MT-MBB Privat 20 GB no commit-1741 | MT-MBB Privat Service Bundle-1741 | kzx@cg.com | 574698765432 | 467100060018472 | 
      | 199806166774 | MT-MBB Privat 20 GB no commit-1741 | MT-MBB Privat Service Bundle-1741 | kzx@cg.com | 574698765432 | 467100060018472 | 
      | 199806166774 | MT-MBB Privat 20 GB no commit-1741 | MT-MBB Privat Service Bundle-1741 | kzx@cg.com | 574698765432 | 467100060018472 | 
      | 199806166774 | MT-MBB Privat 40 GB no commit-1741 | MT-MBB Privat Service Bundle-1741 | kzx@cg.com | 574698765432 | 467100060018472 | 
      | 199806166774 | MT-MBB Privat 40 GB no commit-1741 | MT-MBB Privat Service Bundle-1741 | kzx@cg.com | 574698765432 | 467100060018472 | 
  
  
  
