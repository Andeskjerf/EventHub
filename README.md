# EventHub

A website where you can register your participation for any upcoming events

Built with Vue & Java Spring.

## Getting started

### Setting up database

```shell
touch src/main/resources/database.db
```

### Generating keys used for JWT

```shell
openssl genpkey -algorithm RSA -out src/main/resources/key.pem
openssl rsa -pubout -in src/main/resources/key.pem -out src/main/resources/key.pub
```
