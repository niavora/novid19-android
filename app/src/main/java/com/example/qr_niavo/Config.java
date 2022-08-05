package com.example.qr_niavo;

public class Config {
    public static String HOST="https://calm-mesa-49918.herokuapp.com";
    //GET
    public static String AUTHENTIFICATION="/api/personne/";
    //GET
    public static String TESTSCAN="/api/test/";
    //VACCIN:GET
    public static String VACCINSCAN="/api/vaccin/";
    //LIEU:POST
    public static String HISTORIQUELIEU="/api/historique";

    //GET LIEU HISTORIQUE
    public static String _HISTORIQUELIEU="/api/historiquePersonne/";

    //GET TEST HISTORIQUE
    public static String _HISTORIQUETEST="/api/testPersonne/";


    //GET VACCIN HISTORIQUE
    public static String _HISTORIQUEVACCIN="/api/vaccinCarte/";

    //TEST:POST
    public static String HISTORIQUETEST="/api/test";

    //CENTRE:POST
    public static String CENTRE="/api/centre/";

    //LIEU:POST
    public static String LIEU="/api/lieu";





}
