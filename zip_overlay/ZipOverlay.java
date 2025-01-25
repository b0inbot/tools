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
 * A tool for taking a list of ZIP files and merging them together.
 *
 * <p>Usage:
 *
 * <p>zip_overlay output.jar layer1.zip layer2.zip layer3.zip
 */
public class ZipOverlay {

  public static void info(String msg) {
    System.err.printf("[INFO] %s\n", msg);
  }

  public static void warn(String msg) {
    System.err.printf("[WARN] %s\n", msg);
  }

  static void copyEntry(ZipOutputStream out, ReadableZipEntry rze)
      throws IOException, ZipException {
    try {
      out.putNextEntry(rze.ze());
    } catch (ZipException exn) {
      if (exn.getMessage().startsWith("duplicate entry:")) {
        // TODO: allow configurable handling of duplicate entries
        if (!rze.isDirectory()) {
          warn("got duplicate entry " + rze.name());
        }
        return;
      }
      throw exn;
    }
    try (var is = rze.getInputStream()) {
      is.transferTo(out);
    }
  }

  public static void main(String[] args) throws IOException {
    info("zipoverlay");
    var dest = Path.of(args[0]);

    List<Path> inputs = new LinkedList<>();
    for (int x = 1; x != args.length; x++) {
      inputs.add(Path.of(args[x]));
    }

    try (var out = new ZipOutputStream(Files.newOutputStream(dest));
        var in = new ManyZipEntryStream(inputs)) {
      in.stream()
          .map((rze) -> Try.success(null).andThenTry(() -> copyEntry(out, rze)))
          .map(Try::get)
          .toList();
    }
  }
}
