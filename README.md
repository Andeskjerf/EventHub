# EventHub

A website where you can register your participation for any upcoming events

Built with Vue & Java Spring.

## Getting started

### Generating keys used for JWT

```shell
cd backend
openssl genpkey -algorithm RSA -out src/main/resources/key.pem
openssl rsa -pubout -in src/main/resources/key.pem -out src/main/resources/key.pub
```
