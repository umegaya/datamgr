// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlStatement.java

package jp.enterquest.system;

import java.sql.*;

// Referenced classes of package jp.enterquest.system:
//            SqlResult

public final class SqlStatement
{

    static final SqlStatement newInstance(PreparedStatement source)
    {
        return new SqlStatement(source);
    }

    private SqlStatement(PreparedStatement source)
    {
        this.source = source;
    }

    public final void setBoolean(int index, boolean value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setBoolean(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setByte(int index, byte value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setByte(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setInt16(int index, short value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setShort(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setInt32(int index, int value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setInt(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setInt64(int index, long value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setLong(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setFloat32(int index, float value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setFloat(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setFloat64(int index, double value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setDouble(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setString(int index, String value)
    {
        try
        {
            if(value == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setString(index + 1, value);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setDate(int index, long value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setDate(index + 1, new Date(value));
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setTime(int index, long value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setTime(index + 1, new Time(value));
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setDateTime(int index, long value)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setTimestamp(index + 1, new Timestamp(value));
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final void setNull(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            source.setString(index + 1, null);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final SqlResult executeQuery()
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            else
                return SqlResult.newInstance(source.executeQuery());
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final int executeUpdate()
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("statement is closed.");
            else
                return source.executeUpdate();
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

    private final PreparedStatement source;
}
