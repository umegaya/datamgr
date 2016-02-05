// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Md5Encoder.java

package jp.enterquest.system;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Referenced classes of package jp.enterquest.system:
//            CharacterEncoding

public final class Md5Encoder
{

    public static final Md5Encoder getInstance()
    {
        return instance;
    }

    private Md5Encoder()
    {
    }

    public final String encode(String string, CharacterEncoding encoding)
    {
        try
        {
            if(string == null || encoding == null)
                throw new NullPointerException();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(string.getBytes(encoding.getName()));
            byte bytes[] = md5.digest();
            StringBuilder buffer = new StringBuilder(bytes.length * 2);
            for(int index = 0; index < bytes.length; index++)
            {
                String hex = Integer.toHexString(bytes[index] & 0xff);
                if(hex.length() == 1)
                    buffer.append("0");
                buffer.append(hex);
            }

            return buffer.toString();
        }
        catch(NoSuchAlgorithmException cause)
        {
            throw new RuntimeException(cause);
        }
        catch(UnsupportedEncodingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private static final Md5Encoder instance = new Md5Encoder();

}
