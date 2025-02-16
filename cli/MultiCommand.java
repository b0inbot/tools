package boinsoft.tools.cli;

import io.vavr.control.Try;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MultiCommand {
  private final Args args_;
  private final Map<String, Supplier<Try<Void>>> choices_;

  public MultiCommand(Args args) {
    this.args_ = args;
    this.choices_ = new HashMap<>();
  }

  public <T> void add(String subcommand, SimpleCLI<T> cli) {
    choices_.put(
        subcommand,
        () -> {
          return Try.run(
              () -> {
                CLI.invoke(cli, args_.rest());
              });
        });
  }

  public Try<Integer> run() {
    if (args_.size() < 1) {
      return Try.failure(new UsageException("subcommand required"));
    }
    return args_
        .choices(choices_.keySet())
        .flatMap(
            (String subcommand) -> {
              return choices_.get(subcommand).get().map((e) -> 0);
            });
  }
}
