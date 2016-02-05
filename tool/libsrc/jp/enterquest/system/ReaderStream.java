// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReaderStream.java

package jp.enterquest.system;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package jp.enterquest.system:
//            BinaryReader, TextReader, CharacterEncoding

public final class ReaderStream
{

    public static final ReaderStream newInstance(InputStream source)
    {
        return new ReaderStream(source);
    }

    private ReaderStream(InputStream source)
    {
        this.source = source;
        binary_reader = null;
        text_reader = null;
        closed = false;
    }

    public final synchronized BinaryReader getBinaryReader()
    {
        if(text_reader != null)
            throw new RuntimeException("reader stream is already used.");
        if(binary_reader == null)
            binary_reader = BinaryReader.newInstance(this, source);
        return binary_reader;
    }

    public final synchronized TextReader getTextReader(CharacterEncoding encoding)
    {
        if(encoding == null)
            throw new NullPointerException();
        if(binary_reader != null)
            throw new RuntimeException("reader stream is already used.");
        if(text_reader == null)
            text_reader = TextReader.newInstance(this, source, encoding);
        return text_reader;
    }

    public final void close()
    {
        try
        {
            if(binary_reader != null)
                binary_reader.close();
            else
            if(text_reader != null)
                text_reader.close();
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

    private final InputStream source;
    private volatile BinaryReader binary_reader;
    private volatile TextReader text_reader;
    private volatile boolean closed;
}
