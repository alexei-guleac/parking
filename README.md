# parking_app

<br/>
<p align="center">
  <a href="https://github.com/isd-soft/parking">
    <img src="images/ParKingLogo.png" alt="Logo" width="190" height="100">
  </a>
  <a href="http://isd-soft.com/"><img src="images/isd-logo.png"  width="100" height="100" title="ISD::Software" alt="ISD::Software"></a>
   </br>
   <a href="https://github.com/isd-soft/parking/issues">Report Bug</a>
   ·
   <a href="https://github.com/isd-soft/parking/issues">Request Feature</a>
</p>

<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Screenshots](#screenshots)
  * [Used technologies](#used-technologies)
* [How to use application](#how-to-use-application)
  * [Main page](#main-page)
  * [Statistics page](#statistics-page)
  * [Parking layout page](#parking-layout-page)
* [How to use API](#how-to-use-api)
  * [DataBase](#database)
* [Contributing](#contributing)
* [License](#license)


### About the project
IoT project for monitoring the state of parking spaces for an enterprise parking.

This project is made to provide the ISD company parking area with the necessary equipment and software tools control and fixing the employment status of parking lots and maintaining daily statistics on status changes.

The project aims to create a new informative application for monitoring and providing information for end-users about free parking spaces. The purpose of this application is to provide the information in advance if there is an avaliable parking lot or you have to pick up another place.

### Screenshots

You can view screenshoots here: https://drive.google.com/drive/folders/1KWFmRRornm2PnW8DccL7WywEy7r8fwIe?usp=sharing

### Used technologies
Tihs project is a **SPA web application** based on a **RESTful** architecture using **JWT, OAuth, Social services** connect, **WebSockets, LDAP** technologies, frontend: **Angular 8 / Typescript / RxJS**, backend - **Spring Framework 5 / Spring Boot 2.2, Java 11 , Hibernate**, DB - **PostgreSQL 12**, documented with **Swagger 2**, with storing the user base in LDAP with deployment to the server in a **Docker** container and hardware part based on **Arduino / ESP8266** (C++)

Here you will find an implementation of Restful API (Server), Web Single page application (Client), desctop WebSocketClient for API manipulation and hardware code.

Maked in IntelliJ IDEA, tested Postman / Fiddler / Chrome

## How to use application
If you start the application, you will see the main "Dash Board" page with parking lots cards.

### Main page
In this page you can see all parking lots and theirs statuses. By clicking on the card of parking lot, you will be able to see parking lot details.

### Statistics page
Here you can see all statistics records for last week period. You can sort data by date or choose only specified parking lot number.

### Parking layout page
This page represents an interactive parking plan with provided legend. By clicking on the parking lot, you will be able to see parking lot details.

## How to use API

API documentation avaliable with running application at http://server:port/swagger-ui.html or  http://server:port/v2/api-docs (JSON)

### DataBase

!! Before app start first create db 'parking' via pGAdmin or manually.

To setup the database you first need to install a PostgreSQL server [link](https://www.postgresql.org/download/). After you've installed it add the bin folder to your PATH system variable.
Backend application provides an automatic database structure creation for first launch. After first start it initiates database with parking lots data depending on the amount of indicated in com.isd.parking.model.ParkingNumber.class

<!-- CONTRIBUTING -->
### Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)

- **[MIT license](http://opensource.org/licenses/mit-license.php)**
- Copyright 2020 © <a href="http://isd-soft.com/" target="_blank">ISD internship Team</a>.
