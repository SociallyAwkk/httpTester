# HttpTester

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A Small CLI HTTP Testing Tool written in Java 21 and Spring Shell.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Usage](#usage)
- [Configuration](#configuration)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Introduction

This CLI tool is for quickly testing load and performance of HTTP services. It is written in Java 21 and uses Spring Shell.
It is a learning project for me to learn Spring shell and to get more familiar with Java's new virtual threads.

[![Image from Gyazo](https://i.gyazo.com/3758dbe026b96645a477c88a2239e6e1.gif)](https://gyazo.com/3758dbe026b96645a477c88a2239e6e1)

## Features

- Using java virtual threads for high concurrency
- Simple way to test Http services with a single command
- Supports GET, POST methods - More coming soon...
- Simple data calculation and reporting - Verbose mode coming soon...

## Getting Started

Provide instructions on how to get your program up and running.

### Prerequisites

List any prerequisites that users need to have installed before using your program.

For Self Build:
- Java 21 or higher
- Ideally GraalVM 21.3.0 or higher

### Installation

Guide users through the installation process.

Download the latest release from the GitHub Actions artifact.

```bash
# Example installation steps or commands
git clone https://github.com/your-username/your-repo.git
cd your-repo
./install.sh
