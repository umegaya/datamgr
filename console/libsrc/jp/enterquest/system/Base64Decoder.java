// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Base64Decoder.java

package jp.enterquest.system;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

// Referenced classes of package jp.enterquest.system:
//            CharacterEncoding

public final class Base64Decoder
{

    public static final Base64Decoder getInstance()
    {
        return instance;
    }

    private Base64Decoder()
    {
    }

    public final String decode(String string, CharacterEncoding encoding)
    {
        try
        {
            if(string == null || encoding == null)
                throw new NullPointerException();
            else
                return new String(Base64.decodeBase64(string.getBytes()), encoding.getName());
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private static final Base64Decoder instance = new Base64Decoder();

}
