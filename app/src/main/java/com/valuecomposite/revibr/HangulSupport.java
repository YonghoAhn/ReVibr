package com.valuecomposite.revibr;

/**
 * Created by ayh07 on 8/12/2017.
 */

public class HangulSupport {
    //이 클래스는
    //어떤 문자가 한글인지 판별하고,
    //초, 중, 종성을 결합하여 한 문자로 만드는 클래스
    //특정 문자가 한글인지 판별

    //초, 중, 종성 딕셔너리
    private static final char[] FirstSound = {
            'ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'
    };
    private static final char[] MiddleSound = {
            'ㅏ','ㅐ','ㅑ','ㅒ','ㅓ','ㅔ','ㅕ','ㅖ','ㅗ','ㅘ','ㅙ','ㅚ','ㅛ','ㅜ','ㅝ','ㅞ','ㅟ','ㅠ','ㅡ','ㅢ','ㅣ'
    };
    private static final char[] LastSound = {
            ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ',
            'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
            'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public static boolean IsHangul(char c)
    {
        if(c<0xAC00||c>0xD743) //유니코드로 한글 범위 내인지 판별
        {
            return false;
        }
        return true;
    }
    //문자열 전체가 한글인지 판별
    public static boolean IsHangul(String s)
    {
        if(s==null)
            return false;
        int len = s.length();
        for(int i = 0; i < len; i++)
        {
            if(!IsHangul(s.charAt(i)))
                return false;
        }
        return true;
    }

    //한글 자모음자
    public static char CombineHangul(char[] c)
    {
        int[] hindex = new int[3];

        for(int i = 0;i<FirstSound.length;i++)
        {
            if(FirstSound[i] == c[0]){
                hindex[0] = i;
                break;
            }
        }
        for(int i = 0;i<MiddleSound.length;i++)
        {
            if(MiddleSound[i] == c[1]){
                hindex[1] = i;
                break;
            }
        }
        for(int i = 0; i<LastSound.length;i++)
        {
            if(LastSound[i] == c[2]) {
                hindex[2] = i;
                break;
            }
            if(i == LastSound.length - 1)
                hindex[2] = 0;
        }

        char[] chars = new char[1];
        chars[0] = (char) (0xAC00 + (hindex[0]*21*28) + (hindex[1]*28) + hindex[2]);
        return chars[0];
    }
}
