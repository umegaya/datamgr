// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SyslogLogger.java

package jp.enterquest.system;

import java.io.IOException;
import java.net.*;
import java.text.*;
import java.util.Date;
import java.util.Locale;

// Referenced classes of package jp.enterquest.system:
//            Logger, SyslogFacility, SyslogSeverity, LogLevel

public final class SyslogLogger extends Logger
{

    public static final SyslogLogger newInstance(String hostname, String facility, String tag)
    {
        return new SyslogLogger(hostname, 514, facility, tag);
    }

    public static final SyslogLogger newInstance(String hostname, int port, String facility, String tag)
    {
        return new SyslogLogger(hostname, port, facility, tag);
    }

    private SyslogLogger(String hostname, int port, String facility, String tag)
    {
        try
        {
            if(hostname == null || facility == null || tag == null)
                throw new NullPointerException();
            local_host = InetAddress.getLocalHost();
            syslog_host = InetAddress.getByName(hostname);
            this.port = port;
            socket = new DatagramSocket();
            this.facility = SyslogFacility.find(facility);
            time_format = new SimpleDateFormat("MMM dd HH:mm:ss", new DateFormatSymbols(Locale.US));
            this.tag = tag;
            closed = false;
        }
        catch(UnknownHostException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(SocketException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void close()
    {
        socket.close();
        closed = true;
    }

    public final boolean isClosed()
    {
        return closed;
    }

    protected final void log(LogLevel level, String message)
    {
        try
        {
            StringBuilder buffer = new StringBuilder(1024);
            buffer.append("<");
            buffer.append(facility.getCode() << 3 | getSeverity(level).getCode());
            buffer.append(">");
            buffer.append(time_format.format(new Date()));
            buffer.append(" ");
            buffer.append(local_host.getHostName());
            buffer.append(" ");
            buffer.append(tag);
            buffer.append(": ");
            StackTraceElement stack_trace = Thread.currentThread().getStackTrace()[3];
            String class_name = stack_trace.getClassName();
            String content = String.format("[%s][%s.%s] %s", new Object[] {
                level.getName(), class_name.substring(class_name.lastIndexOf(".") + 1), stack_trace.getMethodName(), message
            });
            buffer.append(content);
            byte bytes[] = buffer.toString().getBytes();
            socket.send(new DatagramPacket(bytes, Math.min(bytes.length, 2014), syslog_host, port));
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private final SyslogSeverity getSeverity(LogLevel level)
    {
        if(level == LogLevel.ERROR)
            return SyslogSeverity.ERROR;
        if(level == LogLevel.WARNING)
            return SyslogSeverity.WARNING;
        if(level == LogLevel.INFO)
            return SyslogSeverity.INFO;
        else
            return SyslogSeverity.DEBUG;
    }

    private static final int PORT = 514;
    private static final int PACKET_LIMIT = 2014;
    private final InetAddress local_host;
    private final InetAddress syslog_host;
    private final int port;
    private final DatagramSocket socket;
    private final SyslogFacility facility;
    private final DateFormat time_format;
    private final String tag;
    private volatile boolean closed;
}
