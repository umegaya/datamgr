// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BinaryReader.java

package jp.enterquest.system;

import java.io.*;

// Referenced classes of package jp.enterquest.system:
//            ReaderStream, CharacterEncoding

public final class BinaryReader
{

    static final BinaryReader newInstance(ReaderStream stream, InputStream source)
    {
        return new BinaryReader(stream, source);
    }

    private BinaryReader(ReaderStream stream, InputStream source)
    {
        this.stream = stream;
        this.source = new DataInputStream(source);
    }

    public final boolean readBoolean()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            else
                return source.readBoolean();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final short readByte()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            else
                return (short)source.readByte();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final short readInt16()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            else
                return source.readShort();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final int readInt32()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            else
                return source.readInt();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long readInt64()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            else
                return source.readLong();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final float readFloat32()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            else
                return source.readFloat();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final double readFloat64()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            else
                return source.readDouble();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final String readString(CharacterEncoding encoding)
    {
        try
        {
            if(encoding == null)
                throw new NullPointerException();
            if(stream.isClosed())
            {
                throw new RuntimeException("reader stream is closed.");
            } else
            {
                int size = readInt16();
                byte bytes[] = readBytes(size);
                return new String(bytes, encoding.getName());
            }
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }

    public final byte[] readBytes(int read_size)
    {
        try
        {
            if(stream.isClosed())
            {
                throw new RuntimeException("reader stream is closed.");
            } else
            {
                byte bytes[] = new byte[read_size];
                source.readFully(bytes);
                return bytes;
            }
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final byte[] readFully(int buffer_size)
    {
        ByteArrayOutputStream stream;
        if(this.stream.isClosed())
            throw new RuntimeException("reader stream is closed.");
        stream = new ByteArrayOutputStream(buffer_size);
        byte abyte0[];
        byte bytes[] = new byte[256];
        for(int read_size = 0; 0 < (read_size = source.read(bytes));)
            stream.write(bytes, 0, read_size);

        stream.flush();
        abyte0 = stream.toByteArray();
        stream.close();
        return abyte0;
        Exception exception;
        exception;
        stream.close();
        throw exception;
        IOException cause;
        cause;
        throw new RuntimeException(cause);
    }

    final void close()
        throws IOException
    {
        source.close();
    }

    private final ReaderStream stream;
    private final DataInputStream source;
}
