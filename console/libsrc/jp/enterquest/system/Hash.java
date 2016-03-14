// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Hash.java

package jp.enterquest.system;

import java.util.*;

public final class Hash
    implements Iterable
{

    public static final Hash newInstance()
    {
        return new Hash(new LinkedHashMap(16));
    }

    public static final Hash newInstance(int initial_capacity)
    {
        return new Hash(new LinkedHashMap(0 > initial_capacity ? 0 : initial_capacity));
    }

    public static final Hash newInstance(Map source)
    {
        return new Hash(new LinkedHashMap(source));
    }

    private Hash(Map source)
    {
        this.source = source;
        mutable = true;
    }

    public final boolean has(Object key)
    {
        if(key == null)
            throw new NullPointerException();
        else
            return source.containsKey(key);
    }

    public final void set(Object key, Object value)
    {
        if(key == null || value == null)
            throw new NullPointerException();
        if(!isMutable())
        {
            throw new RuntimeException("hash is immutable.");
        } else
        {
            source.put(key, value);
            return;
        }
    }

    public final Object get(Object key)
    {
        if(key == null)
            throw new NullPointerException();
        Object value = source.get(key);
        if(value == null)
            throw new NullPointerException();
        else
            return value;
    }

    public final void remove(Object key)
    {
        if(key == null)
            throw new NullPointerException();
        if(!isMutable())
        {
            throw new RuntimeException("hash is immutable.");
        } else
        {
            source.remove(key);
            return;
        }
    }

    public final void clear()
    {
        if(!isMutable())
        {
            throw new RuntimeException("hash is immutable.");
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
        return source.keySet().iterator();
    }

    private static final int DEFAULT_CAPACITY = 16;
    private final Map source;
    private volatile boolean mutable;
}
