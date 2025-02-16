package boinsoft.tools.ziptool;

import boinsoft.tools.cli.Args;
import boinsoft.tools.cli.MultiCommand;

public class ZipToolMain {

  public static void main(String[] args_) throws Exception {
    try {
      MultiCommand mc = new MultiCommand(new Args(args_));
      mc.add("overlay", new ZipOverlay());
      System.exit(mc.run().get());
    } catch (Exception exn) {
      System.err.println(exn.getMessage());
      System.exit(1);
    }
  }
}
