// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WriterStream.java

package jp.enterquest.system;

import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package jp.enterquest.system:
//            BinaryWriter, TextWriter, CharacterEncoding, LineSeparator

public final class WriterStream
{

    public static final WriterStream newInstance(OutputStream source)
    {
        return new WriterStream(source);
    }

    private WriterStream(OutputStream source)
    {
        this.source = source;
        binary_writer = null;
        text_writer = null;
        closed = false;
    }

    public final synchronized BinaryWriter getBinaryWriter()
    {
        if(text_writer != null)
            throw new RuntimeException("writer stream is already used.");
        if(binary_writer == null)
            binary_writer = BinaryWriter.newInstance(this, source);
        return binary_writer;
    }

    public final synchronized TextWriter getTextWriter(CharacterEncoding encoding, LineSeparator separator)
    {
        if(encoding == null || separator == null)
            throw new NullPointerException();
        if(binary_writer != null)
            throw new RuntimeException("writer stream is already used.");
        if(text_writer == null)
            text_writer = TextWriter.newInstance(this, source, encoding, separator);
        return text_writer;
    }

    public final void close()
    {
        try
        {
            if(binary_writer != null)
                binary_writer.close();
            else
            if(text_writer != null)
                text_writer.close();
            else
                source.close();
            closed = true;
        }
        catch(IOException ignored)
        {
            ignored.printStackTrace();
        }
    }

    public final boolean isClosed()
    {
        return closed;
    }

    private final OutputStream source;
    private volatile BinaryWriter binary_writer;
    private volatile TextWriter text_writer;
    private volatile boolean closed;
}
