package com.example.metro_santiago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Arrays;

public class Buscador extends AppCompatActivity {
//94.02 aqui programaremos la conectividad de la informacion del usarui administre para buscar la ruta
    //para ello primero agregamos las variable sque podemos utilziar para su creacion

    Lineas_metro[] lineas; //lineas de metro objeto lineas

    Rutas rutas;

    private ProgressBar barraprogrego; //la barra d eprogreso

    EditText origen, destino; //objetos de texto

    Button enviar;
    String direccionOrigen, direccionDestino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);


    }

    //94.03 con el siguiente codigo si se pone en segundo plano la actividad al regresar a primer plano siguein identificados los elementos
    public void onResume() {

        super.onResume();

        origen = ((EditText) findViewById(R.id.origen));
        //ahora si la aplicacion se reinicia o se cierra y se abre, este campo aparecera bacio
        origen.setText("");
        destino = ((EditText) findViewById(R.id.destino));
        destino.setText("");
        enviar = (Button) findViewById(R.id.enviar);
        //ahora con el fin que el boton se active y busque la ruta, este se desactiva hasta crearla , asi el usuario no piensa que la aplicacion callo
        enviar.setAlpha(1); //cuando el methodo onresume se active quiero qeu este boton este activado y 0 cuando este calculando la ruta
        //ahora lo habilitamos
        enviar.setEnabled(true);

        //94.05 creamos la condivional para que cargue una copia de las lineas y poder usar esa ingformacion
        if (lineas == null) {  //comprobamso si cargo correctamente el archivo de datos para las lineas
            //si es nulo debemos desempaquetar el paquete con las lineas que hemos creado en el bundle

            Bundle mibundle = getIntent().getExtras(); //creamos el bundle que recibe la info

            Parcelable[] datos = mibundle.getParcelableArray("LINEAS"); //almacenamos con la llabe el bundle

            //con esto el array del paquete queda en este ya con las lineas cargadas
            lineas = Arrays.copyOf(datos, datos.length, Lineas_metro[].class); //bundle,longitud de bundle, clase a la que pertenece


        }

        //instnciamos la clase rutas video 95

        rutas = new Rutas();
    }

    public void leedirecciones(View vista) { //9404 llee la informacion que el usuario introduce en la interfaz

        //primero debemos dar lugar a que con un click y buscar ruta este se pare hasta dar la respuesta
        enviar.setAlpha(0);
        enviar.setEnabled(false);
        //agregamos la informacion inteoducida por el usuario
        direccionOrigen = origen.getText().toString();
        direccionDestino = destino.getText().toString();

        //utilizamos la clase inpt method manager para habilitar el teclado virtual y que la informacion pueda ser escrita por el usuario

        InputMethodManager introduce = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        introduce.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        //creamos la barra de progreso mientras realiza estas operaciones

        barraprogrego = new ProgressBar(this);

        //95.16, intanciamos la clase ejecutasegundoplano para que ejecute

        ejecutasegundoplano tarea = new ejecutasegundoplano();
        tarea.execute();

    }

    private class ejecutasegundoplano extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            Location puntoorigen; // 95.03almacenanmos el punto de origen del usuario
            Location puntodestino;

            // 95.04 a aplicacion debe poder usar internet asiq eu creamos un contexto para que avise al usuario

            Context contexto_loquesea=getApplicationContext();

            //95.05 comprobamos la conexxtion con un nuevo methodo
            //este codigo otroga informacion de la conexion a internet
            ConnectivityManager mi_manager=(ConnectivityManager)contexto_loquesea.getSystemService(contexto_loquesea.CONNECTIVITY_SERVICE);

            //95.06 ahora para saber el estado de internet
            NetworkInfo estadored=mi_manager.getActiveNetworkInfo();
            //95.07 ahora en una condiional intentamos poner el estado de la concexion y que ahcer en caso de cada situacion,ej no esta conectado
            if (estadored==null || !estadored.isConnected() || !estadored.isAvailable()){ //condicionales con ! no esta conectada y no esta disponible
                //exite una cadena de string creada, la usareos para que mande el mensaje de que no esta disponible la conexion  res/values/string

                return getString(R.string.error_conexion); // 95.08asi si no hay internet lanza el mensaje

            }

            try {
                //95.09 ahora creamos unas clases que usaremos para que busque el punto de origen y destino y los almacene en los objetos de tipo location que ya hemos creado

                puntoorigen=OptimizaconBusqueda.buscar(direccionOrigen);

                //95.10 tambien debemos asegurarnos de que pasa si ell usuario no ha insertado nada y pulso solo buscar
                if (puntoorigen==null) return getString(R.string.error_origen);

                puntodestino=OptimizaconBusqueda.buscar(direccionDestino);

                if (puntodestino==null) return getString(R.string.error_destino);


            }catch (Exception e){

                return getString(R.string.error_red); //95.11 si el try falla llamara al mensaje error de res



            }

            //95.12 llamamos a un objeto aun no creado para las rutas


            rutas.mejorRuta(puntoorigen,puntodestino,lineas);

            return null; //return null para que el methodo no falle y solo lea la informacion


        }

        //95.13 creamos el methodo de la clase asyns task manager que por defecto debemos usar
        protected void onPostExecute(String Resultadoo){

            barraprogrego=null; //desabilitaamos la barra de progreso

            enviar.setAlpha(1); //allamos el boton de enviar
            enviar.setEnabled(true); //lo activamos

            muestraruta(); //este sera el methodo que calculo la ruta mas rapida


        }
    }
    public void muestraruta (){ //95.14 creamos la clase que hara el calculo de la ruta


    }


    public void onBackPressed(){ //95.15 este methodo es en caso de que la persona pulsae la tecla hacia atras en el dispotivo


        if (barraprogrego !=null) barraprogrego=null; //si la barra de progreso no es nula que retorne nula

        moveTaskToBack(true); //cn esto retorna sin problema



    }
}
