(ns tuma.predicates
  (:require [clojure.spec.alpha :as spec])
  (:require [tuma.transition :as trans]))

(defn valid-word? [word input-symbols]
  (assert (spec/valid? string? word))
  (doseq [s input-symbols] (assert (spec/valid? string? s)))
  (every? #(some #{%} input-symbols) word))

(defn- all-reached-states-defined? [transitions states]
  (= (trans/get-reached-states transitions) (set states)))

(defn valid-tm? [transitions states]
  (true? (all-reached-states-defined? transitions states)))

(defn inside-word? [word position]
  (assert (spec/valid? string? word))
  (assert (= Long (type position)))
  (< -1 position (count word)))
