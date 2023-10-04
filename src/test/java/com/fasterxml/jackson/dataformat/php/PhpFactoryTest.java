/*
 * Copyright 2013 Photobucket 
 */
package com.fasterxml.jackson.dataformat.php;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpFactoryTest {

    /**
     * Test of createParser method, of class PhpFactory.
     */
    @Test
    public void testCreateParser_String() throws Exception {
        PhpFactory instance = new PhpFactory();
        PhpParser result = instance.createParser("");
        assertNotNull(result);
    }
}
