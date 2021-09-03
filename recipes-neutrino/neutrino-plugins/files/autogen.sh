#!/bin/sh

package="ni-neutrino-plugins"

srcdir=`dirname $0`
test -z "$srcdir" && srcdir=.

cd "$srcdir"
echo "Generating configuration files for $package, please wait ..."

aclocal --force
libtoolize --force
autoconf --force
autoheader --force
automake --add-missing --force-missing
