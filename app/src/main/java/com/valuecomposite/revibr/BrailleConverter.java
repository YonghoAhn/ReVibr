package com.valuecomposite.revibr;

import java.util.Map;

/**
 * Created by ayh07 on 8/12/2017.
 */

//점자 입력된 데이터를 글자로, 또는 글자를 점자로 바꿔서 제공해주는 클래스
public class BrailleConverter {

    //이게 어디있는건지 가져옴 (한글만 해당)
    //초성 : 1
    //중성 : 2
    //종성 : 3
    //해당 없음 : 0
    public int getHangulPosition(String Braille)
    {
        if(DataManager.HANGUL_FIRST_SOUND.containsKey(Braille))
            return 1;
        else if(DataManager.HANGUL_MIDDLE_SOUND.containsKey(Braille))
            return 2;
        else if(DataManager.HANGUL_LAST_SOUND.containsKey(Braille))
            return 3;
        else
            return 0;
    }

    //점자를 바탕으로 영문자를 가져옴
    public char getEnglishCharacter(String Braille)
    {
        if(DataManager.ALPHABET.containsKey(Braille))
            return DataManager.ALPHABET.get(Braille);
        else
            return ' ';
    }
    //점자를 바탕으로 한글 글자를 가져옴
    public char getHangulCharacter(String Braille)
    {
        if(DataManager.HANGUL_FIRST_SOUND.containsKey(Braille))
            return DataManager.HANGUL_FIRST_SOUND.get(Braille);
        else if(DataManager.HANGUL_MIDDLE_SOUND.containsKey(Braille))
            return DataManager.HANGUL_MIDDLE_SOUND.get(Braille);
        else if(DataManager.HANGUL_LAST_SOUND.containsKey(Braille))
            return DataManager.HANGUL_LAST_SOUND.get(Braille);
        else
            return ' ';
    }
    //점자를 바탕으로 숫자를 가져옴
    public char getNumericCharacter(String Braille)
    {
        if(DataManager.NUMBER.containsKey(Braille))
            return DataManager.NUMBER.get(Braille);
        else
            return ' ';
    }

    public String getEnglishBraille(char ch)
    {
        return (String)getKeyFromValue(DataManager.ALPHABET,ch);
    }

    public String getKoreanBraille(char ch, int index)
    {
        switch(index){
            case 0: //초성
                return (String)getKeyFromValue(DataManager.HANGUL_FIRST_SOUND,ch);
            case 1: //중성
                return (String)getKeyFromValue(DataManager.HANGUL_MIDDLE_SOUND,ch);
            case 2: //종성
                return (String)getKeyFromValue(DataManager.HANGUL_LAST_SOUND,ch);
            default:
                return "000000";
        }
    }

    public String getNumericBraille(char ch)
    {
        return (String)getKeyFromValue(DataManager.NUMBER,ch);
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
