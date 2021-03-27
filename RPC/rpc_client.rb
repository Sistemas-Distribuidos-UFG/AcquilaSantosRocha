#!/usr/bin/env ruby

require 'bunny'
require 'thread'
require_relative 'message'
require 'logging'

include Message


class Client
  attr_accessor :call_id, :response, :lock, :condition, :connection,
                :channel, :server_queue_name, :reply_queue, :exchange

  def initialize server_queue_name
    @connection = Bunny.new(automatically_recover: false)
    @connection.start

    @channel = connection.create_channel
    @exchange = channel.default_exchange
    @server_queue_name = server_queue_name

    setup_reply_queue
  end

  def stop
    channel.close
    connection.close
  end

   def call
    @call_id = generate_uuid
    jj = self._gatherData
    exchange.publish(jj,
                     routing_key: server_queue_name,
                     correlation_id: call_id,
                     reply_to: reply_queue.name)

    # wait for the signal to continue the execution
    lock.synchronize { condition.wait(lock) }

    response
  end

  private

  def generate_uuid
    "#{rand}#{rand}#{rand}"
  end

  def setup_reply_queue
    @lock = Mutex.new
    @condition = ConditionVariable.new

    @reply_queue = channel.queue('', exclusive: true)

    reply_queue.subscribe do |_delivery_info, properties, payload|
      if properties[:correlation_id] == self.call_id
        self.response = payload.to_i
        self.lock.synchronize { self.condition.signal }
      end
    end
  end
end

class Question1 < Client

 
  def _gatherData
    print "[Nome] "
    nome = STDIN.gets.chomp
    print "[Cargo] "
    cargo = STDIN.gets.chomp
    print "[Salário] "
    salario = STDIN.gets.chomp
    data = {'nome': nome.downcase, 'cargo': cargo, 'salario': salario}
    return Message.dumps(data, 1)
  end
end
      

class Question2 < Client

  def _gatherData
    print "[Nome] "
    nome = STDIN.gets.chomp
    print "[Sexo] "
    sexo = STDIN.gets.chomp
    print "[Idade] "
    idade = STDIN.gets.chomp
    data = {'nome': nome.downcase, 'sexo': sexo, 'idade': idade}
    return Message.dumps(data, 2)
  end
end

class Question3 < Client

  def _gatherData
    print "[N1] "
    n1 = STDIN.gets.chomp
    print "[N2] "
    n2 = STDIN.gets.chomp
    print "[N3] "
    n3 = STDIN.gets.chomp
    data = {'N1': n1, 'N2': n2, 'N3': n3}
    return Message.dumps(data, 3)
  end
end
      
class Question4 < Client

  def _gatherData
    print "[Altura] "
    altura = STDIN.gets.chomp
    print "[Sexo] "
    sexo = STDIN.gets.chomp
    data = {'altura': altura, 'sexo': sexo}

    return Message.dumps(data, 4)
  end
end

class Question5 < Client


  def _gatherData
    print "[Idade] "
    idade = STDIN.gets.chomp
    data = {'idade': idade}
    return Message.dumps(data, 5)
  end
end

class Question6 < Client

  def _gatherData
    print "[Nome] "
    nome = STDIN.gets.chomp
    print "[Nível] "
    nivel = STDIN.gets.chomp
    print "[Salário] "
    salario = STDIN.gets.chomp
    print "[Dependentes] "
    dependentes = STDIN.gets.chomp
    data = {'nome': nome.downcase(), 'nivel': nivel, 'salario': salario, 'dependentes': dependentes}
    return Message.dumps(data, 6)
  end
end

class Question7 < Client


  def _gatherData
    print "[Idade] "
    idade = STDIN.gets.chomp
    print "[Tempo Serviço] "
    tempo = STDIN.gets.chomp
    data = {'idade': idade, 'tempo': tempo}
    return Message.dumps(data, 7)
  end
end

class Question8 < Client


  def _gatherData
    print "[Saldo] "
    saldo = STDIN.gets.chomp
    data = {'saldo': saldo}
    return Message.dumps(data, 8)
  end
end

class Question9 < Client
  def _gatherData
    print "[Valor] "
    valor = STDIN.gets.chomp
    print "[Naipe] "
    naipe = STDIN.gets.chomp
    data = {'valor': valor, 'naipe': naipe}
    return Message.dumps(data, 9)
  end
end


class Main

  attr_accessor :problem_number, :logger

  def initialize problem_number, logger
    @logger = logger
    @problem_number = problem_number

    execute

  end


  def execute 
    question_class = Object.const_get("Question#{problem_number}")
    client = question_class.new('rpc_queue')

    logger.info "Requesting solution to problem #{problem_number}" if logger
    response = client.call
    logger.info "Server respond #{response}" if logger

    client.stop
  end
end


USAGE = <<ENDUSAGE
Usage:
   RPC Client [-h] problem [-al activate_log]
ENDUSAGE

HELP = <<ENDHELP
   -h,  --help            Show this help.
   -al, --activate_log    Activate the console log.
   -l,  --log_level       Set the log level.
ENDHELP

ARGS = { :shell=>'default', :writer=>'chm' } # Setting default values
UNFLAGGED_ARGS = [ :problem ]              # Bare arguments (no flag)
next_arg = UNFLAGGED_ARGS.first
ARGV.each do |arg|
  case arg
    when '-h','--help'            then ARGS[:help]              = true
    when '-al','--activate_log'   then ARGS[:activate_log]      = true
    when '-l','--log_level'       then next_arg                 = :log_level
    else
      if next_arg
        ARGS[next_arg] = arg
        UNFLAGGED_ARGS.delete( next_arg )
      end
      next_arg = UNFLAGGED_ARGS.first
  end
end


if ARGS[:help] or !ARGS[:problem]
  puts USAGE
  puts HELP if ARGS[:help]
  exit
end

if ARGS[:activate_log]
  Logging.color_scheme( 'bright',
    :levels => {
      :info  => :green,
      :warn  => :yellow,
      :error => :red,
      :fatal => [:white, :on_red]
    },
    :date => :blue,
    :logger => :cyan,
    :message => :magenta
  )

  Logging.appenders.stdout(
    'stdout',
    :layout => Logging.layouts.pattern(
      :pattern => '[%d] %-5l %c: %m\n',
      :color_scheme => 'bright'
    )
  )
  @logger = Logging.logger('rpc_client.log')
  @logger.add_appenders 'stdout'
  if ARGS[:log_level] and [:info, :warn, :error, :fatal].include? ARGS[:log_level].to_sym
    @logger.level = ARGS[:log_level].to_sym
  else
    @logger.level = :info
  end
end


mainobj = Main.new ARGS[:problem], @logger
mainobj.execute
