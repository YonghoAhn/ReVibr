package com.valuecomposite.revibr.utils;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ayh07 on 8/13/2017.
 */

public class BrailleOutput {
//
    // 이 클래스는 점자를 문자로 변경 또는 출력하는 것이 목적이다.
    // BrailleConverter에 Access해서 점자를 가져오고, 진동으로 변환하는 것이 목적이다.
    //
    //3점 단위로 나눠서 판단한다.
    private static String Braille = "";
    static BrailleConverter BC = new BrailleConverter();

    public static ArrayList<String> parseSMS(String Braille)
    {
        ArrayList<String> BrailleContent = new ArrayList<>();
        boolean isEnglish = false;
        boolean isNumeric = false;
        char[] chars = Braille.toCharArray();
        Log.d("MisakaMOE","Content : " + Character.toString(chars[0]));
        Log.d("MisakaMOE","Content : " + new String("" + chars[0]).matches(".*[a-z|A-Z]"));
        for(int i = 0; i < chars.length;i++) {
            String str = Character.toString(chars[i]);
            try {

                if (new String("" + chars[0]).matches(".*[a-z|A-Z]")) //Is it English
                {
                    isNumeric = false;
                    if (!isEnglish) {
                        BrailleContent.add("001");
                        BrailleContent.add("011");
                        isEnglish = true;
                    }
                    String s = BC.getEnglishBraille(chars[i]);
                    BrailleContent.add(s.substring(0, 3));
                    BrailleContent.add(s.substring(3, 6));
                } else if (Character.isDigit(chars[i])) //Is It Numeric
                {
                    isEnglish = false;
                    if (!isNumeric) {
                        BrailleContent.add("001");
                        BrailleContent.add("111");
                    }
                    String s = BC.getNumericBraille(chars[i]);
                    BrailleContent.add(s.substring(0, 3));
                    BrailleContent.add(s.substring(3, 6));
                } else //Is It Korean Or Special Text Or Space
                {
                    isEnglish = false;
                    isNumeric = false;
                    if (!new String("" + chars[0]).matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) //특수문자면
                    {
                        //Ignore It
                    } else if (chars[i] == ' ') {
                        //Space, 000 000 Add
                        BrailleContent.add("000");
                        BrailleContent.add("000");
                    } else //Hangul
                    {
                        //한글자만 있는것들
                        //초성 아니면 중성 단독이면 그냥 처리
                        //결합된 조합자면 분리 후 처리
                        if (str.matches(".*[ㄱ-ㅎ]")) //초성류
                        {

                            if (chars[i] == 'ㅇ') {

                                BrailleContent.add("110");
                                BrailleContent.add("110");
                            } else {

                                if (chars[i] == 'ㄲ' || chars[i] == 'ㄸ' || chars[i] == 'ㅃ' || chars[i] == 'ㅆ' || chars[i] == 'ㅉ') {
                                    BrailleContent.add("000");
                                    BrailleContent.add("001");
                                    switch (chars[i]) {
                                        case 'ㄲ':
                                            chars[i] = 'ㄱ';
                                            break;
                                        case 'ㄸ':
                                            chars[i] = 'ㄷ';
                                            break;
                                        case 'ㅃ':
                                            chars[i] = 'ㅂ';
                                            break;
                                        case 'ㅆ':
                                            chars[i] = 'ㅅ';
                                            break;
                                        case 'ㅉ':
                                            chars[i] = 'ㅈ';
                                            break;
                                    }
                                }
                                String s = BC.getKoreanBraille(chars[i], 0);

                                BrailleContent.add(s.substring(0, 3));
                                BrailleContent.add(s.substring(3, 6));
                            }
                        } else if (str.matches(".*[ㅏ-ㅣ]")) {

                            String s = BC.getKoreanBraille(chars[i], 1);

                            BrailleContent.add(s.substring(0, 3));
                            BrailleContent.add(s.substring(3, 6));
                        } else {
                            char[] hangul = HangulSupport.HangulAlphabet(chars[i]);
                            int cnt = 0;
                            for (char c1 : hangul) {
                                if (c1 == 'ㅇ' && cnt == 0) {

                                    BrailleContent.add("110");
                                    BrailleContent.add("110");
                                } else {

                                    String s = BC.getKoreanBraille(c1, cnt);

                                    BrailleContent.add(s.substring(0, 3));
                                    BrailleContent.add(s.substring(3, 6));
                                }
                                cnt++;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("MisakaMOE", "Error is " + e.getMessage().toString());
                //Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        }
        return BrailleContent;
    }
}
