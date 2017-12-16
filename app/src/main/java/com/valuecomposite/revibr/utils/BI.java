package com.valuecomposite.revibr.utils;

import com.valuecomposite.revibr.Activities.SendActivity;

import static com.valuecomposite.revibr.utils.BI.FLAG.FLAG_EMPTY;
import static com.valuecomposite.revibr.utils.BI.FLAG.FLAG_ENGLISH;
import static com.valuecomposite.revibr.utils.BI.FLAG.FLAG_NUMBER;

/**
 * Created by anyongho on 2017. 10. 3..
 */

//Temp
public class BI {
    //Static Braille Flags
    //English
    private static final String ENGLISH_TAG         = "001011";
    //Number
    private static final String NUMBER_TAG          = "001111";
    /* Double-Chosung */
    private static final String DOUBLE_CHOSUNG_TAG  = "000001";
    //
    private static final String SPACE               = "000000";
    private static FLAG stateFlag = FLAG_EMPTY;
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
    // uses only input "Hangul" Character
    private static int latestCharacter                     = 0;
    /*
       Braille Converter
     */
    private static BrailleConverter brailleConverter = new BrailleConverter();

    //Input Method
    /*
        @param boolean gesture 제스처
     */
    public static void Input(boolean gesture)
    {
        if(currentBraille.length() == 6) //꽉차있으면 터지므로, 꽉차면 먼저 지워준다. 정상적인 방법으로는 6개 꽉차면 무조건 처리되므로, 강제 플러시 될 때를 염두에 둔다.
            currentBraille = "";
        if(gesture) currentBraille += "1";
        else        currentBraille += "0";
        if(++brailleCount==6) //6개 모이면
        {
            Composition();
        }
    }

    /*
        자소단위로 제거합니다.
    */
    public static int Delete()
    {
        latestCharacter -= 1; //어차피 이게 0되면 -1 리턴돼서 ㄱㅊ
        if(jongsung!=' ') {
            jongsung = ' ';
            return 0;
        }
        else if(joongsung != ' ') {
            joongsung = ' ';
            return 0;
        }
        else if(chosung != ' ') {
            chosung = ' ';
            return 0;
        }
        else {
            return -1;
        }
    }

