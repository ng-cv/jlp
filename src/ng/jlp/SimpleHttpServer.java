package ng.jlp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ng.jlp.pricereduction.PriceReductionService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

/**
 * SimpleHttpServer class to enable a simple REST service call to the PriceReductionService.
 * Note that due to time constraints I have implemented a simple HTTP server rather than HTTPS.
 */
public class SimpleHttpServer {
    public static final String PRICE_REDUCTION = "/priceReduction";
    public static final String EXIT = "/exit";
    public static final String LABEL_TYPE = "labelType";
    public static final String JLP_URL = "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses&key=AIzaSyDD_6O5gUgC4tRW5f9kxC0_76XRC8W7_mI";

    // Very simple HttpServer option
    // https://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
    // https://stackoverflow.com/questions/14729475/can-i-make-a-java-httpserver-threaded-process-requests-in-parallel

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext(EXIT, new ExitHandler());
        server.createContext(PRICE_REDUCTION, new PriceReductionHandler());
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    private static HashMap<String, String> parseQueryParams(String queryParamsString) {
        HashMap<String, String> queryParams = new HashMap<>();
        if (queryParamsString != null) {
            for (String single : queryParamsString.split("&")) {
                String[] split = single.split("=", 2);
                queryParams.put(split[0], split.length > 1 ? split[1] : "");
            }
        }
        return queryParams;
    }

    static class ExitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            System.exit(0);
        }
    }

    static class PriceReductionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("GET")) {
                HashMap<String, String> queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
                String labelType = queryParams.getOrDefault(LABEL_TYPE, "");

                String data = RetrieveData.httpGet(JLP_URL);
                //String data = RetrieveData.fileRead("data/JL.json");
                PriceReductionService service = new PriceReductionService();
                String response = service.handleRequest(data, labelType);
                byte[] responseBytes = response.getBytes(UTF_8);
                httpExchange.getResponseHeaders().put("Content-Type", asList("application/json; charset=utf-8"));
                httpExchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            }
        }
    }
}
