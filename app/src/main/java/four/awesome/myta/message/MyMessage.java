package four.awesome.myta.message;

/**
 * Created by applecz on 2018/1/4.
 */

public class MyMessage {
    String msgStr;
    int msgInt;
    public MyMessage(String msg) {
        this.msgStr = msg;
        msgInt = -1;
    }
    public MyMessage(int msg) {
        this.msgInt = msg;
        msgStr = "";
    }
    public MyMessage(String msgStr, int msgInt) {
        this.msgInt = msgInt;
        this.msgStr = msgStr;
    }
    public int getMsgInt() {
        return msgInt;
    }
    public String getMsgStr() {
        return msgStr;
    }
}
