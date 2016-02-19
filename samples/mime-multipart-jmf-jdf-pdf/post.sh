#!/bin/sh

curl -v -H "Content-Type: multipart/related" --data-binary @elk-approval.mjm http://localhost:8280/jmf/test-01
