(define fib (lambda n
              (if (< n 3)
                1
                (+ (fib (- n 1)) (fib (- n 2)))
                )))

(display (fib 1))
(newline)
(display (fib 2))
(newline)
(display (fib 3))
(newline)
(display (fib 4))
(newline)
(display (fib 5))
(newline)
(display (fib 6))