// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Int16Data.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Data, NullData, Array, Hash

final class Int16Data extends Data
{

    static final Int16Data newInstance(short value)
    {
        return new Int16Data(value);
    }

    private Int16Data(short value)
    {
        this.value = value;
    }

    public final boolean asBoolean()
    {
        return value != 0;
    }

    public final byte asByte()
    {
        return (byte)value;
    }

    public final short asInt16()
    {
        return value;
    }

    public final int asInt32()
    {
        return value;
    }

    public final long asInt64()
    {
        return (long)value;
    }

    public final float asFloat32()
    {
        return (float)value;
    }

    public final double asFloat64()
    {
        return (double)value;
    }

    public final String asString()
    {
        return String.valueOf(value);
    }

    public final Array asArray()
    {
        return NullData.getInstance().asArray();
    }

    public final Hash asHash()
    {
        return NullData.getInstance().asHash();
    }

    public final boolean isBoolean()
    {
        return false;
    }

    public final boolean isByte()
    {
        return false;
    }

    public final boolean isInt16()
    {
        return true;
    }

    public final boolean isInt32()
    {
        return false;
    }

    public final boolean isInt64()
    {
        return false;
    }

    public final boolean isFloat32()
    {
        return false;
    }

    public final boolean isFloat64()
    {
        return false;
    }

    public final boolean isNumber()
    {
        return true;
    }

    public final boolean isString()
    {
        return false;
    }

    public final boolean isArray()
    {
        return false;
    }

    public final boolean isHash()
    {
        return false;
    }

    public final boolean isNull()
    {
        return false;
    }

    private final short value;
}
