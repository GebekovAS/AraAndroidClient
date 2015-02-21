package com.example.aramonitorclient.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by GebekovAS on 19.02.2015.
 * Класс для получения JSON данных с хоста посредством GET запроса
 * Адрес проекта: www.gebekovas.ru/soft
 * Почтовый адрес: gebekovas@yandex.ru
 */

public class netManager extends AsyncTask<String,String,String> {
    //Глобальные переменные
    MainActivity owner = null;
    String hostUrl=null;
    String userName=null;
    String password=null;
    Boolean usedCreditor=false;

    //Метод выполняющийся в отдельном потоке
    @Override
    protected String doInBackground(String... strings) {
        String result="";
        try {
            result=getJSONData(hostUrl);
        }
        catch (Exception e) {
        }
        return result;
    }

    //Метод обработки результата doInBackground метода
    //Этот метод выполняется в основном потоке
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Полученные данные отправляю методу обновления списка
        if (s.length()>0)
            owner.viewUpdate(s);
        else
            owner.showSettingLayout();
    }

    //Метод получения данных GET запросом
    public String getJSONData (String host) {
        DefaultHttpClient httpClient=new DefaultHttpClient(new BasicHttpParams());
        //Если требуется авторизация
        CredentialsProvider creditor = null;
        if (usedCreditor) {
            creditor = new BasicCredentialsProvider();
            creditor.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), new UsernamePasswordCredentials(userName, password));
            httpClient.setCredentialsProvider(creditor);
        }
        HttpGet httpGet=new HttpGet(host);
        httpGet.setHeader("Content-type", "application/json");
        InputStream inputStream=null;

        //Получаем и обрабатываем данные
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity=response.getEntity();
            inputStream = entity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            StringBuilder sb=new StringBuilder();
            String line=null;
            while ((line = reader.readLine())!=null)
                sb.append(line+"\n");
            try {
                if (inputStream != null) inputStream.close();
                reader.close();
            }
            catch (IOException ex) {

            }

            //Возвращаем полученные данные
            return sb.toString();
        } catch (IOException e) {

        }

        return "";
    }

    //Конструктор
    public netManager(MainActivity pOwner, String pUrl, Boolean pUsedCreditials, String pUserName, String pPassword ) {
        owner=pOwner;
        hostUrl=pUrl;
        if (pUsedCreditials) {
            userName=pUserName;
            password=pPassword;
            usedCreditor=true;
        }
    }
}
