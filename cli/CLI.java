package boinsoft.tools.cli;

import io.vavr.control.Try;
import java.util.function.Function;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/** The main entrypoint for the CLI library */
public class CLI {

  static class CLIInterrupt extends Exception {}

  /**
   * invoke calls the given SimpleCLI object with the given arguments, using the SimpleCLI
   * implementation methods to parse and execute our given CLI.
   *
   * <p>this acts as glue code between our custom code, vavr, and commons cli
   */
  public static <T> void invoke(SimpleCLI<T> cli, String[] args) throws Exception {
    var options = new Options();
    cli.defaultOptions(options);
    cli.options(options);
    Function<CommandLine, Try<T>> converter = cli.converter();

    var x =
        parse(options, args)
            .flatMapTry(
                cl -> {
                  if (cl.hasOption("help")) {
                    printHelp(cli, options);
                    throw new CLI.CLIInterrupt();
                  }
                  return converter.apply(cl);
                })
            .mapTry(
                t -> {
                  cli.run(t);
                  return 0;
                })
            .recover(CLI.CLIInterrupt.class, (exn) -> 1)
            .recover(
                UsageException.class,
                (exn) -> {
                  System.err.println(exn.getMessage());
                  printHelp(cli, options);
                  return 1;
                });
    x = cli.recoverPipeline(x);
    var res = x.get();
    System.exit(res);
  }

  static <T> void printHelp(SimpleCLI<T> cli, Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(cli.usageLine(), options);
  }

  static Try<CommandLine> parse(Options options, String[] args) {
    return Try.of(
        () -> {
          CommandLineParser parser = new DefaultParser();
          var line = parser.parse(options, args);
          return line;
        });
  }
}
