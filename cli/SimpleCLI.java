package boinsoft.tools.cli;

import io.vavr.control.Try;
import java.util.function.Function;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/** The interface used to implement CLIs . */
public interface SimpleCLI<T> {

  /** returns the *first* line in the usage string on help invocation */
  String usageLine();

  /**
   * Returns a function that will convert the given CommandLine object into a type-safe object
   * usable in your custom run function
   */
  Function<CommandLine, Try<T>> converter();

  /** Entrypoint for the CLI to customize the options */
  void options(Options options) throws Exception;

  /** Main entrypoint for executing the CLI */
  void run(T t) throws Exception;

  /**
   * the recover pipeline is for mapping exceptions to exit codes
   *
   * @returns an updated try object that handles exceptions
   */
  default Try<Integer> recoverPipeline(Try<Integer> t) {
    return t;
  }

  default void defaultOptions(Options options) throws Exception {
    options.addOption("h", "help", false, "print this message");
  }
}
