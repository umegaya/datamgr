// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Timer.java

package jp.enterquest.system;

import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

// Referenced classes of package jp.enterquest.system:
//            TimerDelegate, Array

public final class Timer extends TimerTask
{

    public static final Timer newInstance()
    {
        return new Timer();
    }

    private Timer()
    {
        source = null;
        interval = 0L;
        fixed_rate = false;
        running = false;
    }

    public final synchronized void start(long interval, boolean fixed_rate)
    {
        if(source == null)
        {
            source = new java.util.Timer();
            this.interval = interval;
            this.fixed_rate = fixed_rate;
            if(fixed_rate)
                source.scheduleAtFixedRate(this, interval, interval);
            else
                source.schedule(this, interval, interval);
            running = true;
        }
    }

    public final synchronized void start(long first_time, long interval, boolean fixed_rate)
    {
        if(source == null)
        {
            source = new java.util.Timer();
            this.interval = interval;
            this.fixed_rate = fixed_rate;
            if(fixed_rate)
                source.scheduleAtFixedRate(this, new Date(first_time), interval);
            else
                source.schedule(this, new Date(first_time), interval);
            running = true;
        }
    }

    public final synchronized void stop()
    {
        if(source != null)
        {
            source.cancel();
            source.purge();
            source = null;
            running = false;
        }
    }

    public final boolean isRunning()
    {
        return running;
    }

    public final long getExecutionTime()
    {
        if(isRunning())
            return scheduledExecutionTime();
        else
            return 0L;
    }

    public final long getInterval()
    {
        return interval;
    }

    public final boolean isFixedRate()
    {
        return fixed_rate;
    }

    public final Array getDelegates()
    {
        return delegates;
    }

    public final synchronized void run()
    {
        TimerDelegate delegate;
        for(Iterator i$ = delegates.iterator(); i$.hasNext(); delegate.onTick(this))
            delegate = (TimerDelegate)i$.next();

    }

    private volatile java.util.Timer source;
    private volatile long interval;
    private volatile boolean fixed_rate;
    private volatile boolean running;
    private final Array delegates = Array.newInstance();
}
