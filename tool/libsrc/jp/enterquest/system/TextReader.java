// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextReader.java

package jp.enterquest.system;

import java.io.*;

// Referenced classes of package jp.enterquest.system:
//            ReaderStream, CharacterEncoding

public final class TextReader
{

    static final TextReader newInstance(ReaderStream stream, InputStream source, CharacterEncoding encoding)
    {
        return new TextReader(stream, source, encoding);
    }

    private TextReader(ReaderStream stream, InputStream source, CharacterEncoding encoding)
    {
        try
        {
            this.stream = stream;
            this.source = new BufferedReader(new InputStreamReader(source, encoding.getName()));
            line = null;
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final boolean canRead()
    {
        try
        {
            if(stream.isClosed())
                throw new RuntimeException("reader stream is closed.");
            if(line != null)
                return true;
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
        line = source.readLine();
        return line != null;
    }

    public final String readLine()
    {
        if(stream.isClosed())
            throw new RuntimeException("reader stream is closed.");
        if(this.line == null)
        {
            canRead();
            if(this.line == null)
                return "";
        }
        String line = this.line;
        this.line = null;
        return line;
    }

    final void close()
        throws IOException
    {
        source.close();
    }

    private final ReaderStream stream;
    private final BufferedReader source;
    private volatile String line;
}
