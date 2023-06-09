package web;


/**
 * An interface to form HTTP responses.
 *
 * @author Ricky Qin
 */
public interface Response {

    /**
     * Forms an HTTP response
     *
     * @param request The HTTP request
     * @return A byte array to write to an OutputStream
     */
    public byte[] createResponse(Request request, Server server);
}
