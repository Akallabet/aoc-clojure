(ns aoc22.day09-test
  (:require
   [clojure.test :as test]
   [aoc22.day09] :ad day09))

(test/deftest id-adjecent-row
  (test/testing "is adjecent row"
    (test/is (= true (day09/adj-row [1 4] [1 4])))))