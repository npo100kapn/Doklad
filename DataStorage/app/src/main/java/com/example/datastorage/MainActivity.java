package com.example.datastorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.datastorage.DatabaseHelper.COLUMN_CUR_TEXT;

public class MainActivity extends AppCompatActivity {
    EditText editTextEnter;
    Button button2;
    final String FILENAME_SD = "fileSD";
    final String DIR_SD = "MyFiles";
    final String SAVED_TEXT = "saved_text";
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    SharedPreferences sPref;
/*
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextEnter = (EditText) findViewById(R.id.editTextEnter);
        button2 = (Button) findViewById(R.id.button2);
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    public void OpenClick(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("idButton", view.getId());
        startActivity(intent);
    }

    public void SaveFile(View view) {
        String FILE_NAME = "file.txt";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            fos.write(editTextEnter.getText().toString().getBytes());
            Toast.makeText(this, "Файл сохранён", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        editTextEnter.setText("");
    }

    public void writeFileSD(View view) {
        if (!Environment.getExternalStorageState().equals( // проверяем доступность SD
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_SHORT).show();
            return;
        }
        File sdPath = Environment.getExternalStorageDirectory(); // получаем путь к SD
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD); // добавляем свой каталог к пути
        sdPath.mkdirs(); // создаем каталог
        File sdFile = new File(sdPath, FILENAME_SD); // формируем объект File, который содержит путь к файлу
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile)); // открываем поток для записи
            bw.write(editTextEnter.getText().toString()); // пишем данные
            bw.close(); // закрываем поток
            Toast.makeText(this, "Файл записан на SD: "+ sdFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            editTextEnter.setText("");
        } catch (IOException e) {
            Toast.makeText(this, "Не удалось записать файл", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void readFileSD(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("idButton", view.getId());
        startActivity(intent);
    }

    public void SaveInPref(View view) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, editTextEnter.getText().toString());
        ed.commit();
        Toast.makeText(this, "Текст сохранён", Toast.LENGTH_SHORT).show();
        editTextEnter.setText("");
    }

    public void LoadPref(View view) {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "");
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("idButton", view.getId());
        intent.putExtra("spref", savedText);
        startActivity(intent);
    }

    public void OpenBD(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("idButton", view.getId());
        startActivity(intent);
    }

    public void SaveInBD(View view) {
        db = databaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(COLUMN_CUR_TEXT, editTextEnter.getText().toString());
        db.insert(DatabaseHelper.TABLE, null, newValues);
        Toast.makeText(this, "Текст сохранён в БД", Toast.LENGTH_SHORT).show();
        editTextEnter.setText("");
    }

    public void buttonClear(View view) {
        editTextEnter.setText("");
    }
/*
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        else Toast.makeText(activity, "Разрешение получено", Toast.LENGTH_SHORT).show();
    }*/
}