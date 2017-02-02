#!/bin/bash
pandoc -t revealjs --self-contained --metadata date="`date +%D`" presentation*.md --variable theme="serif" -o presentation.html && open presentation.html
