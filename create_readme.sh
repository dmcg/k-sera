#!/bin/bash
set -e

function write_file_contents {
    echo '```kotlin'
    sed -e '1,/README_TEXT/d' -e '/README_TEXT/,$d' $1
    echo '```'
}

echo "
K-Sera
=====

A JMock wrapper for Kotlin.

[KSeraExampleTests](src/test/java/com/oneeyedmen/kSera/KSeraExampleTests.kt)
shows how to write a test.
"  > README.md

write_file_contents src/test/java/com/oneeyedmen/kSera/KSeraExampleTests.kt >> README.md

echo "
k-sera is available at Maven central.
" >> README.md