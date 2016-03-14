package jp.enterquest.manager.core.process;

import jp.enterquest.system.Array;
import jp.enterquest.system.Hash;
import jp.enterquest.system.Data;
import jp.enterquest.system.DataFactory;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Arrays;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public final class CSVDecoder
{
    public static String SEP = "\t";

    public static final CSVDecoder getInstance()
    {
        return instance;
    }

    private CSVDecoder()
    {
    }

    public final Data decode(String data)
    {
        Data ret = DataFactory.getInstance().newArray();
        String[] columns = null;
        for (String line : Arrays.asList(data.split("\n"))) {
            if (columns != null) {
                ret.asArray().add(convertToHash(columns, line));
            }
            else {
                columns = line.split(SEP);
            }
        }
        return ret;
    }

    public final Data convertToHash(String[] columns, String line) {
        Data data = DataFactory.getInstance().newHash();
        String[] values = line.split(SEP);
        for (int i = 0; i < columns.length; i++) {
            data.asHash().set(columns[i], convertToData(values[i]));
        }
        return data;
    }

    public final Data convertToData(String value) {
        if (value.equals("null")) {
            return DataFactory.getInstance().getNull();
        }
        else if (value.equals("true")) {
            return DataFactory.getInstance().newBoolean(true);
        }
        else if (value.equals("false")) {
            return DataFactory.getInstance().newBoolean(false);
        }

		// 先頭に0が付いているものは基本的に文字列のため強制的に文字列として処理してしまう。
		// (間違って数値として判断された場合に、先頭の0が無くなってしまい問題が発生するため
		// また、本来であればデータベースの型を利用して判定するべきだが一旦この処理で回避する)
		Pattern p1 = Pattern.compile("^0");				// 先頭の0にマッチ
		Matcher m1 = p1.matcher(value);
		Pattern p2 = Pattern.compile("^?\\.");			// 小数点のドットにマッチ
		Matcher m2 = p2.matcher(value);
		if((true == m1.find()) && (false == m2.find())) {
			return DataFactory.getInstance().newString(value.replace("¥n", "\r\n"));
		}

        /* 以下、整数ないしfloatに変換可能かtryで、だめならstring */
        try {
            Integer i = Integer.valueOf(value);
            return DataFactory.getInstance().newInt32(i);
        }
        catch (NumberFormatException e) {
        }
        try {
            Long i = Long.valueOf(value);
            return DataFactory.getInstance().newInt64(i);
        }
        catch (NumberFormatException e) {
        }
        try {
            Float f = Float.valueOf(value);
            return DataFactory.getInstance().newFloat32(f);
        }
        catch (NumberFormatException e) {
        }        
        try {
            Double f = Double.valueOf(value);
            return DataFactory.getInstance().newFloat64(f);
        }
        catch (NumberFormatException e) {
        }

        return DataFactory.getInstance().newString(value.replace("¥n", "\r\n"));
    }

    private static final CSVDecoder instance = new CSVDecoder();

}
