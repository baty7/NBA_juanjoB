package org.example;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.internal.MongoClientImpl;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NbaBD {
    String maquina = "localhost";
    String nombreBaseDatos = "NBA";
    String password = "123456";
    MongoClient clienteMongo;
    MongoDatabase baseDatos;
    int puerto = 27017;


    public NbaBD() {
        // Configuración del logger de MongoDB
       /* Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoCredential credential = MongoCredential.createCredential("nbaUsr", nombreBaseDatos,
                password.toCharArray());
        clienteMongo = new MongoClient(new ServerAddress(maquina), credential,
                MongoClientOptions.builder().build());*/

        clienteMongo = new MongoClient(maquina, puerto);
        baseDatos = clienteMongo.getDatabase(nombreBaseDatos);

    }

    public void cerrarConexion() {
        clienteMongo.close();
    }


   public void leerDatosColeccion(String coleccion) {
        MongoCollection<Document> collection = baseDatos.getCollection(coleccion);
        for (Document document : collection.find()) {
            System.out.println(document.toJson());
        }
    }

    public void insertarDatos(){
        MongoCollection<Document> jugadores = baseDatos.getCollection("equipos");
        List<Document> idJugadores = new ArrayList<>();
        idJugadores.append("_id", 7007);
        empleado.append("nombre", "Carmelo Guirao Costa");
        empleado.append("puesto", "Sistemas");
        empleado.append("departamento", 20);
        empleados.insertMany();

    }


}
