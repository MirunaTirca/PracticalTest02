package ro.pub.cs.systems.eim.practicaltest02;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private EditText portEditTextServer;
    private Button startServerButton;
    private ServerThread serverThread;
    private EditText addressEditText;
    private EditText portEditTextClient;
    private EditText cityEditText;
    private Button getWeatherForecast;
    private TextView responseTextView;
    private ClientThread clientThread;
    private Spinner informationType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        portEditTextServer = findViewById(R.id.PortEditText);
        startServerButton = findViewById(R.id.ConnectButton);
        addressEditText = findViewById(R.id.address);
        portEditTextClient = findViewById(R.id.portClient);
        cityEditText = findViewById(R.id.city);
        getWeatherForecast = findViewById(R.id.get_weather_forecast);
        responseTextView = findViewById(R.id.response);
        informationType = findViewById(R.id.informationType);
        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int port = Integer.parseInt(portEditTextServer.getText().toString());
                serverThread = new ServerThread(port);
                serverThread.start();
            }
        });

        getWeatherForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressEditText.getText().toString();
                int port = Integer.parseInt(portEditTextClient.getText().toString());
                String city = cityEditText.getText().toString();
                String information = informationType.getSelectedItem().toString();
                clientThread = new ClientThread(port, address, city, information, responseTextView);
                clientThread.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i("Destroy", "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}