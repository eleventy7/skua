package io.skua.configurationservice.server.protocol;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

@SuppressWarnings("unused")
public class QuillServiceRegisteredEvent
{
    /**
     * The wire protocol id for this type. Useful in switch statements to detect type in first 16bits.
     */
    public static final short WIRE_PROTOCOL_ID = 101;
    /**
     * The total bytes required to store this fixed length object.
     */
    public static final int BUFFER_LENGTH = 21;
    /**
     * Indicates if this flyweight holds a fixed length object.
     */
    public static final boolean FIXED_LENGTH = true;
    /**
     * The wire protocol version for this type.
     */
    public static final short WIRE_PROTOCOL_VERSION = 1;
    /**
     * The offset for the message length within the buffer.
     */
    private static final int MESSAGE_LENGTH_OFFSET = 0;
    /**
     * The offset for the encoding type within the buffer.
     */
    private static final int EIDER_WIRE_ENCODING_TYPE_OFFSET = 4;
    /**
     * The offset for the WIRE_PROTOCOL_ID within the buffer.
     */
    private static final int PROTOCOL_ID_OFFSET = 6;
    /**
     * The offset for the WIRE_PROTOCOL_VERSION within the buffer.
     */
    private static final int HEADER_VERSION_OFFSET = 8;
    /**
     * The byte offset in the byte array for this LONG. Byte length is 8.
     */
    private static final int CORRELATIONID_OFFSET = 10;
    /**
     * The byte offset in the byte array for this BOOLEAN. Byte length is 1.
     */
    private static final int SUCCESS_OFFSET = 18;
    /**
     * The byte offset in the byte array for this SHORT. Byte length is 2.
     */
    private static final int STATUSCODE_OFFSET = 19;
    /**
     * The internal DirectBuffer.
     */
    private DirectBuffer buffer = null;

    /**
     * The internal DirectBuffer used for mutatation opertions. Valid only if a mutable buffer was provided.
     */
    private MutableDirectBuffer mutableBuffer = null;

    /**
     * The internal UnsafeBuffer. Valid only if an unsafe buffer was provided.
     */
    private UnsafeBuffer unsafeBuffer = null;

    /**
     * The starting offset for reading and writing.
     */
    private int initialOffset;

    /**
     * Flag indicating if the buffer is mutable.
     */
    private boolean isMutable = false;

    /**
     * Flag indicating if the buffer is an UnsafeBuffer.
     */
    private boolean isUnsafe = false;

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
        if (buffer instanceof UnsafeBuffer)
        {
            unsafeBuffer = (UnsafeBuffer)buffer;
            mutableBuffer = (MutableDirectBuffer)buffer;
            isUnsafe = true;
            isMutable = true;
        }
        else if (buffer instanceof MutableDirectBuffer)
        {
            mutableBuffer = (MutableDirectBuffer)buffer;
            isUnsafe = false;
            isMutable = true;
        }
        else
        {
            isUnsafe = false;
            isMutable = false;
        }
        buffer.checkLimit(initialOffset + BUFFER_LENGTH);
    }

    /**
     * Returns the wire protocol id.
     *
     * @return WIRE_PROTOCOL_ID.
     */
    public short wireProtocolId()
    {
        return WIRE_PROTOCOL_ID;
    }

    /**
     * Writes the header data to the buffer.
     */
    public void writeHeader()
    {
        if (!isMutable)
        {
            throw new RuntimeException("cannot write to immutable buffer");
        }
        mutableBuffer.putInt(initialOffset + MESSAGE_LENGTH_OFFSET, BUFFER_LENGTH, java.nio.ByteOrder.LITTLE_ENDIAN);
        mutableBuffer.putShort(initialOffset + EIDER_WIRE_ENCODING_TYPE_OFFSET, (short)43,
            java.nio.ByteOrder.LITTLE_ENDIAN);
        mutableBuffer.putShort(initialOffset + PROTOCOL_ID_OFFSET, WIRE_PROTOCOL_ID, java.nio.ByteOrder.LITTLE_ENDIAN);
        mutableBuffer.putShort(initialOffset + HEADER_VERSION_OFFSET, WIRE_PROTOCOL_VERSION,
            java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Validates the length and wireProtocolId in the header against the expected values. False if invalid.
     */
    public boolean validateHeader()
    {
        final int bufferLength = buffer.getInt(initialOffset + MESSAGE_LENGTH_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        final int encodingType =
            buffer.getShort(initialOffset + EIDER_WIRE_ENCODING_TYPE_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        final short wireProtocolId =
            buffer.getShort(initialOffset + PROTOCOL_ID_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        final short wireProtocolVersion =
            buffer.getShort(initialOffset + HEADER_VERSION_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
        if (encodingType != EIDER_WIRE_ENCODING_TYPE_OFFSET)
        {
            return false;
        }
        if (wireProtocolId != WIRE_PROTOCOL_ID)
        {
            return false;
        }
        if (wireProtocolVersion != WIRE_PROTOCOL_VERSION)
        {
            return false;
        }
        return bufferLength == BUFFER_LENGTH;
    }

    /**
     * Reads correlationId as stored in the buffer.
     */
    public long readCorrelationId()
    {
        return buffer.getLong(initialOffset + CORRELATIONID_OFFSET, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Writes correlationId to the buffer. Returns true if success, false if not.
     *
     * @param value Value for the correlationId to write to buffer.
     */
    public boolean writeCorrelationId(final long value)
    {
        if (!isMutable)
        {
            throw new RuntimeException("Cannot write to immutable buffer");
        }
        mutableBuffer.putLong(initialOffset + CORRELATIONID_OFFSET, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return true;
    }

    /**
     * Reads success as stored in the buffer.
     */
    public boolean readSuccess()
    {
        return buffer.getByte(initialOffset + SUCCESS_OFFSET) == (byte)1;
    }

    /**
     * Writes success to the buffer. Returns true if success, false if not.
     *
     * @param value Value for the success to write to buffer.
     */
    public boolean writeSuccess(final boolean value)
    {
        if (!isMutable)
        {
            throw new RuntimeException("Cannot write to immutable buffer");
        }
        mutableBuffer.putByte(initialOffset + SUCCESS_OFFSET, value ? (byte)1 : (byte)0);
        return true;
    }

    /**
     * Reads statusCode as stored in the buffer.
     */
    public short readStatusCode()
    {
        return buffer.getShort(initialOffset + STATUSCODE_OFFSET);
    }

    /**
     * Writes statusCode to the buffer. Returns true if success, false if not.
     *
     * @param value Value for the statusCode to write to buffer.
     */
    public boolean writeStatusCode(final short value)
    {
        if (!isMutable)
        {
            throw new RuntimeException("Cannot write to immutable buffer");
        }
        mutableBuffer.putShort(initialOffset + STATUSCODE_OFFSET, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return true;
    }

    /**
     * Uses the provided {@link DirectBuffer} from the given offset.
     *
     * @param buffer - buffer to read from and write to.
     * @param offset - offset to begin reading from/writing to in the buffer.
     */
    public void setBufferWriteHeader(final DirectBuffer buffer, final int offset)
    {
        setUnderlyingBuffer(buffer, offset);
        writeHeader();
    }
}
