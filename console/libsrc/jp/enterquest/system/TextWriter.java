// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextWriter.java

package jp.enterquest.system;

import java.io.*;

// Referenced classes of package jp.enterquest.system:
//            WriterStream, CharacterEncoding, LineSeparator

public final class TextWriter
{

    static final TextWriter newInstance(WriterStream stream, OutputStream source, CharacterEncoding encoding, LineSeparator separator)
    {
        return new TextWriter(stream, source, encoding, separator);
    }

    private TextWriter(WriterStream stream, OutputStream source, CharacterEncoding encoding, LineSeparator separator)
    {
        try
        {
            this.stream = stream;
            this.source = new BufferedWriter(new OutputStreamWriter(source, encoding.getName()));
            this.separator = separator;
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void write(String string)
    {
        try
        {
            if(string == null)
                throw new NullPointerException();
            if(stream.isClosed())
                throw new RuntimeException("writer stream is closed.");
            source.write(string);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void writeLine()
    {
        write(separator.getCode());
    }

    public final LineSeparator getLineSeparator()
    {
        return separator;
    }

    final void close()
        throws IOException
    {
        source.close();
    }

    private final WriterStream stream;
    private final BufferedWriter source;
    private final LineSeparator separator;
}
