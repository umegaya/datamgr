// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpPart.java

package jp.enterquest.system;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.Part;

// Referenced classes of package jp.enterquest.system:
//            Data, DataFactory, Array, ReaderStream

public final class HttpPart
{

    static final HttpPart newInstance(Part part)
    {
        return new HttpPart(part);
    }

    private HttpPart(Part part)
    {
        this.part = part;
    }

    public final String getName()
    {
        return part.getName();
    }

    public final boolean hasHeader(String name)
    {
        if(name == null)
            throw new NullPointerException();
        else
            return part.getHeader(name) != null;
    }

    public final Data getHeader(String name)
    {
        if(name == null)
            throw new NullPointerException();
        DataFactory factory = DataFactory.getInstance();
        Array values = Array.newInstance();
        String value;
        for(Iterator i$ = part.getHeaders(name).iterator(); i$.hasNext(); values.add(factory.newString(value)))
            value = (String)i$.next();

        values.immutable();
        if(values.count() == 1)
            return (Data)values.get(0);
        else
            return factory.newArray(values);
    }

    public final Array getHeaderNames()
    {
        return Array.newInstance(part.getHeaderNames());
    }

    public final ReaderStream getStream()
    {
        try
        {
            return ReaderStream.newInstance(part.getInputStream());
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private final Part part;
}
