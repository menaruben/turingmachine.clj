(ns main.tuma.transition)

(defrecord Transition [source reads destination writes direction])

(defn get-reached-states [transitions]
  (doseq [t transitions] (assert Transition (type t)))
  (set (concat (map :source transitions) (map :destination transitions))))
