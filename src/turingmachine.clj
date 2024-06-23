(ns turingmachine)

(defrecord Transition [source reads destination writes direction])
(defrecord UniversalTuringmachine [states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states])

(defn find-transition [source readSymbol transitions]
  (first (filter #(and (= (:source %) source) (= (:reads %) readSymbol)) transitions)))

(defn print-transition [transition]
  (println (str
    "source: " (:source transition)
    ", reads: " (:reads transition)
    ", destination: " (:destination transition)
    ", writes: " (:writes transition)
    ", direction: " (:direction transition))))

(defn write-tape [tape headPosition symbol blank-symbol]
  (str (subs tape 0 headPosition) symbol (subs tape (+ headPosition 1))))

(defn execute-transitions [tape transitions headPosition currentState blank-symbol]
  (cond
    (= headPosition (count tape)) {:currentState currentState :tape tape}

    :else (let [
    readSymbol (str (nth tape headPosition))
    transition (find-transition currentState readSymbol transitions)]

    ;; (print-transition transition) ;; uncomment if you like to print each transition :)
    (cond (nil? transition) {:currentState currentState :tape tape}

      :else (
        let [
          newTape (write-tape tape headPosition (:writes transition) blank-symbol)
          newHeadPosition (if (= (:direction transition) :right)
                              (+ headPosition 1)
                              (- headPosition 1))]
            (execute-transitions newTape transitions newHeadPosition (:destination transition) blank-symbol))))))

(defn emulate-utm [utm tape]
  (let [result (execute-transitions tape (:transitions utm) 0 (:initial-state utm) (:blank-symbol utm))]
    (if (contains? (set (:accepted-states utm)) (:currentState result))
      {:input tape :output (:tape result) :endState (:currentState result) :verdict :accepted}
      {:input tape :output (:tape result) :endState (:currentState result) :verdict :rejected})))
