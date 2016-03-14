// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpStatus.java

package jp.enterquest.system;


public final class HttpStatus extends Enum
{

    public static HttpStatus[] values()
    {
        return (HttpStatus[])$VALUES.clone();
    }

    public static HttpStatus valueOf(String name)
    {
        return (HttpStatus)Enum.valueOf(jp/enterquest/system/HttpStatus, name);
    }

    private HttpStatus(String s, int i, int code, String name)
    {
        super(s, i);
        this.code = code;
        this.name = name;
    }

    public final int getCode()
    {
        return code;
    }

    public final String getName()
    {
        return name;
    }

    public static final HttpStatus _100;
    public static final HttpStatus _101;
    public static final HttpStatus _200;
    public static final HttpStatus _201;
    public static final HttpStatus _202;
    public static final HttpStatus _203;
    public static final HttpStatus _204;
    public static final HttpStatus _205;
    public static final HttpStatus _206;
    public static final HttpStatus _300;
    public static final HttpStatus _301;
    public static final HttpStatus _302;
    public static final HttpStatus _303;
    public static final HttpStatus _304;
    public static final HttpStatus _305;
    public static final HttpStatus _307;
    public static final HttpStatus _400;
    public static final HttpStatus _401;
    public static final HttpStatus _402;
    public static final HttpStatus _403;
    public static final HttpStatus _404;
    public static final HttpStatus _405;
    public static final HttpStatus _406;
    public static final HttpStatus _407;
    public static final HttpStatus _408;
    public static final HttpStatus _409;
    public static final HttpStatus _410;
    public static final HttpStatus _411;
    public static final HttpStatus _412;
    public static final HttpStatus _413;
    public static final HttpStatus _414;
    public static final HttpStatus _415;
    public static final HttpStatus _416;
    public static final HttpStatus _417;
    public static final HttpStatus _500;
    public static final HttpStatus _501;
    public static final HttpStatus _502;
    public static final HttpStatus _503;
    public static final HttpStatus _504;
    public static final HttpStatus _505;
    private final int code;
    private final String name;
    private static final HttpStatus $VALUES[];

    static 
    {
        _100 = new HttpStatus("_100", 0, 100, "Continue");
        _101 = new HttpStatus("_101", 1, 101, "Switching Protocols");
        _200 = new HttpStatus("_200", 2, 200, "OK");
        _201 = new HttpStatus("_201", 3, 201, "Created");
        _202 = new HttpStatus("_202", 4, 202, "Accepted");
        _203 = new HttpStatus("_203", 5, 203, "Non-Authoritative Information");
        _204 = new HttpStatus("_204", 6, 204, "No Content");
        _205 = new HttpStatus("_205", 7, 205, "Reset Content");
        _206 = new HttpStatus("_206", 8, 206, "Partial Content");
        _300 = new HttpStatus("_300", 9, 300, "Multiple Choices");
        _301 = new HttpStatus("_301", 10, 301, "Moved Permanently");
        _302 = new HttpStatus("_302", 11, 302, "Found");
        _303 = new HttpStatus("_303", 12, 303, "See Other");
        _304 = new HttpStatus("_304", 13, 304, "Not Modified");
        _305 = new HttpStatus("_305", 14, 305, "Use Proxy");
        _307 = new HttpStatus("_307", 15, 307, "Temporary Redirect");
        _400 = new HttpStatus("_400", 16, 400, "Bad Request");
        _401 = new HttpStatus("_401", 17, 401, "Unauthorized");
        _402 = new HttpStatus("_402", 18, 402, "Payment Required");
        _403 = new HttpStatus("_403", 19, 403, "Forbidden");
        _404 = new HttpStatus("_404", 20, 404, "Not Found");
        _405 = new HttpStatus("_405", 21, 405, "Method Not Found");
        _406 = new HttpStatus("_406", 22, 406, "Not Acceptable");
        _407 = new HttpStatus("_407", 23, 407, "Proxy Authentication Required");
        _408 = new HttpStatus("_408", 24, 408, "Request Timeout");
        _409 = new HttpStatus("_409", 25, 409, "Conflict");
        _410 = new HttpStatus("_410", 26, 410, "Gone");
        _411 = new HttpStatus("_411", 27, 411, "Length Required");
        _412 = new HttpStatus("_412", 28, 412, "Precondition Failed");
        _413 = new HttpStatus("_413", 29, 413, "Request Entity Too Large");
        _414 = new HttpStatus("_414", 30, 414, "Request-URI Too Long");
        _415 = new HttpStatus("_415", 31, 415, "Unsupported Media Type");
        _416 = new HttpStatus("_416", 32, 416, "Requested Range Not Satisfiable");
        _417 = new HttpStatus("_417", 33, 417, "Expectation Failed");
        _500 = new HttpStatus("_500", 34, 500, "Internal Server Error");
        _501 = new HttpStatus("_501", 35, 501, "Not Implemented");
        _502 = new HttpStatus("_502", 36, 502, "Bad Gateway");
        _503 = new HttpStatus("_503", 37, 503, "Service Unavailable");
        _504 = new HttpStatus("_504", 38, 504, "Gateway Timeout");
        _505 = new HttpStatus("_505", 39, 505, "HTTP Version Not Supported");
        $VALUES = (new HttpStatus[] {
            _100, _101, _200, _201, _202, _203, _204, _205, _206, _300, 
            _301, _302, _303, _304, _305, _307, _400, _401, _402, _403, 
            _404, _405, _406, _407, _408, _409, _410, _411, _412, _413, 
            _414, _415, _416, _417, _500, _501, _502, _503, _504, _505
        });
    }
}
