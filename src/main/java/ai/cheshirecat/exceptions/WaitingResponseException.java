package ai.cheshirecat.exceptions;

public class WaitingResponseException extends RuntimeException {

    public WaitingResponseException() {
        super("Cannot send a message if we are still waiting for a response from the server");
    }
}
