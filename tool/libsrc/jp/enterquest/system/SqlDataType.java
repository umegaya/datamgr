// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlDataType.java

package jp.enterquest.system;


public final class SqlDataType extends Enum
{

    public static SqlDataType[] values()
    {
        return (SqlDataType[])$VALUES.clone();
    }

    public static SqlDataType valueOf(String name)
    {
        return (SqlDataType)Enum.valueOf(jp/enterquest/system/SqlDataType, name);
    }

    private SqlDataType(String s, int i, String name)
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

    public static final SqlDataType TINYINT;
    public static final SqlDataType SMALLINT;
    public static final SqlDataType MEDIUMINT;
    public static final SqlDataType INT;
    public static final SqlDataType BIGINT;
    public static final SqlDataType FLOAT;
    public static final SqlDataType DOUBLE;
    public static final SqlDataType DATE;
    public static final SqlDataType TIME;
    public static final SqlDataType DATETIME;
    public static final SqlDataType TIMESTAMP;
    public static final SqlDataType CHAR;
    public static final SqlDataType VARCHAR;
    public static final SqlDataType TEXT;
    private final String name;
    private static final SqlDataType $VALUES[];

    static 
    {
        TINYINT = new SqlDataType("TINYINT", 0, "tinyint");
        SMALLINT = new SqlDataType("SMALLINT", 1, "smallint");
        MEDIUMINT = new SqlDataType("MEDIUMINT", 2, "mediumint");
        INT = new SqlDataType("INT", 3, "int");
        BIGINT = new SqlDataType("BIGINT", 4, "bigint");
        FLOAT = new SqlDataType("FLOAT", 5, "float");
        DOUBLE = new SqlDataType("DOUBLE", 6, "double");
        DATE = new SqlDataType("DATE", 7, "date");
        TIME = new SqlDataType("TIME", 8, "time");
        DATETIME = new SqlDataType("DATETIME", 9, "datetime");
        TIMESTAMP = new SqlDataType("TIMESTAMP", 10, "timestamp");
        CHAR = new SqlDataType("CHAR", 11, "char");
        VARCHAR = new SqlDataType("VARCHAR", 12, "varchar");
        TEXT = new SqlDataType("TEXT", 13, "text");
        $VALUES = (new SqlDataType[] {
            TINYINT, SMALLINT, MEDIUMINT, INT, BIGINT, FLOAT, DOUBLE, DATE, TIME, DATETIME, 
            TIMESTAMP, CHAR, VARCHAR, TEXT
        });
    }
}
