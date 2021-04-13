#include <iostream>
#include <fstream>
#include <cerrno>
#include <cinttypes>

#include "defs.hpp"
#include "threadhelper.hpp"

using namespace std;

void checkArguments(const char *argv[], std::size_t *numProducers, std::size_t *numConsumers, std::size_t *buffer_capacity, std::size_t *target){
    std::size_t argCheck;
    unsigned char i = 1;
    for(; i < 5; ++i){
      if(argv[i][0] == '-')
        break;
      errno = 0;
      argCheck = std::strtoumax(argv[i], NULL, 10);
      if(errno == ERANGE || argCheck == 0)
        break;
      switch(i){
          case 1:
            *numProducers = argCheck;
            continue;
          case 2:
            *numConsumers = argCheck;
            continue;
          case 3:
            *buffer_capacity = argCheck;
            continue;
          case 4:
            *target = argCheck;
            return;
      }
    }
    std::cerr << "argument " << i << " (\'" << argv[i] << "\') not valid. Please provide a positive integer no greater than " << SIZE_MAX << std::endl;
    std::cerr << "Usage: " << argv[0] << " <# producer threads> <# consumer threads> <buffer size> <# items to produce>" << std::endl;
}

int main(const int argc, const char *argv[]){
    if(argc != 5){
      std::cerr << "Usage: " << argv[0] << " <# producer threads> <# consumer threads> <buffer size> <# items to produce>" << std::endl;
      return EXIT_FAILURE;
    }
    std::size_t numProducers, numConsumers, buffer_capacity, target;
    checkArguments(argv, &numProducers, &numConsumers, &buffer_capacity, &target);
    
    pcbb::ThreadHelper threads;
    threads.run(numProducers, numConsumers, buffer_capacity, target);

    if(threads.producerLogWritten()){
      std::ifstream producerLog;
      producerLog.exceptions(std::ifstream::failbit);
      std::string buff;
      try{
          producerLog.open(PRODUCER_LOG);
          std::cout << "Reading from " PRODUCER_LOG ":" << std::endl;
          while(std::getline(producerLog, buff))
            std::cout << buff << std::endl;
      }
      catch(const std::ifstream::failure& e){
          if(producerLog.eof())
            std::cout << "End of " PRODUCER_LOG << std::endl << std::endl;
          else
            std::cerr << "An error occurred while attempting to read from " PRODUCER_LOG << std::endl << std::endl;
      }
      if(producerLog.is_open())
          producerLog.close();
    }
    else{
      std::cerr << PRODUCER_LOG " was not written to, so it will not be read." << std::endl << std::endl;
    }

    if(threads.consumerLogWritten()){
      std::ifstream consumerLog;
      consumerLog.exceptions(std::ifstream::failbit);
      std::string buff;
      try{
          consumerLog.open(CONSUMER_LOG);
          std::cout << "Reading from " CONSUMER_LOG ":" << std::endl;
          while(std::getline(consumerLog, buff))
            std::cout << buff << std::endl;
      }
      catch(const std::ifstream::failure& e){
          if(consumerLog.eof())
            std::cout << "End of " CONSUMER_LOG << std::endl;
          else
            std::cerr << "An error occurred while attempting to read from " CONSUMER_LOG << std::endl;
      }
      if(consumerLog.is_open())
          consumerLog.close();
    }
    else {
      std::cerr << CONSUMER_LOG " was not written to, so it will not be read." << std::endl;
    }
    
    return EXIT_SUCCESS;
}//end of main()