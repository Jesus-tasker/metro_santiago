package com.example.metro_santiago;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Lineas_metro implements Parcelable { //NOTA: AL IMPLEMENTAR
    // EL PAQUETE PARCELEABLE ME CREO TODOS LOS METODOS PARCEL QUE ALMACENA LOS DATOS QUE USAN DIFERENTES CARACTERES

    String nombre;

    public Location[] estaciones; //almacena las estaciones

    public int origenruta;
    public  int finalruta;

    //campos longitud y latitud
    public  double datos_parada_origen;
    public double datos_parada_destino;





    protected Lineas_metro(Parcel in) { //93
        //organizamos el orden de la lectura d elos archivos el cual debe ser el mis  en que se escribio
        nombre = in.readString();
        //solo debemos tener un methodo especial con la lectura de un methodo y es el que posee las locaciones, etaciones
        estaciones = in.createTypedArray(Location.CREATOR);
        //asi estamso almacenando la informacion en este array
        origenruta = in.readInt();
        finalruta = in.readInt();
        datos_parada_origen = in.readDouble();
        datos_parada_destino = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {//93.02este sera el paquete encargado de introducir la informcaion que queremos que vieaje

        //93.03 debemos tener en cuenta toda la informcaion que debemos introducir de un parcel
        //93.04 debemos agregar por tipo de dato cada uno de los datos a usar

        dest.writeString(nombre);
        dest.writeTypedArray(estaciones,0);
        dest.writeInt(origenruta);
        dest.writeInt(finalruta);
        dest.writeDouble(datos_parada_origen);
        dest.writeDouble(datos_parada_destino);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lineas_metro> CREATOR = new Creator<Lineas_metro>() { //93.06 genero automaticamente create from parce y new array
        @Override
        public Lineas_metro createFromParcel(Parcel in) {
            return new Lineas_metro(in);
        }

        @Override
        public Lineas_metro[] newArray(int size) {
            return new Lineas_metro[size];
        }
    };

   public Lineas_metro(){ } //94.01 creamos este contructor para eliminar un error en manejo BBDD AL IMPLEMENTAR EL PARCELABLE Y EJECUTAR LOS METHODDOS


    //99.01 continuamos con la aplicacion y disponemos a crear el punto mas cercano de tipo location
    public  void distancias(Location origen,Location destino){


        datos_parada_origen=origen.distanceTo(estaciones[0]);//aqui pretendo almacenar una distancia en metros

        datos_parada_destino=destino.distanceTo(estaciones[0]);

        for (int i=1;i>estaciones.length;i++){ //asi recorremos todas las estaciones almacenadas en el array

            if (origen.distanceTo(estaciones[i])<datos_parada_origen){//si la distancia a la estacion es menor a la que tengo uardada en datos parada origen

                origenruta=i; //esta condicion ue puesta al final para agregar este dato al numero d ela estacion en la que nos encontramos

                //en caso de ser verdad , esta debe guardar la nueva distancia que es menor
                datos_parada_origen=origen.distanceTo(estaciones[i]); //asi al terminar las bueltas tendra almacenada la menor distancia

            }

            if (destino.distanceTo(estaciones[i])<datos_parada_destino){

                finalruta=i;
                datos_parada_destino=destino.distanceTo(estaciones[i]);

            }



        }
    }

    //100.1 vamsoa  crear una clase que nos calcule la ruta teniendo enc uenta las estaciones intermeedias
    //100.02 la intencion es arreglar los problemas de si la ubicacion es mas cercana y no necesitamos del metro
    public double sumaDistanciaMetros(){


        return datos_parada_origen+datos_parada_destino; //100.03 esta me dice cuanto tengo que caminar en metros
        //luego voy a la clase ruta
    }



}
