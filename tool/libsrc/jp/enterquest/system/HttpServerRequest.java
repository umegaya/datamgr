// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpServerRequest.java

package jp.enterquest.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

// Referenced classes of package jp.enterquest.system:
//            Data, Array, CharacterEncoding, DataFactory, 
//            HttpPart, ReaderStream

public final class HttpServerRequest
{

    static final HttpServerRequest newInstance(HttpServletRequest request, HttpServletResponse response)
    {
        return new HttpServerRequest(request, response);
    }

    private HttpServerRequest(HttpServletRequest request, HttpServletResponse response)
    {
        this.request = request;
        this.response = response;
        stream = null;
    }

    public final boolean hasAttribute(String name)
    {
        if(name == null)
            throw new NullPointerException();
        else
            return request.getAttribute(name) != null;
    }

    public final void setAttribute(String name, Object value)
    {
        if(name == null || value == null)
        {
            throw new NullPointerException();
        } else
        {
            request.setAttribute(name, value);
            return;
        }
    }

    public final Object getAttribute(String name)
    {
        if(name == null)
            throw new NullPointerException();
        else
            return request.getAttribute(name);
    }

    public final void setEncoding(CharacterEncoding encoding)
    {
        try
        {
            request.setCharacterEncoding(encoding.getName());
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final String getEncoding()
    {
        return request.getCharacterEncoding();
    }

    public final boolean hasHeader(String name)
    {
        if(name == null)
            throw new NullPointerException();
        else
            return request.getHeader(name) != null;
    }

    public final Data getHeader(String name)
    {
        if(name == null)
            throw new NullPointerException();
        Array values = Array.newInstance();
        for(Enumeration enumeration = request.getHeaders(name); enumeration.hasMoreElements(); values.add(DataFactory.getInstance().newString((String)enumeration.nextElement())));
        values.immutable();
        if(values.count() == 1)
            return (Data)values.get(0);
        else
            return DataFactory.getInstance().newArray(values);
    }

    public final Array getHeaderNames()
    {
        Array names = Array.newInstance();
        for(Enumeration enumeration = request.getHeaderNames(); enumeration.hasMoreElements(); names.add(enumeration.nextElement()));
        names.immutable();
        return names;
    }

    public final String getMethod()
    {
        return request.getMethod();
    }

    public final boolean hasParameter(String name)
    {
        if(name == null)
            throw new NullPointerException();
        else
            return request.getParameter(name) != null;
    }

    public final Data getParameter(String name)
    {
        if(name == null)
            throw new NullPointerException();
        Array values = Array.newInstance();
        String arr$[] = request.getParameterValues(name);
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String value = arr$[i$];
            values.add(DataFactory.getInstance().newString(value));
        }

        values.immutable();
        if(values.count() == 1)
            return (Data)values.get(0);
        else
            return DataFactory.getInstance().newArray(values);
    }

    public final Array getParameterNames()
    {
        Array names = Array.newInstance();
        for(Enumeration enumeration = request.getParameterNames(); enumeration.hasMoreElements(); names.add(enumeration.nextElement()));
        names.immutable();
        return names;
    }

    public final boolean hasPart(String name)
    {
        try
        {
            if(name == null)
                throw new NullPointerException();
            else
                return request.getPart(name) != null;
        }
        catch(IllegalStateException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(ServletException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final HttpPart getPart(String name)
    {
        try
        {
            if(name == null)
                throw new NullPointerException();
            else
                return HttpPart.newInstance(request.getPart(name));
        }
        catch(IllegalStateException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(ServletException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final Array getParts()
    {
        try
        {
            Array parts = Array.newInstance();
            Part part;
            for(Iterator i$ = request.getParts().iterator(); i$.hasNext(); parts.add(HttpPart.newInstance(part)))
                part = (Part)i$.next();

            parts.immutable();
            return parts;
        }
        catch(IllegalStateException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(ServletException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final String getRemoteAddr()
    {
        return request.getRemoteAddr();
    }

    public final String getRemoteHost()
    {
        return request.getRemoteHost();
    }

    public final ReaderStream getStream()
    {
        try
        {
            if(stream == null)
                stream = ReaderStream.newInstance(request.getInputStream());
            return stream;
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final String getRequestUrl()
    {
        return request.getRequestURL().toString();
    }

    public final void forward(String path)
    {
        try
        {
            if(path == null)
                throw new NullPointerException();
            request.getRequestDispatcher(path).forward(request, response);
        }
        catch(ServletException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private volatile ReaderStream stream;
}
