package antenna.direction.boundary;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class Message {
    public static final byte STX = 0x02;
    public static final byte ETX = 0x03;
    public static final byte ACK = 0x06;
    public static final byte NAK = 0x15;
    public static final byte ADDR = 0x45;

    public static final Message READ_ELEVATION_COMMAND_MESSAGE = Message.ofChecksum(
            new byte[] { STX, ADDR, 'r', 'e', ETX, 0 }
    );
    public static final Message READ_AZIMUTH_COMMAND_MESSAGE = Message.ofChecksum(
            new byte[] { STX, ADDR, 'r', 'a', ETX, 0 }
    );
    public static final Message READ_POLARIZATION_COMMAND_MESSAGE = Message.ofChecksum(
            new byte[] { STX, ADDR, 'r', 'p', ETX, 0 }
    );

    private byte[] data;

    private Message(byte[] data) {
        this.data = data;
    }

    /**
     * Create Message with specified byte array, the last byte value is replaced by the checksum.
     *
     * @param data message bytes, last item should replaced by the checksum.
     * @return message
     */
    static Message ofChecksum(byte[] data) {
        writeChecksum(data);
        return new Message(data);
    }

    /**
     * Create Message with specified byte array.
     *
     * @param data byte array of message
     * @return message
     */
    static Message of(byte[] data) {
        return new Message(data);
    }

    /**
     * Calculate checksum between index 0 and length -2, then write the sum at length -1.
     *
     * @param data byte array of message
     */
    static void writeChecksum(byte[] data) {
        byte sum = checksum(data, 0, data.length - 2);
        data[data.length - 1] = sum;
    }

    static byte checksum(byte[] data, int startIndex, int endIndex) {
        byte checksum = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            checksum ^= data[i];
        }
        return checksum;
    }

    public static String hexString(byte[] data) {
        return IntStream.range(0, data.length)
                .mapToObj(i -> String.format("%02x", data[i]))
                .collect(joining(" ", "", ""));
    }

    public static String hexString(ByteBuffer buffer) {
        byte[] data = Arrays.copyOf(buffer.array(), buffer.position());
        return hexString(data);
    }

    byte[] getData() {
        return data;
    }

    boolean isAck() {
        return data[0] == ACK;
    }

    boolean isNak() {
        return data[0] == NAK;
    }

    boolean isCommand() {
        return data[0] == STX;
    }

    byte getNakStatus() {
        assert isNak();
        return data[4];
    }

    public String toHexString() {
        return hexString(data);
    }

}
