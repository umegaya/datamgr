// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NullData.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Data, Array, Hash

final class NullData extends Data
{

    static final NullData getInstance()
    {
        return instance;
    }

    private NullData()
    {
        array.immutable();
        hash.immutable();
    }

    public final boolean asBoolean()
    {
        return false;
    }

    public final byte asByte()
    {
        return 0;
    }

    public final short asInt16()
    {
        return 0;
    }

    public final int asInt32()
    {
        return 0;
    }

    public final long asInt64()
    {
        return 0L;
    }

    public final float asFloat32()
    {
        return 0.0F;
    }

    public final double asFloat64()
    {
        return 0.0D;
    }

    public final String asString()
    {
        return "";
    }

    public final Array asArray()
    {
        return array;
    }

    public final Hash asHash()
    {
        return hash;
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
        return false;
    }

    public final boolean isHash()
    {
        return false;
    }

    public final boolean isNull()
    {
        return true;
    }

    private static final NullData instance = new NullData();
    private final Array array = Array.newInstance(0);
    private final Hash hash = Hash.newInstance(0);

}
