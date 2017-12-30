/**
 * Created by ayh07 on 8/13/2017.
 */

package com.valuecomposite.revibr.utils;

/**
 * Created by anyongho on 2017. 10. 3..
 */

import android.util.Log;

import com.valuecomposite.revibr.Activities.SendActivity;

import static com.valuecomposite.revibr.utils.BrailleInput.FLAG.FLAG_EMPTY;
import static com.valuecomposite.revibr.utils.BrailleInput.FLAG.FLAG_ENGLISH;
import static com.valuecomposite.revibr.utils.BrailleInput.FLAG.FLAG_NUMBER;

//Temp
public class BrailleInput {
    //Static Braille Flags
    //English
    private static final String ENGLISH_TAG = "001011";
    //Number
    private static final String NUMBER_TAG = "001111";
    /* Double-Chosung */
    private static final String DOUBLE_CHOSUNG_TAG = "000001";
    //
    private static final String SPACE = "000000";
    private static FLAG stateFlag = FLAG_EMPTY;
    /*
    Braille Variables
    */
    private static int brailleCount = 0;
    //
    private static boolean doubleChosungFlag = false;
    //
    private static char chosung = ' ';
    private static char joongsung = ' ';
    private static char jongsung = ' ';
    //
    private static String currentBraille = "";
    // uses only input "Hangul" Character
    private static int latestCharacter = 0;
    //
    private static boolean IsTwiceSpace = false;
    /*
       Braille Converter
     */
    private static BrailleConverter brailleConverter = new BrailleConverter();

    //Input Method
    /*
        @param boolean gesture 제스처
     */
    public static void Input(boolean gesture) {
        if (currentBraille.length() == 6) //꽉차있으면 터지므로, 꽉차면 먼저 지워준다. 정상적인 방법으로는 6개 꽉차면 무조건 처리되므로, 강제 플러시 될 때를 염두에 둔다.
            currentBraille = "";
        if (gesture) currentBraille += "1";
        else currentBraille += "0";
        if (++brailleCount == 6) //6개 모이면
        {
            SendActivity.clearColor();
            Composition();
            brailleCount = 0;
            SendActivity.count = 0;
            currentBraille = "";
        }
    }

    /*
        자소단위로 제거합니다.
    */
    public static int Delete() {
        try{ //Motion 삭제 시도
            currentBraille = currentBraille.substring(0,currentBraille.length()-1);
            SendActivity.setClearColor(brailleCount);
            SendActivity.count--;
            brailleCount--;
        }
        catch (Exception e) //실패시
        {
            Flush(true);
            return -1;
        }
        return 0;
    }


    //
    private static void Composition() {

        if(currentBraille.equals(SPACE))
        {
            //띄어쓰기니까 flush해야됨
            if(IsTwiceSpace)
            {
              //두번째 눌린 Space면 FLush됐고, 띄어쓰기 해야됨
                SendActivity.AddChosung(" ");
                IsTwiceSpace = false;
            }
            else
            {
                Flush(true);
                //처음 눌렸음
                IsTwiceSpace = true;
            }
        }

        if (stateFlag == FLAG_EMPTY) //영어표 숫자표
            EmptyComposition();
        else if (stateFlag == FLAG_ENGLISH) //영어모드
            EnglishComposition();
        else if (stateFlag == FLAG_NUMBER) //숫자모드
            NumberComposition();
    }

    private static boolean EmptyComposition() {
        if (currentBraille.equals(ENGLISH_TAG) && latestCharacter < 2) {
            stateFlag = FLAG_ENGLISH;
            return true;
        } else if (currentBraille.equals(NUMBER_TAG)) {
            stateFlag = FLAG_NUMBER;
            return true;
        } else {
            HangulComposition();
            return true;
        }
    }

