set port=8000
set ip="localhost"
set username="aPsDMJnpQD9dTdEx3qfgoPy9YcJfz8iridqunderTSRLXYSbKasTe8xPLrdpTkAdY7BdpedzRYTunder_id-habit-app-service-eureka"
set password="LCwJSPdLDjYZPI57E5UPDBBdPsPKCaWUVz8fMYQYY7ZDCUhgnYHyMEtuI4bbCkSGvwjpjLph4KcBtNBXKBn8YX6udkZrQZ2FKShBDCC6ynyj463sXS9V4KS4HpC3yBDDX9SNXYbGPLJyznSP52zV8YD7xDySjCcHXa354MnPKd3Gs3AGBB6PQvEtUDSm4JdS"

setx PORT_EUREKA %port%  /M
setx PROFILES "dev" /M
setx HOSTNAME_EUREKA %ip% /M
setx USERNAME_EUREKA %username% /M
setx PASSWORD_EUREKA %password% /M
setx GIT_BRANCH "main" /M
setx DNS "%ip%:%port%"  /M
setx HTTP "http" /M 
setx BASIC_AUTH_EUREKA "%username%:%password%@" /M
refreshenv