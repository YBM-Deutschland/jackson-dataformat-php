/*
 * Copyright 2013 Photobucket
 */
package com.fasterxml.jackson.dataformat.php;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

/**
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpFactory extends JsonFactory {
    /**
     * Name used to identify Php format.
     * (and returned by {@link #getFormatName()}
     */
    public final static String FORMAT_NAME_PHP = "PHP";

    @Override
    public String getFormatName() {
        return FORMAT_NAME_PHP;
    }

    /**
     * Sub-classes need to override this method (as of 1.8)
     */
    @Override
    public MatchStrength hasFormat(final InputAccessor acc) {
        //TODO: make this correct
        return MatchStrength.FULL_MATCH;
    }
    
    /*
    /**********************************************************
    /* Overridden parser factory methods, 2.1
    /**********************************************************
     */

    @Override
    public PhpParser createParser(final File f) throws IOException {
        return _createParser(Files.newInputStream(f.toPath()), _createContext(_createContentReference(f), true));
    }

    @Override
    public PhpParser createParser(final URL url) throws IOException {
        return _createParser(_optimizedStreamFromURL(url), _createContext(_createContentReference(url), true));
    }

    @Override
    public PhpParser createParser(final InputStream in) throws IOException {
        return _createParser(in, _createContext(_createContentReference(in), false));
    }

    @Override
    public JsonParser createParser(final Reader r) {
        return _createParser(r, _createContext(_createContentReference(r), false));
    }

    @Override
    public PhpParser createParser(final byte[] data) throws IOException {
        return _createParser(data, 0, data.length, _createContext(_createContentReference(data), true));
    }

    @Override
    public PhpParser createParser(final byte[] data, final int offset, final int len) throws IOException {
        return _createParser(data, offset, len, _createContext(_createContentReference(data), true));
    }

    @Override
    public PhpParser createParser(final String doc) {
        return _createParser(new StringReader(doc), _createContext(_createContentReference(doc), true));
    }
    
    /*
    /**********************************************************
    /* Overridden parser factory methods, deprecated
    /**********************************************************
     */

    @Override
    public PhpParser createJsonParser(final File f) throws IOException {
        return _createParser(Files.newInputStream(f.toPath()), _createContext(_createContentReference(f), true));
    }

    @Override
    public PhpParser createJsonParser(final URL url)
            throws IOException {
        return _createParser(_optimizedStreamFromURL(url), _createContext(_createContentReference(url), true));
    }

    @Override
    public PhpParser createJsonParser(final InputStream in) throws IOException {
        return _createParser(in, _createContext(_createContentReference(in), false));
    }

    @Override
    public JsonParser createJsonParser(final Reader r) {
        return _createParser(r, _createContext(_createContentReference(r), false));
    }

    @Override
    public PhpParser createJsonParser(final byte[] data) throws IOException {
        return _createParser(data, 0, data.length, _createContext(_createContentReference(data), true));
    }

    @Override
    public PhpParser createJsonParser(final byte[] data, final int offset, final int len) throws IOException {
        return _createParser(data, offset, len, _createContext(_createContentReference(data), true));
    }

    /*
    /**********************************************************
    /* Overridden generator factory methods
    /**********************************************************
     */

    /**
     * <p>
     * note: co-variant return type
     */
    @Override
    public PhpGenerator createJsonGenerator(final OutputStream out, final JsonEncoding enc) throws IOException {
        return createJsonGenerator(out);
    }

    /**
     * This method assumes use of UTF-8 for encoding.
     */
    @Override
    public PhpGenerator createJsonGenerator(final OutputStream out) throws IOException {
        // false -> we won't manage the stream unless explicitly directed to
        IOContext ctxt = _createContext(_createContentReference(out), false);
        return _createGenerator(ctxt, _createWriter(out, JsonEncoding.UTF8, ctxt));
    }
    
    /*
    /******************************************************
    /* Overridden internal factory methods
    /******************************************************
     */

    //protected IOContext _createContext(Object srcRef, boolean resourceManaged)

    /**
     * Overridable factory method that actually instantiates desired
     * parser.
     */
    @Override
    protected PhpParser _createParser(final InputStream in, final IOContext ctxt) throws IOException {
        Reader r = _createReader(in, null, ctxt);
        return new PhpParser(ctxt, _getBufferRecycler(), _parserFeatures, _objectCodec, r);
    }

    /**
     * Overridable factory method that actually instantiates desired
     * parser.
     */
    @Override
    protected PhpParser _createParser(final Reader r, final IOContext ctxt) {
        return new PhpParser(ctxt, _getBufferRecycler(), _parserFeatures, _objectCodec, r);
    }

    /**
     * Overridable factory method that actually instantiates desired
     * parser.
     */
    @Override
    protected PhpParser _createParser(final byte[] data, final int offset, final int len,
                                      final IOContext ctxt) throws IOException {
        Reader r = _createReader(data, offset, len, null, ctxt);
        return new PhpParser(ctxt, _getBufferRecycler(), _parserFeatures, _objectCodec, r);
    }

    /**
     * Overridable factory method that actually instantiates desired
     * generator.
     */
    @Override
    protected PhpGenerator _createGenerator(final Writer out, final IOContext ctxt) {
        return _createGenerator(ctxt, out);
    }

    @Override
    protected Writer _createWriter(final OutputStream out, final JsonEncoding enc,
                                   final IOContext ctxt) throws IOException {
        return new OutputStreamWriter(out, enc.getJavaName());
    }
    
    /*
    /**********************************************************
    /* Internal methods
    /**********************************************************
     */

    protected PhpGenerator _createGenerator(final IOContext ctxt, final Writer out) {
        PhpGenerator gen = new PhpGenerator(ctxt, _objectCodec, out);
        // any other initializations? No?
        return gen;
    }

//    protected final Charset UTF8 = Charset.forName("UTF-8");

    protected Reader _createReader(final byte[] data, final int offset, final int len,
                                   JsonEncoding enc, IOContext ctxt) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(data, offset, len);
        return new InputStreamReader(in, enc.getJavaName());
    }

    protected Reader _createReader(final InputStream in, final JsonEncoding enc,
                                   final IOContext ctxt) throws IOException {
        return new InputStreamReader(in, enc.getJavaName());
    }
}
