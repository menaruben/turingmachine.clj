(ns tuma.transition
  (:require [tuma.predicates :as preds]))

(defrecord -transition [source reads destination writes direction])

(defn new-transition [source reads destination writes direction]
  (assert (preds/valid-transition? source reads destination writes direction))
  (-transition. source reads destination writes direction))
