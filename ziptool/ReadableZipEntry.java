package boinsoft.tools.ziptool;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

record ReadableZipEntry(ZipFile zf, ZipEntry ze) {
  public InputStream getInputStream() throws IOException {
    return zf.getInputStream(ze);
  }

  public String name() {
    return ze.getName();
  }

  public boolean isDirectory() {
    return ze.isDirectory();
  }
}
