package antenna.stc110.emulator;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.ByteBuffer;

public class SingleResponse {

    SerialPort port;
    ByteBuffer buffer = ByteBuffer.allocate(256);

    SingleResponse(String id) {
        port = SerialPort.getCommPort(id);
        if (!port.openPort()) {
            System.err.println("SerialPort open ERROR.");
            System.exit(1);
        };
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
    }

    void oneshot() {
        buffer.clear();
        byte[] data = new byte[256];
        read_loop:
        while (true) {
            System.out.println("Waiting data from serial port");
            int numRead = port.readBytes(data, data.length);
            for (int i = 0; i < numRead; i++) {
                System.out.printf("%02x ", data[i]);
                buffer.put(data[i]);
                if (data[i] == 0x03) {
                    break read_loop;
                }
            }
        }
        System.out.println(toStringBuffer(buffer));
        byte[] response;
        byte[] commandCodes = new byte[2];
        commandCodes[0] = buffer.get(2);
        commandCodes[1] = buffer.get(3);
        if (commandCodes[0] == 'r' && commandCodes[1] == 'e') {
            response = new byte[] { 0x06, 0x45, 'r', 'e', '+', '0', '2', '3', '.', '4', '5', '6', 0x3, 0x64 };
        } else if (commandCodes[0] == 'r' && commandCodes[1] == 'a') {
            response = new byte[] { 0x06, 0x45, 'r', 'a', 'a', '$', '-', '1', '7', '6', '.', '2', '4', '6', 0x3, 0x15 };
        } else if (commandCodes[0] == 'r' && commandCodes[1] == 'p') {
            response = new byte[] { 0x06, 0x45, 'r', 'p', '+', '0', '4', '5', '.', '7', '1', '3', 0x3, 0x43 };
        } else {
            response = new byte[] { 0x15, 0x45, commandCodes[0], commandCodes[1], '1', 0x3, 0 };
            response[response.length - 1] = (byte) (0x15 ^ 0x45 ^ commandCodes[0] ^ commandCodes[1] ^ '1' ^ 0x3);
        }
        port.writeBytes(response, response.length);
    }

    static String toStringBuffer(ByteBuffer buffer) {
        buffer.flip();
        StringBuffer sb = new StringBuffer();
        while (buffer.hasRemaining()) {
            sb.append(String.format("%02x ", buffer.get()));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.lastIndexOf(" "));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SingleResponse emu = new SingleResponse("COM4");
        while (true) {
            emu.oneshot();
        }
    }
}
