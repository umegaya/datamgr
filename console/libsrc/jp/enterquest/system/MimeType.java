// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MimeType.java

package jp.enterquest.system;


public final class MimeType extends Enum
{

    public static MimeType[] values()
    {
        return (MimeType[])$VALUES.clone();
    }

    public static MimeType valueOf(String name)
    {
        return (MimeType)Enum.valueOf(jp/enterquest/system/MimeType, name);
    }

    private MimeType(String s, int i, String name)
    {
        super(s, i);
        this.name = name;
    }

    public final String getName()
    {
        return name;
    }

    public static final MimeType APPLICATION_JSON;
    public static final MimeType APPLICATION_OCTETSTREAM;
    public static final MimeType APPLICATION_XWWWFORMURLENCODED;
    public static final MimeType MULTIPART_FORMDATA;
    public static final MimeType TEXT_HTML;
    public static final MimeType TEXT_PLAIN;
    private final String name;
    private static final MimeType $VALUES[];

    static 
    {
        APPLICATION_JSON = new MimeType("APPLICATION_JSON", 0, "application/json");
        APPLICATION_OCTETSTREAM = new MimeType("APPLICATION_OCTETSTREAM", 1, "application/octet-stream");
        APPLICATION_XWWWFORMURLENCODED = new MimeType("APPLICATION_XWWWFORMURLENCODED", 2, "application/x-www-form-urlencoded");
        MULTIPART_FORMDATA = new MimeType("MULTIPART_FORMDATA", 3, "multipart/form-data");
        TEXT_HTML = new MimeType("TEXT_HTML", 4, "text/html");
        TEXT_PLAIN = new MimeType("TEXT_PLAIN", 5, "text/plain");
        $VALUES = (new MimeType[] {
            APPLICATION_JSON, APPLICATION_OCTETSTREAM, APPLICATION_XWWWFORMURLENCODED, MULTIPART_FORMDATA, TEXT_HTML, TEXT_PLAIN
        });
    }
}
