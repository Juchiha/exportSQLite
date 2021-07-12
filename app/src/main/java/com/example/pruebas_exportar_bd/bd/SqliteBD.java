package com.example.pruebas_exportar_bd.bd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pruebas_exportar_bd.models.Comentario;

import java.util.ArrayList;

public class SqliteBD extends SQLiteOpenHelper {
    private static final String COMMENTS_TABLE_CREATE = "CREATE TABLE comments(_id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, comment TEXT)";
    private static final String DB_NAME = "comments.sqlite";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;


    public SqliteBD(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMENTS_TABLE_CREATE);
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 1')");
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 2')");
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 3')");
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 4')");
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 5')");
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 6')");
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 7')");
        db.execSQL("INSERT INTO comments ( user, comment )" +
                " VALUES ( 'Jose Giron', 'comentario 8')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE comments");
        this.onCreate(db);
    }

    public ArrayList<Comentario> getComments(){
        //Creamos el cursor
        ArrayList<Comentario> lista=new ArrayList<Comentario>();
        Cursor c = db.rawQuery("select _id, user,comment from comments", null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                String user = c.getString(c.getColumnIndex("user"));
                String comment = c.getString(c.getColumnIndex("comment"));
                int id=c.getInt(c.getColumnIndex("_id"));
                Comentario com =new Comentario(id,user,comment);
                //Añadimos el comentario a la lista
                lista.add(com);
            } while (c.moveToNext());
        }
        //Cerramos el cursor
        c.close();
        return lista;
    }
}
