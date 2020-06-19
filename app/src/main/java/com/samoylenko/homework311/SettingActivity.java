package com.samoylenko.homework311;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

        //сначала указываем нужные разрешения
        int permissionsStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //если разрешение есть
        if (permissionsStatus== PackageManager.PERMISSION_GRANTED){
            //newImage(fileName);
            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileName = imgName.getText().toString();
                    newImage(fileName);
                }
            });
        }else {
            //просим разрешение на чтение хранилища
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_PERMISSION_READ_STORAGE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //newImage(fileName);

                }else{
                    Toast.makeText(SettingActivity.this, "Вы не разрешили доступ к файлам"
                            , Toast.LENGTH_LONG)
                            .show();
                }
                return;
        }
    }

    //Добавляем картинку
    private void newImage(String fileName){
        if (fileName!=null){
            if (isExternalStorageReadable()){
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                if (file.exists() && file.isFile()){
                    //ImageView im = findViewById(R.id.imageView);
                    //Bitmap bitmapFactory = BitmapFactory.decodeFile(file.getAbsolutePath());
                    //im.setImageBitmap(bitmapFactory);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("imgName", file.getAbsolutePath());
                    startActivity(intent);
                }else {
                    Toast.makeText(SettingActivity.this, "Файла не существует!"
                            , Toast.LENGTH_LONG)
                            .show();
                }
            }
        }else{
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
