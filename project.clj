(defproject overseer "0.1.0-SNAPSHOT"
  :description "Learn clojure by creating simple bank statements parser and analyzer"
  :url "https://github.com/u6f6o/overseer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot overseer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
