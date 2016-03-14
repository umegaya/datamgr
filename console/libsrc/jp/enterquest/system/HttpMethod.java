// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpMethod.java

package jp.enterquest.system;


public final class HttpMethod extends Enum
{

    public static HttpMethod[] values()
    {
        return (HttpMethod[])$VALUES.clone();
    }

    public static HttpMethod valueOf(String name)
    {
        return (HttpMethod)Enum.valueOf(jp/enterquest/system/HttpMethod, name);
    }

    private HttpMethod(String s, int i, String name)
    {
        super(s, i);
        this.name = name;
    }

    public final String getName()
    {
        return name;
    }

    public static final HttpMethod GET;
    public static final HttpMethod POST;
    private final String name;
    private static final HttpMethod $VALUES[];

    static 
    {
        GET = new HttpMethod("GET", 0, "GET");
        POST = new HttpMethod("POST", 1, "POST");
        $VALUES = (new HttpMethod[] {
            GET, POST
        });
    }
}
