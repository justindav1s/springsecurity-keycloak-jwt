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
#CLIENT="scopedapp"
#CLIENT_SECRET="c29d6502-e392-4c99-b025-3da1c8e8ca8c"
USER="justin"
USER_PASSWORD="12jnd34"

#echo "Keycloak host : $KEYCLOAK"


#Get Token
POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&client_secret=${CLIENT_SECRET}&username=${USER}&password=${USER_PASSWORD}"

#POST_BODY="grant_type=${GRANT_TYPE}&client_id=${CLIENT}&username=${USER}&password=${USER_PASSWORD}"


#echo "Keycloak host : $KEYCLOAK"
#echo POST_BODY=${POST_BODY}

RESPONSE=$(curl -qsk \
    -d ${POST_BODY} \
    -H "Content-Type: application/x-www-form-urlencoded" \
    ${KEYCLOAK}/auth/realms/${REALM}/protocol/openid-connect/token)

#echo "RESPONSE"=${RESPONSE}
ACCESS_TOKEN=$(echo ${RESPONSE} | jq -r .access_token)
PART2_BASE64=$(echo ${ACCESS_TOKEN} | cut -d"." -f2)
PART2_BASE64=$(padBase64 ${PART2_BASE64})
#echo ${PART2_BASE64} | base64 -D | jq .

echo $ACCESS_TOKEN



