# jlp
JLP Price Reduction Service

1. Javadoc<br>
  The javadoc is simple due to time constraints.<br>
  Usually I would make it a bit fuller on some methods including in test classes<br>

2. HTTP vs HTTPS<br>
  I have used a simple HTTP server option. It runs on HTTP rather than HTTPS.<br>

3. Dependencies<br>
  To build you will require :<br>
  a. Jackson Core and DataBind libraries v2.12.3<br>
  b. Junit 4.13.2<br>
  
4. Run instructions<br>
  a. Run the main method in ng.jlp.SimpleHttpServer<br>
  b. Open this url in a browser : http://localhost:8000/priceReduction?labelType=ShowWasNow<br>
      --> alter the labelType as required; labelType is optional <br>
  c. In ng.jlp.SimpleHttpServer line 65, the data source can be switched between the JLP URL or a raw data file located at data/JL.json<br>
    (I have not included this file in the project)<br>
  
5. Spec and assumptions<br>
  In some cases I have made assumptions if the spec did not indicate how to handle something. I have documented these in the code.<br>
