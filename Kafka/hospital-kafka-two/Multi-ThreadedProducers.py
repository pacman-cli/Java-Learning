# ✅ What this does:
# Each thread simulates a separate “hospital process.”
# Produces messages to all topics in parallel.
# Easily scales by increasing num_threads.
from kafka import KafkaProducer
from faker import Faker
import json
import random
import time
from threading import Thread

fake = Faker()

producer = KafkaProducer(
    bootstrap_servers="localhost:9092",
    value_serializer=lambda v: json.dumps(v).encode("utf-8"),
)

topics = ["patients", "appointments", "medicines", "prescriptions"]


def generate_patient():
    return {
        "patient_id": fake.uuid4(),
        "name": fake.name(),
        "age": random.randint(1, 100),
        "gender": random.choice(["Male", "Female", "Other"]),
    }


def generate_doctor():
    return {
        "doctor_id": fake.uuid4(),
        "name": fake.name(),
        "specialty": random.choice(
            ["Cardiology", "Dermatology", "Pediatrics", "General"]
        ),
    }


def generate_medicine():
    return {
        "medicine_id": fake.uuid4(),
        "name": fake.word().capitalize(),
        "dosage": f"{random.randint(1, 3)} times/day",
    }


def generate_prescription(patient, doctor, medicine):
    return {
        "prescription_id": fake.uuid4(),
        "patient_id": patient["patient_id"],
        "doctor_id": doctor["doctor_id"],
        "medicine_id": medicine["medicine_id"],
        "notes": fake.sentence(),
    }


def generate_appointment(patient, doctor):
    return {
        "appointment_id": fake.uuid4(),
        "patient_id": patient["patient_id"],
        "doctor_id": doctor["doctor_id"],
        "datetime": str(fake.date_time_this_year()),
    }


def producer_worker(thread_id):
    while True:
        patient = generate_patient()
        doctor = generate_doctor()
        medicine = generate_medicine()

        producer.send("patients", patient)
        producer.send("appointments", generate_appointment(patient, doctor))
        producer.send("medicines", medicine)
        producer.send("prescriptions", generate_prescription(patient, doctor, medicine))

        producer.flush()
        print(f"[Thread-{thread_id}] Produced patient {patient['name']}")
        time.sleep(random.uniform(0.5, 2))


# Launch multiple producer threads
num_threads = 5  # You can increase to simulate higher load
threads = []
for i in range(num_threads):
    t = Thread(target=producer_worker, args=(i,))
    t.start()
    threads.append(t)

# Keep main thread alive
for t in threads:
    t.join()
