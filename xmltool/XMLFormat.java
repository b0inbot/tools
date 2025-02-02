package boinsoft.tools.xmltool;

import boinsoft.tools.cli.CLI;
import boinsoft.tools.cli.SimpleCLI;
import boinsoft.tools.cli.UsageException;
import io.vavr.control.Try;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/** XMLFormat takes in a path and formats an XML file. */
public class XMLFormat implements SimpleCLI<XMLFormat.CLIOptions> {

  public static class CLIOptions {
    public Path input;
  }

  @Override
  public String usageLine() {
    return "xmlformat input <flags>";
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
            java.io.FileNotFoundException.class,
            (exn) -> {
              System.out.println("can't file file: " + exn.getMessage());
              return 2;
            })
        .recover(
            org.dom4j.DocumentException.class,
            (exn) -> {
              System.out.println("malformed input file: " + exn.getMessage());
              return 3;
            });
  }

  @Override
  public Function<CommandLine, Try<CLIOptions>> converter() {
    return (cl -> {
      var files = cl.getArgList();
      if (files.size() == 0) {
        return Try.failure(new UsageException("No files provided"));
      } else if (files.size() > 1) {
        return Try.failure(new UsageException("Too many files provided"));
      }
      var input = Path.of(files.get(0));

      var opts = new CLIOptions();
      opts.input = input;
      return Try.success(opts);
    });
  }

  @Override
  public void options(Options options) {
    options.addOption(Option.builder("write").longOpt("write").desc("no-op").build());
  }

  @Override
  public void run(CLIOptions cliopts) throws Exception {
    SAXReader reader = new SAXReader();
    Document document = reader.read(cliopts.input.toFile());

    // write to a string since we are editing inplace. Not the biggest
    // deal as we have already read it but if the workflow changes or we swap
    // to java.xml Transformer based design this will be important.
    StringWriter sw = new StringWriter();

    formatDocument(document, sw);

    try (FileWriter wr = new FileWriter(cliopts.input.toFile())) {
      wr.write(sw.toString());
    }
  }

  public void formatDocument(Document document, Writer w) throws Exception {
    OutputFormat format = OutputFormat.createPrettyPrint();
    format.setIndentSize(4);
    format.setPadText(false);
    format.setNewLineAfterDeclaration(false);
    format.setSuppressDeclaration(false);
    format.setEncoding("UTF-8");

    XMLWriter writer = new XMLWriter(w, format);

    writer.write(document);
  }

  public static void main(String[] args) throws Exception {
    CLI.invoke(new XMLFormat(), args);
  }
}
