#!/usr/bin/env ruby
require 'bunny'


require_relative 'message'
require_relative 'help'
require_relative 'solve'
require 'logging'


include Message
include Help


class Server
  attr_accessor :logger
  def initialize logger
    @logger = logger
    @connection = Bunny.new
    @connection.start
    @channel = @connection.create_channel
  end

  def start(queue_name)
    @queue = channel.queue(queue_name)
    @exchange = channel.default_exchange
    subscribe_to_queue
  end

  def stop
    channel.close
    connection.close
  end

  def loop_forever
    # This loop only exists to keep the main thread
    # alive. Many real world apps won't need this.
    loop { sleep 5 }
  end

  private

  attr_reader :channel, :exchange, :queue, :connection

  def subscribe_to_queue
    queue.subscribe do |_delivery_info, properties, payload|

      begin
        payload = Message.loads payload
      rescue => e
        logger.fatal "Fatal error: #{e}" if logger
        exit
      end

      begin
        result = Solve.new(payload['problem'], payload['data']).run
      rescue
        logger.error "Error while solving the problem. Returning '{}' instead"  if logger
        result = '{}'
      end

      logger.info "Responding #{result} to #{payload['data']}" if logger
      result = Message.dumps result, payload['problem']
      
      exchange.publish(
        result,
        routing_key: properties.reply_to,
        correlation_id: properties.correlation_id
      )
    end
  end
end

class Main
  attr_accessor :problem_number, :logger, :server

  def initialize logger
    @logger = logger
    @server = Server.new logger
  end

  def execute
    logger.info "Awaiting RPC requests" if logger
    begin
      server.start('rpc_queue')
      server.loop_forever
    rescue Interrupt => _
      logger.info "Server Disconnected" if logger
      server.stop
    end

  end
end



logger = Help.init2

mainobj = Main.new logger
mainobj.execute
  

  
  
 
