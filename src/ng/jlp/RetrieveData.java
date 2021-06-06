package ng.jlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * RetrieveData class to retrieve the input data from a URL or a file.
 */
public class RetrieveData {

    private static void safeDisconnect(HttpURLConnection connection) {
        try {
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("WARNING : HttpURLConnection disconnect failed");
        }
    }

    public static String httpGet(String url) {
        HttpURLConnection connection = null;
        try {
            URI uri = new URI(url);
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(5000);
            connection.connect();
            if(connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String response = bufferedReader.lines().collect(Collectors.joining("\n"));
                return response;
            }
            return "";
        } catch (IOException | URISyntaxException e) {
            System.out.println("ERROR : URL request failed");
            e.printStackTrace();
            return "";
        } finally {
            safeDisconnect(connection);
        }
    }

    public static String fileRead(String filename) {
        try {
            Path path = Paths.get(filename);
            byte[] data = Files.readAllBytes(path);
            String input = new String(data);
            return input;
        } catch (IOException e) {
            System.out.println("ERROR : File load failed");
            e.printStackTrace();
            return "";
        }
    }

}
