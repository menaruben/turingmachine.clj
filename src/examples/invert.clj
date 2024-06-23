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
