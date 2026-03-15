package boinsoft.tools.ziptool;

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

  static class ZFile {
    final ReadableZipEntry file;

    public ZFile(ReadableZipEntry f) {
      this.file = f;
    }

    @Override
    public boolean equals(Object other) {
      if (other instanceof ZFile) {
        return ((ZFile) other).file.name().equals(file.name());
      }
      return false;
    }

    @Override
    public int hashCode() {
      return file.name().hashCode();
    }
  }

  static class Options {
    public boolean dedupFolders = false;
    public String dedupFileStrategy = "ignore";
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
    return;
  }

  public Stream<Try<ReadableZipEntry>> stream() throws IOException {
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

    var strategy = this.options.dedupFileStrategy;
    if (!strategy.equalsIgnoreCase("ignore")) {
      Set<ZFile> files = new HashSet<>();
      return x.map(
          f ->
              Try.of(
                  () -> {
                    if (f.isDirectory()) {
                      return f;
                    } else {
                      var added = files.add(new ZFile(f));
                      if (!added) {
                        if (strategy.equalsIgnoreCase("fail")) {
                          throw new Exception("Duplicate entry " + f.name());
                        }
                        // TODO: implement newest-first, oldest-first,
                        // biggest-first, smallest-first, fail-on-file-contents-diff
                      }
                      return f;
                    }
                  }));
    }

    return x.map(Try::success);
  }

  protected static Stream<ReadableZipEntry> fromZipFile(ZipFile zf) {
    return zf.stream().map(z -> new ReadableZipEntry(zf, z));
  }

  public void close() throws IOException {
    this.inputs.stream().map(Try::success).forEach(x -> x.andThenTry(f -> f.close()));
  }
}
