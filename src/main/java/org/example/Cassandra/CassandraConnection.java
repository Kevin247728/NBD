package org.example.Cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;

public class CassandraConnection {
    private CqlSession session;

    public CassandraConnection() {
        try {

            this.session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress("localhost", 9042))
                    .addContactPoint(new InetSocketAddress("localhost", 9043))
                    .withLocalDatacenter("dc1")
                    .withAuthCredentials("nbd", "nbd")
                    //.withKeyspace(CqlIdentifier.fromCql("rent_a_book"))
                    .build();
            System.out.println("Sesja zainicjalizowana pomyślnie.");
        } catch (Exception e) {
            System.err.println("Błąd podczas inicjalizacji sesji: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void createKeyspace() {
        if (session != null) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS rent_a_book " +
                    "WITH replication = {'class':'SimpleStrategy', 'replication_factor':2};");
        }

        else {
            throw new IllegalStateException("Session has not been initialized.");
        }
    }
}
