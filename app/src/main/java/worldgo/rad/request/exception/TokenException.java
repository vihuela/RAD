package worldgo.rad.request.exception;

public class TokenException extends Exception {
    public TokenException() {
        super("token失效");
    }

    public TokenException(String m) {
        super(m);
    }
}
