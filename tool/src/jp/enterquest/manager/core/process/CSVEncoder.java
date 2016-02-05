package jp.enterquest.manager.core.process;

import jp.enterquest.system.Array;
import jp.enterquest.system.Hash;
import jp.enterquest.system.Data;

import java.io.StringWriter;
import java.util.Iterator;

public final class CSVEncoder
{
    public static String SEP = "\t";

    public static final CSVEncoder getInstance()
    {
        return instance;
    }

    private CSVEncoder()
    {
    }

    public final String encode(Data data)
    {
        if(data == null || !data.isArray()) {
            throw new IllegalArgumentException();
        }
        StringWriter writer = new StringWriter(10240);
        boolean first = true;

        for (Data line : data.asArray()) {
            if (first) {
                writeFirstLine(writer, line);
                first = false;
            }
            else {
                writeLine(writer, line);
            }
        }
        return writer.toString();
    }

    protected final void writeFirstLine(StringWriter writer, Data line) {
        String columnLine = "";
        String firstLine = "";
        for (Iterator it = line.asHash().iterator(); it.hasNext();) {
            Object k = it.next();
            Data v = (Data)line.asHash().get(k.toString());
            columnLine += (k + SEP);
            firstLine += (convertToString(v) + SEP);
        }
        writer.write(columnLine); writer.write('\n');
        writer.write(firstLine);writer.write('\n');
    }

    protected final void writeLine(StringWriter writer, Data line) {
        String csvLine = "";
        for (Iterator it = line.asHash().iterator(); it.hasNext();) {
            Object k = it.next();
            Data v = (Data)line.asHash().get(k.toString());
            csvLine += (convertToString(v) + SEP);
        }
        writer.write(csvLine);writer.write('\n');   
    }

    protected final String convertToString(Data value) {
        if(value.isBoolean())
            return value.asBoolean() ? "true" : "false";
        else
        if(value.isByte())
            return String.format("%d", value.asByte());
        else
        if(value.isInt16())
            return String.format("%d", value.asInt16());
        else
        if(value.isInt32())
            return String.format("%d", value.asInt32());
        else
        if(value.isInt64())
            return String.format("%d", value.asInt64());
        else
        if(value.isFloat32())
            return String.format("%f", value.asFloat32());
        else
        if(value.isFloat64())
            return String.format("%f", value.asFloat64());
        else
        if(value.isString())
            return value.asString().replace("\r\n", "¥n");
        else
        if(value.isArray())
            throw new IllegalArgumentException();                
        else
        if(value.isHash())
            throw new IllegalArgumentException();                
        else
        if(value.isNull())
            return "null";
        else 
            throw new IllegalArgumentException();   
    }

    private static final CSVEncoder instance = new CSVEncoder();

}