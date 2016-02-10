#!/bin/sh

curl -v -H "Content-Type: application/vnd.cip4-jmf+xml" --data-binary @QueryKnownMessages.jmf http://localhost:8280/jmf/test-01
