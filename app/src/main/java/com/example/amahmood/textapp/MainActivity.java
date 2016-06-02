package com.example.amahmood.textapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.net.*;
import java.io.*;


public class MainActivity extends AppCompatActivity {
    private String server_msg = "No reply from server";
    private Socket client_socket;
    private PrintWriter outputStream;
    private InputStreamReader input;
    private BufferedReader inputReader;

    private class connectToServer extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... client_msg) {
            try {
                Log.d("Client", "Creating socket...");
                client_socket = new Socket("10.111.2.157", 48765);
                outputStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client_socket.getOutputStream())), true);
                input = new InputStreamReader(client_socket.getInputStream());
                inputReader = new BufferedReader(input);
                Log.d("Client", "Successfully connected");
                return null;
            } catch (Exception e) {
                Log.d("Connection Error", "Log Start");
                e.printStackTrace();
                Log.d("Connection Error", "Log End");
                server_msg = "Failed to connect to host (10.111.2.157) at port: 48765";
                return null;
            }
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
    private class getFromServer extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... client_msg) {
            try {
                outputStream.println(client_msg[0]);
                server_msg = inputReader.readLine();
                client_msg[1] = server_msg;
                Log.d("Client", "Received message from host");;
                return null;
            } catch (Exception e) {
                Log.d("Retrieval Error", "Log Start");
                e.printStackTrace();
                Log.d("Retrieval Error", "Log End");
                server_msg = "Failed to retrieve from host";
                return null;
            }
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new connectToServer().execute();

        Button submit = (Button) findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dp_message = (EditText) findViewById(R.id.displayText);
                String text = dp_message.getText().toString();
                String communication[] = {text, server_msg};
                new getFromServer().execute(communication);
                //Log.d("myTag", entry);
                Snackbar.make(view, "Smaug says " + communication[1] , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                TextView tv = (TextView) findViewById(R.id.longText);
                tv.append("Server: "+ communication[1] + "\n");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


