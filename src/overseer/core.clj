(ns overseer.core
  (:use [overseer.parse :only [file->sheet]]
        [overseer.aggregate :only [summarize summarize-income summarize-expenses]]
        [overseer.util])
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:gen-class))


 (defn -main
   "Calculate income and expenses for given account statements"
   [& args]
   (let [parse-csv (comp file->sheet io/file io/resource)
         sheets {:acc-stmts (parse-csv "account.csv")
                 :distribution (parse-csv "distribution.csv")
                 :expenses (parse-csv "expenses.csv")
                 :income ()}]
     (println (summarize sheets))))

