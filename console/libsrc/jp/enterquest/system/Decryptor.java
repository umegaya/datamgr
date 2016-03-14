// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Decryptor.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Random

public final class Decryptor
{

    public static final Decryptor getInstance()
    {
        return instance;
    }

    private Decryptor()
    {
    }

    public final void decrypt(byte bytes[])
    {
        reverse(bytes);
        operate(bytes);
    }

    private final void operate(byte bytes[])
    {
        int count = bytes.length;
        Random random = Random.newInstance(count);
        for(int index = 0; index < count; index++)
            bytes[index] = (byte)(~(bytes[index] ^ random.nextInt32()));

    }

    private final void reverse(byte bytes[])
    {
        int count = bytes.length / 2;
        int last_index = bytes.length - 1;
        for(int index = 0; index < count; index++)
        {
            byte value = bytes[index];
            bytes[index] = bytes[last_index - index];
            bytes[last_index - index] = value;
        }

    }

    private static final Decryptor instance = new Decryptor();

}
