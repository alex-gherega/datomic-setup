(defproject datomic-setup "0.1.2"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true
  :dependencies [[com.datomic/datomic-pro "0.9.5561" :exclusions [org.slf4j/slf4j-nop org.slf4j/slf4j-log4j12]]
                 [ch.qos.logback/logback-classic "1.1.7"]
                 [com.datomic/clj-client "0.8.606"]]
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}})
