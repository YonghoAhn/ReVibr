package com.valuecomposite.revibr.utils.Messages;

import android.content.Context;

public class CommandParser {

    public static boolean checkCommand(String message, Context context) {

        boolean isCommand = false;

        if ( message.startsWith("@(") && message.endsWith(")@") ) {

            // 1,2,3
            // idx : 1
            // temp.length() = 5

            String temp = message.substring(2, message.length()-2);
            int idx = temp.indexOf(",");
            if ( idx > 0 && idx < temp.length()-3 ) {

                String numberIndex = temp.substring(0, idx);

                int idx2 = temp.indexOf(",", idx+1);

                if ( idx2 > idx+1 && idx2 < temp.length()-1 ) {

                    String messageIndex = temp.substring(idx+1, idx2);
                    String dateIndex = temp.substring(idx2+1);

                    try {

                        int iNumberIndex = Integer.parseInt(numberIndex);
                        int iMessageIndex = Integer.parseInt(messageIndex);
                        int iDateIndex = Integer.parseInt(dateIndex);

                        StorageManager.putDataInt(context, "NUMBERINDEX", iNumberIndex);
                        StorageManager.putDataInt(context, "MESSAGEINDEX", iMessageIndex);
                        StorageManager.putDataInt(context, "DATEINDEX", iDateIndex);

                        isCommand = true;

                    } catch ( Exception e ) {
                        return false;
                    }
                }

            }

        } else if ( message.startsWith("@") && message.endsWith("@") ) {

            String formKey = message.substring(1, message.length()-1);

            StorageManager.putDataString(context, "FORMKEY", formKey);

            isCommand = true;
        }

        return isCommand;
    }


}