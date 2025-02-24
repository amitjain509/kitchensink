# SSL Configuration Guide

## Introduction

This guide explains how to generate and configure SSL certificates to enable secure HTTPS communication for the application.

## Generating SSL Certificates

To enable SSL, you need to generate self-signed certificates or use certificates from a trusted Certificate Authority (CA).

### Generate a Self-Signed Certificate (Development Use Only)

Run the following command to generate a self-signed certificate:

```sh
openssl req -x509 -newkey rsa:4096 -keyout ssl/key.pem -out ssl/cert.pem -days 365 -nodes
```

This will generate:

- `key.pem` (Private Key)
- `cert.pem` (Public Certificate)

### Using Certificates from a Trusted CA

If using a trusted CA, obtain the certificate and private key from the certificate provider and place them in the appropriate directory.

## Copying SSL Certificates to Required Folders

After generating or obtaining the certificates, copy them to the following locations:

- **Nginx Certificates:**

    - `fullchain.pem` → Place inside `nginx/certs/`
    - `privatekey.pem` → Place inside `nginx/certs/`

- **MongoDB Certificates:**

    - `rootCA.pem` → Place inside `mongo/certs/`
    - `mongo-cert.pem` → Place inside `mongo/certs/`
    - `mongo-key.pem` → Place inside `mongo/certs/`

Ensure the files are correctly placed before starting the services.

## Creating a Java Keystore for MongoDB

To allow Java-based applications to trust MongoDB’s SSL certificates, create a Java Keystore file (`mongo-truststore.jks`) using the `keytool` command:

```sh
keytool -importcert -trustcacerts -alias mongo-rootCA -file mongo/certs/rootCA.pem -keystore mongo-truststore.jks -storepass changeit
```

This will create `mongo-truststore.jks`. Copy this file to the `config/certs/` directory to be used by the application.

## Restarting the Services

After placing the SSL certificates, restart the containers to apply the SSL configuration:

## Verifying SSL

Once the services are running, open a browser and navigate to:

[https://demo.kitchensink.com](https://demo.kitchensink.com)

If everything is configured correctly, the site should load over HTTPS.

