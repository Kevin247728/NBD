package org.example;

import org.example.Cassandra.CassandraConnection;

public class Main {
    public static void main(String[] args) {
        CassandraConnection connection = new CassandraConnection();
        try {
            // Inicjalizacja sesji
            System.out.println("Inicjalizacja sesji...");
            connection.initSession();
            System.out.println("Sesja zainicjalizowana.");

            // Tworzenie keyspace
            System.out.println("Tworzenie keyspace 'rent_a_book'...");
            connection.createKeyspace();
            System.out.println("Keyspace 'rent_a_book' został utworzony.");

        } catch (Exception e) {
            System.err.println("Wystąpił błąd: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Zamknięcie sesji
            connection.close();
            System.out.println("Sesja została zamknięta.");
        }
    }
}