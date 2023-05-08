# CIP4 JDF / XJDF Editor

[![License (CIP4 Software License)](https://img.shields.io/badge/license-CIP4%20Software%20License-blue)](https://github.com/cip4/xJdfLib/blob/master/LICENSE.md) ![Snapshot](https://github.com/cip4/JDFEditor/workflows/Snapshot/badge.svg)

The JDFEditor enables you to display, modify and validate JDF Documents and JMF Messages. The application provides a broad range of functionality in order to achieve this. For instance, JDF Documents can be extended by additional elements and attributes in a well defined manner. Further, the JDFEditor supports validation methods as well as a simple communictaion mechanism in order to link JDF Devices directly.With regard to XJDF (or JDF 2.0) the editor also supports XPath evaluation. More featured XJDF functionality are available by enabling the “Extension Options” in “Tools -> Preferences…”. As a result the JDF Editor will becomes able to convert JDF Documents to XJDF Documents and vice versa. More details about XJDF and PrintTalk 2.0 can you find on the page “XJDF / PrintTalk 2.0″.

## Download
All JDF Editor versions are availale at: https://github.com/cip4/JDFEditor/releases

## Issue Tracking
Don't write issues, provide Pull-Requests!

## Development Notes
### Release a new Version
Creation and publishing of a new version to GitHub Release.

```bash
$ git tag -a [VERSION] -m "[TITLE]"
$ git push origin [VERSION]
```

In case a build has been failed, a tag can be deleted using the following command:
```bash
$ git tag -d [VERSION]
$ git push origin :refs/tags/[VERSION]
```
