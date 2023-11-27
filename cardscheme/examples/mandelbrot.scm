; Since card scheme doesn't have complex numbers we are building our own here
(define (make-cplx re im)
  (vector re im))

(define (cplx-re c)
  (vector-ref c 0))

(define (cplx-im c)
  (vector-ref c 1))

(define (cplx-add a b)
  (make-cplx (+ (cplx-re a) (cplx-re b)) (+ (cplx-im a) (cplx-im b))))

(define (cplx-square c)
  (make-cplx (- (* (cplx-re c) (cplx-re c)) (* (cplx-im c) (cplx-im c) )) (* 2 (cplx-re c) (cplx-im c))))

(define (cplx-abs c)
  (sqrt (+ (* (cplx-re c) (cplx-re c)) (* (cplx-im c) (cplx-im c)))))


; Map the xy cordinates to the imaginary numbers
(define (xy-to-cplx x y)
  (define startx (- 0 2.5))
  (define endx 1.5)
  (define starty 2)
  (define endy (- 0 2))
  (make-cplx
     (+ startx (* x (/ (- endx startx) 80)))
     (- starty (* y (abs (/ (- endy starty) 28))))
    ))

; A custom modulo implmentation
(define (mod-ten n)
  (do ((n n (- n 10)))
    ((< n 10) n)
  ))

; The iterative function deciding for each point wether or not it is in the set and and printing the desired character.
(define (mandelbrot c)
  (define  i (do ((i 0 (+ i 1))
                  (z (make-cplx 0 0) (cplx-add c (cplx-square z))))
                  ((or (>= i 64) (>= (cplx-abs z) 2)) i) ))
  (if (< i 64) (display (mod-ten i)) (display " "))
  )


; Iterate over all fields x and y fields we want to fill.
(do ((y 0 (+ y 1)))
  ((>= y 28))
  (do ((x 0 (+ x 1)))
    ((>= x 80))
      (mandelbrot (xy-to-cplx x y)))
  (newline))
