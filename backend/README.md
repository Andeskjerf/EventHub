# EventHub

Built using Java Spring Boot.

## Configuring

There are some environment variables that can be set to alter behavior


| ENV | Behavior | Default |
|--------|-------------|---------|
| `ANONYMIZE_PAST_DAYS` | Sets how long we should keep participant identifiers. Will scrub data past given value | 30 |
