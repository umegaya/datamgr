// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JsonDecoder.java

package jp.enterquest.system;

import com.fasterxml.jackson.core.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

// Referenced classes of package jp.enterquest.system:
//            Data, DataFactory, Array, Hash

public final class JsonDecoder
{

    public static final JsonDecoder getInstance()
    {
        return instance;
    }

    private JsonDecoder()
    {
    }

    public final Data decode(String string)
    {
        JsonParser parser;
        if(string == null)
            throw new NullPointerException();
        JsonFactory factory = new JsonFactory();
        parser = factory.createJsonParser(string);
        com.fasterxml.jackson.core.JsonParser.NumberType type;
        if(parser.nextToken() == null)
            break MISSING_BLOCK_LABEL_399;
        if(parser.getCurrentToken() != JsonToken.START_ARRAY)
            break MISSING_BLOCK_LABEL_57;
        type = readArray(parser);
        parser.close();
        return type;
        if(parser.getCurrentToken() != JsonToken.START_OBJECT)
            break MISSING_BLOCK_LABEL_81;
        type = readObject(parser);
        parser.close();
        return type;
        if(parser.getCurrentToken() != JsonToken.VALUE_TRUE)
            break MISSING_BLOCK_LABEL_110;
        type = DataFactory.getInstance().newBoolean(parser.getBooleanValue());
        parser.close();
        return type;
        if(parser.getCurrentToken() != JsonToken.VALUE_FALSE)
            break MISSING_BLOCK_LABEL_139;
        type = DataFactory.getInstance().newBoolean(parser.getBooleanValue());
        parser.close();
        return type;
        Data data;
        if(parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT)
            break MISSING_BLOCK_LABEL_242;
        type = parser.getNumberType();
        if(type != com.fasterxml.jackson.core.JsonParser.NumberType.INT)
            break MISSING_BLOCK_LABEL_182;
        data = DataFactory.getInstance().newInt32(parser.getIntValue());
        parser.close();
        return data;
        if(type != com.fasterxml.jackson.core.JsonParser.NumberType.LONG)
            break MISSING_BLOCK_LABEL_209;
        data = DataFactory.getInstance().newInt64(parser.getLongValue());
        parser.close();
        return data;
        if(type != com.fasterxml.jackson.core.JsonParser.NumberType.BIG_INTEGER)
            break MISSING_BLOCK_LABEL_399;
        data = DataFactory.getInstance().newFloat64(parser.getBigIntegerValue().doubleValue());
        parser.close();
        return data;
        if(parser.getCurrentToken() != JsonToken.VALUE_NUMBER_FLOAT)
            break MISSING_BLOCK_LABEL_345;
        type = parser.getNumberType();
        if(type != com.fasterxml.jackson.core.JsonParser.NumberType.FLOAT)
            break MISSING_BLOCK_LABEL_285;
        data = DataFactory.getInstance().newFloat32(parser.getFloatValue());
        parser.close();
        return data;
        if(type != com.fasterxml.jackson.core.JsonParser.NumberType.DOUBLE)
            break MISSING_BLOCK_LABEL_312;
        data = DataFactory.getInstance().newFloat64(parser.getDoubleValue());
        parser.close();
        return data;
        if(type != com.fasterxml.jackson.core.JsonParser.NumberType.BIG_DECIMAL)
            break MISSING_BLOCK_LABEL_399;
        data = DataFactory.getInstance().newFloat64(parser.getDecimalValue().doubleValue());
        parser.close();
        return data;
        if(parser.getCurrentToken() != JsonToken.VALUE_STRING)
            break MISSING_BLOCK_LABEL_374;
        type = DataFactory.getInstance().newString(parser.getText());
        parser.close();
        return type;
        if(parser.getCurrentToken() != JsonToken.VALUE_NULL)
            break MISSING_BLOCK_LABEL_399;
        type = DataFactory.getInstance().getNull();
        parser.close();
        return type;
        type = DataFactory.getInstance().getNull();
        parser.close();
        return type;
        Exception exception;
        exception;
        parser.close();
        throw exception;
        IOException cause;
        cause;
        throw new RuntimeException(cause);
    }

