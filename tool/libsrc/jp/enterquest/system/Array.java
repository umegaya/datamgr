// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Array.java

package jp.enterquest.system;

import java.util.*;

public final class Array
    implements Iterable
{

    public static final Array newInstance()
    {
        return new Array(new ArrayList(16));
    }

    public static final Array newInstance(int initial_capacity)
    {
        return new Array(new ArrayList(0 > initial_capacity ? 0 : initial_capacity));
    }

    public static final Array newInstance(Collection source)
    {
        return new Array(new ArrayList(source));
    }

    private Array(List source)
    {
        this.source = source;
        mutable = true;
    }

    public final boolean has(Object value)
    {
        if(value == null)
            throw new NullPointerException();
        else
            return source.contains(value);
    }

    public final void set(int index, Object value)
    {
        if(value == null)
            throw new NullPointerException();
        if(!isMutable())
        {
            throw new RuntimeException("array is immutable.");
        } else
        {
            source.set(index, value);
            return;
        }
    }

    public final Object get(int index)
    {
        return source.get(index);
    }

    public final void add(Object value)
    {
        if(value == null)
            throw new NullPointerException();
        if(!isMutable())
        {
            throw new RuntimeException("array is immutable.");
        } else
        {
            source.add(value);
            return;
        }
    }

    public final void remove(int index)
    {
        if(!isMutable())
        {
            throw new RuntimeException("array is immutable.");
        } else
        {
            source.remove(index);
            return;
        }
    }

    public final void clear()
    {
        if(!isMutable())
        {
            throw new RuntimeException("array is immutable.");
        } else
        {
            source.clear();
            return;
        }
    }

    public final int count()
    {
        return source.size();
    }

    public final boolean isEmpty()
    {
        return count() == 0;
    }

    public final void immutable()
    {
        mutable = true;
    }

    public final boolean isMutable()
    {
        return mutable;
    }

    public final Iterator iterator()
    {
        return source.iterator();
    }

    private static final int DEFAULT_CAPACITY = 16;
    private final List source;
    private volatile boolean mutable;
}
