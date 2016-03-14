// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConsoleLogger.java

package jp.enterquest.system;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package jp.enterquest.system:
//            Logger, LogLevel

public final class ConsoleLogger extends Logger
{

    public static final Logger newInstance()
    {
        return new ConsoleLogger();
    }

    private ConsoleLogger()
    {
        closed = false;
    }

    public final void close()
    {
        closed = true;
    }

    public final boolean isClosed()
    {
        return closed;
    }

    protected final void log(LogLevel level, String message)
    {
        StackTraceElement stack_trace = Thread.currentThread().getStackTrace()[3];
        String class_name = stack_trace.getClassName();
        System.out.println(String.format("[%s][%s][%s.%s] %s", new Object[] {
            time_format.format(new Date()), level.getName(), class_name.substring(class_name.lastIndexOf(".") + 1), stack_trace.getMethodName(), message
        }));
    }

    private final DateFormat time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private volatile boolean closed;
}
