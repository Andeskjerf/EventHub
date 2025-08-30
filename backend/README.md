# EventHub

Built using Java Spring Boot.

## Configuring

There are some environment variables that can be set to alter behavior


| ENV | Behavior | Default |
|--------|-------------|---------|
| `ANONYMIZE_PAST_DAYS` | Sets how long we should keep participant identifiers. Will scrub data past given value | 30 |
| `REGISTRATION_ENABLED` | Enables registration | false |
| `DOMAIN` | Sets domain for CORS. Must include http(s) | http://localhost:* |
| `DEV` | If we're in a dev environment or not, used to set cookie secureOnly | false |
