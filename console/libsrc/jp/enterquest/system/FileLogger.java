// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileLogger.java

package jp.enterquest.system;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Referenced classes of package jp.enterquest.system:
//            Logger, LogLevel, LineSeparator

public final class FileLogger extends Logger
{

    public static final Logger newInstance(String filename, int history)
    {
        return new FileLogger(filename, history);
    }

    private FileLogger(String filename, int history)
    {
        try
        {
            if(filename == null)
                throw new NullPointerException();
            date_format = new SimpleDateFormat("yyyy-MM-dd");
            time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            current_date = getDate(new Date(), 0);
            next_date = getDate(current_date, 1);
            file = new File(filename);
            if(file.exists())
            {
                Date last_date = getDate(new Date(file.lastModified()), 0);
                if(last_date.getTime() < current_date.getTime())
                {
                    String rename = String.format("%s.%s", new Object[] {
                        file.getAbsolutePath(), date_format.format(last_date)
                    });
                    file.setLastModified(getDate(last_date, 1).getTime() - 1L);
                    file.renameTo(new File(rename));
                }
            }
            this.history = 0 > history ? 0 : history;
            stream = new FileWriter(file, true);
            separator = LineSeparator.LF;
            closed = false;
            deleteExpiredFiles();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }

    public final synchronized void close()
    {
        try
        {
            stream.close();
            stream = null;
            closed = true;
        }
        catch(IOException ignored)
        {
            ignored.printStackTrace();
        }
    }

    public final boolean isClosed()
    {
        return closed;
    }

    protected final synchronized void log(LogLevel level, String message)
    {
        try
        {
            Date date = new Date();
            if(next_date.getTime() <= date.getTime())
            {
                stream.close();
                stream = null;
                String rename = String.format("%s.%s", new Object[] {
                    file.getAbsolutePath(), date_format.format(current_date)
                });
                file.setLastModified(next_date.getTime() - 1L);
                file.renameTo(new File(rename));
                current_date = next_date;
                next_date = getDate(next_date, 1);
                deleteExpiredFiles();
                stream = new FileWriter(file);
            }
            StackTraceElement stack_trace = Thread.currentThread().getStackTrace()[3];
            String class_name = stack_trace.getClassName();
            stream.write(String.format("[%s][%s][%s.%s] %s%s", new Object[] {
                time_format.format(date), level.getName(), class_name.substring(class_name.lastIndexOf(".") + 1), stack_trace.getMethodName(), message, separator.getCode()
            }));
            stream.flush();
        }
        catch(IOException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private final Date getDate(Date time, int offset)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(10, 0);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if(offset != 0)
            calendar.add(5, offset);
        return calendar.getTime();
    }

    private final void deleteExpiredFiles()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current_date);
        calendar.add(5, -history);
        long expired_time = calendar.getTimeInMillis();
        File arr$[] = this.file.getParentFile().listFiles();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            File file = arr$[i$];
            if(file.isFile() && file.getName().startsWith(this.file.getName()) && file.lastModified() < expired_time)
                file.delete();
        }

    }

    private final DateFormat date_format;
    private final DateFormat time_format;
    private volatile Date current_date;
    private volatile Date next_date;
    private final File file;
    private final int history;
    private volatile FileWriter stream;
    private final LineSeparator separator;
    private volatile boolean closed;
}
