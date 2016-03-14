// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringData.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Data, NullData, Array, Hash

final class StringData extends Data
{

    static final StringData newInstance(String value)
    {
        return new StringData(value);
    }

    private StringData(String value)
    {
        this.value = value;
    }

    public final boolean asBoolean()
    {
        return Boolean.parseBoolean(value);
    }

    public final byte asByte()
    {
        try
        {
            return Byte.parseByte(value);
        }
        catch(NumberFormatException ignored)
        {
            return 0;
        }
    }

    public final short asInt16()
    {
        try
        {
            return Short.parseShort(value);
        }
        catch(NumberFormatException ignored)
        {
            return 0;
        }
    }

    public final int asInt32()
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch(NumberFormatException ignored)
        {
            return 0;
        }
    }

    public final long asInt64()
    {
        try
        {
            return Long.parseLong(value);
        }
        catch(NumberFormatException ignored)
        {
            return 0L;
        }
    }

    public final float asFloat32()
    {
        try
        {
            return Float.parseFloat(value);
        }
        catch(NumberFormatException ignored)
        {
            return 0.0F;
        }
    }

    public final double asFloat64()
    {
        try
        {
            return Double.parseDouble(value);
        }
        catch(NumberFormatException ignored)
        {
            return 0.0D;
        }
    }

    public final String asString()
    {
        return value;
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
        return true;
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

    private final String value;
}
