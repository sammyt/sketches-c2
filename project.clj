(defproject sketch "0.1.0-SNAPSHOT"
  :repositories {"sonatype-staging" "https://oss.sonatype.org/content/groups/staging"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1889"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [com.keminglabs/c2 "0.2.3"]]
  :hooks [leiningen.cljsbuild]
  :plugins [[lein-cljsbuild "0.3.3"]]
  :cljsbuild {
     :builds [{
       :id "dev"
       :source-paths ["src/sketch"]
       :compiler {
          :output-to "public/sketch-dev.js"
          :pretty-print true }}
      {:id "prod"
       :source-paths ["src/sketch"]
       :compiler {
          :output-to "public/sketch.js"
          :pretty-print false
          :optimizations :advanced}}]})
