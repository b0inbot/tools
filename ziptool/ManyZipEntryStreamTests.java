package boinsoft.tools.ziptool;

import static org.junit.jupiter.api.Assertions.*;

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
  public void testMultiple() throws IOException {
    try (var z =
        new ManyZipEntryStream(List.of(Path.of("ziptool/one.zip"), Path.of("ziptool/two.zip")))) {
      var l = z.stream().toList();
      assertNotNull(l);
      assertEquals(4, l.size());
      assertEquals("a/", l.get(0).name());
      assertEquals("a/1", l.get(1).name());
      assertEquals("a/", l.get(2).name());
      assertEquals("a/2", l.get(3).name());
    }
  }

  @Test
  public void testMultipleDedup() throws IOException {
    var opts = new ManyZipEntryStream.Options();
    opts.dedupFolders = true;

    try (var z =
        new ManyZipEntryStream(
            List.of(Path.of("ziptool/one.zip"), Path.of("ziptool/two.zip")), opts)) {
      var l = z.stream().toList();
      assertNotNull(l);
      assertEquals(3, l.size());
      assertEquals("a/", l.get(0).name());
      assertEquals("a/1", l.get(1).name());
      assertEquals("a/2", l.get(2).name());
    }
  }
}
