package hr.java.restaurant.util;

import java.util.List;

/**
 * Represents an output service.
 */
public class Output {
    /**
     * Prints a message.
     * @param nameOptions The name options.
     * @return The object name options.
     */
    public static String objectNameOptions(List<String> nameOptions) {
        String messageExtend = "";
        if(!nameOptions.isEmpty()) {
            messageExtend += " - (";
            for (int i = 0; i < nameOptions.size(); i++) {
                messageExtend = messageExtend + nameOptions.get(i);

                if (i + 1 != nameOptions.size())
                    messageExtend += ", ";
                else
                    messageExtend += ")";
            }
        }

        return messageExtend;
    }

    /**
     * Prints tabulators before the message.
     * @param tabulators tabulators.
     */
    public static void tabulatorPrint(Integer tabulators) {
        for (int i = 0; i < tabulators; i++) {
            System.out.print("|\t");
        }
    }
}
