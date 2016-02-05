// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ArrayData.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Data, NullData, Array, Hash

final class ArrayData extends Data
{

    static final ArrayData newInstance(Array value)
    {
        return new ArrayData(value);
    }

    private ArrayData(Array value)
    {
        this.value = value;
    }

    public final boolean asBoolean()
    {
        return NullData.getInstance().asBoolean();
    }

    public final byte asByte()
    {
        return NullData.getInstance().asByte();
    }

    public final short asInt16()
    {
        return NullData.getInstance().asInt16();
    }

    public final int asInt32()
    {
        return NullData.getInstance().asInt32();
    }

    public final long asInt64()
    {
        return NullData.getInstance().asInt64();
    }

    public final float asFloat32()
    {
        return NullData.getInstance().asFloat32();
    }

    public final double asFloat64()
    {
        return NullData.getInstance().asFloat64();
    }

    public final String asString()
    {
        return NullData.getInstance().asString();
    }

    public final Array asArray()
    {
        return value;
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
        return true;
    }

    public final boolean isHash()
    {
        return false;
    }

    public final boolean isNull()
    {
        return false;
    }

    private final Array value;
}
