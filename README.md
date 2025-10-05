<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->

<a id="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![project_license][license-shield]][license-url]

<br />
<div align="center">
  <a href="https://github.com/LoveDoLove-School-Projects/Musical_Instruments">
    <img src="web/assets/image/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Musical Instruments Management System</h3>

  <p align="center">
    A web-based application for managing musical instruments, customers, orders, and staff, built with Java EE, JSP, Servlets, and MySQL.
    <br />
    <a href="https://github.com/LoveDoLove-School-Projects/Musical_Instruments"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/LoveDoLove-School-Projects/Musical_Instruments">View Demo</a>
    &middot;
    <a href="https://github.com/LoveDoLove-School-Projects/Musical_Instruments/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    &middot;
    <a href="https://github.com/LoveDoLove-School-Projects/Musical_Instruments/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

## About The Project

Musical Instruments Management System is a Java EE web application designed to manage musical instruments, customer accounts, orders, staff, and transactions. The system supports user registration, authentication (including 2FA), product management, order processing, and administrative features. It is suitable for educational and small business use.

**Key Features:**

- User and staff authentication (with 2FA)
- Product catalog and management
- Shopping cart and order processing
- Billing and payment integration
- Admin and staff dashboards
- Security logging and password management

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

- Java EE (Servlets, JSP)
- Apache Tomcat / GlassFish
- MySQL
- HTML5, CSS3, JavaScript
- JSTL

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting Started

To get a local copy up and running, follow these steps.

### Prerequisites

- Java JDK 8 or higher
- Apache Tomcat 9+ or GlassFish 5+
- MySQL Server
- Apache Ant (for build.xml)

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/LoveDoLove-School-Projects/Musical_Instruments.git
   ```
2. Import the project into your IDE (NetBeans, IntelliJ IDEA, Eclipse, etc.) as a Java web project.
3. Set up the MySQL database:
   - Import the SQL scripts from `Musical_Instruments_DB/` (e.g., `AllInOneCreate.sql`, `AllInOneInsert.sql`).
4. Configure database connection in `src/conf/persistence.xml` and `build/web/WEB-INF/glassfish-resources.xml` as needed.
5. Build the project using Ant or your IDE.
6. Deploy the generated WAR file to your application server (Tomcat/GlassFish).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Usage

1. Access the application via your browser at `http://localhost:8080/Musical_Instruments` (or the context path you configured).
2. Register as a new user or log in as staff/admin.
3. Browse products, add to cart, and complete orders.
4. Admins and staff can manage products, view transactions, and handle user accounts.

_For more details, refer to the documentation in the `web/pages/` and `src/java/` folders._

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request. For major changes, open an issue first to discuss what you would like to change.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Top contributors:

<a href="https://github.com/LoveDoLove-School-Projects/Musical_Instruments/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=LoveDoLove-School-Projects/Musical_Instruments" alt="contrib.rocks image" />
</a>

## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact

LoveDoLove - [@LoveDoLove](https://github.com/LoveDoLove)

Project Link: [https://github.com/LoveDoLove-School-Projects/Musical_Instruments](https://github.com/LoveDoLove-School-Projects/Musical_Instruments)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Acknowledgments

- [othneildrew/Best-README-Template](https://github.com/othneildrew/Best-README-Template)
- [Java EE Documentation](https://javaee.github.io/)
- [MySQL Documentation](https://dev.mysql.com/doc/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[contributors-shield]: https://img.shields.io/github/contributors/LoveDoLove-School-Projects/Musical_Instruments.svg?style=for-the-badge
[contributors-url]: https://github.com/LoveDoLove-School-Projects/Musical_Instruments/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/LoveDoLove-School-Projects/Musical_Instruments.svg?style=for-the-badge
[forks-url]: https://github.com/LoveDoLove-School-Projects/Musical_Instruments/network/members
[stars-shield]: https://img.shields.io/github/stars/LoveDoLove-School-Projects/Musical_Instruments.svg?style=for-the-badge
[stars-url]: https://github.com/LoveDoLove-School-Projects/Musical_Instruments/stargazers
[issues-shield]: https://img.shields.io/github/issues/LoveDoLove-School-Projects/Musical_Instruments.svg?style=for-the-badge
[issues-url]: https://github.com/LoveDoLove-School-Projects/Musical_Instruments/issues
[license-shield]: https://img.shields.io/github/license/LoveDoLove-School-Projects/Musical_Instruments.svg?style=for-the-badge
[license-url]: https://github.com/LoveDoLove-School-Projects/Musical_Instruments/blob/main/LICENSE
[product-screenshot]: web/assets/image/screenshot.png
