package antenna.direction.boundary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    Message fixture;

    @Test
    void writeChecksum() {
    }

    @Test
    void checksum() {
        byte[] data = { 0x02, 0x45, 'r', 'e', 0x03, 0 };
        assertEquals(0x53, Message.checksum(data, 0, data.length - 2));
    }

    @Test
    void toHexString() {
        byte[] data = { 0x02, 0x45, 'r', 'e', 0x03, 0 };
        assertEquals("02 45 72 65 03 00", Message.hexString(data));
    }
}