(ns overseer.util
  (:require [clojure.string :as str]))

(defn matches-ignore-case? [s col]
  (let [s (str/upper-case s)
        col (map str/upper-case col)]
    (some (partial #(.contains %1 %2) s) col)))

;; found this on http://stackoverflow.com/questions/2640169/whats-the-easiest-way-to-parse-numbers-in-clojure
;; I use this because I also want ratio types to be parsed correctly
(let [m (.getDeclaredMethod clojure.lang.LispReader
                            "matchNumber"
                            (into-array [String]))]
  (.setAccessible m true)
  (defn parse-number [s]
    (.invoke m clojure.lang.LispReader (into-array [s]))))
