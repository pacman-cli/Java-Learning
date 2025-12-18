from kafka import KafkaConsumer
import json

import time

topics = ["patients", "appointments", "medicines", "prescriptions"]

consumer = KafkaConsumer(
    *topics,
    bootstrap_servers="localhost:9092",
    value_deserializer=lambda m: json.loads(m.decode("utf-8")),
    group_id="hospital-consumer-group",
    auto_offset_reset="earliest",
)

for message in consumer:
    print(f"[{message.topic}] {message.value}")


start_time = time.time()
count = 0

for message in consumer:
    count += 1
    if count % 100 == 0:
        elapsed = time.time() - start_time
        print(
            f"{count} messages consumed in {elapsed:.2f}s, rate: {count/elapsed:.2f} msg/sec"
        )
