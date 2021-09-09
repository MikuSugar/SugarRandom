#!/usr/bin/env bash
source /etc/profile

source ~/.bash_profile

set -e


if [ -n "${M2_HOME}" ]; then
  MVN_RUN="${M2_HOME}/bin/mvn"
else
  if [ `command -v mvn ` ]; then
    MVN_RUN="mvn "
  else
    echo "M2_HOME is not set" >&2
    exit 1
  fi
fi

echo "☕️ mvn install core "
cd sugar_random_core
MVN_RUN clean install
cd ../

echo "☕️ build web"
MVN_RUN clean package -Pproduction

echo "☕️ build cli"
cd sugar_random_cli
MVN_RUN clean package
cd ../

echo "🎉 build success"
echo "web 😊"
echo target
# shellcheck disable=SC2010
ls -l target/ | grep '.jar$'
echo "cli 😊"
echo sugar_random_cli/target/
ls -l  sugar_random_cli/target/ | grep '.jar$'