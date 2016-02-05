// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpClientDelegate.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            HttpClientRequest, HttpClientResponse

public interface HttpClientDelegate
{

    public abstract void onRequest(HttpClientRequest httpclientrequest);

    public abstract void onResponse(HttpClientResponse httpclientresponse);
}
