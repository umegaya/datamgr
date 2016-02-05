// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JsonEncoder.java

package jp.enterquest.system;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

// Referenced classes of package jp.enterquest.system:
//            Data, Array, Hash

public final class JsonEncoder
{

    public static final JsonEncoder getInstance()
    {
        return instance;
    }

    private JsonEncoder()
    {
    }

    public final String encode(Data data)
    {
        StringWriter writer;
        if(data == null)
            throw new NullPointerException();
        writer = new StringWriter(10240);
        JsonGenerator generator;
        JsonFactory factory = new JsonFactory();
        generator = factory.createGenerator(writer);
        if(data.isBoolean())
            generator.writeBoolean(data.asBoolean());
        else
        if(data.isByte())
            generator.writeNumber(data.asByte());
        else
        if(data.isInt16())
            generator.writeNumber(data.asInt16());
        else
        if(data.isInt32())
            generator.writeNumber(data.asInt32());
        else
        if(data.isInt64())
            generator.writeNumber(data.asInt64());
        else
        if(data.isFloat32())
            generator.writeNumber(data.asFloat32());
        else
        if(data.isFloat64())
            generator.writeNumber(data.asFloat64());
        else
        if(data.isString())
            generator.writeString(data.asString());
        else
        if(data.isArray())
            writeArray(generator, data);
        else
        if(data.isHash())
            writeObject(generator, data);
        else
        if(data.isNull())
            generator.writeNull();
        generator.close();
        break MISSING_BLOCK_LABEL_254;
        Exception exception;
        exception;
        generator.close();
        throw exception;
        String s = writer.toString();
        writer.close();
        return s;
        Exception exception1;
        exception1;
        writer.close();
        throw exception1;
        IOException cause;
        cause;
        throw new RuntimeException(cause);
    }

    private final void writeArray(JsonGenerator generator, Data data)
        throws IOException
    {
        if(data.isArray())
        {
            generator.writeStartArray();
            Iterator i$ = data.asArray().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                Data value = (Data)i$.next();
                if(value.isBoolean())
                    generator.writeBoolean(value.asBoolean());
                else
                if(value.isByte())
                    generator.writeNumber(value.asByte());
                else
                if(value.isInt16())
                    generator.writeNumber(value.asInt16());
                else
                if(value.isInt32())
                    generator.writeNumber(value.asInt32());
                else
                if(value.isInt64())
                    generator.writeNumber(value.asInt64());
                else
                if(value.isFloat32())
                    generator.writeNumber(value.asFloat32());
                else
                if(value.isFloat64())
                    generator.writeNumber(value.asFloat64());
                else
                if(value.isString())
                    generator.writeString(value.asString());
                else
                if(value.isArray())
                    writeArray(generator, value);
                else
                if(value.isHash())
                    writeObject(generator, value);
                else
                if(value.isNull())
                    generator.writeNull();
            } while(true);
            generator.writeEndArray();
        }
    }

    private final void writeObject(JsonGenerator generator, Data data)
        throws IOException
    {
        if(data.isHash())
        {
            generator.writeStartObject();
            Iterator i$ = data.asHash().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                String name = (String)i$.next();
                Data value = (Data)data.asHash().get(name);
                generator.writeFieldName(name);
                if(value.isBoolean())
                    generator.writeBoolean(value.asBoolean());
                else
                if(value.isByte())
                    generator.writeNumber(value.asByte());
                else
                if(value.isInt16())
                    generator.writeNumber(value.asInt16());
                else
                if(value.isInt32())
                    generator.writeNumber(value.asInt32());
                else
                if(value.isInt64())
                    generator.writeNumber(value.asInt64());
                else
                if(value.isFloat32())
                    generator.writeNumber(value.asFloat32());
                else
                if(value.isFloat64())
                    generator.writeNumber(value.asFloat64());
                else
                if(value.isString())
                    generator.writeString(value.asString());
                else
                if(value.isArray())
                    writeArray(generator, value);
                else
                if(value.isHash())
                    writeObject(generator, value);
                else
                if(value.isNull())
                    generator.writeNull();
            } while(true);
            generator.writeEndObject();
        }
    }

    private static final JsonEncoder instance = new JsonEncoder();

}
