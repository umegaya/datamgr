// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimerDelegate.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            Timer

public interface TimerDelegate
{

    public abstract Timer getTimer();

    public abstract void onTick(Timer timer);
}
