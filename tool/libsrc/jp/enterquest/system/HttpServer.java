// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpServer.java

package jp.enterquest.system;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.http.*;

// Referenced classes of package jp.enterquest.system:
//            HttpServerDelegate, Timer, Logger, ConsoleLogger, 
//            Hash, DataFactory, HttpServerRequest, HttpServerResponse

public final class HttpServer extends HttpServlet
{

    public HttpServer()
    {
        _flddelegate = null;
    }

    public final String getName()
    {
        return getServletName();
    }

    public final Hash getParameters()
    {
        return parameters;
    }

    public final Hash getLoggers()
    {
        return loggers;
    }

    public final Hash getDatabases()
    {
        return databases;
    }

    public final Hash getTimers()
    {
        return timers;
    }

    public final void init(ServletConfig config)
    {
        super.init(config);
        console_logger.info("%s start", new Object[] {
            getServletName()
        });
        String name;
        String value;
        for(Enumeration names = getInitParameterNames(); names.hasMoreElements(); parameters.set(name, DataFactory.getInstance().newString(value)))
        {
            name = (String)names.nextElement();
            value = getInitParameter(name);
        }

        parameters.immutable();
        Class delegate_class = Class.forName(getInitParameter("server-delegate-class"));
        Constructor constructor = delegate_class.getConstructor(new Class[] {
            jp/enterquest/system/HttpServer
        });
        _flddelegate = (HttpServerDelegate)constructor.newInstance(new Object[] {
            this
        });
        _flddelegate.onInitialize();
        console_logger.info("%s end", new Object[] {
            getServletName()
        });
        break MISSING_BLOCK_LABEL_237;
        Throwable cause;
        cause;
        console_logger.error("%s error", new Object[] {
            getServletName()
        });
        console_logger.error(cause);
        console_logger.info("%s end", new Object[] {
            getServletName()
        });
        break MISSING_BLOCK_LABEL_237;
        Exception exception;
        exception;
        console_logger.info("%s end", new Object[] {
            getServletName()
        });
        throw exception;
    }

    public final void destroy()
    {
        console_logger.info("%s start", new Object[] {
            getServletName()
        });
        if(_flddelegate != null)
            _flddelegate.onFinalize();
        String name;
        for(Iterator i$ = timers.iterator(); i$.hasNext(); ((Timer)timers.get(name)).stop())
            name = (String)i$.next();

        String name;
        for(Iterator i$ = loggers.iterator(); i$.hasNext(); ((Logger)loggers.get(name)).close())
            name = (String)i$.next();

        timers.clear();
        databases.clear();
        loggers.clear();
        console_logger.info("%s end", new Object[] {
            getServletName()
        });
        console_logger.close();
        super.destroy();
        break MISSING_BLOCK_LABEL_276;
        Throwable cause;
        cause;
        console_logger.error("%s error", new Object[] {
            getServletName()
        });
        console_logger.error(cause);
        console_logger.info("%s end", new Object[] {
            getServletName()
        });
        console_logger.close();
        super.destroy();
        break MISSING_BLOCK_LABEL_276;
        Exception exception;
        exception;
        console_logger.info("%s end", new Object[] {
            getServletName()
        });
        console_logger.close();
        super.destroy();
        throw exception;
    }

    public final void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            if(_flddelegate != null)
                _flddelegate.onGet(HttpServerRequest.newInstance(request, response), HttpServerResponse.newInstance(response));
        }
        catch(Throwable cause)
        {
            console_logger.error("%s error", new Object[] {
                getServletName()
            });
            console_logger.error(cause);
        }
    }

    public final void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            if(_flddelegate != null)
                _flddelegate.onPost(HttpServerRequest.newInstance(request, response), HttpServerResponse.newInstance(response));
        }
        catch(Throwable cause)
        {
            console_logger.error("%s error", new Object[] {
                getServletName()
            });
            console_logger.error(cause);
        }
    }

    private static final String SERVER_DELEGATE_CLASS = "server-delegate-class";
    private final Logger console_logger = ConsoleLogger.newInstance();
    private final Hash parameters = Hash.newInstance();
    private final Hash loggers = Hash.newInstance();
    private final Hash databases = Hash.newInstance();
    private final Hash timers = Hash.newInstance();
    private volatile HttpServerDelegate _flddelegate;
}
