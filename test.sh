#!/bin/sh
export MYSQL_URL='jdbc:mysql://localhost:3306/accountmanagement'
export MYSQL_USERNAME='root'
export MYSQL_PASSWORD='root'
export REDIS_HOST='localhost'
export REDIS_PORT='6379'

mvn test