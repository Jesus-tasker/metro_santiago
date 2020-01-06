package com.example.metro_santiago;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class manejoBBDD_metro extends SQLiteOpenHelper {

    String rutaalmacenamiento;  //87.4
    SQLiteDatabase bbdd;  //87.5 bbdd0base de datos




    public manejoBBDD_metro (Context context){ //87.3
        super(context,"ParadasMetro.db3",null,1); //87.4 pasamos los parametros, aqui ubicamos los datos a analizar de la base
        //conext,
//api, methodo abstracto oncreate de-SQLITE openhelper
        rutaalmacenamiento=context.getFilesDir().getParentFile().getPath()+"ParadasMetro.db3";  //87.6  nos retorna la ruta de un compuesto en concreto

    }

    public void aperturabasedatos(Context context){//87.7

        //87.8 trataremos que abra la base de datos y en caso de que no hallan datos sea nulo o envie un dato
        //llama a la base de datos  de la ruta de almacenamiento.
        try {  //87.09 esta linea de codigo funcionara siempre que no sea la primera vez que se accede a la base de datos
            bbdd=SQLiteDatabase.openDatabase(rutaalmacenamiento,null,SQLiteDatabase.OPEN_READONLY);

        }catch (Exception e){ //  87.10 aqui le decimos que copee la base de datos de assets en el dispositivo y una vez copiada accedemos

            copiabbdd(context); //da error ya que aun no hemos creado este methodo

        }


    }

    private void copiabbdd(Context context) {
        //de asset a la ruta base de datos-usando streams
        try {


            InputStream datosentrada = context.getAssets().open("ParadasMetro.db3"); //789.2 llama al asset en la carpeta assets
            //
            OutputStream datossalida=new FileOutputStream(rutaalmacenamiento);

            byte[] bufferBBDD=new  byte[1024];
            //tamaño de flujo de bites
            int longitud;

            while ((longitud=datosentrada.read(bufferBBDD))>0){ //mientras empiece el proceso y sea mayor que 0

                datossalida.flush(); //para que escriba la informacion

                datossalida.close(); //cerramos los datos de salida y entrada

                datosentrada.close();

                //con esto si es la primera ves que se llama la base de datos queda copiada en el dispositivo, y una vez abiera
                // ya abierta podemos realizar consultas
            }


        }catch (Exception e){

        }

    }
    public Location datosestacion(int id){ //retorna un id,error hasta retornar un entero

        Location estacion;   //objeto de tipo location
        //CURSOR
        //creamos una nueva interfaz cursor retorna un cuery que es una tabla virtual , el cursor se pociciona en esa tabla
        //recorrerla y leerlos, tambien puede retornar la informacion de le campo determinado
        //usaremos un methodo llamado getstring

        Cursor micrusor;

        micrusor=bbdd.rawQuery("SELECT*FROM paradas WHERE id= "+id,null); //registrame los cambios de la ilera paradas cuyos campos sean el id almacenado
        //nota el null anterior del codigo es requisito pero se puede anular

        micrusor.moveToFirst(); //empieza a leer desde el primer registro

        estacion=new Location(micrusor.getString(1));

        //latitud y longitud
        estacion.setLatitude(Double.parseDouble(micrusor.getString(2))); //esto pasa la latitud a double
        estacion.setLongitude(Double.parseDouble(micrusor.getString(3)));
        //cerramos el cursor
        micrusor.close();

        return estacion; //y finalmente nos retorne la estacion  que realiza la consulta

    }

    public Lineas_metro[] dame_info_lineas ( String[] nombredelineas ){ //recibe un rray con las lineas del metro
        //90.0 creamos el methodo lineas que buscara dentro del anterior codigo la infomacion y almacenar la informacion de las lineas de metro
//recibe array con las lineas de methodo

        //90.01 la longitud de este array es de 3 por 3 lineas de metro,
        //90.02 la longitud del array cuantos elementos tinee
        Lineas_metro[] laslineas=new Lineas_metro[nombredelineas.length]; //crea una base de datos con un array  de tipo lineas con la longitud de los parametros

        Cursor micursor=null; //crea cursor para buscar location

        //reamos un array que me busque la informacion desde que el array es 0 hasta su longitud maxima en este caso 0 a 120 lines de metro santiago

        for (int i=0;i<nombredelineas.length;i++){

            //vamos a adquiriri el dato del nombre d ela linea y asi sus otros datos
            laslineas[i]= new Lineas_metro();  //crea un objeto d etipo lineas a cada buelta de bucle linea 1,2,...6

            laslineas[i].nombre=nombredelineas[i]; //va almacenando el nombre de cada linea donde nombre es el string de Lineas_metro java

            //se realiza una consulta sql a la tabla correspondiente  para obtener los ID de las estaciones
            micursor=bbdd.rawQuery("SELECT id FROM"+nombredelineas[i],null); //lee todos los datos de la linea en turno cuando 1=1,2..6

            //nota seguimos en la primera vuelta de bucle

            laslineas[i].estaciones=new Location[micursor.getCount()]; //primera vuelta lee linea1
            //array que retorna objeto location con info de todas las estaciones estciones= objeto array de Lineas_metro
            //lavariable anterior location debe tener tantas pociciones como tienne en la consulta, getcount() retorna los registros de la tabla sql ej linea 2 tiene 23 estaciones


            //ahora debemos llenar el array de ipo location con los datos de las estaciones

            int contador=0;

            micursor.moveToFirst(); //ponemos el cursor en la primera posicion

            //!micursor.isAfterLast() mientras el cursor no este al final del ultimo registro: exclamacion !

            while (!micursor.isAfterLast()){ //mienrras no hallas llegado al ultimo registro

                //almacenamos el id de cada estacion
                int estacion=Integer.parseInt(micursor.getString(0)); //esta instrucccion nos retorna el texto que hay en la comuna 0

                laslineas[i].estaciones[contador]=datosestacion(estacion); //almacenamos location en array

                contador++; //incrementamos posicion del array

                micursor.moveToNext(); //movemos el cursor una posicion en recursed
            }


        }
        if (micursor!=null && !micursor.isClosed()) micursor.close(); // cerramos el cursor

        return laslineas; //retorna el array con toda la info de las estaciones de todas las lineas y las almacena en  dame info lineas

    }

    public void cerrarbasededatos(){ //cierra la base de datos


        bbdd.close();
    }



    public void onCreate(SQLiteDatabase bbdd){



    }

    public void onUpgrade(SQLiteDatabase bbdd,int viejo,int nuevo){


    }


}
