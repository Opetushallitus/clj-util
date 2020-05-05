# clj-access-logging

Ring middleware access-lokitusta varten. Asettaa lokiriviin tarvittavat tiedot
ring response tietueeseen avaimella `:access-log-data`. Varsinainen lokin
kirjoitus hoidetaan esim. `clj-stdout-access-logging` ja
`clj-timbre-access-logging` middlewareilla.

Session tunnisteen lokittamista varten `wrap-session-access-logging`
middleware toimii yhdessä `ring.middleware.session` middlewaren kanssa. Se
tulee sijoittaa middlewarepinossa `ring.middleware.session` middlewaren päälle
jotta sillä on pääsy request tietueen `:session/key` avaimeen.

```
(-> handler
    (wrap-session-access-logging)
    (wrap-session ...)
    (wrap-access-logging))
```
