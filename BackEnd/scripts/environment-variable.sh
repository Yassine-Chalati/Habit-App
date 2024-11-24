#!/bin/bash

port=8000
ip="localhost"
username="aPsDMJnpQD9dTdEx3qfgoPy9YcJfz8iridqunderTSRLXYSbKasTe8xPLrdpTkAdY7BdpedzRYTunder_id-habit-app-service-eureka"
password="LCwJSPdLDjYZPI57E5UPDBBdPsPKCaWUVz8fMYQYY7ZDCUhgnYHyMEtuI4bbCkSGvwjpjLph4KcBtNBXKBn8YX6udkZrQZ2FKShBDCC6ynyj463sXS9V4KS4HpC3yBDDX9SNXYbGPLJyznSP52zV8YD7xDySjCcHXa354MnPKd3Gs3AGBB6PQvEtUDSm4JdS"


export PORT_EUREKA=$port
echo $PORT_EUREKA
export PROFILES="dev"
echo $PROFILES
export HOSTNAME_EUREKA=$ip
export USERNAME_EUREKA=$username
export PASSWORD_EUREKA=$password
export GIT_BRANCH="main"
export DNS="$ip:$port"
export HTTP="http"
export BASIC_AUTH_EUREKA="$username:$password@"
