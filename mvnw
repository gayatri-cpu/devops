#!/usr/bin/env bash
# ----------------------------------------------------------------------------
# Maven Wrapper for VaxGuard Vaccination Reminder Portal (Linux/macOS)
# ----------------------------------------------------------------------------
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`" >/dev/null
APPDIR="`pwd -P`"
cd "$SAVED" >/dev/null

if [ -x "$APPDIR/.tools/apache-maven-3.9.6/bin/mvn" ]; then
    exec "$APPDIR/.tools/apache-maven-3.9.6/bin/mvn" "$@"
elif command -v mvn >/dev/null 2>&1; then
    exec mvn "$@"
else
    echo "Maven not found in .tools or system PATH. Please install Maven."
    exit 1
fi
