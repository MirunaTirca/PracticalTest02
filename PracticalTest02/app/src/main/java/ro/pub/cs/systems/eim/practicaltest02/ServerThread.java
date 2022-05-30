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

public class ServerThread extends Thread {
    private int port;
    private ServerSocket serverSocket;
    private HashMap<String, WeatherForecastInformation> data;

    public ServerThread(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error", "ServerSocket error for port " + port);
        }

        this.data = new HashMap<>();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket clientSocket = serverSocket.accept();

                if (clientSocket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(this, clientSocket);
                    communicationThread.start();

                } else {
                    Log.e("Error", "Null client socket");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", "Error at server socket accept method");
            }
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e("Server", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());

            }
        }
    }

    public synchronized HashMap<String, WeatherForecastInformation> getData() {
        return data;
    }

    public synchronized void setData(HashMap<String, WeatherForecastInformation> data) {
        this.data = data;
    }
}


