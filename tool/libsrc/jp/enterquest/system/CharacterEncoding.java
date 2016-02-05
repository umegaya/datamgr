// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CharacterEncoding.java

package jp.enterquest.system;


public final class CharacterEncoding extends Enum
{

    public static CharacterEncoding[] values()
    {
        return (CharacterEncoding[])$VALUES.clone();
    }

    public static CharacterEncoding valueOf(String name)
    {
        return (CharacterEncoding)Enum.valueOf(jp/enterquest/system/CharacterEncoding, name);
    }

    private CharacterEncoding(String s, int i, String name)
    {
        super(s, i);
        this.name = name;
    }

    public final String getName()
    {
        return name;
    }

    public static final CharacterEncoding ISO_8859_1;
    public static final CharacterEncoding UTF_8;
    private final String name;
    private static final CharacterEncoding $VALUES[];

    static 
    {
        ISO_8859_1 = new CharacterEncoding("ISO_8859_1", 0, "ISO-8859-1");
        UTF_8 = new CharacterEncoding("UTF_8", 1, "UTF-8");
        $VALUES = (new CharacterEncoding[] {
            ISO_8859_1, UTF_8
        });
    }
}
