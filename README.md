# akka-react-cloudant
A Soccer Dashboard created by scraping EPL website using Akka backend and ReactJS frontend and IBM Cloudant for object storage

# Architecture
![Architecture](./assets/soccer%20epl%20architecture.jpg?raw=true "Architecture")


# Components 
The project consists of 3 components
* Data collection using akka-actors
* REST API using akka-http
* ReactJS front end (which this blog doesn't cover)

# Steps
1. clone the project usig `git clone git@github.com:sanjeevghimire/akka-react-cloudant.git`
2. cd akka-epl
3. Run `sbt` followed by commands `compile` and `run` . Make sure you choose `CrawlingApp.scala` as running class. This will crawl http://premierleague.com website and save data as JSON to IBM cloudant database.
4. In another commandline window or tab, Run `sbt` followed by commands `compile` and `run` . Make sure you choose `SoccerMainController.scala` as running class
5. In another command line tab, `cd soccer-epl-ui` and run `npm start`
6. you can now access the Dashboard in url: `http://locahost:3000`

# Output
![Dashboard](./assets/dashboard.png?raw=true "Dashboard")
![Team Standing](./assets/teamstanding.png?raw=true "Team Standing")
![Results](./assets/results.png?raw=true "Results")
![Fixtures](./assets/fixtures.png?raw=true "Fixtures")


# License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
