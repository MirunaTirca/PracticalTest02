package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationThread extends Thread {

    private final Socket clientSocket;
    private HashMap<String, WeatherForecastInformation> data;
    private ServerThread serverThread;
    public CommunicationThread(ServerThread serverThread, Socket clientSocket) {
        this.serverThread = serverThread;
        this.clientSocket = clientSocket;
        this.data = serverThread.getData();
    }

    @Override
    public void run() {

        String clientResponse;
        BufferedReader bufferedReader;
        try {
            bufferedReader = Util.getReader(clientSocket);
            String[] req = bufferedReader.readLine().split(",");
            String request = req[1];
            String informationType = req[0];

            if (!data.containsKey(request)) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://api.openweathermap.org/data/2.5/weather?q=" +
                        request + "&appid=e03c3b32cfb5a6f7069f2ef29237d87e");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity == null) {
                    Log.e("Eroare", "Null response from server");
                }
                String response = EntityUtils.toString(httpEntity);

                JSONObject content = new JSONObject(response);
                String temperature = content.getJSONObject("main").getString("temp");
                String humidity = content.getJSONObject("main").getString("humidity");
                String windSpeed = content.getJSONObject("wind").getString("speed");
                System.out.println(temperature + " " + humidity + " " + windSpeed);
                WeatherForecastInformation weatherForecastInformation = new WeatherForecastInformation();
                if (informationType.equalsIgnoreCase("temperature")) {
                    weatherForecastInformation.setTemperature(temperature);
                } else if (informationType.equalsIgnoreCase("humidity")) {
                    weatherForecastInformation.setHumidity(humidity);
                } else if (informationType.equalsIgnoreCase(windSpeed)) {
                    weatherForecastInformation.setWindSpeed(windSpeed);
                } else {
                    /* all */
                    weatherForecastInformation.setTemperature(temperature);
                    weatherForecastInformation.setHumidity(humidity);
                    weatherForecastInformation.setWindSpeed(windSpeed);
                }

                data.put(request, weatherForecastInformation);
                serverThread.setData(data);
            }

            if (informationType.equalsIgnoreCase("temperature")) {
                clientResponse =  data.get(request).getTemperature();
            } else if (informationType.equalsIgnoreCase("humidity")) {
                clientResponse =  data.get(request).getHumidity();
            } else if (informationType.equalsIgnoreCase("wind speed")) {
                clientResponse =  data.get(request).getWindSpeed();
            } else {
                clientResponse =  data.get(request).getTemperature() + "," +
                        data.get(request).getHumidity() + "," +
                        data.get(request).getWindSpeed();
            }


            PrintWriter printWriter = Util.getWriter(clientSocket);
            printWriter.println(clientResponse);
            printWriter.flush();
            clientSocket.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}
