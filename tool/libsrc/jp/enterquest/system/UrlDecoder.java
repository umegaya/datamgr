// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UrlDecoder.java

package jp.enterquest.system;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

// Referenced classes of package jp.enterquest.system:
//            CharacterEncoding

public final class UrlDecoder
{

    public static final UrlDecoder getInstance()
    {
        return instance;
    }

    private UrlDecoder()
    {
    }

    public final String decode(String string, CharacterEncoding encoding)
    {
        try
        {
            if(string == null || encoding == null)
                throw new NullPointerException();
            else
                return URLDecoder.decode(string, encoding.getName());
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private static final UrlDecoder instance = new UrlDecoder();

}
