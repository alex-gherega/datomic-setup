(ns leiningen.datomic-setup.transactor
  (:require [clojure.java.shell :refer [sh]]))

;;TODO: use conch
(defn transactor [datomic-path transactor-props]
  (prn "[DATOMIC] Starting transactor..." )
  (sh (str datomic-path "/bin/transactor")
        (str (System/getProperty "user.dir") "/" transactor-props)))



