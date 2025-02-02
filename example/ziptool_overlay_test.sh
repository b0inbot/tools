#!/bin/sh

LEFT_IN="$RUNFILES_DIR"/_main/example/expected-output.txt
ZIP="$RUNFILES_DIR"/_main/example/out.zip
RIGHT="$TEST_TMPDIR"/out.txt
LEFT="$TEST_TMPDIR"/expected-output.txt

set -e

sed "s,ZIP,$ZIP,g" <"$LEFT_IN" >"$LEFT"
zip -vT "$ZIP" >"$RIGHT"
diff "$LEFT" "$RIGHT"
