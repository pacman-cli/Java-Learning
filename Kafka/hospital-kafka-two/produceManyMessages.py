from kafka import KafkaProducer
import json

producer = KafkaProducer(
    bootstrap_servers="localhost:9092",
    value_serializer=lambda v: json.dumps(v).encode("utf-8"),
)

patient_data = {
    "patient": "Rahul",
    "medicine": "Themo",
    "doctor": "Dr. Rana",
    "notes": "Once a week",
}

producer.send("patients", patient_data)
producer.flush()
print("Message sent!")
