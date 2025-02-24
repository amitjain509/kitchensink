# README

## Introduction

This application enables enterprise companies to seamlessly integrate their existing user data stores, such as Active Directory (AD), and provides an intuitive UI for managing users, roles, and permissions efficiently.

## System Requirements

- Docker container must be installed.
- Docker CLI must be installed to execute Docker commands.

For installation instructions, refer to the official Docker documentation:

- [Install Docker](https://docs.docker.com/get-docker/)
- [Install Docker CLI](https://docs.docker.com/engine/reference/commandline/cli/)

## Prerequisites

This program runs with SSL enabled by default. To enable SSL, users need to generate the required certificates.

For instructions on generating and configuring the certificates, refer to the [SSL Configuration Guide](./SSL_README.md).

Additionally, the `docker-compose.yml` file needs to be modified to update the Angular frontend project path.

For instructions on configuring the Angular frontend project, refer to the [Angular Configuration Guide](./ANGULAR_README.md).

## Deployment

To deploy the application, run the following command:

```sh
docker-compose --env-file config/env/dev.env up --build -d
```

After running the command, you should see all the containers running:

✔ Container kitchensink-mongo      Running  0.0s\
✔ Container angular-web            Running  0.0s\
✔ Container kitchensink-app        Started  0.6s\
✔ Container kitchensinkv2-nginx-1  Started

## Host Mapping

To access the application, map `localhost` to `demo.kitchensink.com` in `/etc/hosts` by adding the following line:

```sh
127.0.0.1 demo.kitchensink.com
```

## Accessing the Application

Once deployed and configured, the application can be accessed at:

[https://demo.kitchensink.com](https://demo.kitchensink.com)

