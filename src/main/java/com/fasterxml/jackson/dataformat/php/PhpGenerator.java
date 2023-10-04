/*
 * Copyright 2013 Photobucket 
 */
package com.fasterxml.jackson.dataformat.php;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.JsonWriteContext;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpGenerator extends GeneratorBase {
    private static final String DELIM = ":";

    private static final String EOR = ";";

    final protected Writer writer;

    private PhpWriteContext _ctx;
    
    PhpGenerator(final IOContext ignoredCtxt, final ObjectCodec codec, final Writer out) {
         super(0, codec);
         writer = out;
         _ctx = PhpWriteContext.createRootContext(writer);
    }

    /*
    /**********************************************************
    /* Overridden configuration methods
    /**********************************************************
     */
    @Override
    public Object getOutputTarget() {
        return writer;
    }


    /*
    /**********************************************************
    /* Overridden methods
    /**********************************************************
     */

    @Override
    public void writeFieldName(final String name)  throws IOException {
        int status = _ctx.writeFieldName(name);
        if (status == JsonWriteContext.STATUS_EXPECT_VALUE) {
            _reportError("Can not write a field name, expecting a value");
        }
        writeString(name);
    }

    @Override
    public void writeFieldName(final SerializableString name)
        throws IOException {
        writeFieldName(name.getValue());
    }
    
    /*
    /**********************************************************
    /* Output method implementations, structural
    /**********************************************************
     */

    @Override
    public void writeStartArray() {
        _verifyValueWrite("start an array");
        _ctx = _ctx.createChildArrayContext();
        //Can't actually write anything here because we need to know how many fields this array will have
    }

    @Override
    public void writeEndArray() throws IOException {
        if (!_ctx.inArray()) {
            _reportError("Current context not an ARRAY but "+_ctx.typeDesc());
        }
        _flushArray();
    }

    @Override
    public void writeStartObject() {
        _ctx = _ctx.createChildObjectContext();
        //Can't actually write anything here because we need to know how many fields this array will have
    }

    @Override
    public void writeEndObject() throws IOException {
        if (!_ctx.inObject()) {
            _reportError("Current context not an object but "+_ctx.typeDesc());
        }
        _flushArray();
    }

    private void _flushArray() throws IOException {
        //can't actually write an object back to PHP.  what would we call it?  Just use an associative array instead
        Writer writer = _ctx.getParent().getWriter();
        writer.append("a");
        writer.append(DELIM);
        writer.append(Integer.toString(_ctx.getFieldCount()));
        writer.append(DELIM);
        writer.append("{");
        writer.append(_ctx.getWriter().toString());
        writer.append("}");
        _ctx = _ctx.getParent();
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    protected void _releaseBuffers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _verifyValueWrite(final String typeMsg) {
        //?
    }

    @Override
    public void writeString(final String text) throws IOException {
        if(_ctx.inArray()) {
            writeIndex(_ctx.getFieldCount());
            _ctx.incrementFieldCount();
        }
        _ctx.append("s");
        _ctx.append(DELIM);
        _ctx.append(Integer.toString(text.length()));
        _ctx.append(DELIM);
        _ctx.append("\"");
        _ctx.append(text);
        _ctx.append("\"");
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    @Override
    public void writeString(final char[] text, final int offset, final int len) throws IOException {
        writeString(new String(text).substring(offset, len));
    }

    @Override
    public void writeRawUTF8String(final byte[] text, final int offset, final int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeUTF8String(final byte[] text, final int offset, final int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRaw(final String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRaw(final String text, final int offset, final int len) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRaw(final char[] text, final int offset, final int len) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRaw(final char c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeBinary(final Base64Variant b64variant, final byte[] data, final int offset, final int len) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(final int v) throws IOException {
        writeInt(v);
    }

    @Override
    public void writeNumber(final long v) throws IOException {
        writeInt(v);
    }

    @Override
    public void writeNumber(final BigInteger v) throws IOException {
        writeInt(v);
    }

    private void writeInt(final Number v) throws IOException {
        if(_ctx.inArray()) {
            writeIndex(_ctx.getFieldCount());
            _ctx.incrementFieldCount();
        }
        _ctx.append("i");
        _ctx.append(DELIM);
        _ctx.append(v.toString());
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    private void writeIndex(final Integer i) throws IOException {
        _ctx.append("i");
        _ctx.append(DELIM);
        _ctx.append(i.toString());
        _ctx.append(EOR);
    }

    @Override
    public void writeNumber(final double d) throws IOException {
        writeDouble(d);
    }

    @Override
    public void writeNumber(final float f) throws IOException {
        writeDouble(f);
    }

    @Override
    public void writeNumber(final BigDecimal dec) throws IOException {
        writeDouble(dec);
    }

    private void writeDouble(final Number v) throws IOException {
        if(_ctx.inArray()) {
            writeIndex(_ctx.getFieldCount());
            _ctx.incrementFieldCount();
        }
        _ctx.append("d");
        _ctx.append(DELIM);
        _ctx.append(v.toString());
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    @Override
    public void writeNumber(final String encodedValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeBoolean(final boolean state) throws IOException {
        if(_ctx.inArray()) {
            writeIndex(_ctx.getFieldCount());
            _ctx.incrementFieldCount();
        }
        _ctx.append("b");
        _ctx.append(DELIM);
        _ctx.append(state ? "1" : "0");
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    @Override
    public void writeNull() throws IOException {
        _ctx.append("N");
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
    }

}