    private static boolean HangulComposition() {
        int pos = brailleConverter.getHangulPosition(currentBraille);
        switch (pos) {
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

    private static boolean ChosungComposition() {
        // 쌍자음인지 체크
        // 아니라면 그냥 초성
        // 특수한 경우만 쌍자음 처리
        char c = brailleConverter.getHangulCharacter(currentBraille);
        if (doubleChosungFlag && (c == 'ㄱ' || c == 'ㄷ' || c == 'ㅂ' || c == 'ㅈ')) //쌍자음 플래그가 섰다면
        {
            SendActivity.Delete(true); //원래 있던 ㅅ을 지우고 초성을 넘긴다.
            switch (c) //ㅅ은 000001과 겹치므로, 중성 입력 시 처리한다.
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
                case 'ㅅ':
                    c = 'ㅆ';
                    break;
                case 'ㅈ':
                    c = 'ㅉ';
                    break;
            }
            doubleChosungFlag=false;
        }
        if (currentBraille.equals(DOUBLE_CHOSUNG_TAG))
            doubleChosungFlag = true;

        chosung = c;

        SendActivity.AddChosung("" + c);
        latestCharacter = 1;
        return true;
    }

    private static boolean JoongsungComposition() {
        char c = brailleConverter.getHangulCharacter(currentBraille);
        if (latestCharacter == 3) //가장 최근 입력된 글자가 종성이었음
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
        if (doubleChosungFlag && chosung == ' ') //쌍자음 플래그가 서있다 && 초성이 비었다
        {
            chosung = 'ㅅ'; //초성이 ㅅ임
        } else if (chosung == ' ') //초성 입력 없이 그냥 들어옴
        {
            chosung = 'ㅇ'; //초성을 ㅇ으로
            SendActivity.AddChosung(" "); //後に何か文字が入力することに対比
        }
        joongsung = c;
        //Composition Chosung + Joongsung
        char result = HangulSupport.CombineHangul(new char[]{chosung, joongsung, ' '});
        SendActivity.AddText("" + result);
        latestCharacter = 2;
        return true;
    }

    //종성 합성
    private static boolean JongsungComposition() {
        if (joongsung == ' ') return false;
        char c = brailleConverter.getHangulCharacter(currentBraille);
        if (jongsung != ' ' && latestCharacter == 3) {
            c = MultipleJongsungComposition(c);
            Log.d("MisakaMOE", "" + jongsung);
        }
        jongsung = c;
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
        if (c == ' ') return false;
        SendActivity.AddChosung("" + c);
        return true;
    }

    private static boolean NumberComposition() //Numeric
    {
        char c = brailleConverter.getNumericCharacter(currentBraille);
        if (c == ' ') return false;
        SendActivity.AddChosung("" + c);
        return true;
    }

    public static void Initialize()
    {
        brailleCount = 0;
        currentBraille = "";
        Flush(true);
    }

    public static void Flush(boolean clearDoubleChosung) {
        chosung = ' ';
        joongsung = ' ';
        jongsung = ' ';
        latestCharacter = 0;
        stateFlag = FLAG_EMPTY;
        if (clearDoubleChosung)
            doubleChosungFlag = false;
    }

    public static void KimJohnSoo() {
        System.out.println("KimJohnSoo");
    }

    enum FLAG {
        FLAG_EMPTY,
        FLAG_HANGUL,
        FLAG_ENGLISH,
        FLAG_NUMBER
    }


}


/*
public class BrailleInput {
    static BrailleConverter BC = new BrailleConverter();

    // 이 클래스에서는 UP/DOWN 제스처를 받아서 기록하고, 문자로 변경하는 일을 담당한다.
    // 또한 다양한 플래그를 설정하여, 겹문자, 종성 겹문자, 영어, 숫자를 구별한다.
    //플래그 목록
    enum FLAG {
        STATE_EMPTY,
        STATE_HANGUL_CHOSUNG,
        STATE_HANGUL_JOONGSUNG,
        STATE_HANGUL_JONGSUNG,
        STATE_ENGLISH,
        STATE_NUMBER
    }
    //현재 상태 플래그
    private static FLAG m_Flag = FLAG.STATE_EMPTY;
    //영어표
    private static final String ENGLISH_TAG = "001011";
    //숫자표
    private static final String NUMBER_TAG = "001111";
    //쌍자음
    private static final String DOUBLE_CHOSUNG = "000001";
    //쌍자음 플래그
    private static boolean IsDoubleChosung = false;
    //점자 카운트(최대 6)
    private static int BrailleCount = 0;
   //점자 저장
    private static String Braille = "";
    //초성 저장
    private static char chosung = ' ';
    //중성 저장
    private static char joongsung = ' ';
    //종성 저장
    private static char jongsung = ' ';
    //최종 텍스트 저장
    private static String CompositionResult = "";
    //제스처를 입력
    public static void Input(boolean Gesture)
    {
        if(Gesture)
            Braille+="1";
        else
            Braille+="0";
        if(++BrailleCount == 6) {
            SendActivity.clearColor();
            Composition(Braille);
            BrailleCount = 0;            BrailleCount = 0;
            SendActivity.count=0;
            Braille = "";
        }
    }

    //조합 처리
    public static boolean Composition(String s)
    {
        //띄어쓰기면 모든 플래그를 제거하고, EMPTY로 돌린다.
        if(s.equals("0000000"))
        {
            flush();
            SendActivity.AddChosung(" ");
            return true;
        }
        //EMPTY인 경우만 가능함
        if(m_Flag == FLAG.STATE_EMPTY || Braille.equals(DOUBLE_CHOSUNG)) {
            case_empty(Braille);
        }
        else if(m_Flag == FLAG.STATE_ENGLISH)
        {
            //영어로 처리한다. 영어 외의 다른 문자는 모두 무시한다.
            case_alphabet(Braille);
        }
        else if(m_Flag == FLAG.STATE_NUMBER)
        {
            //숫자로 처리한다. 숫자 외의 다른 문자는 모두 무시한다.
            case_number(Braille);
        }
        else {
            int pos = BC.getHangulPosition(Braille);
            if(pos == 1) {
                flush();
                case_chosung(Braille); //초성이 입력되었을때
            }
            if(pos==2)
                case_joongsung(Braille); //ㅅ이 입력되었을때를 대비한 중성처리
            if(pos==3)
                case_jongsung(Braille);
            return true;
        }
        return true;
    }

    //싹 초기화
    public static void flush()
    {
        //모든 state를 초기화하는 메서드
        m_Flag = FLAG.STATE_EMPTY;
        chosung = ' ';
        jongsung = ' ';
        joongsung = ' ';
        //IsDoubleChosung = false;
    }

    //비어있을 경우
    public static boolean case_empty(String Braille)
    {
        // 영어표거나 숫자표거나 한글 초성이거나
        // 한글 초성 쌍자음표 중 하나일 것임
        // 그외의 것들은 무시
        if (Braille.equals(ENGLISH_TAG)) //영문표
        {
            m_Flag = FLAG.STATE_ENGLISH;
            return true;
        }
        else if (Braille.equals(NUMBER_TAG)) //숫자표
        {
            m_Flag = FLAG.STATE_NUMBER;
            return true;
        }
        else if(Braille.equals(DOUBLE_CHOSUNG))
        {
            flush();
            //쌍자음표임
            //만약 쌍자음표 플래그가 안서있으면 쌍자음일수도 있으므로 냅둠
            //쌍자음표 플랴그가 서있으면 쌍자음 처리를 해준다.
            if(IsDoubleChosung)
            {
                chosung = 'ㅆ';
                SendActivity.AddChosung("ㅆ");
                m_Flag = FLAG.STATE_HANGUL_CHOSUNG;
                IsDoubleChosung = false;
            }
            else
            {
                IsDoubleChosung=true; //잃단 쌍자음이라고 생각하기
            }
            return true;
        }
        else
        {
            // 초성처리를 해준다
            //초성만 처리하는 메서드를 하나 더 만들어야 할듯
            //이게 아 야 어 여 오 요 이런거는 ㅇ을 안치니까 중성이 올수도 있음
            int pos = BC.getHangulPosition(Braille);
            if(pos == 1) {
                flush();
                case_chosung(Braille); //초성이 입력되었을때
            }
            if(pos==2)
                case_joongsung(Braille); //ㅅ이 입력되었을때를 대비한 중성처리
            if(pos==3)
                case_jongsung(Braille);
            return true;
        }
    }

    public static boolean case_chosung(String Braille)
    {
        //먼저 쌍자음 플래그를 체크해 준다.
        char c = BC.getHangulCharacter(Braille);
        if(c == ' ') //한글중에 없으면 빠져나감
            return false;
        if(IsDoubleChosung) //ㅅ 아님
        {
            //처리 가능한 초성만 처리해 준다.
            switch (c)
            {
                case 'ㄱ':
                    chosung = 'ㄲ';
                    break;
                case 'ㄷ':
                    chosung='ㄸ';
                    break;
                case 'ㅂ':
                    chosung = 'ㅃ';
                    break;
                case 'ㅈ':
                    chosung = 'ㅉ';
                    break;
                default:break;
            }
            IsDoubleChosung = false;
        }
        else
        {
            chosung = c;
            m_Flag = FLAG.STATE_HANGUL_CHOSUNG;
        }
        String s = chosung + "";
        SendActivity.AddChosung(s);
        return true;
    }

    public static boolean case_joongsung(String Braille)
    {
        if(chosung == ' ')//초성이 비어있음
        {
            if(IsDoubleChosung)
            {
                chosung = 'ㅅ';
                SendActivity.AddChosung("ㅅ");
                IsDoubleChosung = false;
            }
            else {
                chosung = 'ㅇ';
                SendActivity.AddChosung("ㅇ");
            }
        }

        char c = BC.getHangulCharacter(Braille);
        if(c == ' ')
            return false;

        int pos = BC.getHangulPosition(Braille);
        if(pos != 2) //중성이 아닌 경우
        {
            if(pos == 1) //초성일 경우
            {
                case_chosung(Braille);
            }
            else if(pos == 3) //종성일 경우
            {
                case_jongsung(Braille);
            }
        }
        else //중성이 입력됨
        {
            if (c == 'ㅐ' && joongsung != ' ') //뭔가 추가적으로 입력될만한게 있는가?
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
                }
            }
            else if(m_Flag == FLAG.STATE_HANGUL_JOONGSUNG) //중성 추가는 아닌데 모음이 입력된 상태임
            {
                flush(); //플러시
                case_joongsung(Braille);
                return true;
            }
            else {
                joongsung = c;
            }
        }
        m_Flag = FLAG.STATE_HANGUL_JOONGSUNG;
        String s = "" + HangulSupport.CombineHangul(new char[]{chosung,joongsung,' '}) ;
        SendActivity.AddText(s);
        return true;
    }

    public static boolean case_jongsung(String Braille) {
        if(chosung==' ' && joongsung == ' ')
        {
            //ignore it
        }
        int pos = BC.getHangulPosition(Braille);
        char c = BC.getHangulCharacter(Braille);

        m_Flag = FLAG.STATE_HANGUL_JONGSUNG;
        if(jongsung != ' ') //이미 종성이 있다면
        {
            tryCreateMultipleJongsung(Braille);
        }
        else
        {
            jongsung = c;
        }
        String s = "" + HangulSupport.CombineHangul(new char[] {chosung,joongsung,jongsung});
        SendActivity.AddText(s);
        return true;
    }


    public static boolean case_alphabet(String Braille)
    {
        char c = BC.getEnglishCharacter(Braille);
        if(c == ' ')
            return false;
        else
        {
            CompositionResult += c; //추가
            String s = ""+c;
            SendActivity.AddChosung(s);
            return true;
        }
    }

    public static boolean case_number(String Braille)
    {
        char c = BC.getNumericCharacter(Braille);
        if(c == ' ')
            return false;
        else
        {
            CompositionResult += c; //추가
            String s = ""+c;
            SendActivity.AddChosung(s);
            return true;
        }
    }

    private static boolean tryCreateMultipleJongsung(String braille) {
        Character c = BC.getHangulCharacter(braille);
        String str = jongsung + c.toString();
        switch (str) {
            case "ㄱㄱ":
                jongsung = 'ㄲ';
                break;
            case "ㄷㄷ":
                jongsung = 'ㄲ';
                break;
            case "ㄱㅅ":
                jongsung = 'ㄳ';
                break;
            case "ㄴㅈ":
                jongsung = 'ㄵ';
                break;
            case "ㄴㅎ":
                jongsung = 'ㄶ';
                break;
            case "ㄹㄱ":
                jongsung = 'ㄺ';
                break;
            case "ㄹㅁ":
                jongsung = 'ㄻ';
                break;
            case "ㄹㅂ":
                jongsung = 'ㄼ';
                break;
            case "ㄹㅅ":
                jongsung = 'ㄽ';
                break;
            case "ㄹㅌ":
                jongsung = 'ㄾ';
                break;
            case "ㄹㅍ":
                jongsung = 'ㄿ';
                break;
            case "ㄹㅎ":
                jongsung = 'ㅀ';
                break;
            case "ㅂㅅ":
                jongsung = 'ㅄ';
                break;
            case "ㅅㅅ":
                jongsung = 'ㅆ';
                break;
            default:
                //SendMessageActivity.addLastText(_jongsung);
                return false;
        }
        //SendMessageActivity.addLastText(_jongsung);
        return true;
    }

    // 디폴트는 한글 입력모듈로 보낼까? 아님 여기서 다 처리할까?
    //

}
*/