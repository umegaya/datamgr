// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HtmlDecoder.java

package jp.enterquest.system;

import org.apache.commons.lang3.StringEscapeUtils;

public final class HtmlDecoder
{

    public static final HtmlDecoder getInstance()
    {
        return instance;
    }

    private HtmlDecoder()
    {
    }

    public final String decode(String string)
    {
        if(string == null)
            throw new NullPointerException();
        else
            return StringEscapeUtils.unescapeHtml4(string);
    }

    private static final HtmlDecoder instance = new HtmlDecoder();

}
