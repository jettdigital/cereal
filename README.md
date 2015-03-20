#Cereal#
A simple API for managing uniquely identifiable objects moving to/from different states modeled after double-entry accounting. Any number of organizations and states can be created and a pedigree of arbitrary content ( usually JSON ) is maintained throughout the state changes.

##Use case example##
Imagine you have a bike factory and a couple of distributors. Each distributor has a wholesaler network who in turn have retailers that sell bikes to consumers. You want to know what went on with one bike from assembly at the factory all the way to the consumer.
1. Setup
  1. Organizations are created for the factory, wholesalers, and retailers.
  2. States for each org are created, for example the factory org will may have an ASSEMBLY state and a retailer org may have an INVENTORY state.
2. Bike lifecycle
  1. The factory comes up with the serial number for a new bike
  2. The factory creates a serial and any associated data in the API for its org
  3. When the bike parts are ready to be assembled the factory posts a transaction moving the bike to the ASSEMBLY state
  4. When the bike is finished the factory posts another transaction moving the bike to a PACKAGING state
  5. Now the bike is stored waiting for shipping so a transaction is posted moving the bike to a INVENTORY state 
  6. The bike is now being shipped to a retailer and so is posted to the SHIPPED state
  7. When the retailer receives the bike it posts a transaction moving the bike to the retailer's own RECEIVED state. The retailer also adds some of its own information to the transaction log
  8. The retailer checks the bike over and then moves it to the INVENTORY state
  9. As room opens up on the show room floor the bike is put up for sale and a transaction is posted moving the bike to DISPLAY
  10. A customer buys the bike and after the purchase is complete the bike  a transaction is posted moving it to PURCHASED
  
When it's all said and done the retailer or the factory can go back and get a history of every state, including any associated data, the bike was in. Each step it took along the way from assembly to purchase is available for review and verification. If the data associated with the serial object is digitally signed then a pedigre can be established showing a paper trail of the entire life cycle.

## API ##
coming soon...