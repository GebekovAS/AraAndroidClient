package com.example.aramonitorclient.app;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GebekovAS on 19.02.2015.
 * ARA- монитор приложение для мониторинга за состоянием устройств (серверов, домашних компьютеров и т.д.)
 * Адрес проекта: www.gebekovas.ru/soft
 * Почтовый адрес: gebekovas@yandex.ru
 */

public class MainActivity extends ActionBarActivity {

    private Boolean firstRun=null;
    //Данные для формирования списка
    List<Map<String,String>> groupList=null;
    List<List<Map<String,String>>> childList=null;
    List <String> pingList=new ArrayList<String>();

    //Параметры необходимые для хранения и загрузки настроек
    public static final String APP_SETTINGS="ara_settings";
    public static final String APP_SETTINGS_CONNECTSTRING="connect_string";
    public static final String APP_SETTINGS_USED="connect_used";
    public static final String APP_SETTINGS_LOGIN="connect_login";
    public static final String APP_SETTINGS_PASSWORD="connect_password";

    //Параметры хранят в себе основные настройки подключения
    public String connectionString="";
    public Boolean connectionUsed=false;
    public String connectionLogin="";
    public String connectionPassword="";

    //Объект хранения и загрузки данных
    private SharedPreferences araSettings;

    //Метод загружает сохраненые настройки приложения
    private void loadSettings() {
        try {
            if (araSettings.contains(APP_SETTINGS_CONNECTSTRING)) {
                connectionString = araSettings.getString(APP_SETTINGS_CONNECTSTRING, "");
                connectionLogin = araSettings.getString(APP_SETTINGS_LOGIN, "");
                connectionPassword = araSettings.getString(APP_SETTINGS_PASSWORD, "");
                connectionUsed = araSettings.getBoolean(APP_SETTINGS_USED, false);
                init();
            }
        }
        catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    //Метод вызывается при переходе приложения в режим "Остановлено"
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected  void onPause() {
        super.onPause();
        try {
            SharedPreferences.Editor editor = araSettings.edit();
            editor.putString(APP_SETTINGS_CONNECTSTRING, connectionString);
            editor.putString(APP_SETTINGS_LOGIN, connectionLogin);
            editor.putString(APP_SETTINGS_PASSWORD, connectionPassword);
            editor.putBoolean(APP_SETTINGS_USED, connectionUsed);
            editor.apply();
        }
        catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    //Загружаем сохраненные настройки
    @Override
    protected  void onResume() {
        super.onResume();
        loadSettings();

        //Если firstRun не инициирован, то это первый запуск
        if (firstRun==null) {
            Log.d("ARA","firstRun=NULL");
            showSettingLayout();
            firstRun=false;
        }
    }

    //Метод востановления данных после поворота экрана
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        firstRun = savedInstanceState.getBoolean("firstRun");
    }

    //Метод сохранения данных при повороте экрана
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("firstRun",firstRun);
    }

    //Метод при отображении элементов активити
    @Override
    protected void onStart () {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Инициализация объекта настроек
        //APP_SETTINGS-это имя группы наших настроек (p.s. их может быть несколько)
        araSettings= getSharedPreferences(APP_SETTINGS,Context.MODE_PRIVATE);
        loadSettings();
    }

    //Метод вызывает слой(окно) настроек приложения
    public void showSettingLayout() {
        Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra(APP_SETTINGS_CONNECTSTRING,connectionString);
        intent.putExtra(APP_SETTINGS_LOGIN,connectionLogin);
        intent.putExtra(APP_SETTINGS_PASSWORD,connectionPassword);
        intent.putExtra(APP_SETTINGS_USED,connectionUsed);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Метод обработки выбора пункта меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showSettingLayout();
        } else
        if (id==R.id.action_exit) finish();
        else
        if(id==R.id.action_update) init();

