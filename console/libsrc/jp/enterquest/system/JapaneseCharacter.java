// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JapaneseCharacter.java

package jp.enterquest.system;


public final class JapaneseCharacter
{

    public static final JapaneseCharacter getInstance()
    {
        return instance;
    }

    private JapaneseCharacter()
    {
    }

    public final String toHiragana(String string)
    {
        if(string == null)
            throw new NullPointerException();
        else
            return replace(HIRAGANA_AND_KATAKANA, 1, 0, string);
    }

    public final String toKatakana(String string)
    {
        if(string == null)
            throw new NullPointerException();
        else
            return replace(HIRAGANA_AND_KATAKANA, 0, 1, string);
    }

    public final String toFullWidth(String string)
    {
        if(string == null)
            throw new NullPointerException();
        else
            return replace(FULLWIDTH_AND_HALFWIDTH, 1, 0, string);
    }

    public final String toHalfWidth(String string)
    {
        if(string == null)
            throw new NullPointerException();
        else
            return replace(FULLWIDTH_AND_HALFWIDTH, 0, 1, string);
    }

    private final String replace(String entry_sets[][], int before_index, int after_index, String string)
    {
        StringBuilder buffer = new StringBuilder(string);
        if(0 < buffer.length())
        {
            String arr$[][] = entry_sets;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                String entry_set[] = arr$[i$];
                String before_character = entry_set[before_index];
                String after_character = entry_set[after_index];
                for(int start_index = -1; 0 <= (start_index = buffer.indexOf(before_character));)
                {
                    int end_index = start_index + before_character.length();
                    buffer.replace(start_index, end_index, after_character);
                }

            }

        }
        return buffer.toString();
    }

    private static final JapaneseCharacter instance = new JapaneseCharacter();
    private static final String HIRAGANA_AND_KATAKANA[][] = {
        {
            "\u3042", "\u30A2"
        }, {
            "\u3044", "\u30A4"
        }, {
            "\u3046", "\u30A6"
        }, {
            "\u3048", "\u30A8"
        }, {
            "\u304A", "\u30AA"
        }, {
            "\u304B", "\u30AB"
        }, {
            "\u304D", "\u30AD"
        }, {
            "\u304F", "\u30AF"
        }, {
            "\u3051", "\u30B1"
        }, {
            "\u3053", "\u30B3"
        }, {
            "\u3055", "\u30B5"
        }, {
            "\u3057", "\u30B7"
        }, {
            "\u3059", "\u30B9"
        }, {
            "\u305B", "\u30BB"
        }, {
            "\u305D", "\u30BD"
        }, {
            "\u305F", "\u30BF"
        }, {
            "\u3061", "\u30C1"
        }, {
            "\u3064", "\u30C4"
        }, {
            "\u3066", "\u30C6"
        }, {
            "\u3068", "\u30C8"
        }, {
            "\u306A", "\u30CA"
        }, {
            "\u306B", "\u30CB"
        }, {
            "\u306C", "\u30CC"
        }, {
            "\u306D", "\u30CD"
        }, {
            "\u306E", "\u30CE"
        }, {
            "\u306F", "\u30CF"
        }, {
            "\u3072", "\u30D2"
        }, {
            "\u3075", "\u30D5"
        }, {
            "\u3078", "\u30D8"
        }, {
            "\u307B", "\u30DB"
        }, {
            "\u307E", "\u30DE"
        }, {
            "\u307F", "\u30DF"
        }, {
            "\u3080", "\u30E0"
        }, {
            "\u3081", "\u30E1"
        }, {
            "\u3082", "\u30E2"
        }, {
            "\u3084", "\u30E4"
        }, {
            "\u3086", "\u30E6"
        }, {
            "\u3088", "\u30E8"
        }, {
            "\u3089", "\u30E9"
        }, {
            "\u308A", "\u30EA"
        }, {
            "\u308B", "\u30EB"
        }, {
            "\u308C", "\u30EC"
        }, {
            "\u308D", "\u30ED"
        }, {
            "\u308F", "\u30EF"
        }, {
            "\u3090", "\u30F0"
        }, {
            "\u3092", "\u30F2"
        }, {
            "\u3091", "\u30F1"
        }, {
            "\u3093", "\u30F3"
        }, {
            "\u304C", "\u30AC"
        }, {
            "\u304E", "\u30AE"
        }, {
            "\u3050", "\u30B0"
        }, {
            "\u3052", "\u30B2"
        }, {
            "\u3054", "\u30B4"
        }, {
            "\u3056", "\u30B6"
        }, {
            "\u3058", "\u30B8"
        }, {
            "\u305A", "\u30BA"
        }, {
            "\u305C", "\u30BC"
        }, {
            "\u305E", "\u30BE"
        }, {
            "\u3060", "\u30C0"
        }, {
            "\u3062", "\u30C2"
        }, {
            "\u3065", "\u30C5"
        }, {
            "\u3067", "\u30C7"
        }, {
            "\u3069", "\u30C9"
        }, {
            "\u3070", "\u30D0"
        }, {
            "\u3073", "\u30D3"
        }, {
            "\u3076", "\u30D6"
        }, {
            "\u3079", "\u30D9"
        }, {
            "\u307C", "\u30DC"
        }, {
            "\u3071", "\u30D1"
        }, {
            "\u3074", "\u30D4"
        }, {
            "\u3077", "\u30D7"
        }, {
            "\u307A", "\u30DA"
        }, {
            "\u307D", "\u30DD"
        }, {
            "\u3041", "\u30A1"
        }, {
            "\u3043", "\u30A3"
        }, {
            "\u3045", "\u30A5"
        }, {
            "\u3047", "\u30A7"
        }, {
            "\u3049", "\u30A9"
        }, {
            "\u3063", "\u30C3"
        }, {
            "\u3083", "\u30E3"
        }, {
            "\u3085", "\u30E5"
        }, {
            "\u3087", "\u30E7"
        }, {
            "\u308E", "\u30EE"
        }
    };
    private static final String FULLWIDTH_AND_HALFWIDTH[][] = {
        {
            "\u30F4", "\uFF73\uFF9E"
        }, {
            "\u30AC", "\uFF76\uFF9E"
        }, {
            "\u30AE", "\uFF77\uFF9E"
        }, {
            "\u30B0", "\uFF78\uFF9E"
        }, {
            "\u30B2", "\uFF79\uFF9E"
        }, {
            "\u30B4", "\uFF7A\uFF9E"
        }, {
            "\u30B6", "\uFF7B\uFF9E"
        }, {
            "\u30B8", "\uFF7C\uFF9E"
        }, {
            "\u30BA", "\uFF7D\uFF9E"
        }, {
            "\u30BC", "\uFF7E\uFF9E"
        }, {
            "\u30BE", "\uFF7F\uFF9E"
        }, {
            "\u30C0", "\uFF80\uFF9E"
        }, {
            "\u30C2", "\uFF81\uFF9E"
        }, {
            "\u30C5", "\uFF82\uFF9E"
        }, {
            "\u30C7", "\uFF83\uFF9E"
        }, {
            "\u30C9", "\uFF84\uFF9E"
        }, {
            "\u30D0", "\uFF8A\uFF9E"
        }, {
            "\u30D3", "\uFF8B\uFF9E"
        }, {
            "\u30D6", "\uFF8C\uFF9E"
        }, {
            "\u3079", "\uFF8D\uFF9E"
        }, {
            "\u30DC", "\uFF8E\uFF9E"
        }, {
            "\u30D1", "\uFF8A\uFF9F"
        }, {
            "\u30D4", "\uFF8B\uFF9F"
        }, {
            "\u30D7", "\uFF8C\uFF9F"
        }, {
            "\u30DA", "\uFF8D\uFF9F"
        }, {
            "\u30DD", "\uFF8E\uFF9F"
        }, {
            "\u30A2", "\uFF71"
        }, {
            "\u30A4", "\uFF72"
        }, {
            "\u30A6", "\uFF73"
        }, {
            "\u30A8", "\uFF74"
        }, {
            "\u30AA", "\uFF75"
        }, {
            "\u30AB", "\uFF76"
        }, {
            "\u30AD", "\uFF77"
        }, {
            "\u30AF", "\uFF78"
        }, {
            "\u30B1", "\uFF79"
        }, {
            "\u30B3", "\uFF7A"
        }, {
            "\u30B5", "\uFF7B"
        }, {
            "\u30B7", "\uFF7C"
        }, {
            "\u30B9", "\uFF7D"
        }, {
            "\u30BB", "\uFF7E"
        }, {
            "\u30BD", "\uFF7F"
        }, {
            "\u30BF", "\uFF80"
        }, {
            "\u30C1", "\uFF81"
        }, {
            "\u30C4", "\uFF82"
        }, {
            "\u30C6", "\uFF83"
        }, {
            "\u30C8", "\uFF84"
        }, {
            "\u30CA", "\uFF85"
        }, {
            "\u30CB", "\uFF86"
        }, {
            "\u30CC", "\uFF87"
        }, {
            "\u30CD", "\uFF88"
        }, {
            "\u30CE", "\uFF89"
        }, {
            "\u30CF", "\uFF8A"
        }, {
            "\u30D2", "\uFF8B"
        }, {
            "\u30D5", "\uFF8C"
        }, {
            "\u30D8", "\uFF8D"
        }, {
            "\u30DB", "\uFF8E"
        }, {
            "\u30DE", "\uFF8F"
        }, {
            "\u30DF", "\uFF90"
        }, {
            "\u30E0", "\uFF91"
        }, {
            "\u30E1", "\uFF92"
        }, {
            "\u30E2", "\uFF93"
        }, {
            "\u30E4", "\uFF94"
        }, {
            "\u30E6", "\uFF95"
        }, {
            "\u30E8", "\uFF96"
        }, {
            "\u30E9", "\uFF97"
        }, {
            "\u30EA", "\uFF98"
        }, {
            "\u30EB", "\uFF99"
        }, {
            "\u30EC", "\uFF9A"
        }, {
            "\u30ED", "\uFF9B"
        }, {
            "\u30EF", "\uFF9C"
        }, {
            "\u30F2", "\uFF66"
        }, {
            "\u30F3", "\uFF9D"
        }, {
            "\u30A1", "\uFF67"
        }, {
            "\u30A3", "\uFF68"
        }, {
            "\u30A5", "\uFF69"
        }, {
            "\u30A7", "\uFF6A"
        }, {
            "\u30A9", "\uFF6B"
        }, {
            "\u30C3", "\uFF6F"
        }, {
            "\u30E3", "\uFF6C"
        }, {
            "\u30E5", "\uFF6D"
        }, {
            "\u30E7", "\uFF6E"
        }, {
            "\uFF10", "0"
        }, {
            "\uFF11", "1"
        }, {
            "\uFF12", "2"
        }, {
            "\uFF13", "3"
        }, {
            "\uFF14", "4"
        }, {
            "\uFF15", "5"
        }, {
            "\uFF16", "6"
        }, {
            "\uFF17", "7"
        }, {
            "\uFF18", "8"
        }, {
            "\uFF19", "9"
        }, {
            "\uFF21", "A"
        }, {
            "\uFF22", "B"
        }, {
            "\uFF23", "C"
        }, {
            "\uFF24", "D"
        }, {
            "\uFF25", "E"
        }, {
            "\uFF26", "F"
        }, {
            "\uFF27", "G"
        }, {
            "\uFF28", "H"
        }, {
            "\uFF29", "I"
        }, {
            "\uFF2A", "J"
        }, {
            "\uFF2B", "K"
        }, {
            "\uFF2C", "L"
        }, {
            "\uFF2D", "M"
        }, {
            "\uFF2E", "N"
        }, {
            "\uFF2F", "O"
        }, {
            "\uFF30", "P"
        }, {
            "\uFF31", "Q"
        }, {
            "\uFF32", "R"
        }, {
            "\uFF33", "S"
        }, {
            "\uFF34", "T"
        }, {
            "\uFF35", "U"
        }, {
            "\uFF36", "V"
        }, {
            "\uFF37", "W"
        }, {
            "\uFF38", "X"
        }, {
            "\uFF39", "Y"
        }, {
            "\uFF3A", "Z"
        }, {
            "\uFF41", "a"
        }, {
            "\uFF42", "b"
        }, {
            "\uFF43", "c"
        }, {
            "\uFF44", "d"
        }, {
            "\uFF45", "e"
        }, {
            "\uFF46", "f"
        }, {
            "\uFF47", "g"
        }, {
            "\uFF48", "h"
        }, {
            "\uFF49", "i"
        }, {
            "\uFF4A", "j"
        }, {
            "\uFF4B", "k"
        }, {
            "\uFF4C", "l"
        }, {
            "\uFF4D", "m"
        }, {
            "\uFF4E", "n"
        }, {
            "\uFF4F", "o"
        }, {
            "\uFF50", "p"
        }, {
            "\uFF51", "q"
        }, {
            "\uFF52", "r"
        }, {
            "\uFF53", "s"
        }, {
            "\uFF54", "t"
        }, {
            "\uFF55", "u"
        }, {
            "\uFF56", "v"
        }, {
            "\uFF57", "w"
        }, {
            "\uFF58", "x"
        }, {
            "\uFF59", "y"
        }, {
            "\uFF5A", "z"
        }
    };
    private static final int HIRAGANA_INDEX = 0;
    private static final int KATAKANA_INDEX = 1;
    private static final int FULLWIDTH_INDEX = 0;
    private static final int HALFWIDTH_INDEX = 1;

}
