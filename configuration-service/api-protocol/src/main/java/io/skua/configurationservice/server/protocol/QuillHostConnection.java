package io.skua.configurationservice.server.protocol;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;

@SuppressWarnings("unused")
public class QuillHostConnection
{
    /**
     * The total bytes required to store a single record.
     */
    public static final int BUFFER_LENGTH = 52;
    /**
     * The byte offset in the byte array for this SHORT. Byte length is 2.
     */
    private static final int PORT_OFFSET = 0;
    /**
     * The byte offset in the byte array for this FIXED_STRING. Byte length is 50.
     */
    private static final int HOSTNAME_OFFSET = 2;
    /**
     * The internal DirectBuffer.
     */
    private DirectBuffer buffer = null;

    /**
     * The internal DirectBuffer used for mutatation opertions. Valid only if a mutable buffer was provided.
     */
    private MutableDirectBuffer mutableBuffer = null;

    /**
     * The starting offset for reading and writing.
     */
    private int initialOffset;

    /**
     * Flag indicating if the buffer is mutable.
     */
    private boolean isMutable = false;

    /**
     * Uses the provided {@link DirectBuffer} from the given offset.
     *
     * @param buffer - buffer to read from and write to.
     * @param offset - offset to begin reading from/writing to in the buffer.
     */
    public void setUnderlyingBuffer(final DirectBuffer buffer, final int offset)
    {
        this.initialOffset = offset;
        this.buffer = buffer;
        if (buffer instanceof MutableDirectBuffer)
        {
            mutableBuffer = (MutableDirectBuffer)buffer;
            isMutable = true;
        }
        else
        {
            isMutable = false;
        }
        buffer.checkLimit(initialOffset + BUFFER_LENGTH);
    }

    /**
     * Reads port as stored in the buffer.
     */
    public short readPort()
    {
        return buffer.getShort(initialOffset + PORT_OFFSET);
    }

    /**
     * Writes port to the buffer. Returns true if success, false if not.
     *
     * @param value Value for the port to write to buffer.
     */
    public boolean writePort(final short value)
    {
        if (!isMutable)
        {
            throw new RuntimeException("Cannot write to immutable buffer");
        }
        mutableBuffer.putShort(initialOffset + PORT_OFFSET, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return true;
    }

    /**
     * Reads hostName as stored in the buffer.
     */
    public String readHostName()
    {
        return buffer.getStringWithoutLengthAscii(initialOffset + HOSTNAME_OFFSET, 50).trim();
    }

    /**
     * Writes hostName to the buffer. Returns true if success, false if not.Warning! Does not pad the string.
     *
     * @param value Value for the hostName to write to buffer.
     */
    public boolean writeHostName(final String value)
    {
        if (!isMutable)
        {
            throw new RuntimeException("Cannot write to immutable buffer");
        }
        if (value.length() > 50)
        {
            throw new RuntimeException("Field hostName is longer than maxLength=50");
        }
        mutableBuffer.putStringWithoutLengthAscii(initialOffset + HOSTNAME_OFFSET, value);
        return true;
    }

    /**
     * Writes hostName to the buffer with padding.
     *
     * @param value Value for the hostName to write to buffer.
     */
    public boolean writeHostNameWithPadding(final String value)
    {
        final String padded = String.format("%50s", value);
        return writeHostName(padded);
    }
}
