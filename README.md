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
2. Login to IBM Bluemix account, create a IBM cloudant database and save the credentials and add those credentials in `application.conf` in `akka-epl/src/main/resources/application.conf`
2. cd akka-epl
3. Run `sbt` followed by commands `compile` and `run` . Make sure you choose `CrawlingApp.scala` as running class. This will crawl http://premierleague.com website and save data as JSON to IBM cloudant database.
4. In another commandline window or tab, Run `sbt` followed by commands `compile` and `run` . Make sure you choose `SoccerMainController.scala` as running class
5. In another command line tab, `cd soccer-epl-ui` and run `npm start`
6. you can now access the Dashboard in url: `http://locahost:3000`

# Deploying to Cloud Foundry
In order to deploy to Cloud Foundry, make sure you have an IBM Bluemix account. And you have to install the following to get started.

1. Install Cloud  Foundry CLI https://github.com/cloudfoundry/cli
2. Login to CF using: cf login --sso and use one-time password from a given URL to login
3. Create a fat jar using: sbt assembly after going to directory: /akka-epl
4. You need to have manifest.yml file as in the code repository to push it to the cloud foundry app
5. You can push the app using command: cf push
6. For Debugging you can see the logs to make sure your app is successfully pushed or not using cf logs akka-react-cloudant --recent
7. you can also ssh to the application machine using command: cf enable-ssh <app_name> and  cf ssh <app_name>

# Output
![Dashboard](./assets/dashboard.png?raw=true "Dashboard")
![Team Standing](./assets/teamstanding.png?raw=true "Team Standing")
![Results](./assets/results.png?raw=true "Results")
![Fixtures](./assets/fixtures.png?raw=true "Fixtures")


# License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
