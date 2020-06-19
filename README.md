# CIP4 JDF / XJDF Editor

[![License (CIP4 Software License)](https://img.shields.io/badge/license-CIP4%20Software%20License-blue)](https://github.com/cip4/xJdfLib/blob/master/LICENSE.md) ![JDFEditor Snapshot](https://github.com/cip4/JDFEditor/workflows/JDFEditor%20Snapshot/badge.svg)


## Issue Tracking
Don't write issues, provide Pull-Requests!

## Development Notes
### Release a new Version
Creation and publishing of a new version to GitHub Release.

```bash
$ git tag -a JDFEditor-[VERSION] -m "[TITLE]"
$ git push origin JDFEditor-[VERSION]
```

In case a build has been failed, a tag can be deleted using the following command:
```bash
$ git tag -d JDFEditor-[VERSION]
$ git push origin :refs/tags/JDFEditor-[VERSION]
```