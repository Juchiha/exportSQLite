package com.example.pruebas_exportar_bd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pruebas_exportar_bd.bd.SqliteBD;
import com.example.pruebas_exportar_bd.models.Comentario;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnExportarBD = (Button) findViewById(R.id.btnExportarBD);
        //Verifica los permisos de escritura en la external Storage
        isStoragePermissionGranted();
        btnExportarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                task.execute();
            }
        });
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Informacion","Permission is granted");
                return true;
            } else {

                Log.v("Informacion","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Informacion","Permission is granted");
            return true;
        }
    }

    private class ExportDatabaseCSVTask extends AsyncTask<String ,String, String> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected String doInBackground(String... strings) {
            String success = "False";
            File exportDir = new File(Environment.getExternalStorageDirectory(), "/basesDatos/");
            if (!exportDir.exists()) {
                Log.v("Informacion", "hay que crearlo");
                exportDir.getParentFile().mkdir();
            }
            String exportDirStr = exportDir.toString();// to show in dialogbox
            Log.v("Informacion", "exportDir path::" + exportDir);
            File file = new File(exportDir.getAbsolutePath(), "ExcelDbPruebas.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                SqliteBD bd = new SqliteBD(MainActivity.this);
                List<Comentario> comentarioList = bd.getComments();

                String arrStr1[] = { "#", "Usuario", "Comentario" };
                csvWrite.writeNext(arrStr1);

                if (comentarioList.size() > 0){
                    for (int index = 0; index < comentarioList.size(); index++) {
                        Comentario comentario = comentarioList.get(index);
                        int srNo = index;
                        String arrStr[] = { String.valueOf(srNo + 1), comentario.getNombre(), comentario.getComentario() };
                        csvWrite.writeNext(arrStr);
                    }
                    success = "Exito";
                }
                csvWrite.close();
            }catch (IOException e){
                Log.e("MainActivity", e.getMessage(), e);
                return success;
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected void onPostExecute(final String success) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (success.equals("Exito")) {
                Toast.makeText(MainActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Export failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

