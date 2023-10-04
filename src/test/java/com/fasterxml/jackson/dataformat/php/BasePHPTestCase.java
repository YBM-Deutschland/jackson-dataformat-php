package com.fasterxml.jackson.dataformat.php;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base test for PHP data format.
 */
public abstract class BasePHPTestCase {
  private PhpFactory phpFactory;
  private ObjectMapper mapper;

  public BasePHPTestCase() {
    super();
    loadMapper();
  }

  private void loadMapper() {
    phpFactory = new PhpFactory();
    mapper = new ObjectMapper(phpFactory);
  }

  public PhpFactory getPhpFactory() {
    return phpFactory;
  }

  public ObjectMapper getMapper() {
    return mapper;
  }
}