    private final Data readArray(JsonParser parser)
        throws IOException
    {
        Data array = DataFactory.getInstance().newArray();
        if(parser.getCurrentToken() == JsonToken.START_ARRAY)
            do
            {
                if(parser.nextToken() == JsonToken.END_ARRAY)
                    break;
                if(parser.getCurrentToken() == JsonToken.START_ARRAY)
                    array.asArray().add(readArray(parser));
                else
                if(parser.getCurrentToken() == JsonToken.START_OBJECT)
                    array.asArray().add(readObject(parser));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_TRUE)
                    array.asArray().add(DataFactory.getInstance().newBoolean(parser.getBooleanValue()));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_FALSE)
                    array.asArray().add(DataFactory.getInstance().newBoolean(parser.getBooleanValue()));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_NUMBER_INT)
                {
                    com.fasterxml.jackson.core.JsonParser.NumberType type = parser.getNumberType();
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.INT)
                        array.asArray().add(DataFactory.getInstance().newInt32(parser.getIntValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.LONG)
                        array.asArray().add(DataFactory.getInstance().newInt64(parser.getLongValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.BIG_INTEGER)
                        array.asArray().add(DataFactory.getInstance().newFloat64(parser.getBigIntegerValue().doubleValue()));
                } else
                if(parser.getCurrentToken() == JsonToken.VALUE_NUMBER_FLOAT)
                {
                    com.fasterxml.jackson.core.JsonParser.NumberType type = parser.getNumberType();
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.FLOAT)
                        array.asArray().add(DataFactory.getInstance().newFloat32(parser.getFloatValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.DOUBLE)
                        array.asArray().add(DataFactory.getInstance().newFloat64(parser.getDoubleValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.BIG_DECIMAL)
                        array.asArray().add(DataFactory.getInstance().newFloat64(parser.getDecimalValue().doubleValue()));
                } else
                if(parser.getCurrentToken() == JsonToken.VALUE_STRING)
                    array.asArray().add(DataFactory.getInstance().newString(parser.getText()));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_NULL)
                    array.asArray().add(DataFactory.getInstance().getNull());
            } while(true);
        return array;
    }

    private final Data readObject(JsonParser parser)
        throws IOException
    {
        Data hash = DataFactory.getInstance().newHash();
        if(parser.getCurrentToken() == JsonToken.START_OBJECT)
            do
            {
                if(parser.nextToken() == JsonToken.END_OBJECT)
                    break;
                String name = parser.getCurrentName();
                parser.nextToken();
                if(parser.getCurrentToken() == JsonToken.START_ARRAY)
                    hash.asHash().set(name, readArray(parser));
                else
                if(parser.getCurrentToken() == JsonToken.START_OBJECT)
                    hash.asHash().set(name, readObject(parser));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_TRUE)
                    hash.asHash().set(name, DataFactory.getInstance().newBoolean(parser.getBooleanValue()));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_FALSE)
                    hash.asHash().set(name, DataFactory.getInstance().newBoolean(parser.getBooleanValue()));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_NUMBER_INT)
                {
                    com.fasterxml.jackson.core.JsonParser.NumberType type = parser.getNumberType();
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.INT)
                        hash.asHash().set(name, DataFactory.getInstance().newInt32(parser.getIntValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.LONG)
                        hash.asHash().set(name, DataFactory.getInstance().newInt64(parser.getLongValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.BIG_INTEGER)
                        hash.asHash().set(name, DataFactory.getInstance().newFloat64(parser.getBigIntegerValue().doubleValue()));
                } else
                if(parser.getCurrentToken() == JsonToken.VALUE_NUMBER_FLOAT)
                {
                    com.fasterxml.jackson.core.JsonParser.NumberType type = parser.getNumberType();
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.FLOAT)
                        hash.asHash().set(name, DataFactory.getInstance().newFloat32(parser.getFloatValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.DOUBLE)
                        hash.asHash().set(name, DataFactory.getInstance().newFloat64(parser.getDoubleValue()));
                    else
                    if(type == com.fasterxml.jackson.core.JsonParser.NumberType.BIG_DECIMAL)
                        hash.asHash().set(name, DataFactory.getInstance().newFloat64(parser.getDecimalValue().doubleValue()));
                } else
                if(parser.getCurrentToken() == JsonToken.VALUE_STRING)
                    hash.asHash().set(name, DataFactory.getInstance().newString(parser.getText()));
                else
                if(parser.getCurrentToken() == JsonToken.VALUE_NULL)
                    hash.asHash().set(name, DataFactory.getInstance().getNull());
            } while(true);
        return hash;
    }

    private static final JsonDecoder instance = new JsonDecoder();

}
