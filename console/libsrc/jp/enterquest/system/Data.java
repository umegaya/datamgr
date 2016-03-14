// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Data.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Array, Hash

public abstract class Data
{

    Data()
    {
    }

    public abstract boolean asBoolean();

    public abstract byte asByte();

    public abstract short asInt16();

    public abstract int asInt32();

    public abstract long asInt64();

    public abstract float asFloat32();

    public abstract double asFloat64();

    public abstract String asString();

    public abstract Array asArray();

    public abstract Hash asHash();

    public abstract boolean isBoolean();

    public abstract boolean isByte();

    public abstract boolean isInt16();

    public abstract boolean isInt32();

    public abstract boolean isInt64();

    public abstract boolean isFloat32();

    public abstract boolean isFloat64();

    public abstract boolean isNumber();

    public abstract boolean isString();

    public abstract boolean isArray();

    public abstract boolean isHash();

    public abstract boolean isNull();
}
