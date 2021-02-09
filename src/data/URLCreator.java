package data;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class URLCreator {

    public static void sendPostRequest(String address, String[] values) throws IOException, InterruptedException {
        //Setting up the connection
        URL url = new URL(address);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        //Setting up and convert the values which will be send
        Map<String,String> arguments = new HashMap<>();
        arguments.put("recid", values[0]);
        arguments.put("packaging", values[1]);
        StringJoiner sj = new StringJoiner("&");
        for(Map.Entry<String,String> entry : arguments.entrySet()) {
            sj.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        //Sending data
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        boolean connect = true;
        while(connect) {
            try {
                http.connect();
                connect = false;
            } catch (BindException e) {
                Thread.sleep(5000);
            }
        }

        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }

        if(http.getResponseCode() == 400) {
            //System.out.println(values[0] + " " + values[1] + " " + values[2] + " " + values[3] + " " + values[4] + " " + values[5]);
        }

        //Closing connection
        http.disconnect();
    }

}
