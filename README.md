# Chess API

Backend for [ChessNG](https://github.com/SanketRevankar/ChessNg)

![Docker Cloud Automated build](https://img.shields.io/docker/cloud/automated/sanket2497/chessapi?style=for-the-badge)

![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/sanket2497/chessapi?style=for-the-badge)

## Install Dependencies
Run `maven clean install` to install the required dependencies.
                                         
## Development server
- Run `spring-boot:run` with the below environment variables (or add them in application.properties) -
- Add Url for ChessNg (Local - http://localhost:4200)
    - ``` websocket.allowedOrigin=http://localhost:4200 ```
- Create your client id from [Google Client id](https://console.cloud.google.com/apis/credentials)
    - ```google.clientId= ```
- Change it to a random string
    - ```jwt.secret=149aDklbn127A%SW&1214214@#1ewadsa```
- Connection details for a mongodb instance
    - ```spring.data.mongodb.uri= ```

Docker image is available on [DockerHub](https://hub.docker.com/r/sanket2497/chessapi)

