# Clj-log

Korjasto, jossa on yhteiskäyttöistä koodia mm. access- ja error-logitukseen.

## Käyttö

Lisää clj-log riippuvuudeksi `project.clj`:ssä:

```
:dependencies [[oph/clj-log "0.1.0-SNAPSHOT"]]
```

Access-logiin tulevan servicen konfigurointi:

```
(intern 'clj-log.access-log 'service "konfo-backend")
```

Error-logituksen konfigurointi testausta varten:

```
(intern 'clj-log.error-log 'test true)
(intern 'clj-log.error-log 'verbose false)
```