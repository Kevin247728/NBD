package org.example.Cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import java.net.InetSocketAddress;
import java.time.Duration;

public class CassandraConnection {
    private CqlSession session;

    public CassandraConnection() {
        try {
            this.session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress("localhost", 9042))
                    .addContactPoint(new InetSocketAddress("localhost", 9043))
                    .withLocalDatacenter("dc1")
                    .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                            .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofMillis(60000))
                            .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(60000))
                            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000))
                            .build())
                    //.withKeyspace(CqlIdentifier.fromCql("rent_a_book"))
                    .withAuthCredentials("nbd", "nbd")
                    .build();
            dropKeyspace();
            if (session.getKeyspace().isEmpty()) {
                this.createKeyspace();
                if (session != null) {
                    session.close();
                }
                session = CqlSession.builder()
                        .addContactPoint(new InetSocketAddress("localhost", 9042))
                        .addContactPoint(new InetSocketAddress("localhost", 9043))
                        .withLocalDatacenter("dc1")
                        .withKeyspace(CqlIdentifier.fromCql("rent_a_book"))
                        .withConfigLoader(DriverConfigLoader.programmaticBuilder()
                                .withDuration(DefaultDriverOption.METADATA_SCHEMA_REQUEST_TIMEOUT, Duration.ofMillis(60000))
                                .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofMillis(60000))
                                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000))
                                .build())
                        .withAuthCredentials("cassandra", "cassandra")
                        .build();
            }
            this.createClientTable();
            this.createBooksTable();
            this.createRentsTable();
            this.createClientIdIndex();
        } catch (Exception e) {
            if (session != null) {
                session.close();
            }
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

    public void createClientTable() {
        if (session != null) {
            session.execute("""
                    CREATE TABLE IF NOT EXISTS rent_a_book.clients (
                        id UUID,
                        first_name text,
                        last_name text,
                        discriminator text,
                        max_books int,
                        max_rent_days int,
                        additional_fee float,
                        PRIMARY KEY (id, discriminator)
                    );
                    """);
        } else {
            throw new IllegalStateException("Session has not been initialized.");
        }
    }

    public void createBooksTable() {
        if (session != null) {
            session.execute("""
                    CREATE TABLE IF NOT EXISTS rent_a_book.books (
                        id UUID,
                        title text,
                        rented boolean,
                        PRIMARY KEY (id)
                    );
                    """);
        } else {
            throw new IllegalStateException("Session has not been initialized.");
        }
    }

    public void dropKeyspace() {
        if (session != null) {
            session.execute("DROP KEYSPACE IF EXISTS rent_a_book;");
        } else {
            throw new IllegalStateException("Session has not been initialized.");
        }
    }

    public void createRentsTable() {
        if (session != null) {
            session.execute("""
                CREATE TABLE IF NOT EXISTS rent_a_book.rents (
                    client_id UUID,
                    rent_id UUID,
                    begin_date date,
                    end_date date,
                    book_id UUID,
                    fee float,
                    PRIMARY KEY ((rent_id), begin_date, end_date)
                ) WITH CLUSTERING ORDER BY (begin_date ASC, end_date ASC);
                """);
        } else {
            throw new IllegalStateException("Session has not been initialized.");
        }
    }

    public void createClientIdIndex() {
        if (session != null) {
            session.execute("""
            CREATE INDEX IF NOT EXISTS idx_client_id ON rent_a_book.rents(client_id);
        """);
        } else {
            throw new IllegalStateException("Session has not been initialized.");
        }
    }

    public CqlSession getSession() {
        return session;
    }
}
