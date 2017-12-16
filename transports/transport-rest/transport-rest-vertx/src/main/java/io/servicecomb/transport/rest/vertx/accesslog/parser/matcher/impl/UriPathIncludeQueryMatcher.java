package io.servicecomb.transport.rest.vertx.accesslog.parser.matcher.impl;

import java.util.ArrayList;
import java.util.List;

import io.servicecomb.transport.rest.vertx.accesslog.element.impl.UriPathIncludeQueryElement;
import io.servicecomb.transport.rest.vertx.accesslog.parser.AccessLogElementExtraction;
import io.servicecomb.transport.rest.vertx.accesslog.parser.matcher.AccessLogElementMatcher;

public class UriPathIncludeQueryMatcher implements AccessLogElementMatcher {

  public static final UriPathIncludeQueryElement ELEMENT = new UriPathIncludeQueryElement();

  public static final String PLACEHOLDER_PATTERN = "cs-uri";

  public static final String[] EXCLUDE_PATTERNS = new String[] {"cs-uri-stem", "cs-uri-query"};

  @Override
  public List<AccessLogElementExtraction> extractElementPlaceholder(String rawPattern) {
    List<AccessLogElementExtraction> extractionList = new ArrayList<>();

    int start = -1;
    int cursor = 0;
    while (true) {
      start = rawPattern.indexOf(PLACEHOLDER_PATTERN, cursor);
      if (start < 0) {
        break;
      }

      if (shouldExclude(rawPattern, start, cursor)) {
        cursor += PLACEHOLDER_PATTERN.length();
        continue;
      }

      AccessLogElementExtraction extraction = new AccessLogElementExtraction(start,
          start + PLACEHOLDER_PATTERN.length(),
          ELEMENT);
      extractionList.add(extraction);

      cursor = start + PLACEHOLDER_PATTERN.length();
    }

    return extractionList;
  }

  private boolean shouldExclude(String rawPattern, final int start, final int cursor) {
    for (String exclude : EXCLUDE_PATTERNS) {
      if (start == rawPattern.indexOf(exclude, cursor)) {
        return true;
      }
    }

    return false;
  }
}