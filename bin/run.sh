# build
./gradlew clean bootJar

cd build/libs || exit

# run first instance
java -jar hazelcast-demo-0.0.1-SNAPSHOT.jar --server.port=8080 --hazelcast.port=5701 &

# run second instance
java -jar hazelcast-demo-0.0.1-SNAPSHOT.jar --server.port=8081 --hazelcast.port=5702
