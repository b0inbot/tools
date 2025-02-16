package boinsoft.tools.cli;

import io.vavr.control.Try;
import java.util.Collection;

public class Args {
  private String[] args_;
  int idx_;
  int size_;

  public Args(String[] args) {
    this.args_ = args;
    this.idx_ = 0;
    this.size_ = this.args_.length;
  }

  public String shift() {
    this.size_--;
    return this.args_[(this.idx_++)];
  }

  public Try<String> choices(Collection<String> ch) {
    var i = shift();
    if (ch.stream().filter(c -> i.equals(c)).findFirst().isPresent()) {
      return Try.success(i);
    } else {
      return Try.failure(
          new UsageException(
              String.format("argument '%s' not in choices: '%s'", i, String.join(",", ch))));
    }
  }

  public String[] rest() {
    String[] ret = new String[this.size_];
    System.arraycopy(args_, idx_, ret, 0, size_);
    return ret;
  }

  public int size() {
    return this.size_;
  }
}
