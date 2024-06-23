# turingmachine.clj
A simple way to simulate turingmachines using clojure!

> Note that I would like to optimize this repository and also make use of the "gödel number" in the future. This is just a simple implementation.

# Examples
You can find the examples [here](./src/examples/).

## input word must have prefix 00
```clojure
(load-file "../turingmachine.clj")

(ns turingmachine)

;; turingmachine which accepts the language {0^2 1^n | n >= 0}
;; <=> the language of all binary strings with 00 as the prefix
(def t1 (Transition. :q1 "0" :q3 "0" :right))
(def t2 (Transition. :q3 "0" :q2 "0" :right))
(def t3 (Transition. :q2 "1" :q2 "1" :right))
(def transitions (list t1 t2 t3))
(def states (list :q1 :q2 :q3))
(def input-symbols (list "0" "1" "_"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))
(def utm (UniversalTuringmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))

(def inputs (list "00111111" "0111" "00"))
(doseq [input inputs] (println (emulate-utm utm input)))
```
```clojure
{:input 00111111, :output 00111111, :endState :q2, :verdict :accepted}
{:input 0111, :output 0111, :endState :q3, :verdict :rejected}
{:input 00, :output 00, :endState :q2, :verdict :accepted}
```

## invert input word
```clojure
(load-file "../turingmachine.clj")

(ns turingmachine)

;; turingmachine which inverts the input word, which must be of length 1 atleast
(def t1 (Transition. :q1 "0" :q2 "1" :right))
(def t2 (Transition. :q1 "1" :q2 "0" :right))
(def t3 (Transition. :q2 "0" :q2 "1" :right))
(def t4 (Transition. :q2 "1" :q2 "0" :right))
(def transitions (list t1 t2 t3 t4))
(def states (list :q1 :q2))
(def input-symbols (list "0" "1" "_"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))
(def utm (UniversalTuringmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
(def inputs (list "000" "111" "10111011" "0" "1" ""))
(doseq [input inputs] (println (emulate-utm utm input)))
```
```clojure
{:input 000, :output 111, :endState :q2, :verdict :accepted}
{:input 111, :output 000, :endState :q2, :verdict :accepted}
{:input 10111011, :output 01000100, :endState :q2, :verdict :accepted}
{:input 0, :output 1, :endState :q2, :verdict :accepted}
{:input 1, :output 0, :endState :q2, :verdict :accepted}
{:input , :output , :endState :q1, :verdict :rejected}
```
