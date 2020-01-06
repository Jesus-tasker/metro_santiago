package com.example.metro_santiago;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OptimizaconBusqueda {
    //optimizaconBusquedda optimiza datos par buscar el metro y ademas carga la omprovacion de internet


    public static Location buscar(String direccionn){ //96.01 creamos el methodo buscar de tipo location
        //96.02 este methdoo buscara recibir una direccion por parametro e intentara localizar la direccion mas cercana al centro de dicha direccion

        Location centrociudad=new Location(""); //aqui almacenaremos la longitud y latitud del centro d ela ciudad donde estemos trabajando

        centrociudad.setLatitude(-33.4378394); //numero sacado de maps

        centrociudad.setLongitude(-70.6526683);

        direccionn=direccionn+",Santiago"; //con esta al buscar en google maps se acorta a busa¿car en madrir

        Location localizacion; //creamos la variable localizacion


        try { // 96.03 creamos un try y cath para que empiece la busqueda lo mejor posible


            localizacion=consultalocalizacion("calle "+ direccionn,centrociudad);
            //llamamos un methodo consultalocalizacion que ua no ha sido creado
            // pero toma de referencia la calle, direccion y el centro d ela ciudad

            return  localizacion;

        }catch (Exception e){

            return null;
        }


    }

    private static Location consultalocalizacion(String direccion,Location centrociudad)throws Exception{ //96.04

        //97.01 primero creamos llos datos a usar, como retornamos al final un objeto de tipo localizacion lo creamos
        Location localizacion;

        InputStream entradadatos; //97.02 inputstream para usar los tipos de datos stream que son paquetes de daatos


        //97.03 este methodo realizara la conexcion con google maps
        //97.04 establecer la conexcion
        //97.05 eta es la referencia a la conexion con la pagina de maps, siempre se usa esta direccion http://maps.google.com/maps/api/geocode/json?address=
        //97.06 con este codigo especificamos los caracteres en español   URLEncoder.encode(direccion,"UTF-8")
        URL url =new URL("http://maps.google.com/maps/api/geocode/json?address=" + URLEncoder.encode(direccion,"UTF-8"));

        //97.7 creamos la conexcion de la url, esta viene desde la api, solo cambiamos el nombre de la estancia a cliente
        HttpURLConnection cliente= (HttpURLConnection) url.openConnection();

        cliente.connect(); //activamos la conecion

        //97.08 creamos u  try y catch que nos da la api y la adaptamos cambiando los datos necesarios
        try {
            entradadatos = new BufferedInputStream(cliente.getInputStream()); //agregamos el dato de cliente a nuestra necesidad
            leerStreamDatos(entradadatos); //97.08.02cambiamos el nombre de el dato stream

        } finally {
            cliente.disconnect();
        }
        //97.10 para descargar un stream, debemos implementar la clase string bulder que son muchos datos

        StringBuilder cadena=new StringBuilder(); //en este almacenaremos la informacion que nos llega

        int caracter; //creamos esta variable para recorrer la informacion que nos lelga de la cadena que crearemos en el while siguiente

        while ((caracter=entradadatos.read())!=-1) { //mientras halla datos diferentes de -1
            //es decir mientras halla datos vamos a almacenar esos caracteres en la variable caracter

            //cadena.append() para que lo valla añadiendo  se hace un casting interno
            cadena.append((char)caracter);

        }
        //TRANSFORMAMOS LA INFORMACION OBTENIDA DEL FLUJO STREAM EN OBJETO JASON
        JSONObject objetoJSON=new JSONObject(cadena.toString()); //con esto la informacion de flujo de datos
        //ahora debemos obtener  la conexcion con el,debemos preguntarnos si el estatus es ok o que nos retorne un null

        if (!(objetoJSON.getString("status").equals("OK"))){ //si el objeto de tipo jason NO tiene un status igual a ok

            return null;
        }

        //asi que vamos a decirle que si el objeto jason esta vacio retorne un null
        //pero este tambien tiene otro estado el cual es result
        //creamos un objeto jason de tipo array
        JSONArray direcciones=objetoJSON.getJSONArray("result"); //lo que hacemos es almacenar en la variableJsonarray todos los resultados que viajan
        if (direccion==null||direccion.length()==0) return null;

        localizacion=getlocalizacion(direcciones.getJSONObject(0)); //estee parametro recibira las direcciones el cual es un jasonarray, una vez ya halla adquirido la informacion

        //ahora pedimos que nos debuelva la localizacion

        return localizacion;

    }

    //97.089creamos el methodo leerstreamdatos para que no genere error
    private static String leerStreamDatos(InputStream entrada){


        return "";
    }

    //97 video final creamos la variable getlocation para coplementar el codigo qeu usamod con los tipos de datos JSON
    private static Location getlocalizacion(JSONObject dire)throws Exception{

        //98.01 ahora vamos a extraer la informacion

        String direccion=dire.getString("formatted_address"); //esta linea de codigo extrae la direccion
        //98.02 de nuevo esta puede tener caracteres latinos
        //el formato que da google maps no es en utf-8 asiq eu debemos convertir la informacion a utf-8
        direccion=new String(direccion.getBytes("ISO-8859-1"),"UTF-8"); //transforma el formato iso de googlee maps a utf-8 para leerlo en latino por si usa Ñ las direcciones
        //crearemos una nueva variable de tipo localizacion

        Location localizacion=new Location(direccion);
        //ahora falta obtener latitud y longitud, y retornar el objeto location a la llamada
        double latitudee=dire.getJSONObject("geometry").getJSONObject("location").getDouble("lat"); //extrae la latitud
        double longitude=dire.getJSONObject("geometry").getJSONObject("location").getDouble("lng"); //etrae la longitud


        localizacion.setLatitude(latitudee); //agrega latitud desde la latitude almacenada del Jsonobject y los mismo con longitud
        localizacion.setLongitude(longitude);

        return localizacion;
    }

}
