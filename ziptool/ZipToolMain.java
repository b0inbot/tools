package boinsoft.tools.ziptool;

import boinsoft.tools.cli.Args;
import boinsoft.tools.cli.CLI;
import java.util.List;

public class ZipToolMain {

  public static void main(String[] args_) throws Exception {
    Args args = new Args(args_);
    if (args.size() == 0) {
      System.err.println("subcommand required");
      System.exit(1);
    }

    String subcommand = args.choices(List.of("overlay")).get();

    String[] newArgs = args.rest();

    if (subcommand.equals("overlay")) {
      CLI.invoke(new ZipOverlay(), newArgs);
    } else {
      System.err.println("unknown subcommand");
      System.exit(1);
    }
  }
}
