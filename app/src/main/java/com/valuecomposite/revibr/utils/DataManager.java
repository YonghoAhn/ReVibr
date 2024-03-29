package com.valuecomposite.revibr.utils;

import com.valuecomposite.revibr.utils.Messages.SMSItem;

import java.util.ArrayList;
import java.util.HashMap;


public class DataManager {
    public static ArrayList<PhoneBookItem> PBItems = new ArrayList<>();
    public static boolean IsEarphoneConnected = false;
    //public static Context mContext;
    public static ArrayList<SMSItem> NewMMSItems = new ArrayList<>();
    public static SMSItem CurrentSMS = new SMSItem();
    public static int VibrateMode = 1;

    public static int MODE = 0; //0 = Send Mode, 1 = Search Mode

    //한글 초중종성 딕셔너리
    public static HashMap<String, Character> HANGUL_FIRST_SOUND = new HashMap<>();
    public static HashMap<String, Character> HANGUL_MIDDLE_SOUND = new HashMap<>();
    public static HashMap<String, Character> HANGUL_LAST_SOUND = new HashMap<>();
    private static HashMap<String, String> HANGUL_EXPRESS_KEYWORD = new HashMap<>();

    //숫자 딕셔너리
    public static HashMap<String, Character> NUMBER = new HashMap<>();

    //알파벳 딕셔너리
    public static HashMap<String, Character> ALPHABET = new HashMap<>();

    //특수문자 딕셔너리
    //public static HashMap<String, String> SPECIAL = new HashMap<>();

    //Static 상수들
    //public static final String WHITE_SPACE = "000000";
    //public static final String DOUBLE_CHAR = "000001";
    //public static final String ALPHABET_SIGN = "001011";

