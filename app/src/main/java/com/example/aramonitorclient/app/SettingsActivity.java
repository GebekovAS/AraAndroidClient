package com.example.aramonitorclient.app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Алавутдин on 20.02.2015.
 */
public class SettingsActivity extends ActionBarActivity {

    //Переменные для загрузки и сохранения настроект
    private SharedPreferences araSettings;
    public static final String APP_SETTINGS="ara_settings";
    public static final String APP_SETTINGS_CONNECTSTRING="connect_string";
    public static final String APP_SETTINGS_USED="connect_used";
    public static final String APP_SETTINGS_LOGIN="connect_login";
    public static final String APP_SETTINGS_PASSWORD="connect_password";

    //Переменные для работами с элементами формы
    private TextView connectStringView;
    private TextView connectLoginView;
    private TextView connectPasswordView ;
    private CheckBox connectUsed;

    @Override
    protected void onPostCreate(Bundle saveIntsanceState) {
        super.onPostCreate(saveIntsanceState);
        setContentView(R.layout.activity_settings);

        //Получаю ссылки на элементы формы
        connectStringView = (TextView) findViewById(R.id.ConnectStringText);
        connectLoginView = (TextView) findViewById(R.id.loginTextView);
        connectPasswordView = (TextView) findViewById(R.id.passwordTextView);
        connectUsed=(CheckBox) findViewById(R.id.authCheckBox);

        //Объект для сохранения настроек
        araSettings= getSharedPreferences(APP_SETTINGS,Context.MODE_PRIVATE);

        //Объект для получения данных из родительского активити
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        //Получаю данные отправленные с родительского активити
        if (!bundle.getString(APP_SETTINGS_CONNECTSTRING).equals(null)) {
            connectStringView.setText(bundle.getString(APP_SETTINGS_CONNECTSTRING));
            connectLoginView.setText(bundle.getString(APP_SETTINGS_LOGIN));
            connectPasswordView.setText(bundle.getString(APP_SETTINGS_PASSWORD));
            connectUsed.setChecked(bundle.getBoolean(APP_SETTINGS_USED, false));
        }

        //Добавляю к кнопке обработчик события нажатия клавиши
        //сохарнение, указанных пользователем, настроек
        Button saveBtn=(Button)findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=araSettings.edit();
                editor.putString(APP_SETTINGS_CONNECTSTRING,connectStringView.getText().toString());
                editor.putString(APP_SETTINGS_LOGIN,connectLoginView.getText().toString());
                editor.putString(APP_SETTINGS_PASSWORD,connectPasswordView.getText().toString());
                editor.putBoolean(APP_SETTINGS_USED,connectUsed.isChecked());
                editor.apply();
                finish();
            }
        });
    }

}
