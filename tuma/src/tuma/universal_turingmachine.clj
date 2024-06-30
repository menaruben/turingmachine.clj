(ns tuma.universal-turingmachine
  (:require [tuma.messages :as msg]
            [clojure.string :as str]
            [tuma.transition :as trans]
            [tuma.turingmachine :as tm]))

(defn- code-to-symbol [code symbols]
  (nth symbols (dec (count code))))

(defn- code-to-state [code]
  (keyword (str "q" (count code))))

(defn- parse-transition-from-code [tcode tape-symbols]
  (let [fields (str/split tcode #"1")]
    (if (< (count fields) 5)
      (throw (Exception. (str "Invalid transition code: " tcode)))
      (let [source (code-to-state (nth fields 0))
            reads (code-to-symbol (nth fields 1) tape-symbols)
            destination (code-to-state (nth fields 2))
            writes (code-to-symbol (nth fields 3) tape-symbols)
            direction (if (= (count (nth fields 4)) 1) :left :right)]
        (trans/new-transition source reads destination writes direction)))))

(defn- transitions-from-goedel [goedel tape-symbols]
  (assert (string? goedel) (msg/format-not-expected-type goedel "string"))
  (let [transition-codes (str/split goedel #"11")
        transitions (map #(parse-transition-from-code % tape-symbols) transition-codes)]
    transitions))

(defn- get-all-sources [transitions]
  (set (map :source transitions)))

(defn- get-all-destinations [transitions]
  (set (map :destination transitions)))

(defn- get-all-states-from-transitions [transitions]
  (set (concat (get-all-sources transitions) (get-all-destinations transitions))))

(defn new-utm [goedel input-symbols tape-symbols]
  (let [transitions (transitions-from-goedel goedel tape-symbols)
        states (get-all-states-from-transitions transitions)
        utm (tm/new-turingmachine states
                              input-symbols
                              tape-symbols
                              transitions
                              :q1
                              (nth tape-symbols 2)
                              [:q2])]
    utm))

(defn emulate-utm
  ([utm word] (tm/emulate-tm utm word false))
  ([utm word trace?] (tm/emulate-tm utm word trace?)))
