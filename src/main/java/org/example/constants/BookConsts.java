package org.example.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class BookConsts {
    public static final CqlIdentifier ID = CqlIdentifier.fromCql("id");
    public static final CqlIdentifier TITLE = CqlIdentifier.fromCql("title");
    public static final CqlIdentifier IS_RENTED = CqlIdentifier.fromCql("rented");
    public static final CqlIdentifier BOOKS = CqlIdentifier.fromCql("books");
}
