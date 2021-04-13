#ifndef THREAD_HELPER_HPP
#define THREAD_HELPER_HPP

#include <string>
#include <cstddef>
#include <fstream>
#include <thread>
#include <mutex>
#include <condition_variable>


#include "defs.hpp"
#include "queue.hpp"

namespace pcbb { // Producer-Consumer Bounded Buffer
    class ThreadHelper {
        private:
            bool lastProducerStatus = false, lastConsumerStatus = false;
        public:
            void run(std::size_t numProducers, std::size_t numConsumers, std::size_t bufferBound, std::size_t target);

            bool consumerLogWritten() {
                return this->lastConsumerStatus;
            }

            bool producerLogWritten() {
                return this->lastProducerStatus;
            }

    };

    namespace tds { // Threads
        using ulock_t = std::unique_lock<std::mutex>;
        using CV = std::condition_variable;

        class Base {
            private:
                std::mutex *mutex;
                CV *const waitCV, *const signalCV;
                // const std:string type = "generic";
            protected:
                const size_t numThreads, target;
                size_t numProcessed;
                std::unique_ptr<std::thread[]> threads;
                std::ofstream logfile;
                queue<long> *buffer;

                inline void lock(){
                    mutex->lock();
                }

                inline void unlock(){
                    mutex->unlock();
                }

                inline void wait(){
                    ulock_t mtx(*mutex, std::adopt_lock);
                    waitCV->wait(mtx);
                    mtx.release();
                }

                inline void notify(){
                    signalCV->notify_all();
                }

                virtual void routine(){};
            public:
                Base(size_t target, size_t count, std::mutex *mtx, CV *wait, CV *sig, queue<long> *buff)
                    : waitCV(wait), signalCV(sig), numThreads(count), target(target)
                {
                    logfile.exceptions(std::ofstream::badbit);
                    threads = std::make_unique<std::thread[]>(numThreads);
                    mutex = mtx;
                    buffer = buff;
                    numProcessed = 0;
                }

                virtual ~Base() {
                    if(logfile.is_open())
                        logfile.close();
                    threads.reset(nullptr);
                }

                virtual inline void fork() {
                    lock(); // Lock Mutex
                    try {
                        for(size_t i = 0; i < numThreads; i++) {
                            threads[i] = std::thread(&Base::routine, this);
                        }
                    } catch (const std::system_error& e) {
                        throw std::runtime_error("Failed to fork a thread.");
                    }
                    unlock();

                }

                virtual inline size_t join() {
                    try {
                        for(size_t i = 0; i < numThreads; i++) {
                            threads[i].join();
                        }
                    } catch (const std::system_error& e) {
                        throw std::runtime_error("Failed to join a thread.");
                    }
                    return numProcessed;
                }

                inline bool logfile_is_open() const {
                    return logfile.is_open();
                }

                inline void close_logfile() {
                    if(logfile.is_open())
                        logfile.close();
                }
        };
        class ProducerThreads : public pcbb::tds::Base {
            // private:
                // const std:string type = "producer";
            protected:
                virtual void routine();
            public:
                ProducerThreads(size_t target, size_t count, std::mutex *mtx, CV *canPro, CV *canCon, queue<long> *buff);
                virtual ~ProducerThreads(){};
        };

        class ConsumerThreads : public pcbb::tds::Base {
            // private:
                // const std:string type = "consumer";
            protected:
                virtual void routine();
            public:
                ConsumerThreads(size_t target, size_t count, std::mutex *mtx, CV *canPro, CV *canCon, queue<long> *buff);
                virtual ~ConsumerThreads(){};
        };
    }   
}
#endif
