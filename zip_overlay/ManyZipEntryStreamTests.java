package boinsoft.tools.zip_overlay;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ManyZipEntryStreamTests {

  @Test
  public void testEmpty() throws IOException {
    try (var z = new ManyZipEntryStream(List.of())) {}
  }

  @Test
  public void testMissing() {
    assertThrows(
        NoSuchFileException.class,
        () -> {
          try (var z = new ManyZipEntryStream(List.of(Path.of("missing.zip")))) {}
        });
  }

  @Test
  public void testSingle() {
    // TODO: build or find zip
  }
}
