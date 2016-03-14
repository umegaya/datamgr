// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Logger.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            LogLevel

public abstract class Logger
{

    Logger()
    {
        debug_enabled = true;
        info_enabled = true;
        warning_enabled = true;
        error_enabled = true;
    }

    public final void debug(String message)
    {
        if(message == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isDebugEnabled())
            log(LogLevel.DEBUG, message);
    }

    public final transient void debug(String format, Object parameters[])
    {
        if(format == null || parameters == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isDebugEnabled())
            log(LogLevel.DEBUG, String.format(format, parameters));
    }

    public final void info(String message)
    {
        if(message == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isInfoEnabled())
            log(LogLevel.INFO, message);
    }

    public final transient void info(String format, Object parameters[])
    {
        if(format == null || parameters == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isInfoEnabled())
            log(LogLevel.INFO, String.format(format, parameters));
    }

    public final void warning(String message)
    {
        if(message == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isWarningEnabled())
            log(LogLevel.WARNING, message);
    }

    public final transient void warning(String format, Object parameters[])
    {
        if(format == null || parameters == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isWarningEnabled())
            log(LogLevel.WARNING, String.format(format, parameters));
    }

    public final void error(String message)
    {
        if(message == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isErrorEnabled())
            log(LogLevel.ERROR, message);
    }

    public final transient void error(String format, Object parameters[])
    {
        if(format == null || parameters == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isErrorEnabled())
            log(LogLevel.ERROR, String.format(format, parameters));
    }

    public final void error(Throwable cause)
    {
        if(cause == null)
            throw new NullPointerException();
        if(isClosed())
            throw new RuntimeException("logger is closed.");
        if(isErrorEnabled())
        {
            log(LogLevel.ERROR, cause.toString());
            StackTraceElement arr$[] = cause.getStackTrace();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                StackTraceElement stack_trace = arr$[i$];
                log(LogLevel.ERROR, stack_trace.toString());
            }

        }
    }

    public final void isDebugEnabled(boolean enabled)
    {
        debug_enabled = enabled;
    }

    public final boolean isDebugEnabled()
    {
        return debug_enabled;
    }

    public final void isInfoEnabled(boolean enabled)
    {
        info_enabled = enabled;
    }

    public final boolean isInfoEnabled()
    {
        return info_enabled;
    }

    public final void isWarningEnabled(boolean enabled)
    {
        warning_enabled = enabled;
    }

    public final boolean isWarningEnabled()
    {
        return warning_enabled;
    }

    public final void isErrorEnabled(boolean enabled)
    {
        error_enabled = enabled;
    }

    public final boolean isErrorEnabled()
    {
        return error_enabled;
    }

    public abstract void close();

    public abstract boolean isClosed();

    protected abstract void log(LogLevel loglevel, String s);

    private volatile boolean debug_enabled;
    private volatile boolean info_enabled;
    private volatile boolean warning_enabled;
    private volatile boolean error_enabled;
}
