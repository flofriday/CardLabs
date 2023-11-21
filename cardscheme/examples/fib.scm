(define fib (lambda n
              (if (< n 3)
                1
                (+ (fib (- n 1)) (fib (- n 2)))
                )))

(display (fib 1))
(display (fib 2))
(display (fib 3))
(display (fib 4))
(display (fib 5))
(display (fib 6))