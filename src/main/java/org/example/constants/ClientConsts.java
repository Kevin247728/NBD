package org.example.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class ClientConsts {
    public static final CqlIdentifier ID = CqlIdentifier.fromCql("id");
    public static final CqlIdentifier FIRST_NAME = CqlIdentifier.fromCql("first_name");
    public static final CqlIdentifier LAST_NAME = CqlIdentifier.fromCql("last_name");
    public static final CqlIdentifier MAX_BOOKS = CqlIdentifier.fromCql("max_books");
    public static final CqlIdentifier MAX_RENT_DAYS = CqlIdentifier.fromCql("max_rent_days");
    public static final CqlIdentifier ADDITIONAL_FEE = CqlIdentifier.fromCql("additional_fee");
    public static final CqlIdentifier DISCRIMINATOR = CqlIdentifier.fromCql("discriminator");
    public static final CqlIdentifier CLIENTS = CqlIdentifier.fromCql("clients");
}