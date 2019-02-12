package antenna.direction.boundary;

import java.util.Arrays;

public class ElevationBasicFeedbackResponse {
    private Message message;

    ElevationBasicFeedbackResponse(Message message) {
        assert message.isNak() || message.isAck();
        assert message.getData()[2] == 'r' && message.getData()[3] == 'e';
        this.message = message;
    }

    double getElevation() {
        assert message.isAck();
        byte[] data = message.getData();
        byte[] elevation = Arrays.copyOfRange(data, 4, 12);
        String elevationString = new String(elevation);
        return Double.parseDouble(elevationString);
    }

    boolean isNak() {
        return message.isNak();
    }

    byte getNakStatus() {
        return message.getNakStatus();
    }
}
