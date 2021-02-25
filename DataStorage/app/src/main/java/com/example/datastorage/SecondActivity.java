package com.example.datastorage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.datastorage.DatabaseHelper.COLUMN_CUR_TEXT;
import static com.example.datastorage.DatabaseHelper.COLUMN_ID;
import static com.example.datastorage.DatabaseHelper.TABLE;

public class SecondActivity extends AppCompatActivity {

    String FILE_NAME = "file.txt";
    final String FILENAME_SD = "fileSD";
    final String DIR_SD = "MyFiles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        Bundle arguments = getIntent().getExtras();
        int name = (int) arguments.get("idButton");
        switch(name) {
            case R.id.button2:
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            openFileInput(FILE_NAME)));
                    String str = "";
                    while ((str = br.readLine()) != null) {
                        textView2.setText(str);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.button4:
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                Cursor userCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE , null);
                userCursor.moveToFirst();
                int count = userCursor.getInt(0);
                if(count != 0) {
                    userCursor = db.rawQuery("SELECT " + COLUMN_CUR_TEXT + " FROM " + TABLE + " WHERE " +
                            COLUMN_ID + " = ? ", new String[]{String.valueOf(count)});
                    userCursor.moveToFirst();
                    String str = userCursor.getString(0);
                    textView2.setText(str);
                }
                break;
            case R.id.button6:
                if (!Environment.getExternalStorageState().equals( // проверяем доступность SD
                        Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "SD-карта не доступна: " + Environment.getExternalStorageState(), Toast.LENGTH_SHORT).show();
                    return;
                }
                File sdPath = Environment.getExternalStorageDirectory(); // получаем путь к SD
                sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD); // добавляем свой каталог к пути
                File sdFile = new File(sdPath, FILENAME_SD); // формируем объект File, который содержит путь к файлу
                try {
                    BufferedReader br = new BufferedReader(new FileReader(sdFile)); // открываем поток для чтения
                    String str = ""; // читаем содержимое
                    while ((str = br.readLine()) != null) {
                        textView2.setText(str);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button8:
                String savedText = arguments.get("spref").toString();
                textView2.setText(savedText);
                break;
            default:
                break;
        }
    }
}
