// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SyslogFacility.java

package jp.enterquest.system;


public final class SyslogFacility extends Enum
{

    public static SyslogFacility[] values()
    {
        return (SyslogFacility[])$VALUES.clone();
    }

    public static SyslogFacility valueOf(String name)
    {
        return (SyslogFacility)Enum.valueOf(jp/enterquest/system/SyslogFacility, name);
    }

    public static final SyslogFacility find(int code)
    {
        SyslogFacility arr$[] = values();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            SyslogFacility facility = arr$[i$];
            if(facility.getCode() == code)
                return facility;
        }

        throw new RuntimeException(String.format("code=%s : code is not found in syslog facilities.", new Object[] {
            Integer.valueOf(code)
        }));
    }

    public static final SyslogFacility find(String name)
    {
        if(name == null)
            throw new NullPointerException();
        SyslogFacility arr$[] = values();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            SyslogFacility facility = arr$[i$];
            if(facility.getName().equalsIgnoreCase(name))
                return facility;
        }

        throw new RuntimeException(String.format("name=%s : name is not found in syslog facilities.", new Object[] {
            name
        }));
    }

    private SyslogFacility(String s, int i, int code, String name)
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

    public static final SyslogFacility KERNEL;
    public static final SyslogFacility USER;
    public static final SyslogFacility MAIL;
    public static final SyslogFacility DAEMON;
    public static final SyslogFacility AUTH;
    public static final SyslogFacility SYSLOG;
    public static final SyslogFacility LPR;
    public static final SyslogFacility NEWS;
    public static final SyslogFacility UUCP;
    public static final SyslogFacility CRON;
    public static final SyslogFacility AUTHPRIV;
    public static final SyslogFacility FTP;
    public static final SyslogFacility NTP;
    public static final SyslogFacility AUDIT;
    public static final SyslogFacility ALERT;
    public static final SyslogFacility CLOCK;
    public static final SyslogFacility LOCAL0;
    public static final SyslogFacility LOCAL1;
    public static final SyslogFacility LOCAL2;
    public static final SyslogFacility LOCAL3;
    public static final SyslogFacility LOCAL4;
    public static final SyslogFacility LOCAL5;
    public static final SyslogFacility LOCAL6;
    public static final SyslogFacility LOCAL7;
    private final int code;
    private final String name;
    private static final SyslogFacility $VALUES[];

    static 
    {
        KERNEL = new SyslogFacility("KERNEL", 0, 0, "kernel");
        USER = new SyslogFacility("USER", 1, 1, "user");
        MAIL = new SyslogFacility("MAIL", 2, 2, "mail");
        DAEMON = new SyslogFacility("DAEMON", 3, 3, "daemon");
        AUTH = new SyslogFacility("AUTH", 4, 4, "auth");
        SYSLOG = new SyslogFacility("SYSLOG", 5, 5, "syslog");
        LPR = new SyslogFacility("LPR", 6, 6, "lpr");
        NEWS = new SyslogFacility("NEWS", 7, 7, "news");
        UUCP = new SyslogFacility("UUCP", 8, 8, "uucp");
        CRON = new SyslogFacility("CRON", 9, 9, "cron");
        AUTHPRIV = new SyslogFacility("AUTHPRIV", 10, 10, "authpriv");
        FTP = new SyslogFacility("FTP", 11, 11, "ftp");
        NTP = new SyslogFacility("NTP", 12, 12, "ntp");
        AUDIT = new SyslogFacility("AUDIT", 13, 13, "audit");
        ALERT = new SyslogFacility("ALERT", 14, 14, "alert");
        CLOCK = new SyslogFacility("CLOCK", 15, 15, "clock");
        LOCAL0 = new SyslogFacility("LOCAL0", 16, 16, "local0");
        LOCAL1 = new SyslogFacility("LOCAL1", 17, 17, "local1");
        LOCAL2 = new SyslogFacility("LOCAL2", 18, 18, "local2");
        LOCAL3 = new SyslogFacility("LOCAL3", 19, 19, "local3");
        LOCAL4 = new SyslogFacility("LOCAL4", 20, 20, "local4");
        LOCAL5 = new SyslogFacility("LOCAL5", 21, 21, "local5");
        LOCAL6 = new SyslogFacility("LOCAL6", 22, 22, "local6");
        LOCAL7 = new SyslogFacility("LOCAL7", 23, 23, "local7");
        $VALUES = (new SyslogFacility[] {
            KERNEL, USER, MAIL, DAEMON, AUTH, SYSLOG, LPR, NEWS, UUCP, CRON, 
            AUTHPRIV, FTP, NTP, AUDIT, ALERT, CLOCK, LOCAL0, LOCAL1, LOCAL2, LOCAL3, 
            LOCAL4, LOCAL5, LOCAL6, LOCAL7
        });
    }
}
