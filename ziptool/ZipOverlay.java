package boinsoft.tools.ziptool;

import boinsoft.tools.cli.CLI;
import boinsoft.tools.cli.SimpleCLI;
import boinsoft.tools.cli.UsageException;
import io.vavr.control.Try;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;
import java.util.zip.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * A tool for taking a list of ZIP files and merging them together.
 *
 * <p>Usage:
 *
 * <p>ziptool overlay output.jar layer1.zip layer2.zip layer3.zip
 */
public class ZipOverlay implements SimpleCLI<ZipOverlay.CLIOptions> {

  public static class CLIOptions {
    public boolean dedupFolders;
    public Path output;
    public List<Path> inputs;
  }

  @Override
  public String usageLine() {
    return "ziptool overlay <flags> <output> <input> <input> ...";
  }

  @Override
  public Try<Integer> recoverPipeline(Try<Integer> t) {
    return t.recover(
            java.nio.file.NoSuchFileException.class,
            (exn) -> {
              System.out.println("can't find file: " + exn.getMessage());
              return 2;
            })
        .recover(
            java.util.zip.ZipException.class,
            (exn) -> {
              System.out.println("fatal error: " + exn.getMessage());
              return 3;
            });
  }

  @Override
  public Try<CLIOptions> convert(CommandLine cl) {
    var dedupFolders = cl.hasOption("dedup-folders");

    var files = cl.getArgList();
    if (files.size() == 0) {
      return Try.failure(new UsageException("No files provided"));
    }
    var dest = Path.of(files.get(0));

    List<Path> inputs = new LinkedList<>();
    for (int x = 1; x != files.size(); x++) {
      inputs.add(Path.of(files.get(x)));
    }

    var opts = new CLIOptions();
    opts.inputs = inputs;
    opts.dedupFolders = dedupFolders;
    opts.output = dest;
    return Try.success(opts);
  }

  @Override
  public void options(Options options) {
    options.addOption(
        Option.builder("dedup-folders")
            .longOpt("dedup-folders")
            .desc("deduplicate folders")
            .build());
  }

  @Override
  public void run(CLIOptions cliopts) throws Exception {
    info("zipoverlay");

    var opts = new ManyZipEntryStream.Options();
    opts.dedupFolders = cliopts.dedupFolders;

    try (var out = new ZipOutputStream(Files.newOutputStream(cliopts.output));
        var in = new ManyZipEntryStream(cliopts.inputs, opts)) {
      in.stream()
          .map((rze) -> Try.success(null).andThenTry(() -> copyEntry(out, rze)))
          .map(Try::get)
          .toList();
    }
  }

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

  public static void main(String[] args) throws Exception {
    CLI.invoke(new ZipOverlay(), args);
  }
}
