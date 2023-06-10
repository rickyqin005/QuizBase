package web;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * An interface to form HTTP responses.
 *
 * @author Ricky Qin
 */
public class HTTPResponse {

    private int status;
    private HashMap<String, String> headerFields = new HashMap<String, String>();
    private byte[] body = new byte[0];

    public HTTPResponse() {
    }

    public HTTPResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * Adds a field to the header (excluding Content-Length)
     * @param name The name
     * @param value The field
     * @return
     */
    public HTTPResponse setHeaderField(String name, String value) {
        headerFields.put(name, value);
        return this;
    }

    public HTTPResponse appendBody(String str) {
        try {
            return appendBody(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HTTPResponse appendBody(byte[] bytes) {
        byte[] newBody = new byte[body.length + bytes.length];
        for(int i = 0; i < body.length; i++) {
            newBody[i] = body[i];
        }
        for(int i = 0; i < bytes.length; i++) {
            newBody[i+body.length] = bytes[i];
        }
        body = newBody;
        return this;
    }

    /**
     * Writes and flushes this HTTP Response to the output stream
     * @param output
     */
    public void writeResponse(DataOutputStream output) {
        try {
            output.writeBytes("HTTP/1.1 " + status + " " + HTTP.getStatusMessage(status) + "\n");
            headerFields.put("Content-Length", String.valueOf(body.length));
            for(Map.Entry<String, String> entry: headerFields.entrySet()) {
                output.writeBytes(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            output.write('\n');
            output.write(body);
            output.flush();
        } catch(IOException e) {
            System.out.println("Error occurred when writing to output stream");
            e.printStackTrace();
        }
    }
}