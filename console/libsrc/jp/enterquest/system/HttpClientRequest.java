// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpClientRequest.java

package jp.enterquest.system;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

// Referenced classes of package jp.enterquest.system:
//            Data, HttpClientDelegate, HttpMethod, Hash, 
//            WriterStream, DataFactory, Array, HttpClientResponse

public final class HttpClientRequest
{

    static final HttpClientRequest newInstance()
    {
        return new HttpClientRequest();
    }

    private HttpClientRequest()
    {
        url = "";
        method = HttpMethod.GET;
        stream = WriterStream.newInstance(buffer);
    }

    public final void setUrl(String url)
    {
        if(url == null)
        {
            throw new NullPointerException();
        } else
        {
            this.url = url;
            return;
        }
    }

    public final void setMethod(HttpMethod method)
    {
        if(method == null)
        {
            throw new NullPointerException();
        } else
        {
            this.method = method;
            return;
        }
    }

    public final void setQuery(String name, String value)
    {
        if(name == null || value == null)
            throw new NullPointerException();
        if(!queries.has(name))
            queries.set(name, DataFactory.getInstance().newArray());
        else
            ((Data)queries.get(name)).asArray().clear();
        ((Data)queries.get(name)).asArray().add(DataFactory.getInstance().newString(value));
    }

    public final void addQuery(String name, String value)
    {
        if(name == null || value == null)
            throw new NullPointerException();
        if(!queries.has(name))
            queries.set(name, DataFactory.getInstance().newArray());
        ((Data)queries.get(name)).asArray().add(DataFactory.getInstance().newString(value));
    }

    public final void setHeader(String name, String value)
    {
        if(name == null || value == null)
            throw new NullPointerException();
        if(!headers.has(name))
            headers.set(name, DataFactory.getInstance().newArray());
        else
            ((Data)headers.get(name)).asArray().clear();
        ((Data)headers.get(name)).asArray().add(DataFactory.getInstance().newString(value));
    }

    public final void addHeader(String name, String value)
    {
        if(name == null || value == null)
            throw new NullPointerException();
        if(!headers.has(name))
            headers.set(name, DataFactory.getInstance().newArray());
        ((Data)headers.get(name)).asArray().add(DataFactory.getInstance().newString(value));
    }

    public final WriterStream getStream()
    {
        return stream;
    }

    final void process(HttpClientDelegate delegate)
    {
        HttpURLConnection connection;
        delegate.onRequest(this);
        this.stream.close();
        StringBuilder url_buffer = new StringBuilder(1024);
        url_buffer.append(this.url);
        boolean first = true;
        for(Iterator i$ = queries.iterator(); i$.hasNext();)
        {
            String name = (String)i$.next();
            Iterator i$ = ((Data)queries.get(name)).asArray().iterator();
            while(i$.hasNext()) 
            {
                Data value = (Data)i$.next();
                url_buffer.append(first ? "?" : "&");
                url_buffer.append(name);
                url_buffer.append("=");
                url_buffer.append(value.asString());
                first = false;
            }
        }

        URL url = new URL(url_buffer.toString());
        connection = (HttpURLConnection)url.openConnection();
        OutputStream stream;
        for(Iterator i$ = headers.iterator(); i$.hasNext();)
        {
            String name = (String)i$.next();
            Iterator i$ = ((Data)headers.get(name)).asArray().iterator();
            while(i$.hasNext()) 
            {
                Data value = (Data)i$.next();
                connection.addRequestProperty(name, value.asString());
            }
        }

        connection.setRequestMethod(method.getName());
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(method == HttpMethod.POST);
        if(!connection.getDoOutput())
            break MISSING_BLOCK_LABEL_360;
        stream = connection.getOutputStream();
        stream.write(buffer.toByteArray());
        stream.close();
        break MISSING_BLOCK_LABEL_360;
        Exception exception;
        exception;
        stream.close();
        throw exception;
        connection.connect();
        HttpClientResponse response = HttpClientResponse.newInstance(connection);
        response.process(delegate);
        connection.disconnect();
        break MISSING_BLOCK_LABEL_409;
        Exception exception1;
        exception1;
        connection.disconnect();
        throw exception1;
        IOException cause;
        cause;
        throw new RuntimeException(cause);
    }

    private volatile String url;
    private volatile HttpMethod method;
    private final Hash queries = Hash.newInstance();
    private final Hash headers = Hash.newInstance();
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final WriterStream stream;
}
