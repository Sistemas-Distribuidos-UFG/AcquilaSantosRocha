USAGE = <<ENDUSAGE
Usage:
   RPC [-h] problem [-al activate_log]
ENDUSAGE

HELP = <<ENDHELP
   -h,  --help            Show this help.
   -al, --activate_log    Activate the console log.
   -l,  --log_level       Set the log level.
ENDHELP

ARGS = { :shell=>'default', :writer=>'chm' } # Setting default values
UNFLAGGED_ARGS = [ :problem ]              # Bare arguments (no flag)


USAGE2 = <<ENDUSAGE2
Usage:
   RPC Server [-h] [-al activate_log]
ENDUSAGE2

HELP2 = <<ENDHELP2
   -h,  --help            Show this help.
   -al, --activate_log    Activate the console log.
   -l,  --log_level       Set the log level.
ENDHELP2

ARGS2 = { :shell=>'default', :writer=>'chm' } # Setting default values
UNFLAGGED_ARGS2 = [ ]              # Bare arguments (no flag)

module Help
  def init
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
        :message => :white
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
    return @logger
  end

  def init2
    next_arg = UNFLAGGED_ARGS2.first
    ARGV.each do |arg|
      case arg
        when '-h','--help'            then ARGS2[:help]              = true
        when '-al','--activate_log'   then ARGS2[:activate_log]      = true
        when '-l','--log_level'       then next_arg                 = :log_level
        else
          if next_arg
            ARGS2[next_arg] = arg
            UNFLAGGED_ARGS2.delete( next_arg )
          end
          next_arg = UNFLAGGED_ARGS2.first
      end
    end


    if ARGS2[:help]
      puts USAGE2
      puts HELP2 if ARGS2[:help]
      exit
    end

    if ARGS2[:activate_log]
      Logging.color_scheme( 'bright',
        :levels => {
          :info  => :green,
          :warn  => :yellow,
          :error => :red,
          :fatal => [:white, :on_red]
        },
        :date => :blue,
        :logger => :cyan,
        :message => :white
      )

      Logging.appenders.stdout(
        'stdout',
        :layout => Logging.layouts.pattern(
          :pattern => '[%d] %-5l %c: %m\n',
          :color_scheme => 'bright'
        )
      )
      @logger = Logging.logger('rpc_server.log')
      @logger.add_appenders 'stdout'
      if ARGS2[:log_level] and [:info, :warn, :error, :fatal].include? ARGS2[:log_level].to_sym
        @logger.level = ARGS2[:log_level].to_sym
      else
        @logger.level = :info
      end
    end
    return @logger
  end
end
