#!/usr/bin/env bb
(require '[clojure.string :as str])

(defn most-calories
  [calories]
  (->> (str/split calories #"\n\n") 
       (map #(str/split % #"\n"))
       (map #(map parse-long %))
       (map #(reduce + %))
       (apply max)
       ))

(defn top-three-most-calories
  [calories]
  (->> (str/split calories #"\n\n")
       (map #(str/split % #"\n"))
       (map #(map parse-long %))
       (map #(reduce + %))
       (sort >)
       (take 3)
       (apply +)
       ))

(def input "1000\n2000\n3000\n\n4000\n\n5000\n6000\n\n7000\n8000\n9000\n\n10000")

(comment
  (most-calories input)
  (most-calories (slurp "./input.txt"))
  (top-three-most-calories input)
  (top-three-most-calories (slurp "./input.txt"))
  )