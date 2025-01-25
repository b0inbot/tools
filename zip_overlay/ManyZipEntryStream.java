package boinsoft.tools.zip_overlay;

import io.vavr.control.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;
import java.util.zip.*;

/**
 * Utility class which takes in a series of Path objects which point to ZIP files and generates a
 * stream of ReadableZipEntries, for reading each item.
 */
class ManyZipEntryStream implements AutoCloseable {
  List<ZipFile> inputs;
  Options options;

  static class Options {
    public boolean dedupFolders = false;
  }

  public ManyZipEntryStream(List<Path> paths) throws IOException {
    this(paths, new Options());
  }

  public ManyZipEntryStream(List<Path> paths, Options options) throws IOException {
    this.options = options;
    this.inputs =
        paths.stream()
            .map(Path::toFile)
            .map(Try::success)
            .map(p -> p.mapTry(f -> new ZipFile(f)))
            .map(Try::get)
            .toList();
  }

  public Stream<ReadableZipEntry> stream() throws IOException {
    var x = this.inputs.stream().flatMap(ManyZipEntryStream::fromZipFile);
    if (this.options.dedupFolders) {
      Set<String> folders = new HashSet<>();
      x =
          x.filter(
              f -> {
                if (f.isDirectory()) {
                  return folders.add(f.name());
                } else {
                  return true;
                }
              });
    }
    return x;
  }

  protected static Stream<ReadableZipEntry> fromZipFile(ZipFile zf) {
    return zf.stream().map(z -> new ReadableZipEntry(zf, z));
  }

  public void close() throws IOException {
    this.inputs.stream().map(Try::success).map(x -> x.andThenTry(f -> f.close())).toList();
  }
}
