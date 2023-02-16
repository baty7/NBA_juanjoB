package org.example;

public class Main {
    public static void main(String[] args) {

        NbaBD nbaBD = new NbaBD();

        //nbaBD.insertarListaID();
        //nbaBD.nuevaCiudad();
        //nbaBD.nuevoSalario("Los Angeles Lakers", "salario", 0);
        nbaBD.menuImprimir();
        nbaBD.cerrarConexion();
    }
}