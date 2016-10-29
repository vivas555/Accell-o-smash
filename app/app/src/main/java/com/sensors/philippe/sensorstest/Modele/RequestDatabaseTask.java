package com.sensors.philippe.sensorstest.Modele;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RequestDatabaseTask extends AsyncTask<Object, Integer, Object> {

    //10.200.76.175:8080
    private final String IP_ADRESS = "10.0.2.2:8080";
    private final String DATABASE_NAME = "database";

    public void setListener(DatabaseManagerListener listener) {
        this.listener = listener;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }


    private DatabaseManagerListener listener;
    private RequestType requestType;
    private Object[] extras;
    private ProtocolException e = null;
    private boolean connectionSucces = true;

    public RequestDatabaseTask() {

    }

    @Override
    protected Object doInBackground(Object... params) {
        if (params.length >= 2) {
            this.setParamsFromParams(params);

            String link = "http://" + IP_ADRESS + "/" + DATABASE_NAME + "/";
            HttpURLConnection connection = null;
            try {
                switch (requestType) {
                case CREATE_ACCOUNT:
                    link += "accounts/";
                    connection = this.getConnection(link, "PUT");
                    if (connection != null) {
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);

                        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                        out.write((String)extras[0]);
                        //out.flush();
                        out.close();

                        int responseCode = connection.getResponseCode();
                        String response = convertStreamToString(connection.getInputStream());

                        connection.disconnect();

                        if (responseCode == 500) {
                            connectionSucces = false;
                            e = new ProtocolException();
                        }
                    }
                    return (String)extras[0];
                    case GET_ACCOUNT:
                        link += "accounts/" + (String)extras[0];
                        connection = this.getConnection(link, "GET");
                        if (connection != null) {
                            connection.connect();

                            ObjectMapper mapper = new ObjectMapper();
                            String accountAsString = convertStreamToString(connection.getInputStream());
                            Account acc = mapper.readValue(accountAsString, Account.class);

                            int responseCode = connection.getResponseCode();
                            connection.disconnect();

                            if (acc != null || responseCode > 200) {
                                return acc;
                            } else {
                                connectionSucces = false;
                                 e = new ProtocolException();
                            }
                        }
                    case CREATE_COLLISION:
                        link += "collisions/";
                        connection = this.getConnection(link, "PUT");
                        if (connection != null) {
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setDoOutput(true);
                            connection.setDoInput(true);

                            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                            out.write((String)extras[0]);
                            //out.flush();
                            out.close();

                            int responseCode = connection.getResponseCode();
                            String response = convertStreamToString(connection.getInputStream());

                            connection.disconnect();

                            if (responseCode == 500) {
                                connectionSucces = false;
                                 e = new ProtocolException();
                            }
                        }
                        return (String)extras[0];
                    case GET_COLLISIONS:
                        link += "collisions/" + (String)extras[0];
                        connection = this.getConnection(link, "GET");
                        if (connection != null) {
                            connection.connect();

                            String accountAsString = convertStreamToString(connection.getInputStream());

                            int responseCode = connection.getResponseCode();
                            connection.disconnect();

                            if (accountAsString != null || responseCode > 200) {
                                return accountAsString;
                            } else {
                                connectionSucces = false;
                                 e = new ProtocolException();
                            }
                        }
                    case TEST:
                        return true;
                default:
                     e = new ProtocolException();
            }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {

        if (e != null){
            try {
                throw e;
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            }
        }
        DatabaseManager.requestResult(this.listener, this.requestType, o);
    }

    private void setParamsFromParams(Object[] params) {
        //Il y a toujours le listener à l'indice 0 et le 'RequestType' à l'indice 1.
        listener = (DatabaseManagerListener)params[0];
        requestType = (RequestType)params[1];
        //Il y a un tableau d'object à l'indice '2' qui contient les paramètres supplémentaires.
        extras = (Object[])params[2];
    }

    private HttpURLConnection getConnection(String link, String requestMethod) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(requestMethod);
            return connection;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
