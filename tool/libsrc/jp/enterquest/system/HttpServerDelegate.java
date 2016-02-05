// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpServerDelegate.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            HttpServer, HttpServerRequest, HttpServerResponse

public interface HttpServerDelegate
{

    public abstract HttpServer getServer();

    public abstract void onInitialize();

    public abstract void onFinalize();

    public abstract void onGet(HttpServerRequest httpserverrequest, HttpServerResponse httpserverresponse);

    public abstract void onPost(HttpServerRequest httpserverrequest, HttpServerResponse httpserverresponse);
}
