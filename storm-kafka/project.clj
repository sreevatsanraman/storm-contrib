(defproject storm/storm-kafka "0.7.4-SNAPSHOT"
  :source-path "src/clj"
  :java-source-path "src/jvm"
  :javac-options {:debug "true" :fork "true"}
  :repositories {"klout-snapshots" "http://maven-repo:8081/artifactory/libs-snapshot-local"
  		 "releases" "http://artifactory.local.twitter.com/libs-releases-local"
                 "snapshots" "http://artifactory.local.twitter.com/libs-snapshots-local"
                 "artifactory" "http://artifactory.local.twitter.com/repo"}
  :dependencies [[kafka/core-kafka_2.9.1 "0.7.0-SNAPSHOT"
                   :exclusions [org.apache.zookeeper/zookeeper
                                log4j/log4j]]]
  :dev-dependencies [[storm "0.7.2"]
                     [org.clojure/clojure "1.4.0"]]
  :jvm-opts ["-Djava.library.path=/usr/local/lib:/opt/local/lib:/usr/lib"]
)
