(ns aoc22.day05
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.edn :as edn]
            #?(:cljs [promesa.core :as p])))

(defn parse-lines [lines]
  (->> lines
       str/split-lines
       (map #(str/split % #" "))))

(defn parse-instructions [instructions]
  (->> instructions
       parse-lines
       (map
        (fn
          [instr]
          {:crates (edn/read-string (get instr 1))
           :from (- (edn/read-string (get instr 3)) 1)
           :to (- (edn/read-string (get instr 5)) 1)}))))

(defn parse-input [input]
  (let [[stacks instructions] (str/split input #"\n\n")]
    {:stacks (vec (parse-lines stacks)) :instructions (parse-instructions instructions)}))

(defn move [stacks instr] 
  (assoc 
   stacks 
   (:from instr) (vec (drop (:crates instr) (stacks (:from instr))))
   (:to instr) (vec (concat (reverse (take (:crates instr) (stacks (:from instr)))) (stacks (:to instr))))))

(defn moves
  [{stacks :stacks  instructions :instructions}]
  (cond
    (empty? instructions) (str/join "" (map first stacks))
    :else (recur {:stacks (move stacks (first instructions)) :instructions (drop 1 instructions)})))

;; stacks [[N Z] [D C M] [P]]
(def example "N Z\nD C M\nP\n\nmove 1 from 2 to 1\nmove 3 from 1 to 3\nmove 2 from 2 to 1\nmove 1 from 1 to 2")

(comment 
  (move (:stacks (parse-input example)) {:crates 1 :from 1 :to 2})
  (move (:stacks (parse-input example)) {:crates 2 :from 1 :to 2})
  (move (:stacks (parse-input example)) {:crates 3 :from 1 :to 2})
  (move (:stacks (parse-input example)) {:crates 1 :from 0 :to 1})
  (moves (parse-input example))
  (moves (parse-input (slurp (io/resource "aoc22/day05.txt"))))
         )