// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LineSeparator.java

package jp.enterquest.system;


public final class LineSeparator extends Enum
{

    public static LineSeparator[] values()
    {
        return (LineSeparator[])$VALUES.clone();
    }

    public static LineSeparator valueOf(String name)
    {
        return (LineSeparator)Enum.valueOf(jp/enterquest/system/LineSeparator, name);
    }

    private LineSeparator(String s, int i, String code)
    {
        super(s, i);
        this.code = code;
    }

    public final String getCode()
    {
        return code;
    }

    public static final LineSeparator CR;
    public static final LineSeparator CRLF;
    public static final LineSeparator LF;
    private final String code;
    private static final LineSeparator $VALUES[];

    static 
    {
        CR = new LineSeparator("CR", 0, "\r");
        CRLF = new LineSeparator("CRLF", 1, "\r\n");
        LF = new LineSeparator("LF", 2, "\n");
        $VALUES = (new LineSeparator[] {
            CR, CRLF, LF
        });
    }
}
