package com.mygdx.game.businessLayer.others;

public class Constants {


    //TODO nu mai vreau sa vad in fiata mea rexolutie CONSTANTA
    //public final static float WORLD_WIDTH = 800f;
    //public final static float WORLD_HEIGHT = 400f;

    public final static float CARD_HAND_Y = 5;

    public static int fontSize = 32;

    public final static String skinJsonString  = "clean-crispy-ui.json";


    //public final static String serverHTTP = "https://myapp1.serverless.social";
    //public static final String serverHTTP = "http://localhost:8080";


    public final static String serverHTTP = "http://192.168.0.94:8080";

    //public final static String serverHTTP = "http://192.168.56.1:8080";

    //public final static String serverHTTP = "https://terrible-swan-90.loca.lt";

    //public final static String serverHTTP = "https://myapp.serverless.social";



    public static final float HUD_FONT_REFERENCE_SCREEN_SIZE =  480.0f;


    public static String generateNickname(){
        // chose a Character random from this String
        String AlphaNumericString = "abcdefghijklmnopqrstuvxyz";

        int n = 8;
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();

    }
}
