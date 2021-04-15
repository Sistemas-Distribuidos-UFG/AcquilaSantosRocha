#ifndef QUEUE_HPP
#define QUEUE_HPP

namespace pcbb {
    template<typename T>
    class queue {
        protected:
            size_t head, tail, size;
            const size_t capacity;
            std::unique_ptr<T[]> array;
        public:
            explicit queue(const size_t cap) : capacity(cap) {
                array = std::make_unique<T[]>(this->capacity);
                tail = this->capacity - 1;
                head = 0;
                size = 0;
            }

            ~queue() {
                array.reset(nullptr);
            }

            inline size_t enqueue(const T val) noexcept{
                tail = (tail + 1) % capacity;
                array[tail] = val;
                size++;
                return tail;
            }

            inline size_t dequeue(T *const val) noexcept{
                T old_head = head;
                *val = array[head];
                size--;
                head = (old_head + 1) % capacity;
                return old_head;
            }

            inline bool isEmpty() const noexcept {
                return size == 0;
            }

            inline bool isFull() const noexcept {
                return size == capacity;
            }
    };
}

#endif