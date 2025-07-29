<!--
SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
SPDX-License-Identifier: CC-BY-4.0
-->

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.2.0] - 2025-07-31

### Added

- New option to enter the profile activation code manually during the profile download
- Support for new interface of TelephonyManager
- MEP-B ready

### Changed

- Migrate from .reuse/dep5 to [REUSE.toml](REUSE.toml)


## [4.1.0] - 2024-03-28

Features:

- Added support for GSMA SGP.22 v3.1
  - ASN1 schema support
  - Configuration support
  - NOTE: Server name extension (v3-specific FQDN) is not supported (yet)

Maintenance:

- Update to latest dependency versions
- Minor housekeeping
- Migrated userguide also to Markdown

## [4.0.3] - 2023-04-19

Maintenance:

- Update to latest dependency versions
- Minor housekeeping

## [4.0.2] - 2022-08-23

Bugfix:

- Fixed ProGuard rules for release build variant

## [4.0.1] - 2022-08-10

Features:

- Added support for two new Identiv USB readers
  - Identive CLOUD 4700 F Dual Interface Reader
  - Identiv uTrust 4701 F Dual Interface Reader
- Added error message for unknown USB readers

## [4.0.0] - 2022-06-24

Initial release for distribution.
