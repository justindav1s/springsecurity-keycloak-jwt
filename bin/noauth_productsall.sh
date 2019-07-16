#!/bin/bash

ACCESS_TOKEN=$(./get_token_direct_grant.sh)

echo $ACCESS_TOKEN

curl -v -X GET \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    http://127.0.0.1:8081/api/products/all

