package com.kis.serializer;

public final class SerializerFactory {

    private SerializerFactory() {

    }

    public static Serializer getXmlSerializer() {
        return XmlSerializer.INSTANCE;
    }

    public static Serializer getSqlSerializer() {
        return SqlSerializer.INSTANCE;
    }
}
