// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BinaryWriter.java

package jp.enterquest.system;

import java.io.*;

// Referenced classes of package jp.enterquest.system:
//            WriterStream, CharacterEncoding

public final class BinaryWriter
{

    static final BinaryWriter newInstance(WriterStream stream, OutputStream source)
    {
        return new BinaryWriter(stream, source);
    }

    private BinaryWriter(WriterStream stream, OutputStream source)
    {
        this.stream = stream;
        this.source = new DataOutputStream(source);
    }

    public final void writeBoolean(boolean value)
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.writeBoolean(value);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeByte(byte value)
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.writeByte(value);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeInt16(short value)
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.writeShort(value);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeInt32(int value)
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.writeInt(value);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeInt64(long value)
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.writeLong(value);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeFloat32(float value)
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.writeFloat(value);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeFloat64(double value)
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.writeDouble(value);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeString(String string, CharacterEncoding encoding)
    {
        try
        {
            if(string == null || encoding == null)
                throw new NullPointerException();
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            byte bytes[] = string.getBytes(encoding.getName());
            writeInt16((short)bytes.length);
            writeBytes(bytes);
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeBytes(byte bytes[])
    {
        try
        {
            if(bytes == null)
                throw new NullPointerException();
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.write(bytes);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    final void close()
        throws IOException
    {
        source.close();
    }

    private final WriterStream stream;
    private final DataOutputStream source;
}
