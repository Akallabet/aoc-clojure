(ns aoc22.day07
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.edn :as edn]
            #?(:cljs [promesa.core :as p])))

(defn parse-input [input] (str/split-lines input))

(defn is-command? [line] (str/starts-with? line "$"))
(defn is-dir-up? [line] (and (is-command? line) (str/includes? line "cd ..")))
(defn is-list? [line] (and (is-command? line) (str/includes? line "ls")))
(defn is-open-dir? [line] (and (is-command? line) (not (or (is-dir-up? line) (is-list? line)))))
(defn is-dir? [line] (str/starts-with? line "dir"))
(defn dir-name [line] (last (str/split line #" ")))
(defn file-size [line] (edn/read-string (first (str/split line #" "))))

(defn traverse
  ([lines] (traverse (drop 1 lines) (first lines) 1) 0)
  ([lines line size]
   (println line size)
   (cond
     (or (nil? lines) (empty? lines)) size
     (is-list? line) (recur (drop 1 lines) (first lines) size)
     (is-dir-up? line) (recur (drop 1 lines) (first lines) size)
     (is-open-dir? line) (recur (drop 1 lines) (first lines) size)
     (is-dir? line) (recur (drop 1 lines) (first lines) size)
     :else (recur (drop 1 lines) (first lines) (+ (file-size line) size)))))

(def input "$ cd /\n$ ls\ndir a\n14848514 b.txt\n8504156 c.dat\ndir d\n$ cd a\n$ ls\ndir e\n29116 f\n2557 g\n62596 h.lst\n$ cd e\n$ ls\n584 i\n$ cd ..\n$ cd ..\n$ cd d\n$ ls\n4060174 j\n8033020 d.log\n5626152 d.ext\n7214296 k")

(comment
  (is-command? "$ dc /")
  (is-command? "fir a")
  (is-dir-up? "$ cd ..")
  (is-dir-up? "$ ls")
  (is-dir-up? "12343 lll.txt")
  (is-open-dir? "$ cd a")
  (is-open-dir? "$ cd ..")
  (is-open-dir? "$ ls")
  (is-list? "$ ls")
  (is-list? "$ dir a")
  (is-dir? "dir a")
  (is-dir? "12344 a.txt")
  (dir-name "dir a")
  (file-size "12345 ddd.txt")
  (traverse (parse-input input))
  (empty? (drop 1 '(1))))