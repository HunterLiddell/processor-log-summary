# ![](https://github.com/sourceplusplus/live-platform/blob/master/.github/media/sourcepp_logo.svg)

[![License](https://img.shields.io/github/license/sourceplusplus/processor-log-summary)](LICENSE)
![GitHub release](https://img.shields.io/github/v/release/sourceplusplus/processor-log-summary?include_prereleases)
[![Build](https://github.com/sourceplusplus/processor-log-summary/actions/workflows/build.yml/badge.svg)](https://github.com/sourceplusplus/processor-log-summary/actions/workflows/build.yml)

## What is this?

The project provides additional backend processing to the [Source++](https://github.com/sourceplusplus/live-platform) open-source live coding platform.

## Requirements

- SkyWalking OAP
  - Version >= 8.0.0
  - Storage = elasticsearch
- Modules
  - `StorageModule`

## Live Coding - Index
- Live Coding Server
  - [Live Platform](https://github.com/sourceplusplus/live-platform)
- Live Coding Clients
  - [JetBrains Plugin](https://github.com/sourceplusplus/SourceMarker)
  - [CLI](https://github.com/sourceplusplus/interface-cli)
- Live Coding Probes
  - [JVM](https://github.com/sourceplusplus/probe-jvm)
  - [Python](https://github.com/sourceplusplus/probe-python)
- Live Coding Processors
  - [Live Instrument](https://github.com/sourceplusplus/processor-instrument)
  - [Log Summary](https://github.com/sourceplusplus/processor-log-summary)
