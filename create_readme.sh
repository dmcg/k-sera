#!/bin/bash
set -e

function write_file_contents {
    echo '```kotlin'
    sed -e '1,/README_TEXT/d' -e '/README_TEXT/,$d' $1
    echo '```'
}

echo "
Amock
=====

A JMock wrapper for Kotlin.

[AmockExampleTests](src/test/java/com/oneeyedmen/amock/AmockExampleTests.kt)
shows how to write a test.
"  > README.md

write_file_contents src/test/java/com/oneeyedmen/amock/AmockExampleTests.kt >> README.md

echo "
Amock is available at Maven central.
" >> README.md