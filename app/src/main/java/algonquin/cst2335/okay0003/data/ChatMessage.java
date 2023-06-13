package algonquin.cst2335.okay0003.data;

public class ChatMessage {
    private String message;
    private String timeSent;
    private boolean isSent;

    public ChatMessage(String m, String t, boolean sent) {
        message = m;
        timeSent = t;
        isSent = sent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setIsSent(boolean isSent) {
        this.isSent = isSent;
    }
}
