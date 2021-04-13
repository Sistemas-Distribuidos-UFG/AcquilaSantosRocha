#ifndef DEFS_HPP
#define DEFS_HPP

#define PRODUCER_LOG "producer.log"
#define CONSUMER_LOG "consumer.log"

#ifndef SIZE_MAX
#ifdef __SIZE_MAX__
#define SIZE_MAX __SIZE_MAX__
#else
#define SIZE_MAX (size_t)-1
#endif//ifdef __SIZE_MAX__
#endif//ifndef SIZE_MAX

#ifdef __linux__
#include <unistd.h>
#include <sys/syscall.h>
#include <string>
#define GET_TID syscall(SYS_gettid)
#else
#include <thread>
#define GET_TID std::this_thread::get_id()

#endif

#endif