// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlConnection.java

package jp.enterquest.system;

import java.sql.Connection;
import java.sql.SQLException;

// Referenced classes of package jp.enterquest.system:
//            SqlStatement

public final class SqlConnection
{

    static final SqlConnection newInstance(Connection source)
    {
        return new SqlConnection(source);
    }

    private SqlConnection(Connection source)
    {
        this.source = source;
    }

    public final SqlStatement newStatement(String sql)
    {
        try
        {
            if(sql == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("connection is closed.");
            else
                return SqlStatement.newInstance(source.prepareStatement(sql));
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void isAutoCommit(boolean auto_commit)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("connection is closed.");
            source.setAutoCommit(auto_commit);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final boolean isAutoCommit()
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("connection is closed.");
            else
                return source.getAutoCommit();
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void commit()
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("connection is closed.");
            source.commit();
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void rollback()
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("connection is closed.");
            source.rollback();
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void close()
    {
        try
        {
            source.close();
        }
        catch(SQLException ignored)
        {
            ignored.printStackTrace();
        }
    }

    public final boolean isClosed()
    {
        try
        {
            return source.isClosed();
        }
        catch(SQLException ignored)
        {
            ignored.printStackTrace();
        }
        return false;
    }

    private final Connection source;
}
