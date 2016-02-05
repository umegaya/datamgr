// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Base64Encoder.java

package jp.enterquest.system;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

// Referenced classes of package jp.enterquest.system:
//            CharacterEncoding

public final class Base64Encoder
{

    public static final Base64Encoder getInstance()
    {
        return instance;
    }

    private Base64Encoder()
    {
    }

    public final String encode(String string, CharacterEncoding encoding)
    {
        try
        {
            if(string == null || encoding == null)
                throw new NullPointerException();
            else
                return Base64.encodeBase64String(string.getBytes(encoding.getName()));
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private static final Base64Encoder instance = new Base64Encoder();

}
