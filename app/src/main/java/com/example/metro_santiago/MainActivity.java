package com.example.metro_santiago;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {


    public  final String[] LINEAS= {"Linea1","Linea2","Linea3","Linea4","Linea5","linea6"};  //91.01

    Lineas_metro[] lineas; //91.02 para la clase de progreso

    private ProgressBar barra_de_progreso;  //91.03 creamos obeto progressbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        barra_de_progreso=(ProgressBar)findViewById(R.id.barraprogreso);//92.01 casting a la imagen barra de progreso
        barra_de_progreso.setVisibility(View.VISIBLE);//92.02. LO HACEMOS VISIBLE
        //92.03 como es una constante de clase podemos usar un view.visible para que se muestre YA QUE ESE METHODO PIDE UN ENTERO, PERO ASI LO AGREGAMOS DIRECTAMNET CON UN VALOR DE 0
        sincroniza comienzo=new sincroniza(); //92.07llamamos al methodo que carga la informacion
        comienzo.execute(); //92.08lalma al objeto
    }

    private class sincroniza extends AsyncTask<String,Integer,String> { //asistnte de descarga de hilos

        //90.05 nos genera error hasta implementar los methodos

        protected String doInBackground(String...String){  //  90.06 Este método explica que abre la base de datos, obtienen la información y la almacena en el array líneas, luego cerrara la base de datos para que no nos quede abierta

            manejoBBDD_metro bbdd=new manejoBBDD_metro(getApplicationContext()); //90.07instanciamos el contextodel metodo que posee la informacion

            //90.08creamos un try,catch que nos permita hacer la operacion}

            //nota simepre nos pedira agregar el contexto getApplicationContext()

            try {

                //abrimos la base de datos
                bbdd.aperturabasedatos(getApplicationContext());
                //instanciamos la informacion d elas lineas con un array con los nombres de las lineas que se creo abajo ene ste mismo codigo 90.01

                bbdd.dame_info_lineas(LINEAS); //instancai dinfo d elas lineas y los nombre en LINEAs

                //una vez tenemos toda la informacion, cerramos la base de datos
                bbdd.close();

            } catch (Exception e){

                finish();
            }

            //la aplicacion nos pide retornar un string, pero como no nos interresa retornar un valor simplemente lo anulamos , asi carga la informacion
            //y podemos continuar

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... valores){ //92.04 creamos esta variable para animar la imagen en segundo plano con hilos
            //este methodo interer recibe un numero indeterminado de parametros
            barra_de_progreso.setProgress(valores[0],true); //puede generar error debido a la actualizacion constante de androidd


        }

        protected void onPostExcecute(String resultado){//92.05 creamos este nuevo methodo para enviar la informacion ya cargada

            comenzar(); //94.06 nos da error por que aun no hemos creado este methodo se puede aqui mismo pero en este caso se hara aparte


        }



    }

    public void comenzar(){ //el proposito de comenzar sera crear un bundle que cargue la informacion y se la pase a la otra actividad

        //creamos el bundle
        Bundle mi_bundle=new Bundle();
        //tner en cuenta que el archivo que queremos paasar es un array por lo caul debemos usar unn objeto de tipo array
        mi_bundle.putParcelableArray("LINEAS",lineas);//este nos pide un nombre significativo, y el objeto que en nuestro caso es el array lineas
        //generara error en lineas , el segundo archivo, despues veremos por que
        //creamos el intento para decir a quien queremos pasar la informacion

        Intent miintento=new Intent(this,Buscador.class);
        startActivity(miintento);


    }


}
