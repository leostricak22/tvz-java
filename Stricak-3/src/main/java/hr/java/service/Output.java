package hr.java.service;

public class Output {
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


    public static void tabulatorPrint(Integer tabulators) {
        for (int i = 0; i < tabulators; i++) {
            System.out.print("|\t");
        }
    }
}
