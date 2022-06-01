package uk.ac.wlv.chiatiah.database;

public class PropertiesDbSchema {
    public static final class PropertiesTable{
        public static final String NAME = "properties";
        public static final class Cols{
            public static final String UUID = "UUID";
            public static final String HEADING = "heading";
            public static final String DESCRIPTION = "description";
            public static final String ADDRESS = "address";
            public static final String POSTCODE = "postcode";
            public static final String PRICE = "price";
            public static final String DATE = "date";
            public static final String IMAGE = "image";
        }
    }
}
