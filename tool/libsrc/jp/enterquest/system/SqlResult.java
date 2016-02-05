// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlResult.java

package jp.enterquest.system;

import java.sql.*;

public final class SqlResult
{

    static final SqlResult newInstance(ResultSet source)
    {
        return new SqlResult(source);
    }

    private SqlResult(ResultSet source)
    {
        this.source = source;
    }

    public final boolean isExists()
    {
        try
        {
            while (source.next()) {
                return true;
            }
        }
        catch(SQLException cause)
        {
        }
        return false;
    }

    public final boolean next()
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.next();
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final boolean getBoolean(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getBoolean(index + 1);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final boolean getBoolean(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getBoolean(label);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final byte getByte(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getByte(index + 1);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final byte getByte(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getByte(label);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final short getInt16(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getShort(index + 1);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final short getInt16(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getShort(label);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final int getInt32(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getInt(index + 1);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final int getInt32(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getInt(label);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getInt64(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getLong(index + 1);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getInt64(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getLong(label);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final float getFloat32(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getFloat(index + 1);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final float getFloat32(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getFloat(label);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final double getFloat64(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getDouble(index + 1);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }

    public final double getFloat64(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getDouble(label);
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final String getString(int index)
    {
        try
        {
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                String value = source.getString(index + 1);
                return value == null ? "" : value;
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final String getString(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                String value = source.getString(label);
                return value == null ? "" : value;
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getDate(int index)
    {
        try
        {
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                Date value = source.getDate(index + 1);
                return value == null ? 0L : value.getTime();
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getDate(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                Date value = source.getDate(label);
                return value == null ? 0L : value.getTime();
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getTime(int index)
    {
        try
        {
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                Time value = source.getTime(index + 1);
                return value == null ? 0L : value.getTime();
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getTime(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                Time value = source.getTime(label);
                return value == null ? 0L : value.getTime();
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getDateTime(int index)
    {
        try
        {
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                Timestamp value = source.getTimestamp(index + 1);
                return value == null ? 0L : value.getTime();
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final long getDateTime(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
            {
                throw new RuntimeException("result is closed.");
            } else
            {
                Timestamp value = source.getTimestamp(label);
                return value == null ? 0L : value.getTime();
            }
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final boolean isNull(int index)
    {
        try
        {
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getString(index + 1) == null;
        }
        catch(SQLException cause)
        {
            throw new RuntimeException(cause);
        }
    }

    public final boolean isNull(String label)
    {
        try
        {
            if(label == null)
                throw new NullPointerException();
            if(isClosed())
                throw new RuntimeException("result is closed.");
            else
                return source.getString(label) == null;
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

    private final ResultSet source;
}
