// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogType.java

package jp.enterquest.system;


public final class LogType extends Enum
{

    public static LogType[] values()
    {
        return (LogType[])$VALUES.clone();
    }

    public static LogType valueOf(String name)
    {
        return (LogType)Enum.valueOf(jp/enterquest/system/LogType, name);
    }

    private LogType(String s, int i, String name)
    {
        super(s, i);
        this.name = name;
    }

    public final String getName()
    {
        return name;
    }

    public static final LogType CONSOLE;
    public static final LogType FILE;
    public static final LogType SYSLOG;
    private final String name;
    private static final LogType $VALUES[];

    static 
    {
        CONSOLE = new LogType("CONSOLE", 0, "console");
        FILE = new LogType("FILE", 1, "file");
        SYSLOG = new LogType("SYSLOG", 2, "syslog");
        $VALUES = (new LogType[] {
            CONSOLE, FILE, SYSLOG
        });
    }
}
