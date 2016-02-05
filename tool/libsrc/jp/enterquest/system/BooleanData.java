// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BooleanData.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Data, NullData, Array, Hash

final class BooleanData extends Data
{

    static final BooleanData newInstance(boolean value)
    {
        return new BooleanData(value);
    }

    private BooleanData(boolean value)
    {
        this.value = value;
    }

    public final boolean asBoolean()
    {
        return value;
    }

    public final byte asByte()
    {
        return (byte)(value ? 1 : 0);
    }

    public final short asInt16()
    {
        return (short)(value ? 1 : 0);
    }

    public final int asInt32()
    {
        return value ? 1 : 0;
    }

    public final long asInt64()
    {
        return value ? 1L : 0L;
    }

    public final float asFloat32()
    {
        return value ? 1.0F : 0.0F;
    }

    public final double asFloat64()
    {
        return value ? 1.0D : 0.0D;
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
        return true;
    }

    public final boolean isByte()
    {
        return false;
    }

    public final boolean isInt16()
    {
        return false;
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
        return false;
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

    private final boolean value;
}
