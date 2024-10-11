package clueGame;

public class BadConfigFormatException extends Exception {
    public BadConfigFormatException(String message) {
        super(message);
    }

    public BadConfigFormatException() {
        super("Bad configuration format in the setup or layout file.");
    }
}
