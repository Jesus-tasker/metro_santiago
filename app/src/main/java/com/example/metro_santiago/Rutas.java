package com.example.metro_santiago;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;

public class Rutas extends AppCompatActivity {



    //100.04 creamos una variable para almacenar el nombre d ela linea mas el digito
    public String linea;

    public Location[] paradas; //100.05 variale que almacenara las paradas de la linea de metro


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
    }


    public void mejorRuta(Location origen, Location destino, Lineas_metro[] laslineas){ //100.06 este methodo buscara la mejor calculo


        Lineas_metro mejorlinea=null;

        //vamos a recorrer con  un bucle for las lineas para que con cada una d elas lineas ccalcule las ditancias llamando al methodo
        //bucle foreach

        //NOTA CREAMOS EL OBJETO LINEAS DE LA CLASE LINEAS JAVA,ALMACENAMOS EN linea, la llamada al methodo distancia y suamdistanciametros de la clase lineas java

        for (Lineas_metro linea: laslineas){
            //a cada vuelta llama al methodo distancias d la clase java  y al destino
            linea.distancias (origen,destino);

            if (mejorlinea==null ||linea.sumaDistanciaMetros()<mejorlinea.sumaDistanciaMetros())
                mejorlinea=linea; //la idea es almacenar cual linea de metro me hara caminar menos


        }


        //si la distancia entre origen y destino es menor que tomar el mero
        if (mejorlinea==null||origen.distanceTo(destino)<mejorlinea.sumaDistanciaMetros()){

            linea=null; //no tomare ninguna ruta del metro
            //si no hay aradas
            paradas=new Location[2];
            paradas[0]=origen;
            paradas[1]=destino;

            return; //como el methodo ya no tinene nada mas que ahcer hacemos que este salga ya que ya ha realizado el estudio de la ruta


        }


    }







}
