// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpClientResponse.java

package jp.enterquest.system;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;

// Referenced classes of package jp.enterquest.system:
//            Data, Array, Hash, DataFactory, 
//            ReaderStream, HttpClientDelegate

public final class HttpClientResponse
{

    static final HttpClientResponse newInstance(HttpURLConnection connection)
    {
        return new HttpClientResponse(connection);
    }

    private HttpClientResponse(HttpURLConnection connection)
    {
        try
        {
            status_code = connection.getResponseCode();
            header_names = Array.newInstance();
            headers = Hash.newInstance();
            Iterator i$ = connection.getHeaderFields().entrySet().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                java.util.Map.Entry entry_set = (java.util.Map.Entry)i$.next();
                String name = (String)entry_set.getKey();
                if(name != null)
                {
                    Array values = Array.newInstance();
                    String value;
                    for(Iterator i$ = ((List)entry_set.getValue()).iterator(); i$.hasNext(); values.add(DataFactory.getInstance().newString(value)))
                        value = (String)i$.next();

                    values.immutable();
                    header_names.add(name);
                    if(values.count() == 1)
                        headers.set(name, values.get(0));
                    else
                        headers.set(name, DataFactory.getInstance().newArray(values));
                }
            } while(true);
            header_names.immutable();
            stream = ReaderStream.newInstance(connection.getInputStream());
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final int getStatusCode()
    {
        return status_code;
    }

    public final Data getHeader(String name)
    {
        if(name == null)
            throw new NullPointerException();
        else
            return (Data)headers.get(name);
    }

    public final Array getHeaderNames()
    {
        return header_names;
    }

    public final ReaderStream getStream()
    {
        return stream;
    }

    final void process(HttpClientDelegate delegate)
    {
        delegate.onResponse(this);
        stream.close();
    }

    private final int status_code;
    private final Array header_names;
    private final Hash headers;
    private final ReaderStream stream;
}
