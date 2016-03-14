// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HtmlEncoder.java

package jp.enterquest.system;

import org.apache.commons.lang3.StringEscapeUtils;

public final class HtmlEncoder
{

    public static final HtmlEncoder getInstance()
    {
        return instance;
    }

    private HtmlEncoder()
    {
    }

    public final String encode(String string)
    {
        if(string == null)
            throw new NullPointerException();
        else
            return StringEscapeUtils.escapeHtml4(string);
    }

    private static final HtmlEncoder instance = new HtmlEncoder();

}
