(ns leiningen.datomic-setup.peer-server
  (:require [clojure.java.shell :refer [sh]]))

(def retries (atom 5))

(defn start-peer-server [datomic-path host port auth database]
  (let [result (sh (str datomic-path "/bin/run")
           "-m" "datomic.peer-server"
           "-h" host
           "-p" port
           "-a" auth
           "-d" database)]
    (when (and (-> result :exit 1)
               (-> retries deref zero? not))
      (Thread/sleep 1000)
      (swap! retries dec)
      (prn "[DATOMIC] Retrying to start peer server..." @retries)
      (recur datomic-path host port auth database))))

(defn peer-server [datomic-path host port auth database]
  (let [[db-name db-uri] (clojure.string/split database #",")]
    (prn "[DATOMIC] Starting peer server...\n"
         "\t\t Serving " db-uri " as " db-name))
  (start-peer-server datomic-path host port auth database))
