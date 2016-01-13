#!/bin/sh

curl -v -H "Content-Type: multipart/related" --data-binary @one-jmf-one-jdf.txt http://localhost:8280/jmf/test-01
