# pip install kafka-python faker
# We’ll use Faker to generate realistic fake patient, doctor, and medicine data.
from kafka import KafkaProducer
from faker import Faker
import json
import random
import time

fake = Faker()

# Connect to Redpanda
producer = KafkaProducer(
    bootstrap_servers='localhost:9092',
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# Topics
topics = ["patients", "appointments", "medicines", "prescriptions"]

def generate_patient():
    return {
        "patient_id": fake.uuid4(),
        "name": fake.name(),
        "age": random.randint(1, 100),
        "gender": random.choice(["Male", "Female", "Other"])
    }

def generate_doctor():
    return {
        "doctor_id": fake.uuid4(),
        "name": fake.name(),
        "specialty": random.choice(["Cardiology", "Dermatology", "Pediatrics", "General"])
    }

def generate_medicine():
    return {
        "medicine_id": fake.uuid4(),
        "name": fake.word().capitalize(),
        "dosage": f"{random.randint(1, 3)} times/day"
    }

def generate_prescription(patient, doctor, medicine):
    return {
        "prescription_id": fake.uuid4(),
        "patient_id": patient["patient_id"],
        "doctor_id": doctor["doctor_id"],
        "medicine_id": medicine["medicine_id"],
        "notes": fake.sentence()
    }

def generate_appointment(patient, doctor):
    return {
        "appointment_id": fake.uuid4(),
        "patient_id": patient["patient_id"],
        "doctor_id": doctor["doctor_id"],
        "datetime": str(fake.date_time_this_year())
    }

# Simulation loop
while True:
    patient = generate_patient()
    doctor = generate_doctor()
    medicine = generate_medicine()

    # Produce messages
    producer.send("patients", patient)
    producer.send("appointments", generate_appointment(patient, doctor))
    producer.send("medicines", medicine)
    producer.send("prescriptions", generate_prescription(patient, doctor, medicine))

    producer.flush()

    print(f"Produced patient {patient['name']} with appointment and prescription.")

    # Wait 0.5-2 seconds to simulate real traffic
    time.sleep(random.uniform(0.5, 2))
