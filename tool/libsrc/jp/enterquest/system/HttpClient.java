// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpClient.java

package jp.enterquest.system;


// Referenced classes of package jp.enterquest.system:
//            HttpClientRequest, HttpClientDelegate

public final class HttpClient
{

    public static final HttpClient newInstance()
    {
        return new HttpClient();
    }

    private HttpClient()
    {
        _flddelegate = null;
    }

    public final void connect()
    {
        if(_flddelegate != null)
        {
            HttpClientRequest request = HttpClientRequest.newInstance();
            request.process(_flddelegate);
        }
    }

    public final void setDelegate(HttpClientDelegate delegate)
    {
        if(delegate == null)
        {
            throw new NullPointerException();
        } else
        {
            _flddelegate = delegate;
            return;
        }
    }

    private volatile HttpClientDelegate _flddelegate;
}