    static    {
        //한글 초성
        HANGUL_FIRST_SOUND.put("000100", 'ㄱ');
        HANGUL_FIRST_SOUND.put("100100", 'ㄴ');
        HANGUL_FIRST_SOUND.put("010100", 'ㄷ');
        HANGUL_FIRST_SOUND.put("000010", 'ㄹ');
        HANGUL_FIRST_SOUND.put("100010", 'ㅁ');
        HANGUL_FIRST_SOUND.put("000110", 'ㅂ');
        HANGUL_FIRST_SOUND.put("000001", 'ㅅ');
        HANGUL_FIRST_SOUND.put("110110", 'ㅇ');
        HANGUL_FIRST_SOUND.put("000101", 'ㅈ');
        HANGUL_FIRST_SOUND.put("000011", 'ㅊ');
        HANGUL_FIRST_SOUND.put("110100", 'ㅋ');
        HANGUL_FIRST_SOUND.put("110010", 'ㅌ');
        HANGUL_FIRST_SOUND.put("100110", 'ㅍ');
        HANGUL_FIRST_SOUND.put("010110", 'ㅎ');
        //한글 중성
        HANGUL_MIDDLE_SOUND.put("110001", 'ㅏ');
        HANGUL_MIDDLE_SOUND.put("001110", 'ㅑ');
        HANGUL_MIDDLE_SOUND.put("011100", 'ㅓ');
        HANGUL_MIDDLE_SOUND.put("100011", 'ㅕ');
        HANGUL_MIDDLE_SOUND.put("101001", 'ㅗ');
        HANGUL_MIDDLE_SOUND.put("001101", 'ㅛ');
        HANGUL_MIDDLE_SOUND.put("101100", 'ㅜ');
        HANGUL_MIDDLE_SOUND.put("100101", 'ㅠ');
        HANGUL_MIDDLE_SOUND.put("010101", 'ㅡ');
        HANGUL_MIDDLE_SOUND.put("101010", 'ㅣ');
        HANGUL_MIDDLE_SOUND.put("010111", 'ㅢ');
        HANGUL_MIDDLE_SOUND.put("101110", 'ㅔ');
        HANGUL_MIDDLE_SOUND.put("111010", 'ㅐ');
        HANGUL_MIDDLE_SOUND.put("001100", 'ㅖ');
        HANGUL_MIDDLE_SOUND.put("111001", 'ㅘ');
        HANGUL_MIDDLE_SOUND.put("111100", 'ㅝ');
        HANGUL_MIDDLE_SOUND.put("101111", 'ㅚ');
        //한글 종성
        HANGUL_LAST_SOUND.put("100000", 'ㄱ');
        HANGUL_LAST_SOUND.put("010010", 'ㄴ');
        HANGUL_LAST_SOUND.put("001010", 'ㄷ');
        HANGUL_LAST_SOUND.put("010000", 'ㄹ');
        HANGUL_LAST_SOUND.put("010001", 'ㅁ');
        HANGUL_LAST_SOUND.put("110000", 'ㅂ');
        HANGUL_LAST_SOUND.put("001000", 'ㅅ');
        HANGUL_LAST_SOUND.put("011011", 'ㅇ');
        HANGUL_LAST_SOUND.put("101000", 'ㅈ');
        HANGUL_LAST_SOUND.put("011000", 'ㅊ');
        HANGUL_LAST_SOUND.put("011010", 'ㅋ');
        HANGUL_LAST_SOUND.put("011001", 'ㅌ');
        HANGUL_LAST_SOUND.put("010011", 'ㅍ');
        HANGUL_LAST_SOUND.put("001011", 'ㅎ');
        HANGUL_LAST_SOUND.put("001100", 'ㅆ');
        //겹받침
        HANGUL_LAST_SOUND.put("100000001000", 'ㄳ');
        HANGUL_LAST_SOUND.put("010010101000", 'ㄵ');
        HANGUL_LAST_SOUND.put("010010001011", 'ㄶ');
        HANGUL_LAST_SOUND.put("010000100000", 'ㄺ');
        HANGUL_LAST_SOUND.put("010000010001", 'ㄻ');
        HANGUL_LAST_SOUND.put("010000110000", 'ㄼ');
        HANGUL_LAST_SOUND.put("010000001000", 'ㄽ');
        HANGUL_LAST_SOUND.put("010000011001", 'ㄾ');
        HANGUL_LAST_SOUND.put("010000010011", 'ㄿ');
        HANGUL_LAST_SOUND.put("010000001011", 'ㅀ');
        HANGUL_LAST_SOUND.put("110000001000", 'ㅄ');
        //약자
        HANGUL_EXPRESS_KEYWORD.put("110101","가");
        HANGUL_EXPRESS_KEYWORD.put("111000","사");
        HANGUL_EXPRESS_KEYWORD.put("100111","가");
        HANGUL_EXPRESS_KEYWORD.put("111111","옹");
        HANGUL_EXPRESS_KEYWORD.put("111101","울");
        HANGUL_EXPRESS_KEYWORD.put("101101","옥");
        HANGUL_EXPRESS_KEYWORD.put("100001","연");
        HANGUL_EXPRESS_KEYWORD.put("110110","운");
        HANGUL_EXPRESS_KEYWORD.put("111011","온");
        HANGUL_EXPRESS_KEYWORD.put("011111","언");
        HANGUL_EXPRESS_KEYWORD.put("011110","얼");
        HANGUL_EXPRESS_KEYWORD.put("110011","열");
        HANGUL_EXPRESS_KEYWORD.put("111110","인");
        HANGUL_EXPRESS_KEYWORD.put("110111","영");
        HANGUL_EXPRESS_KEYWORD.put("011101","을");
        HANGUL_EXPRESS_KEYWORD.put("101011","은");

        HANGUL_EXPRESS_KEYWORD.put("011100","것");
        HANGUL_EXPRESS_KEYWORD.put("100100","그러나");
        HANGUL_EXPRESS_KEYWORD.put("010010","그러면");
        HANGUL_EXPRESS_KEYWORD.put("011100","그래서");
        HANGUL_EXPRESS_KEYWORD.put("101110","그런데");
        HANGUL_EXPRESS_KEYWORD.put("010001","그러므로");
        HANGUL_EXPRESS_KEYWORD.put("101001","그리고");
        HANGUL_EXPRESS_KEYWORD.put("100011","그리하여");
        //숫자
        NUMBER.put("010110",'0');
        NUMBER.put("100000",'1');
        NUMBER.put("110000",'2');
        NUMBER.put("100100",'3');
        NUMBER.put("100110",'4');
        NUMBER.put("100010",'5');
        NUMBER.put("110100",'6');
        NUMBER.put("110110",'7');
        NUMBER.put("110010",'8');
        NUMBER.put("010100",'9');
        //알파벳
        ALPHABET.put("100000",'a');
        ALPHABET.put("110000",'b');
        ALPHABET.put("100100",'c');
        ALPHABET.put("100110",'d');
        ALPHABET.put("100010",'e');
        ALPHABET.put("110100",'f');
        ALPHABET.put("110110",'g');
        ALPHABET.put("110010",'h');
        ALPHABET.put("010100",'i');
        ALPHABET.put("010110",'j');
        ALPHABET.put("101000",'k');
        ALPHABET.put("111000",'l');
        ALPHABET.put("101100",'m');
        ALPHABET.put("101110",'n');
        ALPHABET.put("101010",'o');
        ALPHABET.put("111100",'p');
        ALPHABET.put("111110",'q');
        ALPHABET.put("111010",'r');
        ALPHABET.put("011100",'s');
        ALPHABET.put("011110",'t');
        ALPHABET.put("101001",'u');
        ALPHABET.put("111001",'v');
        ALPHABET.put("010111",'w');
        ALPHABET.put("101101",'x');
        ALPHABET.put("101111",'y');
        ALPHABET.put("101011",'z');
        //특수문자들

    }
}
