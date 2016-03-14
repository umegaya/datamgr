// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Random.java

package jp.enterquest.system;


public final class Random
{

    public static final Random newInstance()
    {
        return new Random(System.currentTimeMillis());
    }

    public static final Random newInstance(long seed)
    {
        return new Random(seed);
    }

    private Random(long seed)
    {
        this.index = 0;
        values[0] = seed;
        for(int index = 1; index < values.length; index++)
            values[index] = 0x4c957f2dL * (values[index - 1] ^ values[index - 1] >>> 62) + (long)index;

    }

    public final boolean nextBoolean()
    {
        return (generate() & 1L) == 1L;
    }

    public final byte nextByte()
    {
        return (byte)(int)(generate() >>> 57);
    }

    public final short nextInt16()
    {
        return (short)(int)(generate() >>> 49);
    }

    public final int nextInt32()
    {
        return (int)(generate() >>> 33);
    }

    public final long nextInt64()
    {
        return generate() >>> 1;
    }

    public final float nextFloat32()
    {
        return (float)(generate() >>> 40) * 5.960464E-08F;
    }

    public final double nextFloat64()
    {
        return (double)(generate() >>> 11) * 1.1102230246251565E-16D;
    }

    private final long generate()
    {
        long value = 0L;
        if(values.length <= this.index)
        {
            int MARKER = 156;
            long UPPER_MASK = 0x80000000L;
            long LOWER_MASK = 0x7fffffffL;
            long MATRIX[] = {
                0L, 0xa96619e9L
            };
            int index;
            for(index = 0; index < values.length - 156; index++)
            {
                value = values[index] & 0x80000000L | values[index + 1] & 0x7fffffffL;
                values[index] = values[index + 156] ^ value >>> 1 ^ MATRIX[(int)(value & 1L)];
            }

            for(; index < values.length - 1; index++)
            {
                value = values[index] & 0x80000000L | values[index + 1] & 0x7fffffffL;
                values[index] = values[index + (156 - values.length)] ^ value >>> 1 ^ MATRIX[(int)(value & 1L)];
            }

            value = values[values.length - 1] & 0x80000000L | values[0] & 0x7fffffffL;
            values[values.length - 1] = values[155] ^ value >>> 1 ^ MATRIX[(int)(value & 1L)];
            this.index = 0;
        }
        value = values[this.index++];
        value ^= value >>> 29 & 0x55555555L;
        value ^= value << 17 & 0xeda60000L;
        value ^= value << 37 & 0x0L;
        value ^= value >>> 43;
        return value;
    }

    private static final int ARRAY_SIZE = 312;
    private final long values[] = new long[312];
    private volatile int index;
}
