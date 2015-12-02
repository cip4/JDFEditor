#!/bin/sh

curl -v -H "Content-Type: multipart/related" --data-binary @mime-multipart-related-two-jmf.txt http://localhost:8280/jmf/test-01
