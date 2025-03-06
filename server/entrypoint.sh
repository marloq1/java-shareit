echo "Waiting for database to be ready..."
while ! pg_isready -h db -U dbuser -d shareit; do
  sleep 2
done
echo "Database is ready, starting application..."
exec java -jar /app.jar
