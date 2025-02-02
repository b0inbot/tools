package boinsoft.tools.xmltool;

import boinsoft.tools.cli.Args;
import boinsoft.tools.cli.CLI;
import java.util.List;

public class Main {
  public static void main(String[] args_) throws Exception {
    Args args = new Args(args_);
    if (args.size() == 0) {
      System.err.println("subcommand required");
      System.exit(1);
    }

    String subcommand = args.choices(List.of("format")).get();

    String[] newArgs = args.rest();

    if (subcommand.equals("format")) {
      CLI.invoke(new XMLFormat(), newArgs);
    } else {
      System.err.println("unknown subcommand");
      System.exit(1);
    }
    return;
  }
}
