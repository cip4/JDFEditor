#!/bin/sh

curl -v -H "Content-Type: multipart/related" --data-binary @one-jmf-jdf.mjm http://localhost:8280/jmf/test-01
