# jlp
JLP Price Reduction Service

1. Javadoc
  The javadoc is simple due to time constraints. 
  Usually I would make it a bit fuller on some methods including in test classes

2. HTTP vs HTTPS
  I have used a simple HTTP server option. It runs on HTTP rather than HTTPS.

3. Dependencies
  To build you will require :
  a. Jackson Core and DataBind libraries v2.12.3
  b. Junit 4.13.2
  
4. Run instructions
  a. Run the main method in ng.jlp.SimpleHttpServer
  b. Open this url in a browser : http://localhost:8000/priceReduction?labelType=ShowWasNow
      --> alter the labelType as required; labelType is optional 
  c. In ng.jlp.SimpleHttpServer line 65, the data source can be switched between the 
    JLP URL or a raw data file located at data/JL.json 
    (I have not included this file in the project)
  
5. Spec and assumptions
  In some cases I have made assumptions if the spec did not indicate how to handle
  something. I have documented these in the code.
