echo "=== chmod +x gradlew ==="
chmod +x gradlew

echo "=== gradlew clear ==="
./gradlew clean

echo "=== build compileQuerydsl ==="
./gradlew build compileQuerydsl

echo "=== docker compose up ==="
docker compose up -d
