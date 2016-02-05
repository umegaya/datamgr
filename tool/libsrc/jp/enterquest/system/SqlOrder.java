// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlOrder.java

package jp.enterquest.system;


public final class SqlOrder extends Enum
{

    public static SqlOrder[] values()
    {
        return (SqlOrder[])$VALUES.clone();
    }

    public static SqlOrder valueOf(String name)
    {
        return (SqlOrder)Enum.valueOf(jp/enterquest/system/SqlOrder, name);
    }

    private SqlOrder(String s, int i, String name)
    {
        super(s, i);
        this.name = name;
    }

    public final String getName()
    {
        return name;
    }

    public final boolean equals(String name)
    {
        if(name == null)
            throw new NullPointerException();
        else
            return this.name.equalsIgnoreCase(name);
    }

    public static final SqlOrder ASC;
    public static final SqlOrder DESC;
    private final String name;
    private static final SqlOrder $VALUES[];

    static 
    {
        ASC = new SqlOrder("ASC", 0, "ASC");
        DESC = new SqlOrder("DESC", 1, "DESC");
        $VALUES = (new SqlOrder[] {
            ASC, DESC
        });
    }
}
