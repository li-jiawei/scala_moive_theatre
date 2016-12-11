# scala_moive_theatre 
Our final project in 2016 Fall CSYE7200 Big Data System Using Scala
Authors are Jiawei Li and Qiaomin Ling
Our Professor is Dr. Robin Hillyard 

Project Description
	Two part
		a. Ticket sale
		b. Movie review sentiment analysis
For more details, please go through our final-presentation.


The followings are our milestones:

	11.4 -  11.11 	 Data collection

	11.11- 11.20 	 Sentiment analysis of movie review 

	11.20 - 12.2 	 Ticket agent

	12.2 - 12.9 	 Test and evaluate

File Description

	1. databaseCRUD.
		sql: the SQL statements we use.
	2. final-presentation: 
		our final presentation for the project
	3. plan-presentation: 
		our plan presentation for the project
	4. scala-ticket-test.jmx: jMeter script to do the load test
		a. please download Apache jMeter
		b.  use Non-GUI mode to do load test
		c. jmeter -n -t scala-ticket-test.jmx -l test.csv
	5. sentiment_model: 
		a scala application that generates Naive Bayes model by utilizing RDD-based API and DataFrame-based API, also use stanford core NLP to do sentiment analysis
	6. web_application: a web application based on play framework. 