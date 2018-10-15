#!/usr/bin/env bash

HOST=$1
PORT=$2
USERNAME=$3
PASSWORD=$4

while ! curl -i -u $USERNAME:$PASSWORD http://$HOST:$PORT/api/overview; do
    sleep 1;
done