    /*
        이전 글자를 넘겨주고 자소 한 개를 제거합니다.
    */
    public static int Delete(char beforeChar)
    {
        //이전의 문자를 넘겨받았음.
        if(beforeChar == ' ')
            return -1;
        char[] chars = HangulSupport.HangulAlphabet(beforeChar);
        chosung = chars[0]; joongsung = chars[1]; jongsung = chars[2]; //초 중 종성 등록해줌
        latestCharacter = chars[2] != ' ' ? 3 : 2; //최근 입력된 글자를 변경해줌
        Delete(); //한번 호출해서 지워줌
        return 0;
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
                ChosungComposition();
                break;
            case 2://Joongsung
                JoongsungComposition();
                break;
            case 3://Jongsung
                JongsungComposition();
                break;
        }
        return true;
    }

    private static boolean DoubleChosung()
    {
        return true;
    }

    private static boolean ChosungComposition()
    {
        // 쌍자음인지 체크
        // 아니라면 그냥 초성
        // 특수한 경우만 쌍자음 처리
        char c = brailleConverter.getHangulCharacter(currentBraille);
        if(doubleChosungFlag) //쌍자음 플래그가 섰다면
        {
            switch(c) //ㅅ은 000001과 겹치므로, 중성 입력 시 처리한다.
            {
                case 'ㄱ':
                    c = 'ㄲ';
                    break;
                case 'ㄷ':
                    c = 'ㄸ';
                    break;
                case 'ㅂ':
                    c = 'ㅃ';
                    break;
                case 'ㅈ':
                    c = 'ㅉ';
                    break;
            }
        }
        else
        {
            chosung = c;
        }
        SendActivity.AddChosung(""+c);
        latestCharacter = 1;
        return true;
    }

    private static boolean JoongsungComposition()
    {
        char c = brailleConverter.getHangulCharacter(currentBraille);
        if(latestCharacter == 3) //가장 최근 입력된 글자가 종성이었음
        {
            //종성이면 초기화해주고, 초성을 ㅇ처리해야함
            Flush(true);
        } else if (latestCharacter == 2) //가장 최근에 입력한 글자가 중성이었음 : 이게 특수중성 입력인지, 아니면 걍 새 글자 시작인지 체크해서 특수 중성이 아니라면 새 글자로 인식, 초성 ㅇ의 새로운 글자를 입력한다
        {
            if (c == 'ㅐ') //특수 모음 처리
            {
                switch (joongsung) {
                    case 'ㅜ':
                        joongsung = 'ㅟ';
                        break;
                    case 'ㅑ':
                        joongsung = 'ㅒ';
                        break;
                    case 'ㅘ':
                        joongsung = 'ㅙ';
                        break;
                    case 'ㅝ':
                        joongsung = 'ㅞ';
                        break;
                    case ' ':
                        joongsung = 'ㅐ';
                        break;
                }
            } else //특수 중성 입력이 아님
            {
                Flush(true);
            }
        }
        if(doubleChosungFlag && chosung == ' ') //쌍자음 플래그가 서있다 && 초성이 비었다
        {
            chosung = 'ㅅ'; //초성이 ㅅ임
        }
        else if(chosung == ' ') //초성 입력 없이 그냥 들어옴
        {
            chosung = 'ㅇ'; //초성을 ㅇ으로
        }
        //Composition Chosung + Joongsung
        char result = HangulSupport.CombineHangul(new char[]{chosung, joongsung});
        SendActivity.AddText("" + result);
        latestCharacter = 2;
        return true;
    }

    //종성 합성
    private static boolean JongsungComposition()
    {
        if (joongsung == ' ') return false;
        char c = brailleConverter.getHangulCharacter(currentBraille);
        if (jongsung != ' ' && latestCharacter == 3) {
            jongsung = MultipleJongsungComposition(c);
        }
        String result = "" + HangulSupport.CombineHangul(new char[]{chosung, joongsung, jongsung});
        SendActivity.AddText(result);
        //겹종성 처리를 하거나
        //걍 종성을 넣거나
        //모음이 없다면, 무시해야
        latestCharacter = 3;
        return true;
    }

    //중복 종성 합성 및 리턴
    private static char MultipleJongsungComposition(char c) {
        String str = "" + jongsung + c;
        switch (str) {
            case "ㄱㄱ":
                c = 'ㄲ';
                break;
            case "ㄷㄷ":
                c = 'ㄲ';
                break;
            case "ㄱㅅ":
                c = 'ㄳ';
                break;
            case "ㄴㅈ":
                c = 'ㄵ';
                break;
            case "ㄴㅎ":
                c = 'ㄶ';
                break;
            case "ㄹㄱ":
                c = 'ㄺ';
                break;
            case "ㄹㅁ":
                c = 'ㄻ';
                break;
            case "ㄹㅂ":
                c = 'ㄼ';
                break;
            case "ㄹㅅ":
                c = 'ㄽ';
                break;
            case "ㄹㅌ":
                c = 'ㄾ';
                break;
            case "ㄹㅍ":
                c = 'ㄿ';
                break;
            case "ㄹㅎ":
                c = 'ㅀ';
                break;
            case "ㅂㅅ":
                c = 'ㅄ';
                break;
            case "ㅅㅅ":
                c = 'ㅆ';
                break;
            default:
                //SendMessageActivity.addLastText(_jongsung);
                return ' ';
        }
        return c;
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

    enum FLAG {
        FLAG_EMPTY,
        FLAG_HANGUL,
        FLAG_ENGLISH,
        FLAG_NUMBER
    }
}
