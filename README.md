# Soccer Dashboard with Akka and ReactJS

[![Build Status](https://travis-ci.org/IBM/akka-react-cloudant.svg?branch=master)](https://travis-ci.org/IBM/akka-react-cloudant)

In this code pattern, we will create a Soccer Dashboard for English Premier League. The dashboard is created by web crawling the https://www.premierleague.com/ website. The back end utilizes Akka Actor, the front end is done with ReactJS and the data storage is using IBM Cloudant. And the code is deployed on Cloud Foundry.

# Flow
![Architecture](./assets/soccer%20epl%20architecture.jpg?raw=true "Architecture")

1. Create actors for Akka
2. Expose Akka rest APIs
3. Crawler actor will start crawling and store information into DB
4. Deploy the app into IBM Cloud Foundry
5. Ready for user to interact with the app

# Included components
1. Akka: A reactive stream toolkit
2. ReactJS: A JavaScript library for building user interfaces
3. Cloudant DB: A highly scalable and performant JSON database service
4. Cloud Foundry: An open source, multi cloud application platform as a service project


# Steps
We will be deploying to Cloud Foundry for exposing the APIs from Akka that connects to the IBM Cloudant Database.

# Deploying Locally
1. clone the project using `git clone git@github.com:sanjeevghimire/akka-react-cloudant.git`
2. Login to IBM Bluemix account, create a IBM cloudant database and save the credentials and add those credentials in `application.conf` in `akka-epl/src/main/resources/application.conf`
2. cd akka-epl
3. Run `sbt` followed by commands `compile` and `run` . Make sure you choose `CrawlingApp.scala` as running class. This will crawl https://www.premierleague.com/ website and save data as JSON to IBM cloudant database
4. In another command line window or tab, run `sbt` followed by commands `compile` and `run`. Make sure you choose `SoccerMainController.scala` as running class
5. In another command line tab, `cd soccer-epl-ui` and run `npm start`
6. you can now access the Dashboard in url: `http://locahost:3000`

# Deploying to Cloud Foundry
In order to deploy to Cloud Foundry, make sure you have an IBM Bluemix account. And you have to install the following to get started.

1. Install [Cloud Foundry CLI](https://github.com/cloudfoundry/cli)
2. Login to CF using: `cf login --sso` and use one-time password from a given URL to login
3. Create a fat jar using: `sbt assembly` after going to directory: `/akka-epl`
4. You need to have `manifest.yml` file as in the code repository to push it to the cloud foundry app
5. You can push the app using command: `cf push`
6. For Debugging you can see the logs to make sure your app is successfully pushed or not using `cf logs akka-react-cloudant --recent`
7. you can also ssh to the application machine using command: `cf enable-ssh <app_name>` and  `cf ssh <app_name>`

# Sample Output
![Dashboard](./assets/dashboard.png?raw=true "Dashboard")
![Team Standing](./assets/teamstanding.png?raw=true "Team Standing")
![Results](./assets/results.png?raw=true "Results")
![Fixtures](./assets/fixtures.png?raw=true "Fixtures")

# Links

<ul>
 	<li><a href="https://doc.akka.io/docs/akka/current/scala/guide/introduction.html">Introduction to Akka</a></li>
 	<li><a href="https://doc.akka.io/docs/akka-http/current/scala/http/introduction.html">Introduction to Akka HTTP</a></li>
 	<li><a href="https://reactjs.org/">ReactJS</a></li>
 	<li><a href="https://github.com/cloudfoundry/cli">Cloud Foundry CLI</a></li>
 	<li><a href="https://docs.cloudfoundry.org/deploying/index.html">Deploying to Cloud Foundry</a></li>
</ul>


# License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
