// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UrlEncoder.java

package jp.enterquest.system;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// Referenced classes of package jp.enterquest.system:
//            CharacterEncoding

public final class UrlEncoder
{

    public static final UrlEncoder getInstance()
    {
        return instance;
    }

    private UrlEncoder()
    {
    }

    public final String encode(String string, CharacterEncoding encoding)
    {
        try
        {
            if(string == null || encoding == null)
                throw new NullPointerException();
            else
                return URLEncoder.encode(string, encoding.getName());
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private static final UrlEncoder instance = new UrlEncoder();

}
