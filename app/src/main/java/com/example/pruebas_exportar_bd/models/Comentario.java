package com.example.pruebas_exportar_bd.models;

public class Comentario {
    //Campos correspondientes a la base de datos
    int id;
    String nombre;
    String comentario;

    public Comentario(int _id,String _nombre,String _comentario){
        id=_id;
        nombre=_nombre;
        comentario=_comentario;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public int getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getComentario(){
        return comentario;
    }
}
