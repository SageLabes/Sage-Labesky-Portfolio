package hw3;

//custom exception to handle incorrect avatar inputs
public class BadAvatarException extends Exception {
	public BadAvatarException(String message) {
		super(message);
	}
}
