# datomic-setup

A Leiningen plugin to do some basic Datomic launch and setup

## Pre-requisites
You need to have some Datomic package on your local file system. I'll refer to this location, throughout this doc, by $DATOMIC-PATH
## Tasks

* start the Datomic transactor
`$ lein datomic-setup transactor $DATOMIC-PATH path-to-<trasactor.properties>`
* create a database
`$ lein datomic-setup database a-valid-datomic-uri`
* star the Datomic peer server
`$ lein datomic-setup peer-server $DATOMIC-PATH hostname port auth uri`

## Lein alternative

You can also use boot instead of this leiningen plugin.
Just checkout the two boot files:
`$ build.boot`
`$ boot.properties`

into your Clojure project's root.

You can call similar tasks to leiningen plugin tasks or to have a full start-up:
    $ boot datomic-setup -d $DATOMIC-PATH -b database a-valid-datomic-uri
    
## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.