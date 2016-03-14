// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpServerResponse.java

package jp.enterquest.system;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

// Referenced classes of package jp.enterquest.system:
//            HttpStatus, WriterStream

public final class HttpServerResponse
{

    static final HttpServerResponse newInstance(HttpServletResponse source)
    {
        return new HttpServerResponse(source);
    }

    private HttpServerResponse(HttpServletResponse source)
    {
        this.source = source;
        stream = null;
    }

    public final void setStatus(HttpStatus status)
    {
        if(status == null)
        {
            throw new NullPointerException();
        } else
        {
            source.setStatus(status.getCode());
            return;
        }
    }

    public final void setHeader(String name, String value)
    {
        if(name == null || value == null)
        {
            throw new NullPointerException();
        } else
        {
            source.setHeader(name, value);
            return;
        }
    }

    public final void addHeader(String name, String value)
    {
        if(name == null || value == null)
        {
            throw new NullPointerException();
        } else
        {
            source.addHeader(name, value);
            return;
        }
    }

    public final WriterStream getStream()
    {
        try
        {
            if(stream == null)
                stream = WriterStream.newInstance(source.getOutputStream());
            return stream;
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private final HttpServletResponse source;
    private volatile WriterStream stream;
}
