# Angular Frontend Configuration Guide

This guide provides steps to configure the Angular frontend project for the application.

## Prerequisites

Before configuring the Angular frontend, ensure that **npm** (Node Package Manager) is installed.

### Install npm

npm comes bundled with **Node.js**. Download and install the latest LTS version of Node.js from the [official website](https://nodejs.org/).

Verify the installation by running:

```sh
node -v
npm -v
```

## Building the Angular Project

Before deploying, build the Angular project using the following command:

```sh
ng build --configuration=production
```

## Copying the Build Output

After building the Angular project, copy the output from the `dist` folder and update the path in the `docker-compose.yml` file.

1. Locate the built files inside the `dist` directory of your Angular project.
2. Update the `docker-compose.yml` file to use the correct `dist` path.

## Updating the Docker Compose Configuration

By default, the Angular frontend project path needs to be updated in the `docker-compose.yml` file. To do this:

1. Open the `docker-compose.yml` file in a text editor.
2. Locate the `angular-web` service configuration.
3. Update the `volumes` section to point to the correct path of your Angular project.

Example:

```yaml
services:
  angular-web:
    volumes:
      - /absolute/path/to/angular/project:/usr/share/nginx/html
```
