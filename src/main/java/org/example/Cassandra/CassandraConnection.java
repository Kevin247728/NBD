package org.example.Cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class CassandraConnection implements AutoCloseable {
    private CqlSession session;

    public CassandraConnection() {
        try {

            this.session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                    .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                    .withLocalDatacenter("dc1")
                    .withAuthCredentials("nbd", "nbd")
                    .withKeyspace(CqlIdentifier.fromCql("rent_a_book"))
                    .build();
            createKeyspace();
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

    @Override
    public void close() {}
}
