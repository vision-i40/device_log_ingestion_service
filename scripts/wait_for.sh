#!/usr/bin/env bash

until nc -z $1 $2
do
    echo "No connection with $1:$2"
    sleep 1
done
