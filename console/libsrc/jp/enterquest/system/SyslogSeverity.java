// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SyslogSeverity.java

package jp.enterquest.system;


public final class SyslogSeverity extends Enum
{

    public static SyslogSeverity[] values()
    {
        return (SyslogSeverity[])$VALUES.clone();
    }

    public static SyslogSeverity valueOf(String name)
    {
        return (SyslogSeverity)Enum.valueOf(jp/enterquest/system/SyslogSeverity, name);
    }

    private SyslogSeverity(String s, int i, int code, String name)
    {
        super(s, i);
        this.code = code;
        this.name = name;
    }

    public final int getCode()
    {
        return code;
    }

    public final String getName()
    {
        return name;
    }

    public static final SyslogSeverity EMERGENCY;
    public static final SyslogSeverity ALERT;
    public static final SyslogSeverity CRITICAL;
    public static final SyslogSeverity ERROR;
    public static final SyslogSeverity WARNING;
    public static final SyslogSeverity NOTICE;
    public static final SyslogSeverity INFO;
    public static final SyslogSeverity DEBUG;
    private final int code;
    private final String name;
    private static final SyslogSeverity $VALUES[];

    static 
    {
        EMERGENCY = new SyslogSeverity("EMERGENCY", 0, 0, "emergency");
        ALERT = new SyslogSeverity("ALERT", 1, 1, "alert");
        CRITICAL = new SyslogSeverity("CRITICAL", 2, 2, "critical");
        ERROR = new SyslogSeverity("ERROR", 3, 3, "error");
        WARNING = new SyslogSeverity("WARNING", 4, 4, "warning");
        NOTICE = new SyslogSeverity("NOTICE", 5, 5, "notice");
        INFO = new SyslogSeverity("INFO", 6, 6, "info");
        DEBUG = new SyslogSeverity("DEBUG", 7, 7, "debug");
        $VALUES = (new SyslogSeverity[] {
            EMERGENCY, ALERT, CRITICAL, ERROR, WARNING, NOTICE, INFO, DEBUG
        });
    }
}
