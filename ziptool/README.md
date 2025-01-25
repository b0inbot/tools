# boinsoft/tools/ziptool

A tool for operating on zip files

## ziptool overlay

Join zip / jar files together.

This is used for "release mega-jars"
that can contain arbitrary trees of files.

## ziptool patch

```
ziptool.patched(
    name = "my-file",
    src = ":my-file-src",
    commands = ":a.sky",
)
```
