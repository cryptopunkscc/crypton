# Integration tests
Are used to automate integration testing process of whole domain logic working with external server.

## how to run
```
./gradlew jvm:test:test
```
Can be used to integration tests run.
However is recommended to run tests via IDE to obtain execution logs.

## Test server
See `./ejabber/test_server_ejabberd.yml` for details how to install and configure local server for integration testing. 
