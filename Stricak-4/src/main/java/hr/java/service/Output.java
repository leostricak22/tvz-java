package hr.java.service;

/**
 * Represents an output service.
 */
public class Output {
    /**
     * Prints a message.
     * @param nameOptions The name options.
     * @return The object name options.
     */
    public static String objectNameOptions(String[] nameOptions) {
        String messageExtend = "";
        if(nameOptions.length > 0) {
            messageExtend += " - (";
            for (int i = 0; i < nameOptions.length; i++) {
                messageExtend += nameOptions[i];

                if (i + 1 != nameOptions.length)
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
