package clueGame;

public class BadConfigFormatException extends Exception {
    public BadConfigFormatException() {
        super("Bad configuration format.");
    }
    
    public BadConfigFormatException(String param) {
    	super(param);
    }
}
