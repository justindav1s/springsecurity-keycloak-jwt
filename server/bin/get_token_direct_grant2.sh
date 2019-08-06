#!/bin/bash

function padBase64  {
    STR=$1
    MOD=$((${#STR}%4))
    if [ $MOD -eq 1 ]; then
       STR="${STR}="
    elif [ $MOD -gt 1 ]; then
       STR="${STR}=="
    fi
    echo ${STR}
}


KEYCLOAK=http://127.0.0.1:8080
REALM="amazin"
GRANT_TYPE="password"
CLIENT="webapp"
CLIENT_SECRET="cc48f8ad-80d3-4019-8d36-7dfb6f5c86e7"
USER="test123"
USER_PASSWORD="test%C2%A3123"

echo "Keycloak host : $KEYCLOAK"

curl -vk \
    -d "client_secret=cc48f8ad-80d3-4019-8d36-7dfb6f5c86e7" \
    -d "client_id=webapp" \
    -d "username=test123" \
    -d "password=test123" \
    -d "grant_type=password" \
    "${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token"

echo "RESPONSE"=${RESPONSE}
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
echo ${PART2_BASE64} | base64 -D | jq .

echo $ACCESS_TOKEN



