package boinsoft.tools.zip_overlay;

import io.vavr.control.Try;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;
import java.util.zip.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

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

  public void run(CommandLine args) throws Exception {
    info("zipoverlay");
    var files = args.getArgList();
    var dest = Path.of(files.get(0));

    List<Path> inputs = new LinkedList<>();
    for (int x = 1; x != files.size(); x++) {
      inputs.add(Path.of(files.get(x)));
    }

    var opts = new ManyZipEntryStream.Options();
    opts.dedupFolders = args.hasOption("dedup-folders");

    try (var out = new ZipOutputStream(Files.newOutputStream(dest));
        var in = new ManyZipEntryStream(inputs, opts)) {
      in.stream()
          .map((rze) -> Try.success(null).andThenTry(() -> copyEntry(out, rze)))
          .map(Try::get)
          .toList();
    }
  }

  Options options;

  public ZipOverlay() {
    options = new Options();
    options.addOption("h", "help", false, "print this message");
    options.addOption(
        Option.builder("dedup-folders")
            .longOpt("dedup-folders")
            .desc("deduplicate folders")
            .build());
  }

  public Try<CommandLine> parse(String[] args) {
    return Try.of(
        () -> {
          CommandLineParser parser = new DefaultParser();
          var line = parser.parse(options, args);
          return line;
        });
  }

  public static void main(String[] args) throws Exception {
    var zl = new ZipOverlay();
    zl.parse(args)
        .mapTry(
            cl -> {
              if (cl.hasOption("help") || cl.getArgList().size() == 0) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("zip_overlay <flags> <output> <input> <input> ...", zl.options);
                return 1;
              } else {
                zl.run(cl);
                return 0;
              }
            })
        .get();
  }
}
