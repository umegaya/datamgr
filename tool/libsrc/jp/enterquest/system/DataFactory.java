// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataFactory.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            BooleanData, ByteData, Int16Data, Int32Data, 
//            Int64Data, Float32Data, Float64Data, StringData, 
//            Array, ArrayData, Hash, HashData, 
//            NullData, Data

public final class DataFactory
{

    public static final DataFactory getInstance()
    {
        return instance;
    }

    private DataFactory()
    {
    }

    public final Data newBoolean(boolean value)
    {
        return BooleanData.newInstance(value);
    }

    public final Data newByte(byte value)
    {
        return ByteData.newInstance(value);
    }

    public final Data newInt16(short value)
    {
        return Int16Data.newInstance(value);
    }

    public final Data newInt32(int value)
    {
        return Int32Data.newInstance(value);
    }

    public final Data newInt64(long value)
    {
        return Int64Data.newInstance(value);
    }

    public final Data newFloat32(float value)
    {
        return Float32Data.newInstance(value);
    }

    public final Data newFloat64(double value)
    {
        return Float64Data.newInstance(value);
    }

    public final Data newString(String value)
    {
        if(value == null)
            throw new NullPointerException();
        else
            return StringData.newInstance(value);
    }

    public final Data newArray()
    {
        return ArrayData.newInstance(Array.newInstance());
    }

    public final Data newArray(int initial_capacity)
    {
        return ArrayData.newInstance(Array.newInstance(initial_capacity));
    }

    public final Data newArray(Array value)
    {
        if(value == null)
            throw new NullPointerException();
        else
            return ArrayData.newInstance(value);
    }

    public final Data newHash()
    {
        return HashData.newInstance(Hash.newInstance());
    }

    public final Data newHash(int initial_capacity)
    {
        return HashData.newInstance(Hash.newInstance(initial_capacity));
    }

    public final Data newHash(Hash value)
    {
        if(value == null)
            throw new NullPointerException();
        else
            return HashData.newInstance(value);
    }

    public final Data getNull()
    {
        return NullData.getInstance();
    }

    private static final DataFactory instance = new DataFactory();

}
