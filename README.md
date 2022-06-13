## Overview 

Basic Elasticsearch example that allows to search person data.

## Setup

Use the Docker Compose file in the dockercompose folder to spin up the following services:

- Zookeeper
- Kafka
- Elasticsearch
- Kibana

## Kibana

Go to http://localhost:5601/ in your browser

## Example usage

```
curl --location --request POST 'http://localhost:9653/api/persons' \
--header 'Content-Type: application/json' \
--data-raw '{
    "searchString": "Nils"
}'
```