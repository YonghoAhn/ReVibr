package com.valuecomposite.revibr;

import static com.valuecomposite.revibr.BI.FLAG.*;

/**
 * Created by anyongho on 2017. 10. 3..
 */

//Temp
public class BI {
    enum FLAG{
        FLAG_EMPTY,
        FLAG_HANGUL,
        FLAG_ENGLISH,
        FLAG_NUMBER
    }
    private static FLAG stateFlag = FLAG_EMPTY;

    //Static Braille Flags
    //English
    private static final String ENGLISH_TAG         = "001011";
    //Number
    private static final String NUMBER_TAG          = "001111";
    /* Double-Chosung */
    private static final String DOUBLE_CHOSUNG_TAG  = "000001";
    //
    private static final String SPACE               = "000000";
    /*
    Braille Variables
    */
    private static int brailleCount                        = 0;
    //
    private static boolean doubleChosungFlag           = false;
    //
    private static char chosung                          = ' ';
    private static char joongsung                        = ' ';
    private static char jongsung                         = ' ';
    //
    private static String currentBraille                  = "";
    /*
       Braille Converter
     */
    private static BrailleConverter brailleConverter = new BrailleConverter();


    public static void Input(boolean gesture)
    {
        if(gesture) currentBraille += "1";
        else        currentBraille += "0";
        if(++brailleCount==6)
        {
            Composition();
        }
    }
    //
    private static void Composition()
    {
        if(stateFlag == FLAG_EMPTY) //영어표 숫자표
        {
            EmptyComposition();
        }
        else if(stateFlag == FLAG_ENGLISH) //영어모드
        {
            EnglishComposition();
        }
        else if(stateFlag == FLAG_NUMBER) //숫자모드
        {
            NumberComposition();
        }
    }

    private static boolean EmptyComposition()
    {
        if(currentBraille.equals(ENGLISH_TAG))
        {
            stateFlag = FLAG_ENGLISH;
            return true;
        }
        else if(currentBraille.equals(NUMBER_TAG))
        {
            stateFlag = FLAG_NUMBER;
            return true;
        }
        else
        {
            HangulComposition();
            return true;
        }
    }

    private static boolean HangulComposition()
    {
        if(currentBraille.equals(DOUBLE_CHOSUNG_TAG))//쌍자음 태그라면
        {
            DoubleChosung();
        }

        int pos = brailleConverter.getHangulPosition(currentBraille);
        switch(pos)
        {
            case 1://Chosung
                break;
            case 2://Joongsung
                break;
            case 3://Jongsung
                break;
        }
        return true;
    }

    private static boolean DoubleChosung()
    {
        return true;
    }

    private static boolean EnglishComposition() //English
    {
        char c = brailleConverter.getEnglishCharacter(currentBraille);
        if(c == ' ') return false;
        SendActivity.AddChosung("" + c);
        return true;
    }

    private static boolean NumberComposition() //Numeric
    {
        char c = brailleConverter.getNumericCharacter(currentBraille);
        if(c == ' ') return false;
        SendActivity.AddChosung("" + c);
        return true;
    }

    public static void Flush(boolean clearDoubleChosung)
    {
        chosung = ' ';
        joongsung = ' ';
        jongsung = ' ';
        if(clearDoubleChosung)
            doubleChosungFlag = false;
    }

    public static void KimJohnSoo()
    {
        System.out.println("KimJohnSoo");
    }
}
