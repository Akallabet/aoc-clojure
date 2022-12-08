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
(defn is-start? [line] (= line "$ cd /"))
(defn is-open-dir? [line] (and (is-command? line) (not (or (is-dir-up? line) (is-list? line)))))
(defn is-dir? [line] (str/starts-with? line "dir"))
(defn dir-name [line] (last (str/split line #" ")))
(defn file-size [line] (edn/read-string (first (str/split line #" "))))

(defn traverse
  ([lines stack level]
   (let [line (first lines)]
     (println stack line level)
     (cond
       (empty? lines) stack
       (is-start? line) (recur (drop 1 lines) stack level)
       (is-dir? line) (recur (drop 1 lines) stack level)
       (is-list? line) (recur (drop 1 lines) stack level)
       (is-open-dir? line) (recur (drop 1 lines) (conj stack [0]) (+ 1 level))
       (is-dir-up? line) (recur (drop 1 lines) stack (- 1 level))
       :else (recur (drop 1 lines) (assoc stack level (+ (get stack level) (file-size line))) level)))))
    ;;  (or (nil? lines) (empty? lines)) size
    ;;  (is-list? line) (recur (drop 1 lines) (first lines) size)
    ;;  (is-dir-up? line) (recur (drop 1 lines) (first lines) size)
    ;;  (is-open-dir? line) (recur (drop 1 lines) (first lines) size)
    ;;  (is-dir? line) (recur (drop 1 lines) (first lines) size)
    ;;  :else (recur (drop 1 lines) (first lines) (+ (file-size line) size)))))

(def input "$ cd /\n$ ls\ndir a\n14848514 b.txt\n8504156 c.dat\ndir d\n$ cd a\n$ ls\ndir e\n29116 f\n2557 g\n62596 h.lst\n$ cd e\n$ ls\n584 i\n$ cd ..\n$ cd ..\n$ cd d\n$ ls\n4060174 j\n8033020 d.log\n5626152 d.ext\n7214296 k")

{:dir "/" {:dir "a"}}

(comment
  (traverse ["$ cd /"
             "dir a"
             "1000 b.txt"
             "2000 b.txt"
             "$ cd a"
             "123 s.txt"] [0] 0)
  (traverse ["$ cd /" "dir a" "1000 b.txt" "2000 b.txt" "$ cd a" "123 s.txt" "$ cd .." "234 g.t"] [0] 0)
  (traverse (parse-input input) [0] 0))