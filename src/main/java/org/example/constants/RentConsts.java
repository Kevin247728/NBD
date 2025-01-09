package org.example.constants;

import com.datastax.oss.driver.api.core.CqlIdentifier;

public class RentConsts {
    public static final CqlIdentifier RENT_ID = CqlIdentifier.fromCql("rent_id");
    public static final CqlIdentifier CLIENT_ID = CqlIdentifier.fromCql("client_id");
    public static final CqlIdentifier BOOK_ID = CqlIdentifier.fromCql("book_id");
    public static final CqlIdentifier BEGIN_DATE = CqlIdentifier.fromCql("begin_date");
    public static final CqlIdentifier END_DATE = CqlIdentifier.fromCql("end_date");
    public static final CqlIdentifier FEE = CqlIdentifier.fromCql("fee");
    public static final CqlIdentifier RENTS = CqlIdentifier.fromCql("rents");
}
