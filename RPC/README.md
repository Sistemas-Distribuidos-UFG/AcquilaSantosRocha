# RPC in ruby

## Requirements
- ruby (2.7.0p0)
- bunny (2.17.0)
- logger  (default: 1.4.2)
- logging  (2.3.0)
- rabbitMQ  (3.8.14)

## Instalation
Requires [RabbitMQ](https://www.rabbitmq.com/download.html) 3.8.14 to run.

Install Ruby
```sh
sudo apt-get install ruby-full
```
Install all gems
```sh
gem install bunny
gem install logging
gem rabbitMQ
```
## Helpers
Client Helper
```sh
Usage:
   RPC [-h] problem [-al activate_log]
   -h,  --help            Show this help.
   -al, --activate_log    Activate the console log.
   -l,  --log_level       Set the log level.
```
Server Helper
```sh
Usage:
   RPC [-h] problem [-al activate_log]
   -h,  --help            Show this help.
   -al, --activate_log    Activate the console log.
   -l,  --log_level       Set the log level.
```
## Running
### Example - Server
```sh
ruby rpc_server.rb -al -l info
```

### Example - Client to solve problem 8
```sh
ruby rpc_client.rb 8 -al -l info
```