        return super.onOptionsItemSelected(item);
    }

    //Метод вызывает задачу обновления данных
    private void init() {
        netManager netObj=new netManager(this,connectionString,connectionUsed,connectionLogin,connectionPassword);
        netObj.execute("");
    }

    //Метод обновляет данные списка (данные берет из JSON строки)
     public void viewUpdate(String jsonString) {
         jsonString="["+jsonString+"]";
         final String ITEM_TITLE="TITLE";
         final String ITEM_SUBSTRING="SUBSTRING";
         List<Map<String,String>> groupList=new ArrayList<Map<String, String>>();
         List<List<Map<String,String>>> childList=new ArrayList<List<Map<String, String>>>();

         JSONObject jsObj= null;
         List<String> newPingList=new ArrayList<String>();
         List<String> colorList=new ArrayList<String>();

         try {
             jsObj = new JSONArray(jsonString).getJSONObject(0);
             JSONArray cpuList=jsObj.names();
             for (int i = 0; i < cpuList.length(); i++) {
                 Map<String, String> itemGroup = new HashMap<String, String>();
                 groupList.add(itemGroup);

                 String grpName=cpuList.getString(i);
                 String LocalAdrs="non";
                 JSONArray childObj=jsObj.getJSONArray(grpName);
                 itemGroup.put(ITEM_TITLE, grpName);

                 //Формирую список дочерних item'сов
                 List<Map<String, String>> children = new ArrayList<Map<String, String>>();
                 for (int j = 0; j < childObj.length(); j++) {
                     Map<String, String> childItem = new HashMap<String, String>();
                     children.add(childItem);
                     childItem.put(ITEM_TITLE, childObj.getJSONObject(j).getString("title"));
                     childItem.put(ITEM_SUBSTRING, childObj.getJSONObject(j).getString("value"));

                     //IP-адрес выводим на заголовке группы и хаписываем пинг
                     if (childObj.getJSONObject(j).getString("type").toString().indexOf("ip4adress")>=0)
                         LocalAdrs=childObj.getJSONObject(j).getString("value");
                     else
                     if (childObj.getJSONObject(j).getString("type").toString().indexOf("ping")>=0)
                         newPingList.add(childObj.getJSONObject(j).getString("value"));
                 }
                 itemGroup.put(ITEM_SUBSTRING, LocalAdrs);

                 //Подбираем цвет фона группы (красный -если пинги совпадают, зеленый -если пинг изменился)
                 try {
                     if (!pingList.get(i).equals(newPingList.get(i))) {
                         colorList.add("#C0FFC0");
                     }
                     else
                         colorList.add("#F29F8E");
                 } catch (Exception e) {
                     colorList.add("#F29F8E");
                 }
                 childList.add(children);
             }
             //Обновляем данные о пингах
             pingList.clear();
             pingList=newPingList;

             //Формируем адаптер данных
             ExpandableListAdapter exListAdapter = new ARAExpandableListAdapter(
                     // new SimpleExpandableListAdapter(
                     this,
                     groupList,
                     android.R.layout.simple_expandable_list_item_2,
                     new String[]{ITEM_TITLE, ITEM_SUBSTRING},
                     new int[]{android.R.id.text1, android.R.id.text2},
                     childList,
                     android.R.layout.simple_expandable_list_item_2,
                     new String[]{ITEM_TITLE, ITEM_SUBSTRING},
                     new int[]{android.R.id.text1, android.R.id.text2},
                     colorList);

             //Устанавливаем полученный адаптер данных
             ExpandableListView exListView = (ExpandableListView) (findViewById(R.id.expandableListView));
             exListView.setAdapter(exListAdapter);
         }
          catch (JSONException e) {
             e.printStackTrace();
         }
    }

    //Метод вызова всплывающих окон
    void showMessage(String Msg) {
        AlertDialog dlg= new AlertDialog.Builder(this).create();
        dlg.setMessage(Msg);
        dlg.setButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dlg.show();
    }

}
