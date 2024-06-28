(ns test.tuma.IsEven
  (:require [main.tuma.turingmachine :as tm]
            [main.tuma.transition])
  (:import [main.tuma.transition Transition])
  (:import [main.tuma.turingmachine Turingmachine]))

(def t1 (Transition. :q1 "1" :q1 "1" :right))
(def t2 (Transition. :q1 "0" :q1 "0" :right))
(def t3 (Transition. :q1 "_" :q3 "_" :left))
(def t4 (Transition. :q3 "0" :q2 "0" :right))
(def transitions (list t1 t2 t3 t4))

(def states (list :q1 :q2 :q3))
(def input-symbols (list "0" "1"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))

(def tm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
(def inputs (list "000" "111" "10111011" "0" "1"))
(doseq [input inputs] (println (tm/emulate-tm tm input)))
