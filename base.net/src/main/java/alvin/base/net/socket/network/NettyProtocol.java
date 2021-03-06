package alvin.base.net.socket.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.function.Consumer;

import alvin.base.net.socket.common.commands.Command;
import alvin.base.net.socket.common.commands.CommandAck;
import alvin.lib.common.exceptions.Throwables;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyProtocol {
    private static final int CHECK_SUM_SIZE = 16;

    private final ObjectMapper objectMapper;

    private int length = -1;
    private byte[] checksum = null;
    private byte[] message = null;

    public NettyProtocol(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private void reset() {
        this.length = -1;
        this.checksum = null;
        this.message = null;
    }

    ByteBuf makeCommandPackage(Command cmd) {
        try {
            String msg = objectMapper.writeValueAsString(cmd);
            return packMessage(msg);
        } catch (JsonProcessingException ignore) {
            throw new AssertionError(ignore);
        }
    }

    private static byte[] calculateChecksum(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw Throwables.propagate(e);
        }
    }

    private static ByteBuf packMessage(String msg) {
        final byte[] msgData = msg.getBytes(Charsets.UTF_8);

        final ByteBuf buffer = Unpooled.buffer(Integer.BYTES + CHECK_SUM_SIZE + msgData.length);
        buffer.writeInt(msgData.length);
        buffer.writeBytes(calculateChecksum(msgData));
        buffer.writeBytes(msgData);
        return buffer;
    }

    public ByteBuf unpack(ByteBuf buffer, Consumer<CommandAck> onSuccess, Consumer<Throwable> onError) {
        try {
            if (length < 0 && buffer.readableBytes() >= Integer.BYTES) {
                int length = buffer.readInt();
                buffer = buffer.slice();
                this.length = length;
            }

            if (length > 0 && checksum == null && buffer.readableBytes() >= CHECK_SUM_SIZE) {
                byte[] checksum = new byte[CHECK_SUM_SIZE];
                buffer.readBytes(checksum);
                buffer = buffer.slice();
                this.checksum = checksum;
            }

            if (length > 0 && checksum != null && message == null && buffer.readableBytes() >= length) {
                byte[] message = new byte[length];
                buffer.readBytes(message);
                buffer = buffer.slice();

                byte[] checksumVerifyData = calculateChecksum(message);
                if (Arrays.equals(checksum, checksumVerifyData)) {
                    this.message = message;
                    onSuccess.accept(objectMapper.readValue(new String(this.message, Charsets.UTF_8), CommandAck.class));
                    reset();
                } else {
                    onError.accept(new IOException("Checksum invalid"));
                }
            }
        } catch (Exception e) {
            onError.accept(e);
        }
        return buffer;
    }
}
