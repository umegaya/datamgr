// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogLevel.java

package jp.enterquest.system;


public final class LogLevel extends Enum
{

    public static LogLevel[] values()
    {
        return (LogLevel[])$VALUES.clone();
    }

    public static LogLevel valueOf(String name)
    {
        return (LogLevel)Enum.valueOf(jp/enterquest/system/LogLevel, name);
    }

    private LogLevel(String s, int i, String name)
    {
        super(s, i);
        this.name = name;
    }

    public final String getName()
    {
        return name;
    }

    public static final LogLevel DEBUG;
    public static final LogLevel INFO;
    public static final LogLevel WARNING;
    public static final LogLevel ERROR;
    private final String name;
    private static final LogLevel $VALUES[];

    static 
    {
        DEBUG = new LogLevel("DEBUG", 0, "DEBUG");
        INFO = new LogLevel("INFO", 1, "INFO");
        WARNING = new LogLevel("WARNING", 2, "WARNING");
        ERROR = new LogLevel("ERROR", 3, "ERROR");
        $VALUES = (new LogLevel[] {
            DEBUG, INFO, WARNING, ERROR
        });
    }
}
