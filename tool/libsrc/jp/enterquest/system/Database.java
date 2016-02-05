// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Database.java

package jp.enterquest.system;

import java.sql.SQLException;
import javax.naming.*;
import javax.sql.DataSource;

// Referenced classes of package jp.enterquest.system:
//            SqlConnection

public final class Database
{

    public static final Database newInstance(String resource_name)
    {
        return new Database(resource_name);
    }

    private Database(String resource_name)
    {
        try
        {
            Context context = new InitialContext();
            source = (DataSource)context.lookup((new StringBuilder()).append("java:/comp/env/").append(resource_name).toString());
        }
        catch(NamingException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final SqlConnection getConnection()
    {
        try
        {
            return SqlConnection.newInstance(source.getConnection());
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    private final DataSource source;
}
