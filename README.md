# restfulpi
A general purpose REST API for controlling Raspberry Pi GPIO pins over HTTP

##Basic Usage
Download the latest release, copy to your Pi, and run with "sudo java -jar restfulPi.jar". Using the API is roughly similar to locally using GPIO. A pin must be provisioned, and can then be used.

###With default options

* ###Provisioning a pin:
  
    POST http://\<pi address\>:8080/pins/\<pin number\>
    
    JSON body: {"name":"\<pin name\>","initialState":"\<high or low\>"}
  
* ###Getting the pin state of provisioned pin:

    GET http://\<pi address\>:8080/pins/\<pin number\>
  
* ###Getting All provisioned pins:

    GET http://\<pi address\>:8080/pins
  
* ###Setting a provisioned pin to high:

    PUT http://\<pi address\>:8080/pins/\<pin number\>/high
  
* ###Setting a provisioned pin to low:

    PUT http://\<pi address\>:8080/pins/\<pin number\>/low
    
##Advanced configuration
You can change defaults and set up advanced features using a properties file. RestfulPi will automatically check the for file /home/pi/rest.properties and use the settings in it. You can specify a different file by adding the java option -Drestproperties="\<file path and name\>". RestfulPi has the following options:
* port: Change the port that RestfulPi will run on
* output_pins: pins to automatically provision on start up. Pins are comma seperated and in the format \<pin number\>:\<pin name\>
* CORS_headers: include on CORS headers for cross site requests
* web_directory: RestfulPi will serve static web content from the specified folder
* ~~ssl_on: turn on ssl. This requires the keystore and keystore_password properties be set~~
* ~~basic_auth_on: turn on basic HTTP authentication using Jettys HashLoginService. This requires an additional properties file to specify users, passwords, and roles. The file is given in the auth_realm_properties option~~
