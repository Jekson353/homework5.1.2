package com.samoylenko.homework311;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SettingActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PERMISSION_READ_STORAGE = 10;
    public String fileName;
    public TextView imgName;
    public Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        imgName = findViewById(R.id.image_name);
        btnApply = findViewById(R.id.button_apply);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions()){
                    fileName = imgName.getText().toString();
                    newImage(fileName);
                }else{
                    //просим разрешение на чтение хранилища
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_STORAGE);
                }
            }
        });

    }

    private boolean hasPermissions(){
        //указываем нужные разрешения
        int permissionsStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //если разрешение есть
        return permissionsStatus == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fileName = imgName.getText().toString();
                newImage(fileName);
            } else {
                Toast.makeText(SettingActivity.this, "Вы не разрешили доступ к файлам"
                        , Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    //Добавляем картинку
    private void newImage(String fileName) {
        if (fileName != null) {
            if (isExternalStorageReadable()) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                if (file.exists() && file.isFile()) {
                    Intent intent = new Intent();
                    intent.putExtra("imgName", file.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    this.finish();
                } else {
                    Toast.makeText(SettingActivity.this, "Файла не существует!"
                            , Toast.LENGTH_LONG)
                            .show();
                }
            }
        } else {
            Toast.makeText(SettingActivity.this, "Имя файла пустое"
                    , Toast.LENGTH_LONG)
                    .show();
        }
    }

    /* Проверка внутреннего хранилища на доступность записи */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Проверка внутреннего хранилища на доступность чтения */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
