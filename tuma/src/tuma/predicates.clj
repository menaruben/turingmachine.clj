(ns tuma.predicates
  (:require [tuma.messages :as msg]
            [clojure.set :as s]))

(defn valid-word? [word input-symbols]
  (assert (string? word))
  (doseq [s input-symbols] (assert (string? s)))
  (every? #(some #{%} input-symbols) word))

(defn- get-reached-states [transitions]
  (set (concat (map :source transitions) (map :destination transitions))))

(defn- all-reached-states-defined? [transitions states]
  (= (get-reached-states transitions) (set states)))

(defn valid-tm-transition?
  "Validates a transition for a Turing machine for specific tape-symbols and states of the machine."
  [source reads destination writes direction tape-symbols states]
  (doseq [state states] (assert        (keyword? state) (msg/format-not-expected-type state "keyword")))
  (doseq [symbol tape-symbols] (assert (string? symbol) (msg/format-not-expected-type symbol "string")))
  (assert (keyword? source) (msg/format-not-expected-type source "keyword"))
  (assert (some #{source} states) (msg/format-not-defined source "state"))
  (assert (keyword? destination) (msg/format-not-expected-type destination "keyword"))
  (assert (some #{destination} states) (msg/format-not-defined destination "state"))
  (assert (string? reads) (msg/format-not-expected-type reads "string"))
  (assert (some #{reads} tape-symbols) (msg/format-not-defined reads "tape symbol"))
  (assert (string? writes) (msg/format-not-expected-type writes "string"))
  (assert (some #{writes} tape-symbols) (msg/format-not-defined writes "tape symbol"))
  (assert (keyword? direction) (msg/format-not-expected-type direction "keyword"))
  (assert (or (= direction :left) (= direction :right)) (msg/format-not-expected-type direction "direction"))
  true)

(defn valid-transition?
  "Validates if a transition has a valid source, reads, destination, writes and direction in general."
  [source reads destination writes direction]
  (assert (keyword? source) (msg/format-not-expected-type source "keyword"))
  (assert (keyword? destination) (msg/format-not-expected-type destination "keyword"))
  (assert (string? reads) (msg/format-not-expected-type reads "string"))
  (assert (string? writes) (msg/format-not-expected-type writes "string"))
  (assert (keyword? direction) (msg/format-not-expected-type direction "keyword"))
  (assert (or (= direction :left) (= direction :right)) (msg/format-not-expected-type direction "direction"))
  true)

(defn valid-tm? [states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states]
  (doseq [s states] (assert (keyword? s) (msg/format-not-expected-type s "keyword")))
  (doseq [s input-symbols] (assert (string? s) (msg/format-not-expected-type s "string")))
  (doseq [s tape-symbols] (assert (string? s) (msg/format-not-expected-type s "string")))
  (assert (keyword? initial-state) (msg/format-not-expected-type initial-state "keyword"))
  (assert (some #{initial-state} states) (msg/format-not-defined initial-state "state"))
  (assert (string? blank-symbol) (msg/format-not-expected-type blank-symbol "string"))
  (assert (s/subset? (set accepted-states) (set states)) (msg/format-not-defined accepted-states "state"))
  (every? #(valid-tm-transition?
            (:source %)
            (:reads %)
            (:destination %)
            (:writes %)
            (:direction %)
            tape-symbols states) transitions)
  (all-reached-states-defined? transitions states))

(defn inside-word? [word position]
  (assert (string? word))
  (assert (= Long (type position)))
  (< -1 position (count word)))
