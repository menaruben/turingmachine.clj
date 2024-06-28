(ns test.tuma.Invert
  (:require [main.tuma.transition :as trans]
            [main.tuma.turingmachine :as tm])
  (:import [main.tuma.transition Transition])
  (:import [main.tuma.turingmachine Turingmachine]))

(defn -main []
  (def t1 (Transition. :q1 "_" :q1 "_" :right))
  (def t2 (Transition. :q1 "0" :q2 "1" :right))
  (def t3 (Transition. :q1 "1" :q2 "0" :right))
  (def t4 (Transition. :q2 "0" :q2 "1" :right))
  (def t5 (Transition. :q2 "1" :q2 "0" :right))

  (def transitions [t1 t2 t3 t4 t5])
  (def states [:q1 :q2])
  (def input-symbols ["0" "1" "_"])
  (def tape-symbols ["0" "1" "_"])
  (def accepted-states [:q2])

  (def tm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
  (def inputs ["000" "111" "10111011" "0" "1" "" "abc" "___110001"])
  (doseq [input inputs] (println (tm/emulate-tm tm input))))
