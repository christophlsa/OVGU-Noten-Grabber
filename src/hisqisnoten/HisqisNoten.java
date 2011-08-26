package hisqisnoten;

public class HisqisNoten {
    
    public static void main(String[] args) {
        String user = null;
        String pass = null;

        if (args.length == 2) {
            user = args[0];
            pass = args[1];
        }

        new HisqisConsole(user, pass);
    }
}
