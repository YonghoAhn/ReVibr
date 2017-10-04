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
    //

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

    private static void EmptyComposition()
    {
        if(currentBraille.equals(ENGLISH_TAG))
        {

        }
        else if(currentBraille.equals(NUMBER_TAG))
        {

        }
        else
        {
            HangulComposition();
        }
    }

    private static void HangulComposition()
    {
        
    }

    private static void EnglishComposition()
    {

    }

    private static void NumberComposition()
    {

    }
}
