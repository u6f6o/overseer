(ns overseer.aggregate-spec
  (:require [speclj.core :refer :all]
            [overseer.aggregate :refer :all]))

(describe "extract holder names"
  (it "empty on nil"
    (should= #{} (extract-holders nil)))
  (it "empty on empty col"
    (should= #{} (extract-holders [])))
  (it "surname keywords and names remain"
    (let [input       [{:surname-1 "Pinkel" :age 50 :sex :male}
                       {:age 20 :surname-2 "Lüdenscheid" :sex :female}
                       {:age 39 :sex :female :surname-3 "Pforfel" }]
          exp-result  #{"Pinkel" "Lüdenscheid" "Pforfel"}]
      (should= exp-result (extract-holders input)))))


(describe "filter expenses"
  (it "remove acc stmts that do not match"
    (let [acc-stmts   [{:desc "House cleaning"}
                       {:desc "Garbage fee"}
                       {:desc "Electricity"}
                       {:desc "Plumbing work"}]
          expense     {:tokens "CLEANING,ELECTRICITY" }
          exp-result  [{:desc "House cleaning"}
                       {:desc "Electricity"}]]
      (should= exp-result (filter-expenses acc-stmts expense)))))


(describe "filter income"
  (it "remove acc stmts that do not match"
    (let [acc-stmts   [{:desc "GutschriftLÜDENSCHEID"}
                       {:desc "Garbage fee"}
                       {:desc "GutschriftPINKEL"}
                       {:desc "GutschriftLÜDENSCHEID"}]
          holder      "Lüdenscheid"
          exp-result  [{:desc "GutschriftLÜDENSCHEID"}
                       {:desc "GutschriftLÜDENSCHEID"}]]
      (should= exp-result (filter-income acc-stmts holder)))))


(describe "sum-up column values"
  (it "0 on nil"
    (should= 0 (sum-column nil :in)))
  (it "0 on empty col"
    (should= 0 (sum-column [] :in)))
  (it "666 on three positions(123, 444, 99)"
    (let [input [{:desc "foo" :in "123"}
                 {:desc "bar" :in "444"}
                 {:desc "fnu" :in "99"}]]
      (should= 666 (sum-column input :in)))))

