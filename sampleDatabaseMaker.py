import pysolr
import uuid
import random
from datetime import datetime, timedelta

# Initialize the Solr client
solr_url = 'http://localhost:8983/solr/DocumentCollection'
solr = pysolr.Solr(solr_url, always_commit=True, timeout=10)

# Random data generation functions
def random_log_level():
    return random.choice(['INFO', 'DEBUG', 'ERROR', 'WARN'])

def random_log_type():
    return random.choice(['SYSTEM', 'APPLICATION', 'SECURITY'])

def random_hardware_name():
    return random.choice(['Server1', 'Server2', 'RouterA', 'RouterB'])

def random_function_type():
    return random.choice(['AUTH', 'PROCESS', 'MONITOR'])

def random_log_date():
    start_date = datetime.now() - timedelta(days=365)
    random_date = start_date + timedelta(days=random.randint(0, 365))
    return random_date.isoformat()

def random_context():
    return "Context data " + str(random.randint(1, 1000))

# Generate and add 100 random documents
documents = []
for _ in range(100):
    document = {
        "id": str(uuid.uuid4()),
        "logLevel": random_log_level(),
        "logType": random_log_type(),
        "hardwareName": random_hardware_name(),
        "functionType": random_function_type(),
        "logDate": random_log_date(),
        "context": random_context()
    }
    documents.append(document)

# Add the documents to Solr
solr.add(documents)

print("100 random documents added to DocumentCollection.")
