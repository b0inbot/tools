package boinsoft.tools.xmltool;

import boinsoft.tools.cli.Args;
import boinsoft.tools.cli.MultiCommand;

public class Main {
  public static void main(String[] args_) throws Exception {
    try {
      MultiCommand mc = new MultiCommand(new Args(args_));
      mc.add("format", new XMLFormat());
      System.exit(mc.run().get());
    } catch (Exception exn) {
      System.err.println(exn.getMessage());
      System.exit(1);
    }
  }
}
