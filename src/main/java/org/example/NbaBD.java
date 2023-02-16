package org.example;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class NbaBD {
    String maquina = "localhost";
    String nombreBaseDatos = "NBA";
    String usuario = "nbaUsr";
    String password = "123456";
    MongoClient clienteMongo;
    MongoDatabase baseDatos;
    int puerto = 27017;


    public NbaBD() {
        // Configuración del logger de MongoDB
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoCredential credential = MongoCredential.createCredential(usuario, nombreBaseDatos,
                password.toCharArray());
        clienteMongo = new MongoClient(new ServerAddress(maquina), credential,
                MongoClientOptions.builder().build());

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

    public void insertarListaID() {
        MongoCollection<Document> jugadores = baseDatos.getCollection("jugadores");
        MongoCollection<Document> equipo = baseDatos.getCollection("equipo");
        for (Document jugador : jugadores.find()) {
            DBObject elemento = BasicDBObject.parse(jugador.toJson());
            equipo.updateOne(
                    eq("_id", elemento.get("equipo")),
                    Updates.addToSet("listaIds", elemento.get("_id")));
        }

    }

    public void nuevaCiudad() {
        Scanner teclado = new Scanner(System.in);
        MongoCollection<Document> ciudad = baseDatos.getCollection("equipo");
        for (int i = 0; i < 4; i++) {
            System.out.println("Ingrese el nombre de la ciudad: ");
            String nombreCiudad = teclado.nextLine();
            ciudad.updateMany(new BasicDBObject(), Updates.set("ciudad", nombreCiudad));
        }

    }

    public void nuevoSalario(String equipo, String campo, int valor) {
        MongoCollection<Document> equipos = baseDatos.getCollection("equipo");
        equipos.updateMany(Filters.in("nombre_equipo", equipo), Updates.set(campo, valor));

    }

    public void menuImprimir() {
        Scanner sc = new Scanner(System.in);

        MongoCollection<Document> equiposCollection = baseDatos.getCollection("equipo");
        MongoCollection<Document> jugadoresCollection = baseDatos.getCollection("jugadores");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("a. Mostrar todos los equipos");
            System.out.println("b. Mostrar todos los jugadores");
            System.out.println("c. Mostrar un equipo por un campo (se pide al usuario campo y valor)");
            System.out.println("d. Mostrar un jugador por campo (se pide al usuario campo y valor)");
            System.out.println("e. Añadir un equipo (se piden los datos al usuario)");
            System.out.println("f. Añadir un jugador (se piden los datos al usuario)");
            System.out.println("g. Modificar un equipo (se pide el nombre y luego los datos a modificar al usuario)");
            System.out.println("h. Modificar un jugador (se pide el nombre y luego los datos a modificar al usuario)");
            System.out.println("i. Eliminar un equipo por nombre (se pide al usuario)");
            System.out.println("j. Eliminar un jugador por nombre (se pide al usuario)");
            System.out.println("k. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = sc.next().toLowerCase();

            switch (opcion) {
                case "a":
                    // Mostrar todos los equipos
                    System.out.println("--- TODOS LOS EQUIPOS ---");
                    for (Document equipo : equiposCollection.find()) {
                        System.out.println(equipo.toJson());
                    }
                    break;
                case "b":
                    // Mostrar todos los jugadores
                    System.out.println("--- TODOS LOS JUGADORES ---");
                    for (Document jugador : jugadoresCollection.find()) {
                        System.out.println(jugador.toJson());
                    }
                    break;
                case "c":
                    // Mostrar un equipo por un campo (se pide al usuario campo y valor)
                    System.out.print("Introduzca el campo para buscar el equipo: ");
                    String campoEquipo = sc.next().toLowerCase();
                    System.out.print("Introduzca el valor del campo para buscar el equipo: ");
                    String valorEquipo = sc.next().toLowerCase();
                    System.out.println("--- EQUIPO BUSCADO ---");
                    Document filtroEquipo = new Document(campoEquipo, valorEquipo);
                    for (Document equipo : equiposCollection.find(filtroEquipo)) {
                        System.out.println(equipo.toJson());
                    }
                    break;
                case "d":
                    // Mostrar un jugador por campo (se pide al usuario campo y valor)
                    System.out.print("Introduzca el campo para buscar el jugador: ");
                    String campoJugador = sc.next().toLowerCase();
                    System.out.print("Introduzca el valor del campo para buscar el jugador: ");
                    String valorJugador = sc.next().toLowerCase();
                    System.out.println("--- JUGADOR BUSCADO ---");
                    Document filtroJugador = new Document(campoJugador, valorJugador);
                    for (Document jugador : jugadoresCollection.find(filtroJugador)) {
                        System.out.println(jugador.toJson());
                    }
                    break;
                case "e":
                    // Añadir un equipo (se piden los datos al usuario)
                    System.out.print("Introduzca el id del equipo: ");
                    int id = sc.nextInt();
                    System.out.print("Introduzca el nombre del equipo: ");
                    String nombreEquipo = sc.next().toLowerCase();
                    System.out.print("Introduzca la ciudad del equipo: ");
                    String ciudadEquipo = sc.next().toLowerCase();
                    Document equipo = new Document("nombre_equipo", nombreEquipo)
                            .append("_id", id)
                            .append("nombre_equipo", nombreEquipo)
                            .append("ciudad", ciudadEquipo);
                    equiposCollection.insertOne(equipo);
                    System.out.println("Equipo agregado correctamente.");
                    break;
                case "f":
                    // Añadir un jugador (se piden los datos al usuario)
                    System.out.print("Introduzca el id del jugador: ");
                    int idJugador = sc.nextInt();
                    System.out.print("Introduzca el nombre del jugador: ");
                    String nombreJugador = sc.next().toLowerCase();
                    System.out.print("Introduzca el puesto del jugador: ");
                    String puesto = sc.next().toLowerCase();
                    System.out.print("Introduzca la estatura del jugador: ");
                    int estatura = sc.nextInt();
                    System.out.print("Introduzca el id de equipo del jugador: ");
                    int numeroJugador = sc.nextInt();
                    Document jugador = new Document("nombre_jugador", nombreJugador)
                            .append("_id", idJugador)
                            .append("nombre", nombreJugador)
                            .append("puesto", puesto)
                            .append("estatura", estatura)
                            .append("equipo", numeroJugador);
                    jugadoresCollection.insertOne(jugador);
                    System.out.println("Jugador agregado correctamente.");
                    break;
                case "g":
                    // Modificar un equipo (se pide el nombre y luego los datos a modificar al usuario)
                    // Buscar el documento del equipo en la colección de equipos
                    System.out.print("Introduzca el nombre del equipo a modificar: ");
                    String nombreEquipoModificado = sc.next().toLowerCase();
                    System.out.print("Introduzca la nueva ciudad del equipo: ");
                    String nuevaCiudad = sc.next().toLowerCase();
                    sc.next();
                    equiposCollection.updateMany(new BasicDBObject(), Updates.set("nombre_equipo", nombreEquipoModificado));
                    equiposCollection.updateMany(new BasicDBObject(), Updates.set("ciudad", nuevaCiudad));

                    System.out.println("Equipo modificado correctamente.");
                    break;
                case "h":
                    // Modificar un jugador (se pide el nombre y luego los datos a modificar al usuario)

                    System.out.print("Introduzca el nombre del jugador a modificar: ");
                    String nombreJugadorModificado = sc.next().toLowerCase();
                    break;
                case "i":
                    // Eliminar un equipo por nombre (se pide al usuario)
                    // Pedir al usuario el nombre del equipo que se desea eliminar
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Introduzca el nombre del equipo que desea eliminar:");
                    String nombreEliminar = scanner.nextLine();
                    // Crear un filtro de búsqueda utilizando la función eq de la clase Filters
                    Object filtro1 = Filters.eq("nombre_equipo", nombreEliminar);

                    // Utilizar la función deleteOne de la colección de equipos para eliminar el primer documento que coincida con el filtro de búsqueda
                    equiposCollection.deleteOne((Bson) filtro1);

                    // Imprimir un mensaje de éxito
                    System.out.println("El equipo " + nombreEliminar + " ha sido eliminado.");
                    break;
                case "j":
                    // Eliminar un jugador por nombre (se pide al usuario)
                    // Pedir al usuario el nombre del jugador que se desea eliminar
                    System.out.print("Introduzca el nombre del jugador que desea eliminar: ");
                    String nombreJugadorEliminar = sc.next().toLowerCase();
                    // Crear un filtro de búsqueda utilizando la función eq de la clase Filters
                    Object filtro2 = Filters.eq("nombre", nombreJugadorEliminar);
                    // Utilizar la función deleteOne de la colección de equipos para eliminar el primer documento que coincida con el filtro de búsqueda
                    jugadoresCollection.deleteOne((Bson) filtro2);

                    // Imprimir un mensaje de éxito
                    System.out.println("El jugador " + nombreJugadorEliminar + " ha sido eliminado.");
                    break;
                case "k":
                    salir = true;
            }
        }
    }
}














