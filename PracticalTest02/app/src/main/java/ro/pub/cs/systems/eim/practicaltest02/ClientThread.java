package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private int port;
    private String address;
    private String request;
    private String informationType;
    private TextView response;

    public ClientThread(int port, String address, String request, String informationType, TextView response) {
        this.port = port;
        this.address = address;
        this.request = request;
        this.informationType = informationType;
        this.response = response;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);
            BufferedReader bufferedReader = Util.getReader(socket);
            PrintWriter printWriter = Util.getWriter(socket);

            printWriter.println(informationType + "," + request);

            String res = bufferedReader.readLine();
            Log.d("Client ", "Received data: " + res);

            response.post(new Runnable() {
                @Override
                public void run() {
                    response.setText(res);
                }
            });
        } catch (IOException e) {
            Log.e("Client error", "Socket error on client for ip " + address + " and port " + port);
            e.printStackTrace();
        }
    }
}
