#include <iostream>
#include <chrono>
#include <iomanip>

#include "threadhelper.hpp"

using namespace std;
namespace pcbb {
    void ThreadHelper::run(std::size_t numProducers, std::size_t numConsumers, std::size_t bufferBound, std::size_t target)
    {
        queue<long> *buffer;
        try {
            buffer = new queue<long>(bufferBound);
        } catch (const std::bad_alloc& e) {
            std::cout << "Not Initialized buffer" << std::endl;
            std::exit(EXIT_FAILURE);
        }

        std::mutex mtx;
        std::condition_variable canProduce, canConsume;
        tds::Base *producers, *consumers;

        try {
            producers = new tds::ProducerThreads(target, numProducers, &mtx, &canProduce, &canConsume, buffer);
        } catch(const std::bad_alloc& e) {
            std::cout << "Failed to allocate memory for producer threads." << std::endl;
            std::exit(EXIT_FAILURE);
        }

        try {
            consumers = new tds::ConsumerThreads(target, numConsumers, &mtx, &canProduce, &canConsume, buffer);
        } catch(const std::bad_alloc& e) {
            std::cout << "Failed to allocate memory for consumer threads." << std::endl;
            std::exit(EXIT_FAILURE);
        }

        size_t numProduced, numConsumed;
        try {
            producers->fork();
            consumers->fork(); 
            numProduced = producers->join();
            numConsumed = producers->join();
        } catch(const std::runtime_error& e){
            std::exit(EXIT_FAILURE);
        }

        lastProducerStatus = producers->logfile_is_open();
        lastConsumerStatus = consumers->logfile_is_open();

        delete producers;
        delete consumers;
        delete buffer;

        std::cout << "Finished all threads" << std::endl;
        std::cout << "Produced: " << numProduced << std::endl << "Consumed: " << numConsumed << std::endl << std::endl;
    }

    namespace tds { // Threads
        ProducerThreads::ProducerThreads(size_t target, size_t count, std::mutex *mtx, CV *canPro, CV *canCon, queue<long> *buff)
            :Base(target, count, mtx, canPro, canCon, buff)
        {
            try {
                logfile.open(PRODUCER_LOG, std::fstream::out | std::fstream::trunc);
            } catch(const std::ofstream::failure& e){
                std::cerr << PRODUCER_LOG " could not be opened for writing" << std::endl;
            }
            srand48(std::time(nullptr));
        }
        void ProducerThreads::routine() {
            const auto threadId = GET_TID;
            struct timespec ts;
            lock();
            std::cout << "Producer thread " << threadId << " started." << std::endl;
            unlock();

            while(numProcessed < target) {
                std::this_thread::sleep_for(std::chrono::microseconds(5));
                lock();
                while(buffer->isFull()) {
                    wait();
                }
                if (numProcessed == target) {
                    std::cout << "Producer thread " << threadId << " finished." << std::endl;
                    unlock();
                    return;
                }
                const long num = lrand48();
                const size_t index = buffer->enqueue(num);
                timespec_get(&ts, TIME_UTC);
                if(logfile.is_open() && logfile)
                    logfile << ts.tv_sec << std::setfill('0') << std::setw(9) << ts.tv_nsec << std::setw(0) << " Producer " << threadId << " " << index << " " << num << std::endl;

                std::cout << "Producer thread " << threadId << " produced " << num << " and stored it at index " << index << std::endl;
                ++numProcessed;
                unlock();
                notify();
            }
            lock();
            std::cout << "Producer thread " << threadId << " finished." << std::endl;
            unlock();
        }

        ConsumerThreads::ConsumerThreads(size_t target, size_t count, std::mutex *mtx, CV *canPro, CV *canCon, queue<long> *buff)
            :Base(target, count, mtx, canPro, canCon, buff)
        {
            try {
                logfile.open(CONSUMER_LOG, std::fstream::out | std::fstream::trunc);
            } catch(const std::ofstream::failure& e){
                std::cerr << CONSUMER_LOG " could not be opened for writing" << std::endl;
            }
            srand48(std::time(nullptr));
        }

        void ConsumerThreads::routine() {
            const auto threadId = GET_TID;
            struct timespec ts;
            lock();
            std::cout << "Consumer thread " << threadId << " started." << std::endl;
            unlock();
            while(numProcessed < target){
                std::this_thread::sleep_for(std::chrono::microseconds(5));//sleep for 5 microseconds so that other consumer threads have a chance to acquire the mutex
                lock();
                while(numProcessed < target && buffer->isEmpty()){
                wait();
                }
                if(numProcessed == target){
                std::cout << "Consumer thread " << threadId << " finished." << std::endl;
                unlock();
                return;
                }

                long num;
                const size_t index = buffer->dequeue(&num);
                timespec_get(&ts, TIME_UTC);
                if(logfile.is_open() && logfile)
                logfile << ts.tv_sec << std::setfill('0') << std::setw(9) << ts.tv_nsec << std::setw(0) << " Consumer " << threadId << " " << index << " " << num << std::endl;

                std::cout << "Consumer thread " << threadId << " consumed " << num << " from index " << index << std::endl;
                ++numProcessed;
                unlock();
                notify();
            }
            lock();
            std::cout << "Consumer thread " << threadId << " finished." << std::endl;
            unlock();
        }
    }
}